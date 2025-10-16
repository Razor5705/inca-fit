package com.incafit.Controller.admin;

import com.incafit.Model.Reserva;
import com.incafit.Repository.ReservaRepository;
import com.incafit.Repository.SocioRepository;
import com.incafit.Repository.ClaseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/admin/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final SocioRepository socioRepository;
    private final ClaseRepository claseRepository;

    public ReservaController(ReservaRepository reservaRepository, 
                            SocioRepository socioRepository, 
                            ClaseRepository claseRepository) {
        this.reservaRepository = reservaRepository;
        this.socioRepository = socioRepository;
        this.claseRepository = claseRepository;
    }

    @GetMapping
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaRepository.findAll();
        model.addAttribute("reservas", reservas);
        return "admin/reservas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("clases", claseRepository.findAll());
        return "admin/reservas/formulario";
    }

    @PostMapping("/nueva")
    public String guardarReserva(@Valid @ModelAttribute Reserva reserva, 
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/reservas/formulario";
        }
        
        try {
            reservaRepository.save(reserva);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la reserva: " + e.getMessage());
        }
        
        return "redirect:/admin/reservas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        model.addAttribute("reserva", reserva);
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("clases", claseRepository.findAll());
        return "admin/reservas/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarReserva(@PathVariable Long id, 
                                   @Valid @ModelAttribute Reserva reserva,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/reservas/formulario";
        }
        
        try {
            reserva.setId(id);
            reservaRepository.save(reserva);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la reserva: " + e.getMessage());
        }
        
        return "redirect:/admin/reservas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la reserva: " + e.getMessage());
        }
        return "redirect:/admin/reservas";
    }
}