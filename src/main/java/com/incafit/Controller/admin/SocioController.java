package com.incafit.Controller.admin;

import com.incafit.Model.Socio;
import com.incafit.Model.Membresia;
import com.incafit.service.SocioService;
import com.incafit.service.MembresiaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/socios")
public class SocioController {

    private final SocioService socioService;
    private final MembresiaService membresiaService;

    public SocioController(SocioService socioService,
                           MembresiaService membresiaService) {
        this.socioService = socioService;
        this.membresiaService = membresiaService;
    }

    @GetMapping
    public String listarSocios(Model model) {
        model.addAttribute("socios", socioService.obtenerTodosSocios());
        return "admin/socios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("socio", new Socio());
        model.addAttribute("membresias", membresiaService.obtenerTodasMembresias());
        return "admin/socios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarSocio(@ModelAttribute Socio socio) {
        socioService.guardarSocio(socio);
        return "redirect:/admin/socios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Socio socio = socioService.obtenerSocioPorId(id);
        List<Membresia> membresias = membresiaService.obtenerTodasMembresias();

        model.addAttribute("socio", socio);
        model.addAttribute("membresias", membresias);
        return "admin/socios/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarSocio(@PathVariable Long id) {
        socioService.eliminarSocio(id);
        return "redirect:/admin/socios";
    }
}