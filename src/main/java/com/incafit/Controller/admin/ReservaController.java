// src/main/java/com/incafit/controller/admin/ReservaController.java
package com.incafit.Controller.admin;

import com.incafit.Model.Reserva;
import com.incafit.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaService.obtenerTodasReservas();
        model.addAttribute("reservas", reservas);
        return "admin/reservas/lista";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarReserva(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reservaService.cancelarReserva(id);
            redirect.addFlashAttribute("success", "Reserva cancelada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al cancelar reserva: " + e.getMessage());
        }
        return "redirect:/admin/reservas";
    }
}