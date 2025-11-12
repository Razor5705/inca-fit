package com.incafit.Controller.admin;

import com.incafit.Model.Reserva;
import com.incafit.Model.Clase;
import com.incafit.Repository.ReservaRepository;
import com.incafit.Repository.SocioRepository;
import com.incafit.Repository.ClaseRepository;
import com.incafit.service.ClaseHorarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/reservas")
public class ReservaController {

    private static final Locale LOCALE_ES = new Locale("es", "ES");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final ReservaRepository reservaRepository;
    private final SocioRepository socioRepository;
    private final ClaseRepository claseRepository;
    private final ClaseHorarioService claseHorarioService;

    public ReservaController(ReservaRepository reservaRepository, 
                             SocioRepository socioRepository, 
                             ClaseRepository claseRepository,
                             ClaseHorarioService claseHorarioService) {
        this.reservaRepository = reservaRepository;
        this.socioRepository = socioRepository;
        this.claseRepository = claseRepository;
        this.claseHorarioService = claseHorarioService;
    }

    @GetMapping
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaRepository.findAll();
        model.addAttribute("reservas", reservas);
        return "admin/reservas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        List<Clase> clases = claseRepository.findAll();
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("clases", clases);
        model.addAttribute("horariosClases", construirMapaHorarios(clases));
        model.addAttribute("horasClases", construirMapaHoras(clases));
        model.addAttribute("diasPorClase", construirMapaDias(clases));
        return "admin/reservas/formulario";
    }

    @GetMapping("/ver/{id}")
    public String verReserva(@PathVariable Long id, Model model) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        model.addAttribute("reserva", reserva);
        return "admin/reservas/detalle";
    }

    @PostMapping("/nueva")
    public String guardarReserva(@Valid @ModelAttribute Reserva reserva, 
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/reservas/formulario";
        }
        
        try {
            reservaRepository.save(reserva);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la reserva: " + e.getMessage());
        }
        
        return "redirect:/admin/reservas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        List<Clase> clases = claseRepository.findAll();
        model.addAttribute("reserva", reserva);
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("clases", clases);
        model.addAttribute("horariosClases", construirMapaHorarios(clases));
        model.addAttribute("horasClases", construirMapaHoras(clases));
        model.addAttribute("diasPorClase", construirMapaDias(clases));
        return "admin/reservas/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarReserva(@PathVariable Long id, 
                                   @Valid @ModelAttribute Reserva reserva,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/reservas/formulario";
        }
        
        try {
            reserva.setId(id);
            reservaRepository.save(reserva);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la reserva: " + e.getMessage());
        }
        
        return "redirect:/admin/reservas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la reserva: " + e.getMessage());
        }
        return "redirect:/admin/reservas";
    }

    private Map<Long, String> construirMapaHorarios(List<Clase> clases) {
        Map<Long, String> resultado = new HashMap<>();
        for (Clase clase : clases) {
            resultado.put(clase.getId(), formatearDescripcionHorario(clase));
        }
        return resultado;
    }

    private Map<Long, String> construirMapaHoras(List<Clase> clases) {
        Map<Long, String> resultado = new HashMap<>();
        for (Clase clase : clases) {
            if (clase.getHora() != null) {
                resultado.put(clase.getId(), clase.getHora().format(TIME_FORMATTER));
            } else {
                resultado.put(clase.getId(), "");
            }
        }
        return resultado;
    }

    private Map<Long, String> construirMapaDias(List<Clase> clases) {
        Map<Long, String> resultado = new HashMap<>();
        for (Clase clase : clases) {
            Set<DayOfWeek> dias = claseHorarioService.obtenerDiasPermitidos(clase.getId());
            String diasCodificados = dias.stream()
                    .sorted()
                    .map(DayOfWeek::name)
                    .collect(Collectors.joining(","));
            resultado.put(clase.getId(), diasCodificados);
        }
        return resultado;
    }

    private String formatearDescripcionHorario(Clase clase) {
        Set<DayOfWeek> dias = claseHorarioService.obtenerDiasPermitidos(clase.getId());
        String diasTexto = dias.stream()
                .sorted()
                .map(dia -> {
                    String raw = dia.getDisplayName(TextStyle.FULL, LOCALE_ES);
                    return raw.substring(0, 1).toUpperCase(LOCALE_ES) + raw.substring(1);
                })
                .collect(Collectors.joining(", "));
        if (clase.getHora() != null) {
            return diasTexto + " · " + clase.getHora().format(TIME_FORMATTER) + " h";
        }
        return diasTexto + " · Horario sin definir";
    }
}
