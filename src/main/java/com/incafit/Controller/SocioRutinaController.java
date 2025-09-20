package com.incafit.Controller;

import com.incafit.Model.Rutina;
import com.incafit.service.RutinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/socio/rutinas")
public class SocioRutinaController {

    private final RutinaService rutinaService;

    public SocioRutinaController(RutinaService rutinaService) {
        this.rutinaService = rutinaService;
    }

    @GetMapping
    public String listarRutinas(Model model) {
        model.addAttribute("rutinas", rutinaService.findAll());
        return "socio/rutinas/lista";
    }

    @GetMapping("/{id}")
    public String verRutina(@PathVariable Long id, Model model) {
        Rutina rutina = rutinaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de rutina inválido:" + id));
        model.addAttribute("rutina", rutina);
        return "socio/rutinas/detalle";
    }
}
