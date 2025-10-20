package com.incafit.Controller.admin;

import com.incafit.Model.Clase;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.InstructorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

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
    
    @GetMapping("/activas")
    public String listarClasesActivas(Model model) {
        model.addAttribute("clases", claseRepository.findByActivoTrue());
        model.addAttribute("titulo", "Clases Activas");
        return "admin/clases/lista";
    }
    
    @GetMapping("/inactivas")
    public String listarClasesInactivas(Model model) {
        model.addAttribute("clases", claseRepository.findByActivoFalse());
        model.addAttribute("titulo", "Clases Inactivas");
        return "admin/clases/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaClase(Model model) {
        model.addAttribute("clase", new Clase());
        model.addAttribute("instructores", instructorRepository.findAll());
        return "admin/clases/formulario";
    }

    @PostMapping("/nueva")
    public String guardarClase(@Valid @ModelAttribute Clase clase, 
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/clases/formulario";
        }
        
        try {
            claseRepository.save(clase);
            redirectAttributes.addFlashAttribute("successMessage", "Clase guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la clase: " + e.getMessage());
        }
        
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
    public String actualizarClase(@PathVariable Long id, 
                                 @Valid @ModelAttribute Clase clase,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/clases/formulario";
        }
        
        try {
            clase.setId(id);
            claseRepository.save(clase);
            redirectAttributes.addFlashAttribute("successMessage", "Clase actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la clase: " + e.getMessage());
        }
        
        return "redirect:/admin/clases";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarClase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            claseRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Clase eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la clase: " + e.getMessage());
        }
        return "redirect:/admin/clases";
    }
    
    @PostMapping("/activar/{id}")
    public String activarClase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de clase inválido: " + id));
            clase.setActivo(true);
            claseRepository.save(clase);
            redirectAttributes.addFlashAttribute("successMessage", "Clase activada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al activar la clase: " + e.getMessage());
        }
        return "redirect:/admin/clases";
    }
    
    @PostMapping("/desactivar/{id}")
    public String desactivarClase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de clase inválido: " + id));
            clase.setActivo(false);
            claseRepository.save(clase);
            redirectAttributes.addFlashAttribute("successMessage", "Clase desactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al desactivar la clase: " + e.getMessage());
        }
        return "redirect:/admin/clases";
    }
}