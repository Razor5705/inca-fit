package com.incafit.Config;

import com.incafit.Model.Clase;
import com.incafit.Repository.ClaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ClaseScheduleInitializer {

    private static final Logger log = LoggerFactory.getLogger(ClaseScheduleInitializer.class);

    @Value("${clases.schedule:}")
    private String definicionHorarios;

    @Bean
    @Transactional
    public ApplicationRunner rellenarDiasDesdePropiedad(ClaseRepository claseRepository) {
        return args -> {
            Map<Long, Set<DayOfWeek>> config = parsear(definicionHorarios);
            if (config.isEmpty()) {
                log.info("clases.schedule vacío o sin entradas válidas; no se rellenan días en BD.");
                return;
            }
            boolean huboCambios = false;
            var clases = claseRepository.findAll();
            for (Clase clase : clases) {
                boolean sinDias = clase.getDiasSemana() == null || clase.getDiasSemana().isEmpty();
                if (!sinDias) {
                    continue; // respetar configuración existente en BD
                }
                Set<DayOfWeek> dias = config.get(clase.getId());
                if (dias != null && !dias.isEmpty()) {
                    clase.setDiasSemana(dias);
                    huboCambios = true;
                    log.info("Asignando días {} a clase id={} desde clases.schedule", dias, clase.getId());
                }
            }
            if (huboCambios) {
                claseRepository.saveAll(clases);
                log.info("Se guardaron los días de clases basados en clases.schedule");
            }
        };
    }

    private Map<Long, Set<DayOfWeek>> parsear(String raw) {
        Map<Long, Set<DayOfWeek>> resultado = new HashMap<>();
        if (raw == null || raw.isBlank()) {
            return resultado;
        }
        Arrays.stream(raw.split(";"))
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
                            resultado.put(claseId, dias);
                        }
                    } catch (Exception ignored) {
                        // entrada inválida, se ignora
                    }
                });
        return resultado;
    }
}
