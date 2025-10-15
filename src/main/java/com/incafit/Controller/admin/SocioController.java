package com.incafit.Controller.admin;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import com.incafit.Repository.MembresiaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/socios")
public class SocioController {

    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;

    public SocioController(SocioRepository socioRepository, MembresiaRepository membresiaRepository) {
        this.socioRepository = socioRepository;
        this.membresiaRepository = membresiaRepository;
    }

    @GetMapping
    public String listarSocios(Model model) {
        List<Socio> socios = socioRepository.findAll();
        model.addAttribute("socios", socios);
        return "admin/socios/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("socio", new Socio());
        model.addAttribute("membresias", membresiaRepository.findAll());
        return "admin/socios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarSocio(@ModelAttribute Socio socio) {
        socioRepository.save(socio);
        return "redirect:/admin/socios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        model.addAttribute("membresias", membresiaRepository.findAll());
        return "admin/socios/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarSocio(@PathVariable Long id) {
        socioRepository.deleteById(id);
        return "redirect:/admin/socios";
    }
}