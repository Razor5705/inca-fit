package com.incafit.Controller.admin;

import com.incafit.Model.Membresia;
import com.incafit.service.MembresiaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/membresias")
public class MembresiaController {

    private final MembresiaService membresiaService;

    public MembresiaController(MembresiaService membresiaService) {
        this.membresiaService = membresiaService;
    }

    @GetMapping
    public String listarMembresias(Model model) {
        model.addAttribute("membresias", membresiaService.obtenerTodasMembresias());
        return "admin/membresias/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("membresia", new Membresia());
        return "admin/membresias/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMembresia(@ModelAttribute Membresia membresia) {
        membresiaService.guardarMembresia(membresia);
        return "redirect:/admin/membresias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("membresia", membresiaService.obtenerMembresiaPorId(id));
        return "admin/membresias/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMembresia(@PathVariable Long id) {
        membresiaService.eliminarMembresia(id);
        return "redirect:/admin/membresias";
    }
}