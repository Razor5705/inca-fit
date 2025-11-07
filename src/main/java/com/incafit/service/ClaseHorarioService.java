package com.incafit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio auxiliar que mantiene el mapa de días permitidos por clase.
 * Se carga desde application.properties usando la propiedad:
 * clases.schedule=1:MONDAY,WEDNESDAY,FRIDAY;2:TUESDAY,THURSDAY
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

    public ClaseHorarioService(@Value("${clases.schedule:}") String definicionHorarios) {
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
        return diasPermitidosPorClase.getOrDefault(claseId, DEFAULT_DIAS);
    }
}
