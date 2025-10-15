package com.incafit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactoController {

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("email", "incafit.soporte@gmail.com");
        model.addAttribute("telefono", "+34 603180852");
        return "contacto";
    }
}
