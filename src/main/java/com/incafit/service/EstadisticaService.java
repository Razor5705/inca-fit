// src/main/java/com/incafit/service/EstadisticaService.java
package com.incafit.service;

import com.incafit.Model.Factura;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.InstructorRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, Object> obtenerSociosPorMes(int meses) {
        List<Socio> socios = socioService.obtenerTodosSocios();
        YearMonth inicio = YearMonth.now().minusMonths(meses - 1);
        Map<YearMonth, Long> conteoPorMes = socios.stream()
                .filter(s -> s.getFechaRegistro() != null)
                .map(s -> YearMonth.from(s.getFechaRegistro()))
                .filter(ym -> !ym.isBefore(inicio))
                .collect(Collectors.groupingBy(ym -> ym, Collectors.counting()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", new Locale("es"));
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (int i = 0; i < meses; i++) {
            YearMonth periodo = inicio.plusMonths(i);
            String label = formatter.format(periodo);
            // Capitalizar primera letra
            label = label.substring(0, 1).toUpperCase(new Locale("es")) + label.substring(1);
            labels.add(label);
            data.add(conteoPorMes.getOrDefault(periodo, 0L));
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("labels", labels);
        resultado.put("data", data);
        return resultado;
    }

    public Map<String, Object> obtenerIngresosPorMes(int meses) {
        List<Factura> facturas = facturaService.obtenerTodasFacturas();
        YearMonth inicio = YearMonth.now().minusMonths(meses - 1);

        Map<YearMonth, BigDecimal> ingresosPorMes = facturas.stream()
                .filter(f -> f.getFecha() != null
                        && f.getEstado() != null
                        && f.getEstado().equalsIgnoreCase("PAGADA"))
                .collect(Collectors.groupingBy(
                        f -> YearMonth.from(f.getFecha()),
                        Collectors.mapping(Factura::getTotal,
                                Collectors.reducing(BigDecimal.ZERO, (a, b) -> {
                                    BigDecimal valorA = a != null ? a : BigDecimal.ZERO;
                                    BigDecimal valorB = b != null ? b : BigDecimal.ZERO;
                                    return valorA.add(valorB);
                                }))))
                .entrySet().stream()
                .filter(entry -> !entry.getKey().isBefore(inicio))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", new Locale("es"));
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (int i = 0; i < meses; i++) {
            YearMonth periodo = inicio.plusMonths(i);
            String label = formatter.format(periodo);
            label = label.substring(0, 1).toUpperCase(new Locale("es")) + label.substring(1);
            labels.add(label);
            BigDecimal monto = ingresosPorMes.getOrDefault(periodo, BigDecimal.ZERO);
            data.add(monto.setScale(2, RoundingMode.HALF_UP).doubleValue());
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("labels", labels);
        resultado.put("data", data);
        return resultado;
    }

    public Map<String, Object> obtenerReservasPorEstado() {
        List<Reserva> reservas = reservaService.obtenerTodasReservas();
        Map<String, Long> conteo = reservas.stream()
                .collect(Collectors.groupingBy(reserva -> {
                    String estado = reserva.getEstado();
                    return estado != null ? estado.toUpperCase() : "SIN ESTADO";
                }, Collectors.counting()));

        List<String> orden = Arrays.asList("CONFIRMADA", "PENDIENTE", "CANCELADA");
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (String estado : orden) {
            if (conteo.containsKey(estado)) {
                labels.add(capitalizar(estado.toLowerCase(Locale.ROOT)));
                data.add(conteo.get(estado));
                conteo.remove(estado);
            }
        }

        long otros = conteo.values().stream().mapToLong(Long::longValue).sum();
        if (otros > 0) {
            labels.add("Otros");
            data.add(otros);
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("labels", labels);
        resultado.put("data", data);
        return resultado;
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        Locale locale = new Locale("es");
        return texto.substring(0, 1).toUpperCase(locale) + texto.substring(1);
    }
}
