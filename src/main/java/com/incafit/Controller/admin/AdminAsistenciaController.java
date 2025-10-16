package com.incafit.Controller.admin;

import com.incafit.Model.Asistencia;
import com.incafit.Repository.AsistenciaRepository;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

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
    public String registrarAsistencia(@Valid @ModelAttribute Asistencia asistencia, 
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/asistencias/formulario";
        }
        
        try {
            asistenciaRepository.save(asistencia);
            redirectAttributes.addFlashAttribute("successMessage", "Asistencia registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar la asistencia: " + e.getMessage());
        }
        
        return "redirect:/admin/asistencias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarAsistencia(@PathVariable Long id, Model model) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de asistencia inválido:" + id));
        model.addAttribute("asistencia", asistencia);
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("clases", claseRepository.findAll());
        return "admin/asistencias/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarAsistencia(@PathVariable Long id, 
                                      @Valid @ModelAttribute Asistencia asistencia,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/asistencias/formulario";
        }
        
        try {
            asistencia.setId(id);
            asistenciaRepository.save(asistencia);
            redirectAttributes.addFlashAttribute("successMessage", "Asistencia actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la asistencia: " + e.getMessage());
        }
        
        return "redirect:/admin/asistencias";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAsistencia(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            asistenciaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Asistencia eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la asistencia: " + e.getMessage());
        }
        return "redirect:/admin/asistencias";
    }
}