// src/main/java/com/incafit/controller/admin/DashboardController.java
package com.incafit.Controller.admin;

import com.incafit.service.EstadisticaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AnimeDashboardController {
    private final EstadisticaService estadisticaService;

    public AnimeDashboardController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping
    public String mostrarDashboard(Model model) {
        model.addAttribute("estadisticas", estadisticaService.obtenerEstadisticas());
        return "admin/dashboard";
    }
}