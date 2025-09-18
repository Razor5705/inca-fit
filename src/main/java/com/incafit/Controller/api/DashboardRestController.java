package com.incafit.Controller.api;

import com.incafit.dto.DataPointDTO;
import com.incafit.service.FacturaService;
import com.incafit.service.ReservaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

    private final FacturaService facturaService;
    private final ReservaService reservaService;

    public DashboardRestController(FacturaService facturaService, ReservaService reservaService) {
        this.facturaService = facturaService;
        this.reservaService = reservaService;
    }

    @GetMapping("/revenue")
    public List<DataPointDTO> getMonthlyRevenue() {
        return facturaService.getMonthlyRevenue();
    }

    @GetMapping("/attendance")
    public List<DataPointDTO> getMonthlyAttendance() {
        return reservaService.getMonthlyAttendance();
    }
}
