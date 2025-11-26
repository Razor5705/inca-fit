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
import com.incafit.dto.ResumenPago;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/socio")
public class SocioMembresiaController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");
    private static final BigDecimal DESCUENTO_FIDELIDAD = new BigDecimal("0.05");
    private static final String METODO_TARJETA_GUARDADA = "Tarjeta guardada";
    private static final String METODO_TARJETA_NUEVA = "Tarjeta nueva";
    private static final String METODO_TRANSFERENCIA = "Transferencia bancaria";

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
        ResumenPago resumenPago = construirResumenPago(membresiaSeleccionada, socio);

        String metodoSeleccionado = model.containsAttribute("metodoSeleccionado")
                ? (String) model.asMap().get("metodoSeleccionado")
                : METODO_TARJETA_GUARDADA;

        model.addAttribute("socio", socio);
        model.addAttribute("membresias", planes);
        model.addAttribute("selectedMembresiaId", selectedId);
        model.addAttribute("renovacionInicio", inicioRenovacion);
        model.addAttribute("renovacionInicioTexto", inicioRenovacion.format(DATE_FORMATTER));
        model.addAttribute("renovacionFinTexto", finEstimada.format(DATE_FORMATTER));
        model.addAttribute("diasRestantes", calcularDiasRestantes(socio));
        model.addAttribute("metodosPagoDisponibles", List.of(
                METODO_TARJETA_GUARDADA,
                METODO_TARJETA_NUEVA,
                METODO_TRANSFERENCIA
        ));
        model.addAttribute("metodoSeleccionado", metodoSeleccionado);
        model.addAttribute("resumenPago", resumenPago);
        model.addAttribute("igvRate", IGV_RATE);
        model.addAttribute("descuentoRate", aplicaDescuentoFidelidad(socio) ? DESCUENTO_FIDELIDAD : BigDecimal.ZERO);
        model.addAttribute("descuentoActivo", aplicaDescuentoFidelidad(socio));
        return "socio/membresia/renovar";
    }

    @PostMapping("/membresia/renovar")
    public String renovarMembresia(@RequestParam("membresiaId") Long membresiaId,
                                   @RequestParam(value = "metodoPago", required = false, defaultValue = "Tarjeta guardada") String metodoPago,
                                   @RequestParam(value = "titularTarjeta", required = false) String titularTarjeta,
                                   @RequestParam(value = "numeroTarjeta", required = false) String numeroTarjeta,
                                   @RequestParam(value = "fechaTarjeta", required = false) String fechaTarjeta,
                                   @RequestParam(value = "cvvTarjeta", required = false) String cvvTarjeta,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        Socio socio = socioService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        redirectAttributes.addFlashAttribute("metodoSeleccionado", metodoPago);
        redirectAttributes.addFlashAttribute("titularTarjeta", titularTarjeta);

        Membresia nuevaMembresia = membresiaService.findById(membresiaId)
                .orElseThrow(() -> new RuntimeException("La membresia seleccionada no existe"));

        LocalDate inicio = calcularInicioRenovacion(socio);
        LocalDate fin = calcularFinRenovacion(inicio, nuevaMembresia.getDuracionDias());
        ResumenPago resumenPago = construirResumenPago(nuevaMembresia, socio);

        if (METODO_TARJETA_NUEVA.equalsIgnoreCase(metodoPago)) {
            List<String> erroresTarjeta = validarDatosTarjeta(titularTarjeta, numeroTarjeta, fechaTarjeta, cvvTarjeta);
            if (!erroresTarjeta.isEmpty()) {
                redirectAttributes.addFlashAttribute("tarjetaErrores", erroresTarjeta);
                redirectAttributes.addFlashAttribute("pagoError", "Corrige los datos de la tarjeta para continuar.");
                return "redirect:/socio/membresia/renovar";
            }
        }

        PasarelaResult resultadoPasarela = simularPasarela(metodoPago, numeroTarjeta);
        if (resultadoPasarela.estado == EstadoPasarela.ERROR) {
            redirectAttributes.addFlashAttribute("pagoError", resultadoPasarela.mensaje);
            return "redirect:/socio/membresia/renovar";
        }

        socio.setMembresia(nuevaMembresia);
        socio.setFechaInicioMembresia(inicio);
        socio.setFechaFinMembresia(fin);
        socio.setActivo(true);
        socioService.guardarSocio(socio);

        Factura factura = new Factura();
        factura.setSocio(socio);
        factura.setFecha(LocalDate.now());
        factura.setTotal(resumenPago.getTotal());
        factura.setEstado(resultadoPasarela.estado == EstadoPasarela.PENDIENTE ? "PENDIENTE" : "PAGADA");
        facturaRepository.save(factura);

        DetalleFactura detalle = new DetalleFactura();
        detalle.setFactura(factura);
        detalle.setDescripcion("Renovacion de membresia " + nuevaMembresia.getNombre());
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(nuevaMembresia.getPrecio());
        detalle.setSubtotal(resumenPago.getSubtotal());
        detalle.setTipoItem("MEMBRESIA");
        detalle.setMembresia(nuevaMembresia);
        detalleFacturaRepository.save(detalle);

        if (resultadoPasarela.estado == EstadoPasarela.APROBADO) {
            Pago pago = new Pago();
            pago.setFactura(factura);
            pago.setFechaPago(LocalDate.now());
            pago.setMetodoPago(metodoPago);
            pago.setMontoPagado(resumenPago.getTotal().doubleValue());
            pagoRepository.save(pago);
        }

        if (resultadoPasarela.estado == EstadoPasarela.PENDIENTE) {
            redirectAttributes.addFlashAttribute("pagoPendiente", true);
            redirectAttributes.addFlashAttribute("mensajePasarela", resultadoPasarela.mensaje);
        } else {
            redirectAttributes.addFlashAttribute("pagoExito", true);
            redirectAttributes.addFlashAttribute("referenciaPago", resultadoPasarela.referencia);
            redirectAttributes.addFlashAttribute("mensajePasarela", resultadoPasarela.mensaje);
        }

        redirectAttributes.addFlashAttribute("resumenRenovacion", Map.of(
                "plan", nuevaMembresia.getNombre(),
                "fin", fin.format(DATE_FORMATTER),
                "metodo", metodoPago
        ));

        return "redirect:/socio/membresia/renovar";
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

    private boolean aplicaDescuentoFidelidad(Socio socio) {
        return calcularDiasRestantes(socio) > 0 && socio.getMembresia() != null;
    }

    private ResumenPago construirResumenPago(Membresia membresia, Socio socio) {
        BigDecimal subtotal = membresia.getPrecio() != null ? membresia.getPrecio() : BigDecimal.ZERO;
        BigDecimal impuesto = subtotal.multiply(IGV_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal descuento = BigDecimal.ZERO;
        if (aplicaDescuentoFidelidad(socio)) {
            descuento = subtotal.multiply(DESCUENTO_FIDELIDAD).setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal total = subtotal.add(impuesto).subtract(descuento).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        return new ResumenPago(subtotal, impuesto, descuento, total);
    }

    private List<String> validarDatosTarjeta(String titular, String numero, String fecha, String cvv) {
        List<String> errores = new ArrayList<>();
        if (titular == null || titular.trim().isEmpty()) {
            errores.add("El nombre del titular es obligatorio.");
        }
        if (numero == null || numero.trim().isEmpty()) {
            errores.add("El numero de tarjeta es obligatorio.");
        } else {
            String normalizado = numero.replaceAll("\\s", "");
            if (!normalizado.matches("\\d{16}")) {
                errores.add("El numero de tarjeta debe contener 16 digitos.");
            }
        }
        if (fecha == null || !fecha.matches("\\d{2}/\\d{2}")) {
            errores.add("La fecha debe tener el formato MM/YY.");
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            errores.add("El CVV debe tener 3 digitos.");
        }
        return errores;
    }

    private PasarelaResult simularPasarela(String metodoPago, String numeroTarjeta) {
        String metodoNormalizado = metodoPago == null ? "" : metodoPago.trim();
        if (METODO_TRANSFERENCIA.equalsIgnoreCase(metodoNormalizado)) {
            return new PasarelaResult(EstadoPasarela.PENDIENTE, null,
                    "Generamos la orden de transferencia. Recuerda subir el comprobante en un plazo de 48 horas.");
        }

        pausarPasarela();
        if (METODO_TARJETA_GUARDADA.equalsIgnoreCase(metodoNormalizado)) {
            return new PasarelaResult(EstadoPasarela.APROBADO, generarReferencia(),
                    "Pago aprobado con tu tarjeta guardada.");
        }

        if (METODO_TARJETA_NUEVA.equalsIgnoreCase(metodoNormalizado)) {
            String terminacion = "";
            if (numeroTarjeta != null) {
                String soloDigitos = numeroTarjeta.replaceAll("\\D", "");
                if (soloDigitos.length() >= 4) {
                    terminacion = soloDigitos.substring(soloDigitos.length() - 4);
                }
            }
            return new PasarelaResult(EstadoPasarela.APROBADO, generarReferencia(),
                    terminacion.isEmpty()
                            ? "Pago aprobado con la tarjeta nueva."
                            : "Pago aprobado con la tarjeta **** " + terminacion + ".");
        }

        return new PasarelaResult(EstadoPasarela.ERROR, null, "Metodo de pago no soportado.");
    }

    private void pausarPasarela() {
        try {
            Thread.sleep(1400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String generarReferencia() {
        return "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private enum EstadoPasarela { APROBADO, PENDIENTE, ERROR }

    private static class PasarelaResult {
        private final EstadoPasarela estado;
        private final String referencia;
        private final String mensaje;

        PasarelaResult(EstadoPasarela estado, String referencia, String mensaje) {
            this.estado = estado;
            this.referencia = referencia;
            this.mensaje = mensaje;
        }
    }
}
