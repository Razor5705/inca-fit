package com.incafit.Controller.admin;

import com.incafit.Model.Membresia;
import com.incafit.Repository.MembresiaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/admin/membresias")
public class MembresiaController {

    private final MembresiaRepository membresiaRepository;

    public MembresiaController(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    @GetMapping
    public String listarMembresias(Model model) {
        List<Membresia> membresias = membresiaRepository.findAll();
        model.addAttribute("membresias", membresias);
        return "admin/membresias/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("membresia", new Membresia());
        return "admin/membresias/formulario";
    }

    @PostMapping("/nueva")
    public String guardarMembresia(@Valid @ModelAttribute Membresia membresia, 
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/membresias/formulario";
        }
        
        try {
            membresiaRepository.save(membresia);
            redirectAttributes.addFlashAttribute("successMessage", "Membresía guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la membresía: " + e.getMessage());
        }
        
        return "redirect:/admin/membresias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Membresia membresia = membresiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        model.addAttribute("membresia", membresia);
        return "admin/membresias/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarMembresia(@PathVariable Long id, 
                                     @Valid @ModelAttribute Membresia membresia,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/membresias/formulario";
        }
        
        try {
            membresia.setId(id);
            membresiaRepository.save(membresia);
            redirectAttributes.addFlashAttribute("successMessage", "Membresía actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la membresía: " + e.getMessage());
        }
        
        return "redirect:/admin/membresias";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMembresia(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            membresiaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Membresía eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la membresía: " + e.getMessage());
        }
        return "redirect:/admin/membresias";
    }
}