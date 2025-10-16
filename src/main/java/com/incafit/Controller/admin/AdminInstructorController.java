package com.incafit.Controller.admin;

import com.incafit.Model.Instructor;
import com.incafit.Repository.InstructorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/instructores")
public class AdminInstructorController {

    private final InstructorRepository instructorRepository;

    public AdminInstructorController(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public String listarInstructores(Model model) {
        model.addAttribute("instructores", instructorRepository.findAll());
        return "admin/instructores/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoInstructor(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "admin/instructores/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarInstructor(@Valid @ModelAttribute Instructor instructor, 
                                   BindingResult result, 
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/instructores/formulario";
        }
        
        try {
            instructorRepository.save(instructor);
            redirectAttributes.addFlashAttribute("successMessage", "Instructor guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el instructor: " + e.getMessage());
        }
        
        return "redirect:/admin/instructores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarInstructor(@PathVariable Long id, Model model) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de instructor inválido:" + id));
        model.addAttribute("instructor", instructor);
        return "admin/instructores/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarInstructor(@PathVariable Long id, 
                                      @Valid @ModelAttribute Instructor instructor,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/instructores/formulario";
        }
        
        try {
            instructor.setId(id);
            instructorRepository.save(instructor);
            redirectAttributes.addFlashAttribute("successMessage", "Instructor actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el instructor: " + e.getMessage());
        }
        
        return "redirect:/admin/instructores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarInstructor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            instructorRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Instructor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el instructor: " + e.getMessage());
        }
        return "redirect:/admin/instructores";
    }
}