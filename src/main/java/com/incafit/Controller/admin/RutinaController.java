package com.incafit.Controller.admin;

import com.incafit.Model.Rutina;
import com.incafit.service.RutinaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rutinas")
public class RutinaController {

    private final RutinaService rutinaService;

    public RutinaController(RutinaService rutinaService) {
        this.rutinaService = rutinaService;
    }

    @GetMapping
    public String listarRutinas(Model model) {
        model.addAttribute("rutinas", rutinaService.findAll());
        return "admin/rutinas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("rutina", new Rutina());
        model.addAttribute("pageTitle", "Nueva Rutina");
        return "admin/rutinas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Rutina rutina = rutinaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de rutina inválido:" + id));
        model.addAttribute("rutina", rutina);
        model.addAttribute("pageTitle", "Editar Rutina");
        return "admin/rutinas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarRutina(@Valid @ModelAttribute("rutina") Rutina rutina,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/rutinas/formulario";
        }
        rutinaService.save(rutina);
        redirectAttributes.addFlashAttribute("success", "Rutina guardada exitosamente.");
        return "redirect:/admin/rutinas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarRutina(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rutinaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Rutina eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la rutina.");
        }
        return "redirect:/admin/rutinas";
    }
}
