package com.incafit.service;

import com.incafit.Model.Clase;
import com.incafit.Repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio auxiliar que mantiene el mapa de dias permitidos por clase.
 * Se carga desde application.properties usando la propiedad:
 * clases.schedule=1:MONDAY,WEDNESDAY,FRIDAY;2:TUESDAY,THURSDAY
 * y ahora tambien desde la columna dias_semana de la entidad Clase.
 */
@Component
public class ClaseHorarioService {

    private final Map<Long, Set<DayOfWeek>> diasPermitidosPorClase = new HashMap<>();
    private static final Set<DayOfWeek> DEFAULT_DIAS = EnumSet.of(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );

    private final ClaseRepository claseRepository;
    private final String definicionOriginal;

    public ClaseHorarioService(@Value("${clases.schedule:}") String definicionHorarios,
                               ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
        this.definicionOriginal = definicionHorarios == null ? "" : definicionHorarios;
        cargarDesdePropiedades(definicionHorarios);
    }

    private void cargarDesdePropiedades(String definicionHorarios) {
        if (definicionHorarios == null || definicionHorarios.isBlank()) {
            return;
        }
        Arrays.stream(definicionHorarios.split(";"))
                .map(String::trim)
                .filter(entry -> !entry.isEmpty())
                .forEach(entry -> {
                    String[] partes = entry.split(":");
                    if (partes.length != 2) {
                        return;
                    }
                    try {
                        Long claseId = Long.valueOf(partes[0].trim());
                        Set<DayOfWeek> dias = Arrays.stream(partes[1].split(","))
                                .map(String::trim)
                                .filter(dia -> !dia.isEmpty())
                                .map(dia -> DayOfWeek.valueOf(dia.toUpperCase(Locale.ROOT)))
                                .collect(Collectors.toCollection(() -> EnumSet.noneOf(DayOfWeek.class)));
                        if (!dias.isEmpty()) {
                            diasPermitidosPorClase.put(claseId, dias);
                        }
                    } catch (Exception ignored) {
                        // Si ocurre un error al parsear, dejamos que la clase use el default.
                    }
                });
    }

    public Set<DayOfWeek> obtenerDiasPermitidos(Long claseId) {
        if (claseId != null) {
            Set<DayOfWeek> diasConfigurados = claseRepository.findById(claseId)
                    .map(Clase::getDiasSemana)
                    .filter(dias -> dias != null && !dias.isEmpty())
                    .map(this::copiarDias)
                    .orElse(null);
            if (diasConfigurados != null && !diasConfigurados.isEmpty()) {
                return diasConfigurados;
            }
        }

        if (diasPermitidosPorClase.containsKey(claseId)) {
            return copiarDias(diasPermitidosPorClase.get(claseId));
        }

        return copiarDias(DEFAULT_DIAS);
    }

    public boolean tieneConfiguracion(Long claseId) {
        if (claseId != null) {
            boolean configuradaEnClase = claseRepository.findById(claseId)
                    .map(Clase::getDiasSemana)
                    .map(dias -> dias != null && !dias.isEmpty())
                    .orElse(false);
            if (configuradaEnClase) {
                return true;
            }
        }
        return definicionOriginal != null && !definicionOriginal.isBlank() && diasPermitidosPorClase.containsKey(claseId);
    }

    public String getDefinicionOriginal() {
        return definicionOriginal;
    }

    public Set<DayOfWeek> diasPorDefecto() {
        return copiarDias(DEFAULT_DIAS);
    }

    public String diasComoTexto(Long claseId, Locale locale) {
        Set<DayOfWeek> dias = obtenerDiasPermitidos(claseId);
        return dias.stream()
                .sorted()
                .map(d -> d.getDisplayName(TextStyle.FULL, locale))
                .map(this::capitalizar)
                .collect(Collectors.joining(", "));
    }

    private Set<DayOfWeek> copiarDias(Set<DayOfWeek> dias) {
        if (dias == null || dias.isEmpty()) {
            return EnumSet.noneOf(DayOfWeek.class);
        }
        return EnumSet.copyOf(dias);
    }

    private String capitalizar(String nombreDia) {
        if (nombreDia == null || nombreDia.isBlank()) {
            return "";
        }
        return Character.toUpperCase(nombreDia.charAt(0)) + nombreDia.substring(1);
    }
}
