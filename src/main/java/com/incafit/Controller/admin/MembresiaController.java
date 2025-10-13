package com.incafit.Controller.admin;
import com.incafit.Model.Membresia;
import com.incafit.service.MembresiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/membresias")
public class MembresiaController {

    @Autowired
    private MembresiaService membresiaService;

    // READ: Muestra la lista de todas las membresías
    @GetMapping
    public String listarMembresias(Model model) {
        model.addAttribute("membresias", membresiaService.findAll());
        return "admin/membresias/lista";
    }

    // CREATE (Paso 1): Muestra el formulario para crear una nueva membresía
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaMembresia(Model model) {
        model.addAttribute("membresia", new Membresia());
        model.addAttribute("pageTitle", "Crear Nueva Membresía");
        return "admin/membresias/formulario";
    }

    // UPDATE (Paso 1): Muestra el formulario para editar una membresía existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMembresia(@PathVariable Long id, Model model) {
        Membresia membresia = membresiaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de membresía no válido: " + id));
        model.addAttribute("membresia", membresia);
        model.addAttribute("pageTitle", "Editar Membresía");
        return "admin/membresias/formulario";
    }

    // SAVE (Cubre CREATE y UPDATE): Procesa el formulario y guarda los datos
    @PostMapping("/guardar")
    public String guardarMembresia(@ModelAttribute("membresia") Membresia membresia) {
        membresiaService.save(membresia);
        return "redirect:/admin/membresias";
    }

    // DELETE: Elimina una membresía por su ID
    @GetMapping("/eliminar/{id}")
    public String eliminarMembresia(@PathVariable Long id) {
        membresiaService.deleteById(id);
        return "redirect:/admin/membresias";
    }
}