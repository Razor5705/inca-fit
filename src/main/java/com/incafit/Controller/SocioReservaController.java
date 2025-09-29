// SocioReservaController.java
package com.incafit.Controller;

import com.incafit.Model.Socio;
import com.incafit.Model.Reserva;
import com.incafit.service.FacturaService;
import com.incafit.service.ReservaService;
import com.incafit.service.SocioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/socio")
public class SocioReservaController {

    private final SocioService socioService;
    private final ReservaService reservaService;
    private final FacturaService facturaService;

    public SocioReservaController(SocioService socioService,
                                  ReservaService reservaService,
                                  FacturaService facturaService
                                  ) {
        this.socioService = socioService;
        this.reservaService = reservaService;
        this.facturaService = facturaService;

    }

    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        Socio socio = obtenerSocioActual();
        model.addAttribute("reservas", reservaService.obtenerReservasPorSocio(socio));
        return "socio/reservas/lista";
    }

    @GetMapping("/reservas/nueva")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "socio/reservas/formulario";
    }

    @PostMapping("/reservas/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirect) {
        try {
            Socio socio = obtenerSocioActual();
            reservaService.crearReserva(socio, reserva.getClase(), reserva.getFechaHora());
            redirect.addFlashAttribute("success", "Reserva creada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al crear reserva: " + e.getMessage());
        }
        return "redirect:/socio/reservas";
    }

    @PostMapping("/reservas/{id}/cancelar")
    public String cancelarReserva(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reservaService.cancelarReserva(id);
            redirect.addFlashAttribute("success", "Reserva cancelada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al cancelar reserva: " + e.getMessage());
        }
        return "redirect:/socio/reservas";
    }

    @GetMapping("/facturas")
    public String listarFacturas(Model model) {
        Socio socio = obtenerSocioActual();
        model.addAttribute("facturas", facturaService.obtenerFacturasPorSocio(socio));
        return "socio/facturas/lista";
    }

    @PostMapping("/facturas/{id}/pagar")
    public String pagarFactura(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            facturaService.pagarFactura(id);
            redirect.addFlashAttribute("success", "Factura pagada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al pagar factura: " + e.getMessage());
        }
        return "redirect:/socio/facturas";
    }

    // En SocioReservaController
    private Socio obtenerSocioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return socioService.obtenerSocioPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + email));
    }
}