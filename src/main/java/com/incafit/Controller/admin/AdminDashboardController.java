// src/main/java/com/incafit/controller/admin/DashboardController.java
package com.incafit.Controller.admin;

import com.incafit.service.EstadisticaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    private static final int DEFAULT_MONTH_RANGE = 6;
    private final EstadisticaService estadisticaService;

    public AdminDashboardController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping
    public String mostrarDashboard(Model model) {
        model.addAttribute("estadisticas", estadisticaService.obtenerEstadisticas());
        model.addAttribute("facturasCriticas", estadisticaService.obtenerFacturasPendientesCriticas(5));
        model.addAttribute("reservasPendientes", estadisticaService.obtenerReservasPendientesHoy());
        return "admin/dashboard";
    }

    @GetMapping("/dashboard/datos/socios-mes")
    @ResponseBody
    public Map<String, Object> obtenerSociosPorMes() {
        return estadisticaService.obtenerSociosPorMes(DEFAULT_MONTH_RANGE);
    }

    @GetMapping("/dashboard/datos/ingresos-mes")
    @ResponseBody
    public Map<String, Object> obtenerIngresosPorMes() {
        return estadisticaService.obtenerIngresosPorMes(DEFAULT_MONTH_RANGE);
    }

    @GetMapping("/dashboard/datos/reservas-estado")
    @ResponseBody
    public Map<String, Object> obtenerReservasPorEstado() {
        return estadisticaService.obtenerReservasPorEstado();
    }
}
