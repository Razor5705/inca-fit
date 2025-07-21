package com.incafit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Muestra index.html en la raíz
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/"; // Redirige a la raíz
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Muestra la página de login
    }


}