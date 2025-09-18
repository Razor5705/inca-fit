package com.incafit.Controller.admin;

import com.incafit.Model.Clase;
import com.incafit.service.ClaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.incafit.Model.Clase;
import com.incafit.service.ClaseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping
    public String listarClases(Model model) {
        List<Clase> clases = claseService.findAll();
        model.addAttribute("clases", clases);
        return "admin/clases/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("clase", new Clase());
        model.addAttribute("pageTitle", "Nueva Clase");
        return "admin/clases/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Clase clase = claseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de clase inválido:" + id));
        model.addAttribute("clase", clase);
        model.addAttribute("pageTitle", "Editar Clase");
        return "admin/clases/formulario";
    }

    @PostMapping("/guardar")
    public String guardarClase(@Valid @ModelAttribute("clase") Clase clase,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/clases/formulario";
        }
        claseService.save(clase);
        redirectAttributes.addFlashAttribute("success", "Clase guardada exitosamente.");
        return "redirect:/admin/clases";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarClase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            claseService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Clase eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la clase.");
        }
        return "redirect:/admin/clases";
    }
}
