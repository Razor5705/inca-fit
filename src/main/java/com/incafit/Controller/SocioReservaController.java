// SocioReservaController.java
package com.incafit.Controller;

import com.incafit.Model.Clase;
import com.incafit.Model.Socio;
import com.incafit.Model.Reserva;
import com.incafit.service.ClaseService;
import com.incafit.service.ClaseHorarioService;
import com.incafit.service.FacturaService;
import com.incafit.service.ReservaService;
import com.incafit.service.SocioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Controller
@RequestMapping("/socio")
public class SocioReservaController {

    private final SocioService socioService;
    private final ReservaService reservaService;
    private final FacturaService facturaService;
    private final ClaseService claseService;
    private final ClaseHorarioService claseHorarioService;

    public SocioReservaController(SocioService socioService,
                                  ReservaService reservaService,
                                  FacturaService facturaService,
                                  ClaseService claseService,
                                  ClaseHorarioService claseHorarioService) {
        this.socioService = socioService;
        this.reservaService = reservaService;
        this.facturaService = facturaService;
        this.claseService = claseService;
        this.claseHorarioService = claseHorarioService;
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
        model.addAttribute("clases", claseService.obtenerTodasLasClases());
        return "socio/reservas/formulario";
    }

    @PostMapping("/reservas/guardar")
    public String guardarReserva(@RequestParam("claseId") Long claseId,
                                 @RequestParam(value = "fechaReserva", required = false) String fechaReserva,
                                 @ModelAttribute Reserva reserva,
                                 RedirectAttributes redirect) {
        try {
            Socio socio = obtenerSocioActual();
            Clase claseSeleccionada = claseService.obtenerClasePorId(claseId);
            if (claseSeleccionada == null) {
                throw new IllegalArgumentException("Clase no encontrada");
            }


            LocalDateTime fechaHora = reserva.getFechaHora();
            if (fechaHora == null) {
                if (fechaReserva == null || fechaReserva.isBlank()) {
                    throw new IllegalArgumentException("Debe seleccionar una fecha para la clase");
                }

                LocalDate fecha = LocalDate.parse(fechaReserva);
                DayOfWeek dia = fecha.getDayOfWeek();
                if (!claseHorarioService.obtenerDiasPermitidos(claseSeleccionada.getId()).contains(dia)) {
                    String diaEnEspanol = dia.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                    throw new IllegalArgumentException("La clase seleccionada no se imparte los " + diaEnEspanol + ".");
                }

                LocalDate hoy = LocalDate.now();
                if (fecha.isBefore(hoy)) {
                    throw new IllegalArgumentException("No es posible reservar en una fecha pasada.");
                }

                if (claseSeleccionada.getFechaInicio() != null &&
                        fecha.isBefore(claseSeleccionada.getFechaInicio())) {
                    throw new IllegalArgumentException("La clase seleccionada aún no ha comenzado.");
                }

                if (claseSeleccionada.getFechaFin() != null &&
                        fecha.isAfter(claseSeleccionada.getFechaFin())) {
                    throw new IllegalArgumentException("La clase seleccionada ya ha finalizado.");
                }

                LocalTime hora = claseSeleccionada.getHora();
                if (hora == null) {
                    throw new IllegalStateException("La clase seleccionada no tiene una hora configurada");
                }
                fechaHora = LocalDateTime.of(fecha, hora);
            }

            reservaService.crearReserva(socio, claseId, fechaHora);
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
        return socioService.obtenerSocioConReservasPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + email));
    }
}
