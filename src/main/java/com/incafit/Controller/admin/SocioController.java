package com.incafit.Controller.admin;

import com.incafit.Model.Membresia;
import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import com.incafit.Repository.MembresiaRepository;
import com.incafit.service.SocioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/socios")
public class SocioController {

    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocioService socioService;

    public SocioController(SocioRepository socioRepository,
                           MembresiaRepository membresiaRepository,
                           PasswordEncoder passwordEncoder,
                           SocioService socioService) {
        this.socioRepository = socioRepository;
        this.membresiaRepository = membresiaRepository;
        this.passwordEncoder = passwordEncoder;
        this.socioService = socioService;
    }

    @GetMapping
    public String listarSocios(Model model) {
        List<Socio> socios = socioRepository.findAll();
        model.addAttribute("socios", socios);
        return "admin/socios/lista";
    }

    @GetMapping({"/nueva", "/nuevo"})
    public String mostrarFormularioNueva(Model model) {
        Socio socio = new Socio();
        socio.setFechaRegistro(LocalDate.now());
        socio.setActivo(true);
        model.addAttribute("socio", socio);
        model.addAttribute("membresias", membresiaRepository.findAll());
        model.addAttribute("membresiaSeleccionada", socio.getMembresia() != null ? socio.getMembresia().getId() : null);
        return "admin/socios/formulario";
    }

    @PostMapping({"/nueva", "/nuevo"})
    public String guardarSocio(@Valid @ModelAttribute("socio") Socio socio,
                               BindingResult result,
                               @RequestParam(value = "membresiaId", required = false) Long membresiaId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        asignarMembresiaDesdeFormulario(socio, membresiaId, result);
        if (socio.getFechaRegistro() == null) {
            socio.setFechaRegistro(LocalDate.now());
        }
        if (result.hasErrors()) {
            model.addAttribute("membresias", membresiaRepository.findAll());
            model.addAttribute("membresiaSeleccionada", membresiaId);
            return "admin/socios/formulario";
        }
        
        try {
            if (socio.getPassword() != null && !socio.getPassword().isBlank()) {
                socio.setPassword(passwordEncoder.encode(socio.getPassword()));
            }
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
        model.addAttribute("membresiaSeleccionada", socio.getMembresia() != null ? socio.getMembresia().getId() : null);
        return "admin/socios/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarSocio(@PathVariable Long id,
                                  @Valid @ModelAttribute("socio") Socio socio,
                                  BindingResult result,
                                  @RequestParam(value = "membresiaId", required = false) Long membresiaId,
                                  @RequestParam(value = "nuevaPassword", required = false) String nuevaPassword,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        asignarMembresiaDesdeFormulario(socio, membresiaId, result);
        if (result.hasErrors()) {
            model.addAttribute("membresias", membresiaRepository.findAll());
            model.addAttribute("membresiaSeleccionada", membresiaId);
            return "admin/socios/formulario";
        }
        
        try {
            Socio existente = socioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

            if (nuevaPassword != null && !nuevaPassword.isBlank()) {
                socio.setPassword(passwordEncoder.encode(nuevaPassword));
            } else {
                socio.setPassword(existente.getPassword());
            }

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
        return ejecutarEliminacion(id, redirectAttributes);
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarSocioLegacy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return ejecutarEliminacion(id, redirectAttributes);
    }

    private String ejecutarEliminacion(Long id, RedirectAttributes redirectAttributes) {
        try {
            socioService.eliminarSocio(id);
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

    private void asignarMembresiaDesdeFormulario(Socio socio,
                                                 Long membresiaId,
                                                 BindingResult result) {
        if (membresiaId == null) {
            result.rejectValue("membresia", "socio.membresia", "Seleccione una membresía");
            return;
        }
        Membresia membresia = membresiaRepository.findById(membresiaId)
                .orElse(null);
        if (membresia == null) {
            result.rejectValue("membresia", "socio.membresia", "La membresía seleccionada no existe");
        } else {
            socio.setMembresia(membresia);
        }
    }
}
