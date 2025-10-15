// src/main/java/com/incafit/service/EstadisticaService.java
package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.InstructorRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EstadisticaService {
    private final SocioService socioService;
    private final ReservaService reservaService;
    private final FacturaService facturaService;
    private final ClaseRepository claseRepository;
    private final InstructorRepository instructorRepository;

    public EstadisticaService(SocioService socioService,
                              ReservaService reservaService,
                              FacturaService facturaService,
                              ClaseRepository claseRepository,
                              InstructorRepository instructorRepository) {
        this.socioService = socioService;
        this.reservaService = reservaService;
        this.facturaService = facturaService;
        this.claseRepository = claseRepository;
        this.instructorRepository = instructorRepository;
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Obtener total de socios activos
        long totalSocios = socioService.obtenerTodosSocios().stream()
                .filter(Socio::isActivo)
                .count();
        estadisticas.put("totalSocios", totalSocios);

        // Obtener total de clases
        long totalClases = claseRepository.count();
        estadisticas.put("totalClases", totalClases);

        // Obtener total de instructores
        long totalInstructores = instructorRepository.count();
        estadisticas.put("totalInstructores", totalInstructores);

        // Obtener reservas de hoy
        long reservasHoy = reservaService.obtenerReservasPorFecha(LocalDate.now()).size();
        estadisticas.put("reservasHoy", reservasHoy);

        // Obtener facturas pendientes
        long facturasPendientes = facturaService.obtenerFacturasPorEstado("PENDIENTE").size();
        estadisticas.put("facturasPendientes", facturasPendientes);

        // Calcular ingresos mensuales
        double ingresosMensuales = facturaService.obtenerFacturasPagadasEsteMes().stream()
                .mapToDouble(f -> f.getTotal().doubleValue())
                .sum();
        estadisticas.put("ingresosMensuales", ingresosMensuales);

        return estadisticas;
    }
}