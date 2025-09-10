package com.incafit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registroExitoso", required = false) String registroExitoso,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
        }

        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión exitosamente");
        }

        if (registroExitoso != null) {
            model.addAttribute("mensaje", "Registro exitoso. Por favor inicia sesión.");
        }

        return "login";
    }
}