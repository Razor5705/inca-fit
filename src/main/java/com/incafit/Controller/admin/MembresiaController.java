package com.incafit.Controller.admin;

import com.incafit.Model.Membresia;
import com.incafit.Repository.MembresiaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/membresias")
public class MembresiaController {

    private final MembresiaRepository membresiaRepository;

    public MembresiaController(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    @GetMapping
    public String listarMembresias(Model model) {
        List<Membresia> membresias = membresiaRepository.findAll();
        model.addAttribute("membresias", membresias);
        return "admin/membresias/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("membresia", new Membresia());
        return "admin/membresias/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMembresia(@ModelAttribute Membresia membresia) {
        membresiaRepository.save(membresia);
        return "redirect:/admin/membresias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Membresia membresia = membresiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        model.addAttribute("membresia", membresia);
        return "admin/membresias/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMembresia(@PathVariable Long id) {
        membresiaRepository.deleteById(id);
        return "redirect:/admin/membresias";
    }
}