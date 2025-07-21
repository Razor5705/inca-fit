package com.incafit.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// DashboardController.java
// com/incafit/Controller/DashboardController.java
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Tu lógica existente para el dashboard
        return "dashboard";
    }
}
