package com.incafit.Controller;

import com.incafit.Model.DetalleFactura;
import com.incafit.Model.Factura;
import com.incafit.Model.Socio;
import com.incafit.Repository.DetalleFacturaRepository;
import com.incafit.Repository.FacturaRepository;
import com.incafit.service.SocioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import java.time.LocalDate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final SocioService socioService;
    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;

    public DashboardController(SocioService socioService, FacturaRepository facturaRepository, DetalleFacturaRepository detalleFacturaRepository) {
        this.socioService = socioService;
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.obtenerSocioConReservasPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + email));

        model.addAttribute("socio", socio);
        return "dashboard";
    }

    @GetMapping("/perfil")
    public String verPerfil(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        return "perfil";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfil(@RequestParam String nombre, @RequestParam String telefono, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        socio.setNombre(nombre);
        socio.setTelefono(telefono);
        socioService.guardarSocio(socio);

        redirectAttributes.addFlashAttribute("successMessage", "Perfil actualizado con éxito");
        return "redirect:/dashboard/perfil";
    }

    @GetMapping("/membresia")
    public String verMembresia(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        return "membresia";
    }

    @PostMapping("/membresia/renovar")
    public String renovarMembresia(Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        // Lógica de renovación de membresía (simulada)
        // Crear la factura
        Factura factura = new Factura();
        factura.setSocio(socio);
        factura.setFecha(LocalDate.now());
        factura.setTotal(socio.getMembresia().getPrecio());
        facturaRepository.save(factura);

        // Crear el detalle de la factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setFactura(factura);
        detalle.setDescripcion("Renovación de Membresía " + socio.getMembresia().getNombre());
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(socio.getMembresia().getPrecio());
        detalle.setSubtotal(socio.getMembresia().getPrecio());
        detalleFacturaRepository.save(detalle);

        redirectAttributes.addFlashAttribute("successMessage", "Membresía renovada con éxito");
        return "redirect:/dashboard/membresia";
    }
}