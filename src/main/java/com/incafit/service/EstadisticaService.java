// src/main/java/com/incafit/service/EstadisticaService.java
package com.incafit.service;

import com.incafit.Model.Socio;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EstadisticaService {
    private final SocioService socioService;
    private final ReservaService reservaService;
    private final FacturaService facturaService;

    public EstadisticaService(SocioService socioService,
                              ReservaService reservaService,
                              FacturaService facturaService) {
        this.socioService = socioService;
        this.reservaService = reservaService;
        this.facturaService = facturaService;
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Obtener total de socios activos
        long totalSocios = socioService.obtenerTodosSocios().stream()
                .filter(Socio::isActivo)
                .count();
        estadisticas.put("totalSocios", totalSocios);

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