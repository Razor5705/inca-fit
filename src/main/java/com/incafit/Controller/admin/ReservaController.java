package com.incafit.Controller.admin;

import com.incafit.Model.Reserva;
import com.incafit.Repository.ReservaRepository;
import com.incafit.Repository.SocioRepository;
import com.incafit.Repository.ClaseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva) {
        reservaRepository.save(reserva);
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

    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaRepository.deleteById(id);
        return "redirect:/admin/reservas";
    }
}