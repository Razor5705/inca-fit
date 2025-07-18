package com.incafit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "¡Bienvenido a Inca Fit!");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /*
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }
     */
}