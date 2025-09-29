package com.incafit.Controller;

import com.incafit.Model.Socio;
import com.incafit.service.SocioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final SocioService socioService;

    public DashboardController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.obtenerSocioPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + email));

        model.addAttribute("socio", socio);
        model.addAttribute("username", email); // Passing username for consistency with the template
        return "dashboard";
    }
}