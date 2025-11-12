package com.incafit.Controller.admin;

import com.incafit.Model.Asistencia;
import com.incafit.Repository.AsistenciaRepository;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.ReservaRepository;
import com.incafit.Repository.SocioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/asistencias")
public class AdminAsistenciaController {

    private final AsistenciaRepository asistenciaRepository;
    private final SocioRepository socioRepository;
    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;

    public AdminAsistenciaController(AsistenciaRepository asistenciaRepository,
                                     SocioRepository socioRepository,
                                     ClaseRepository claseRepository,
                                     ReservaRepository reservaRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.socioRepository = socioRepository;
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping
    public String listarAsistencias(Model model) {
        model.addAttribute("asistencias", asistenciaRepository.findAll());
        return "admin/asistencias/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaAsistencia(Model model) {
        model.addAttribute("asistencia", new Asistencia());
        prepararListas(model);
        return "admin/asistencias/formulario";
    }

    @GetMapping("/ver/{id}")
    public String verAsistencia(@PathVariable Long id, Model model) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de asistencia invalido: " + id));
        model.addAttribute("asistencia", asistencia);
        return "admin/asistencias/detalle";
    }

    @PostMapping("/nueva")
    public String registrarAsistencia(@Valid @ModelAttribute Asistencia asistencia,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        if (result.hasErrors()) {
            prepararListas(model);
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
                .orElseThrow(() -> new IllegalArgumentException("ID de asistencia invalido: " + id));
        model.addAttribute("asistencia", asistencia);
        prepararListas(model);
        return "admin/asistencias/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarAsistencia(@PathVariable Long id,
                                       @Valid @ModelAttribute Asistencia asistencia,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        if (result.hasErrors()) {
            prepararListas(model);
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

    private void prepararListas(Model model) {
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("clases", claseRepository.findAll());
        model.addAttribute("reservas", reservaRepository.findAll());
    }
}
