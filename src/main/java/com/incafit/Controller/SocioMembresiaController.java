package com.incafit.Controller;

import com.incafit.Model.Socio;
import com.incafit.Model.Factura;
import com.incafit.Model.DetalleFactura;
import com.incafit.service.SocioService;
import com.incafit.Repository.FacturaRepository;
import com.incafit.Repository.DetalleFacturaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

@Controller
@RequestMapping("/socio")
public class SocioMembresiaController {

    private final SocioService socioService;
    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;

    public SocioMembresiaController(SocioService socioService,
                          FacturaRepository facturaRepository,
                          DetalleFacturaRepository detalleFacturaRepository) {
        this.socioService = socioService;
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    @GetMapping("/membresia")
    public String verMembresia(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        return "membresia";
    }

    @GetMapping("/membresia/renovar")
    public String mostrarRenovacionMembresia(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        return "socio/membresia/renovar";
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
        factura.setEstado("PENDIENTE");
        facturaRepository.save(factura);

        // Crear el detalle de la factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setFactura(factura);
        detalle.setDescripcion("Renovación de Membresía " + socio.getMembresia().getNombre());
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(socio.getMembresia().getPrecio());
        detalle.setSubtotal(socio.getMembresia().getPrecio());
        detalleFacturaRepository.save(detalle);

        redirectAttributes.addFlashAttribute("successMessage", "Membresía renovada con éxito. Se ha generado una factura pendiente de pago.");
        return "redirect:/socio/membresia";
    }
}