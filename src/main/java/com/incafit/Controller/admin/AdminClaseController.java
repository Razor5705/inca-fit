package com.incafit.Controller.admin;

import com.incafit.Model.Clase;
import com.incafit.Model.Instructor;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.InstructorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/clases")
public class AdminClaseController {

    private final ClaseRepository claseRepository;
    private final InstructorRepository instructorRepository;

    public AdminClaseController(ClaseRepository claseRepository, InstructorRepository instructorRepository) {
        this.claseRepository = claseRepository;
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public String listarClases(Model model) {
        model.addAttribute("clases", claseRepository.findAll());
        return "admin/clases/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaClase(Model model) {
        model.addAttribute("clase", new Clase());
        model.addAttribute("instructores", instructorRepository.findAll());
        return "admin/clases/formulario";
    }

    @PostMapping("/nueva")
    public String guardarClase(@ModelAttribute Clase clase) {
        claseRepository.save(clase);
        return "redirect:/admin/clases";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarClase(@PathVariable Long id, Model model) {
        Clase clase = claseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID de clase inválido:" + id));
        model.addAttribute("clase", clase);
        model.addAttribute("instructores", instructorRepository.findAll());
        return "admin/clases/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarClase(@PathVariable Long id, @ModelAttribute Clase clase) {
        clase.setId(id);
        claseRepository.save(clase);
        return "redirect:/admin/clases";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarClase(@PathVariable Long id) {
        claseRepository.deleteById(id);
        return "redirect:/admin/clases";
    }
}
