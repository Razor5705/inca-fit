package com.incafit.Controller.admin;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import com.incafit.Repository.MembresiaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/admin/socios")
public class SocioController {

    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;

    public SocioController(SocioRepository socioRepository, MembresiaRepository membresiaRepository) {
        this.socioRepository = socioRepository;
        this.membresiaRepository = membresiaRepository;
    }

    @GetMapping
    public String listarSocios(Model model) {
        List<Socio> socios = socioRepository.findAll();
        model.addAttribute("socios", socios);
        return "admin/socios/lista";
    }

    @GetMapping({"/nueva", "/nuevo"})
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("socio", new Socio());
        model.addAttribute("membresias", membresiaRepository.findAll());
        return "admin/socios/formulario";
    }

    @PostMapping({"/nueva", "/nuevo"})
    public String guardarSocio(@Valid @ModelAttribute Socio socio, 
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/socios/formulario";
        }
        
        try {
            socioRepository.save(socio);
            redirectAttributes.addFlashAttribute("successMessage", "Socio guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el socio: " + e.getMessage());
        }
        
        return "redirect:/admin/socios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        model.addAttribute("membresias", membresiaRepository.findAll());
        return "admin/socios/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarSocio(@PathVariable Long id, 
                                 @Valid @ModelAttribute Socio socio,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/socios/formulario";
        }
        
        try {
            socio.setId(id);
            socioRepository.save(socio);
            redirectAttributes.addFlashAttribute("successMessage", "Socio actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el socio: " + e.getMessage());
        }
        
        return "redirect:/admin/socios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarSocio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            socioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Socio eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el socio: " + e.getMessage());
        }
        return "redirect:/admin/socios";
    }

    @PostMapping("/{id}/estado")
    public String actualizarEstado(@PathVariable Long id,
                                   @RequestParam("activo") boolean activo,
                                   RedirectAttributes redirectAttributes) {
        try {
            Socio socio = socioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
            socio.setActivo(activo);
            socioRepository.save(socio);
            redirectAttributes.addFlashAttribute("successMessage",
                    activo ? "Socio activado exitosamente" : "Socio desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el estado del socio: " + e.getMessage());
        }
        return "redirect:/admin/socios";
    }
}
