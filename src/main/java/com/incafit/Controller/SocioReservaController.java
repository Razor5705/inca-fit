// SocioReservaController.java
package com.incafit.Controller;

import com.incafit.Model.Clase;
import com.incafit.Model.Socio;
import com.incafit.Model.Reserva;
import com.incafit.Model.Factura;
import com.incafit.service.ClaseService;
import com.incafit.service.ClaseHorarioService;
import com.incafit.service.FacturaService;
import com.incafit.service.ReservaService;
import com.incafit.service.SocioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Controller
@RequestMapping("/socio")
public class SocioReservaController {

    private static final Locale LOCALE_ES = new Locale("es", "ES");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final List<DayOfWeek> DIAS_SEMANA = List.of(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
    );

    private final SocioService socioService;
    private final ReservaService reservaService;
    private final FacturaService facturaService;
    private final ClaseService claseService;
    private final ClaseHorarioService claseHorarioService;

    public SocioReservaController(SocioService socioService,
                                  ReservaService reservaService,
                                  FacturaService facturaService,
                                  ClaseService claseService,
                                  ClaseHorarioService claseHorarioService) {
        this.socioService = socioService;
        this.reservaService = reservaService;
        this.facturaService = facturaService;
        this.claseService = claseService;
        this.claseHorarioService = claseHorarioService;
    }

    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        Socio socio = obtenerSocioActual();
        model.addAttribute("reservas", reservaService.obtenerReservasPorSocio(socio));
        return "socio/reservas/lista";
    }

    @GetMapping("/reservas/calendario")
    public String verCalendarioReservas(Model model) {
        List<Clase> clases = claseService.obtenerTodasLasClases();
        SortedSet<LocalTime> horasDisponibles = new TreeSet<>();
        Map<DayOfWeek, Map<LocalTime, List<Clase>>> matrizHorarios = construirMatrizSemanal(clases, horasDisponibles);

        model.addAttribute("diasSemana", DIAS_SEMANA);
        model.addAttribute("horasDisponibles", horasDisponibles);
        model.addAttribute("matrizHorarios", matrizHorarios);
        model.addAttribute("tieneClases", !horasDisponibles.isEmpty());
        return "socio/reservas/calendario";
    }

    @GetMapping("/reservas/calendario/eventos")
    @ResponseBody
    public List<Map<String, Object>> obtenerEventosCalendario() {
        List<Clase> clases = claseService.obtenerTodasLasClases();
        return generarEventosParaClases(clases);
    }

    @GetMapping("/reservas/nueva")
    public String mostrarFormularioReserva(Model model) {
        List<Clase> clases = claseService.obtenerTodasLasClases();
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("clases", clases);
        model.addAttribute("horariosClases", construirMapaHorarios(clases));
        model.addAttribute("horasClases", construirMapaHoras(clases));
        model.addAttribute("diasPorClase", construirMapaDias(clases));
        return "socio/reservas/formulario";
    }

    @PostMapping("/reservas/guardar")
    public String guardarReserva(@RequestParam("claseId") Long claseId,
                                 @RequestParam(value = "fechaReserva", required = false) String fechaReserva,
                                 @ModelAttribute Reserva reserva,
                                 RedirectAttributes redirect) {
        try {
            Socio socio = obtenerSocioActual();
            Clase claseSeleccionada = claseService.obtenerClasePorId(claseId);
            if (claseSeleccionada == null) {
                throw new IllegalArgumentException("Clase no encontrada");
            }


            LocalDateTime fechaHora = reserva.getFechaHora();
            if (fechaHora == null) {
                if (fechaReserva == null || fechaReserva.isBlank()) {
                    throw new IllegalArgumentException("Debe seleccionar una fecha para la clase");
                }

                LocalDate fecha = LocalDate.parse(fechaReserva);
                DayOfWeek dia = fecha.getDayOfWeek();
                if (!claseHorarioService.obtenerDiasPermitidos(claseSeleccionada.getId()).contains(dia)) {
                    String diaEnEspanol = dia.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                    throw new IllegalArgumentException("La clase seleccionada no se imparte los " + diaEnEspanol + ".");
                }

                LocalDate hoy = LocalDate.now();
                if (fecha.isBefore(hoy)) {
                    throw new IllegalArgumentException("No es posible reservar en una fecha pasada.");
                }

                if (claseSeleccionada.getFechaInicio() != null &&
                        fecha.isBefore(claseSeleccionada.getFechaInicio())) {
                    throw new IllegalArgumentException("La clase seleccionada aún no ha comenzado.");
                }

                if (claseSeleccionada.getFechaFin() != null &&
                        fecha.isAfter(claseSeleccionada.getFechaFin())) {
                    throw new IllegalArgumentException("La clase seleccionada ya ha finalizado.");
                }

                LocalTime hora = claseSeleccionada.getHora();
                if (hora == null) {
                    throw new IllegalStateException("La clase seleccionada no tiene una hora configurada");
                }
                fechaHora = LocalDateTime.of(fecha, hora);
            }

            reservaService.crearReserva(socio, claseId, fechaHora);
            redirect.addFlashAttribute("success", "Reserva creada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al crear reserva: " + e.getMessage());
        }
        return "redirect:/socio/reservas";
    }

    @PostMapping("/reservas/{id}/cancelar")
    public String cancelarReserva(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reservaService.cancelarReserva(id);
            redirect.addFlashAttribute("success", "Reserva cancelada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al cancelar reserva: " + e.getMessage());
        }
        return "redirect:/socio/reservas";
    }

    @GetMapping("/facturas")
    public String listarFacturas(Model model) {
        Socio socio = obtenerSocioActual();
        List<Factura> facturas = facturaService.obtenerFacturasPorSocio(socio);
        long pendientes = facturas.stream()
                .filter(f -> "PENDIENTE".equalsIgnoreCase(f.getEstado()))
                .count();
        BigDecimal totalPendiente = facturas.stream()
                .filter(f -> "PENDIENTE".equalsIgnoreCase(f.getEstado()))
                .map(Factura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Factura facturaDestacada = facturas.stream()
                .filter(f -> "PENDIENTE".equalsIgnoreCase(f.getEstado()))
                .sorted(Comparator.comparing(Factura::getFecha))
                .findFirst()
                .orElse(null);

        model.addAttribute("facturas", facturas);
        model.addAttribute("facturasPendientes", pendientes);
        model.addAttribute("facturasPagadas", facturas.size() - pendientes);
        model.addAttribute("totalPendiente", totalPendiente);
        model.addAttribute("facturaDestacada", facturaDestacada);
        return "socio/facturas/lista";
    }

    @PostMapping("/facturas/{id}/pagar")
    public String pagarFactura(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            facturaService.pagarFactura(id);
            redirect.addFlashAttribute("success", "Factura pagada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al pagar factura: " + e.getMessage());
        }
        return "redirect:/socio/facturas";
    }

    // En SocioReservaController
    private Socio obtenerSocioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return socioService.obtenerSocioConReservasPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + email));
    }

    private Map<Long, String> construirMapaHorarios(List<Clase> clases) {
        Map<Long, String> resultado = new HashMap<>();
        for (Clase clase : clases) {
            String descripcion = formatearDescripcionHorario(clase, clase.getId());
            resultado.put(clase.getId(), descripcion);
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

    private String formatearDescripcionHorario(Clase clase, Long claseId) {
        Set<DayOfWeek> dias = claseHorarioService.obtenerDiasPermitidos(claseId);
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

    private List<Map<String, Object>> generarEventosParaClases(List<Clase> clases) {
        List<Map<String, Object>> eventos = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusWeeks(8);

        for (Clase clase : clases) {
            if (clase.getHora() == null) {
                continue;
            }

            Set<DayOfWeek> diasPermitidos = claseHorarioService.obtenerDiasPermitidos(clase.getId());
            LocalDate inicio = clase.getFechaInicio() != null ? clase.getFechaInicio() : hoy;
            if (inicio.isBefore(hoy)) {
                inicio = hoy;
            }
            LocalDate fin = clase.getFechaFin() != null ? clase.getFechaFin() : limite;
            if (fin.isAfter(limite)) {
                fin = limite;
            }

            for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
                if (!diasPermitidos.contains(fecha.getDayOfWeek())) {
                    continue;
                }
                LocalDateTime inicioEvento = LocalDateTime.of(fecha, clase.getHora());
                LocalDateTime finEvento = clase.getDuracionMinutos() > 0
                        ? inicioEvento.plusMinutes(clase.getDuracionMinutos())
                        : inicioEvento.plusHours(1);

                Map<String, Object> evento = new HashMap<>();
                String titulo = clase.getNombre();
                if (clase.getInstructor() != null) {
                    titulo += " - " + clase.getInstructor().getNombreCompleto();
                }
                evento.put("title", titulo);
                evento.put("start", inicioEvento.toString());
                evento.put("end", finEvento.toString());
                evento.put("extendedProps", Map.of(
                        "capacidad", clase.getCapacidadMaxima(),
                        "instructor", clase.getInstructor() != null ? clase.getInstructor().getNombreCompleto() : "Sin instructor",
                        "descripcion", clase.getDescripcion() != null ? clase.getDescripcion() : ""
                ));
                eventos.add(evento);
            }
        }
        return eventos;
    }

    private Map<DayOfWeek, Map<LocalTime, List<Clase>>> construirMatrizSemanal(List<Clase> clases,
                                                                              SortedSet<LocalTime> horasRegistradas) {
        Map<DayOfWeek, Map<LocalTime, List<Clase>>> matriz = new LinkedHashMap<>();
        for (DayOfWeek dia : DIAS_SEMANA) {
            matriz.put(dia, new TreeMap<>());
        }

        for (Clase clase : clases) {
            if (clase.getHora() == null || !clase.isActivo() || !clase.isVigente()) {
                continue;
            }
            horasRegistradas.add(clase.getHora());
            Set<DayOfWeek> diasPermitidos = claseHorarioService.obtenerDiasPermitidos(clase.getId());
            for (DayOfWeek dia : diasPermitidos) {
                Map<LocalTime, List<Clase>> horasPorDia = matriz.computeIfAbsent(dia, d -> new TreeMap<>());
                horasPorDia.computeIfAbsent(clase.getHora(), h -> new ArrayList<>()).add(clase);
            }
        }

        for (Map<LocalTime, List<Clase>> horasPorDia : matriz.values()) {
            for (LocalTime hora : horasRegistradas) {
                horasPorDia.computeIfAbsent(hora, h -> new ArrayList<>());
            }
        }

        for (Map<LocalTime, List<Clase>> horasPorDia : matriz.values()) {
            for (List<Clase> clasesEnSlot : horasPorDia.values()) {
                clasesEnSlot.sort(Comparator.comparing(Clase::getNombre));
            }
        }

        return matriz;
    }
}
