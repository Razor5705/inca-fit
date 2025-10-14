package com.incafit.Controller.admin;

import com.incafit.Model.Instructor;
import com.incafit.Repository.InstructorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String guardarInstructor(@ModelAttribute Instructor instructor) {
        instructorRepository.save(instructor);
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
    public String actualizarInstructor(@PathVariable Long id, @ModelAttribute Instructor instructor) {
        instructor.setId(id);
        instructorRepository.save(instructor);
        return "redirect:/admin/instructores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarInstructor(@PathVariable Long id) {
        instructorRepository.deleteById(id);
        return "redirect:/admin/instructores";
    }
}