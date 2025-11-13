package com.incafit.Controller;

import com.incafit.Model.DetalleFactura;
import com.incafit.Model.Factura;
import com.incafit.Model.Membresia;
import com.incafit.Model.Socio;
import com.incafit.Repository.DetalleFacturaRepository;
import com.incafit.Repository.FacturaRepository;
import com.incafit.Repository.PagoRepository;
import com.incafit.Model.Pago;
import com.incafit.service.MembresiaService;
import com.incafit.service.SocioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/socio")
public class SocioMembresiaController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SocioService socioService;
    private final MembresiaService membresiaService;
    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final PagoRepository pagoRepository;

    public SocioMembresiaController(SocioService socioService,
                                    MembresiaService membresiaService,
                                    FacturaRepository facturaRepository,
                                    DetalleFacturaRepository detalleFacturaRepository,
                                    PagoRepository pagoRepository) {
        this.socioService = socioService;
        this.membresiaService = membresiaService;
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.pagoRepository = pagoRepository;
    }

    @GetMapping("/membresia")
    public String verMembresia(Model model, Authentication authentication) {
        Socio socio = socioService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        model.addAttribute("diasRestantes", calcularDiasRestantes(socio));
        return "membresia";
    }

    @GetMapping("/membresia/renovar")
    public String mostrarRenovacionMembresia(Model model, Authentication authentication) {
        Socio socio = socioService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        List<Membresia> planes = membresiaService.findAll();
        if (planes.isEmpty()) {
            throw new IllegalStateException("No hay membresias configuradas. Crea al menos una para poder renovar.");
        }

        Long selectedId = socio.getMembresia() != null
                ? socio.getMembresia().getId()
                : planes.get(0).getId();

        Membresia membresiaSeleccionada = planes.stream()
                .filter(m -> m.getId().equals(selectedId))
                .findFirst()
                .orElse(planes.get(0));

        LocalDate inicioRenovacion = calcularInicioRenovacion(socio);
        LocalDate finEstimada = calcularFinRenovacion(inicioRenovacion, membresiaSeleccionada.getDuracionDias());
        model.addAttribute("socio", socio);
        model.addAttribute("membresias", planes);
        model.addAttribute("selectedMembresiaId", selectedId);
        model.addAttribute("renovacionInicio", inicioRenovacion);
        model.addAttribute("renovacionInicioTexto", inicioRenovacion.format(DATE_FORMATTER));
        model.addAttribute("renovacionFinTexto", finEstimada.format(DATE_FORMATTER));
        model.addAttribute("diasRestantes", calcularDiasRestantes(socio));
        model.addAttribute("metodosPagoDisponibles", List.of("Tarjeta guardada", "Tarjeta nueva", "Transferencia bancaria"));
        return "socio/membresia/renovar";
    }

    @PostMapping("/membresia/renovar")
    public String renovarMembresia(@RequestParam("membresiaId") Long membresiaId,
                                   @RequestParam(value = "metodoPago", required = false, defaultValue = "Tarjeta guardada") String metodoPago,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        Socio socio = socioService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        Membresia nuevaMembresia = membresiaService.findById(membresiaId)
                .orElseThrow(() -> new RuntimeException("La membresia seleccionada no existe"));

        LocalDate inicio = calcularInicioRenovacion(socio);
        LocalDate fin = calcularFinRenovacion(inicio, nuevaMembresia.getDuracionDias());

        socio.setMembresia(nuevaMembresia);
        socio.setFechaInicioMembresia(inicio);
        socio.setFechaFinMembresia(fin);
        socio.setActivo(true);
        socioService.guardarSocio(socio);

        Factura factura = new Factura();
        factura.setSocio(socio);
        factura.setFecha(LocalDate.now());
        factura.setTotal(nuevaMembresia.getPrecio());
        factura.setEstado("PAGADA");
        facturaRepository.save(factura);

        DetalleFactura detalle = new DetalleFactura();
        detalle.setFactura(factura);
        detalle.setDescripcion("Renovacion de membresia " + nuevaMembresia.getNombre());
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(nuevaMembresia.getPrecio());
        detalle.setSubtotal(nuevaMembresia.getPrecio());
        detalle.setTipoItem("MEMBRESIA");
        detalle.setMembresia(nuevaMembresia);
        detalleFacturaRepository.save(detalle);

        Pago pago = new Pago();
        pago.setFactura(factura);
        pago.setFechaPago(LocalDate.now());
        pago.setMetodoPago(metodoPago);
        pago.setMontoPagado(nuevaMembresia.getPrecio().doubleValue());
        pagoRepository.save(pago);

        redirectAttributes.addFlashAttribute("successMessage",
                "Has renovado la membresia " + nuevaMembresia.getNombre() +
                        " hasta el " + fin.format(DATE_FORMATTER) +
                        ". Pago simulado con " + metodoPago + ".");
        return "redirect:/socio/membresia";
    }

    private LocalDate calcularInicioRenovacion(Socio socio) {
        LocalDate hoy = LocalDate.now();
        if (socio.getFechaFinMembresia() != null && socio.getFechaFinMembresia().isAfter(hoy)) {
            return socio.getFechaFinMembresia().plusDays(1);
        }
        return hoy;
    }

    private LocalDate calcularFinRenovacion(LocalDate inicio, Integer duracionDias) {
        int totalDias = duracionDias != null && duracionDias > 0 ? duracionDias : 30;
        return inicio.plusDays(totalDias - 1L);
    }

    private long calcularDiasRestantes(Socio socio) {
        if (socio.getFechaFinMembresia() == null) {
            return 0;
        }
        LocalDate hoy = LocalDate.now();
        if (socio.getFechaFinMembresia().isBefore(hoy)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(hoy, socio.getFechaFinMembresia()) + 1;
    }
}
