package com.incafit.Controller.admin;

import com.incafit.service.SocioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private final SocioService socioService;

    public AdminApiController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping("/stats/socios-activos")
    public Map<String, Long> getSociosActivosStats() {
        return socioService.contarSociosActivosEInactivos();
    }
}