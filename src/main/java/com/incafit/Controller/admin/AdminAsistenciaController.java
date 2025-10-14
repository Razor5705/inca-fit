package com.incafit.Controller.admin;

import com.incafit.Model.Asistencia;
import com.incafit.Repository.AsistenciaRepository;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/asistencias")
public class AdminAsistenciaController {

    private final AsistenciaRepository asistenciaRepository;
    private final SocioRepository socioRepository;
    private final ClaseRepository claseRepository;

    public AdminAsistenciaController(AsistenciaRepository asistenciaRepository, SocioRepository socioRepository, ClaseRepository claseRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.socioRepository = socioRepository;
        this.claseRepository = claseRepository;
    }

    @GetMapping
    public String listarAsistencias(Model model) {
        model.addAttribute("asistencias", asistenciaRepository.findAll());
        return "admin/asistencias/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaAsistencia(Model model) {
        model.addAttribute("asistencia", new Asistencia());
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("clases", claseRepository.findAll());
        return "admin/asistencias/formulario";
    }

    @PostMapping("/nueva")
    public String registrarAsistencia(@ModelAttribute Asistencia asistencia) {
        asistenciaRepository.save(asistencia);
        return "redirect:/admin/asistencias";
    }
}
