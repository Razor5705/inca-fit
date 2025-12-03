package com.incafit.Model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class DiaSemanaConverter implements AttributeConverter<Set<DayOfWeek>, String> {

    @Override
    public String convertToDatabaseColumn(Set<DayOfWeek> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(DayOfWeek::name)
                .sorted()
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<DayOfWeek> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return EnumSet.noneOf(DayOfWeek.class);
        }
        EnumSet<DayOfWeek> dias = EnumSet.noneOf(DayOfWeek.class);
        Arrays.stream(dbData.split(","))
                .map(String::trim)
                .filter(valor -> !valor.isEmpty())
                .forEach(valor -> {
                    try {
                        dias.add(DayOfWeek.valueOf(valor.toUpperCase(Locale.ROOT)));
                    } catch (IllegalArgumentException ignored) {
                        // Ignorar valores no reconocidos para no romper la carga
                    }
                });
        return dias;
    }
}
