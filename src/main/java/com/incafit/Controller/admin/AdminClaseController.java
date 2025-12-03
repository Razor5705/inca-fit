package com.incafit.Controller.admin;

import com.incafit.Model.Clase;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.InstructorRepository;
import com.incafit.service.ClaseHorarioService;
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

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/admin/clases")
public class AdminClaseController {

    private static final Locale LOCALE_ES = new Locale("es", "ES");
    private static final List<DayOfWeek> DIAS_SEMANA = Arrays.asList(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
    );

    private final ClaseRepository claseRepository;
    private final InstructorRepository instructorRepository;
    private final ClaseHorarioService claseHorarioService;

    public AdminClaseController(ClaseRepository claseRepository,
                                InstructorRepository instructorRepository,
                                ClaseHorarioService claseHorarioService) {
        this.claseRepository = claseRepository;
        this.instructorRepository = instructorRepository;
        this.claseHorarioService = claseHorarioService;
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
        Clase clase = new Clase();
        clase.setDiasSemana(claseHorarioService.diasPorDefecto());
        model.addAttribute("clase", clase);
        prepararModeloFormulario(model);
        model.addAttribute("diasConfigurados", null);
        model.addAttribute("scheduleEjemplo", obtenerEjemploSchedule());
        return "admin/clases/formulario";
    }

    @PostMapping("/nueva")
    public String guardarClase(@Valid @ModelAttribute Clase clase,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        normalizarDias(clase);
        if (clase.getDiasSemana() == null || clase.getDiasSemana().isEmpty()) {
            result.rejectValue("diasSemana", "diasSemana.vacio", "Selecciona al menos un dia para la clase.");
            prepararModeloFormulario(model);
            model.addAttribute("scheduleEjemplo", obtenerEjemploSchedule());
            return "admin/clases/formulario";
        }
        if (result.hasErrors()) {
            prepararModeloFormulario(model);
            model.addAttribute("scheduleEjemplo", obtenerEjemploSchedule());
            return "admin/clases/formulario";
        }

        if (esDuplicada(clase)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ya existe una clase activa con el mismo instructor y hora. Ajusta la hora o desactiva la otra clase.");
            return "redirect:/admin/clases/nueva";
        }

        if (haySolapamiento(clase)) {
            redirectAttributes.addFlashAttribute("errorMessage", "El instructor ya tiene una clase en ese horario y día. Ajusta hora o días.");
            return "redirect:/admin/clases/nueva";
        }

        try {
            claseRepository.save(clase);
            redirectAttributes.addFlashAttribute("successMessage", "Clase guardada exitosamente");
            if (clase.getId() != null) {
                redirectAttributes.addFlashAttribute("infoMessage", "Dias configurados: " + claseHorarioService.diasComoTexto(clase.getId(), LOCALE_ES));
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la clase: " + e.getMessage());
        }
        
        return "redirect:/admin/clases";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarClase(@PathVariable Long id, Model model) {
        Clase clase = claseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID de clase invalido:" + id));
        if (clase.getDiasSemana() == null || clase.getDiasSemana().isEmpty()) {
            Set<DayOfWeek> dias = claseHorarioService.obtenerDiasPermitidos(clase.getId());
            clase.setDiasSemana(dias);
        }
        model.addAttribute("clase", clase);
        prepararModeloFormulario(model);
        model.addAttribute("diasConfigurados", claseHorarioService.diasComoTexto(clase.getId(), LOCALE_ES));
        model.addAttribute("scheduleEjemplo", obtenerEjemploSchedule());
        return "admin/clases/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarClase(@PathVariable Long id,
                                  @Valid @ModelAttribute Clase clase,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        normalizarDias(clase);
        if (clase.getDiasSemana() == null || clase.getDiasSemana().isEmpty()) {
            result.rejectValue("diasSemana", "diasSemana.vacio", "Selecciona al menos un dia para la clase.");
            prepararModeloFormulario(model);
            model.addAttribute("scheduleEjemplo", obtenerEjemploSchedule());
            return "admin/clases/formulario";
        }
        if (result.hasErrors()) {
            prepararModeloFormulario(model);
            model.addAttribute("scheduleEjemplo", obtenerEjemploSchedule());
            return "admin/clases/formulario";
        }

        clase.setId(id);
        if (esDuplicada(clase)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ya existe una clase activa con el mismo instructor y hora. Ajusta la hora o desactiva la otra clase.");
            return "redirect:/admin/clases/editar/" + id;
        }

        if (haySolapamiento(clase)) {
            redirectAttributes.addFlashAttribute("errorMessage", "El instructor ya tiene una clase en ese horario y día. Ajusta hora o días.");
            return "redirect:/admin/clases/editar/" + id;
        }

        try {
            claseRepository.save(clase);
            redirectAttributes.addFlashAttribute("successMessage", "Clase actualizada exitosamente");
            redirectAttributes.addFlashAttribute("infoMessage", "Dias configurados: " + claseHorarioService.diasComoTexto(clase.getId(), LOCALE_ES));
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
                .orElseThrow(() -> new IllegalArgumentException("ID de clase invalido: " + id));
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
                .orElseThrow(() -> new IllegalArgumentException("ID de clase invalido: " + id));
            clase.setActivo(false);
            claseRepository.save(clase);
            redirectAttributes.addFlashAttribute("successMessage", "Clase desactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al desactivar la clase: " + e.getMessage());
        }
        return "redirect:/admin/clases";
    }

    private boolean esDuplicada(Clase clase) {
        if (clase.getInstructor() == null || clase.getInstructor().getId() == null || clase.getHora() == null) {
            return false;
        }
        return claseRepository.findFirstByInstructorIdAndHora(clase.getInstructor().getId(), clase.getHora())
                .filter(existing -> !existing.getId().equals(clase.getId()))
                .isPresent();
    }

    private String obtenerEjemploSchedule() {
        String raw = claseHorarioService.getDefinicionOriginal();
        if (raw != null && !raw.isBlank()) {
            return raw;
        }
        return "Ejemplo en application.properties: clases.schedule=1:MONDAY,WEDNESDAY,FRIDAY;2:TUESDAY,THURSDAY";
    }

    private void prepararModeloFormulario(Model model) {
        model.addAttribute("instructores", instructorRepository.findAll());
        model.addAttribute("diasSemana", DIAS_SEMANA);
    }

    private void normalizarDias(Clase clase) {
        if (clase.getDiasSemana() == null || clase.getDiasSemana().isEmpty()) {
            clase.setDiasSemana(Set.of());
        }
    }

    private boolean haySolapamiento(Clase clase) {
        if (clase.getInstructor() == null || clase.getInstructor().getId() == null || clase.getHora() == null) {
            return false;
        }
        Set<DayOfWeek> diasSeleccionados = clase.getDiasSemana() == null ? Set.of() : clase.getDiasSemana();
        List<Clase> existentes = claseRepository.findByActivoTrueAndInstructorId(clase.getInstructor().getId());
        for (Clase existente : existentes) {
            if (existente.getId() != null && existente.getId().equals(clase.getId())) {
                continue;
            }
            if (existente.getHora() == null || !existente.getHora().equals(clase.getHora())) {
                continue;
            }
            Set<DayOfWeek> diasExistente = existente.getDiasSemana();
            if (diasExistente == null || diasExistente.isEmpty()) {
                diasExistente = claseHorarioService.obtenerDiasPermitidos(existente.getId());
            }
            if (!Collections.disjoint(diasSeleccionados, diasExistente)) {
                return true;
            }
        }
        return false;
    }
}
