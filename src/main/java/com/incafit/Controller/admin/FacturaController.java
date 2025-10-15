package com.incafit.Controller.admin;

import com.incafit.Model.Factura;
import com.incafit.Repository.FacturaRepository;
import com.incafit.Repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/facturas")
public class FacturaController {

    private final FacturaRepository facturaRepository;
    private final SocioRepository socioRepository;

    public FacturaController(FacturaRepository facturaRepository, SocioRepository socioRepository) {
        this.facturaRepository = facturaRepository;
        this.socioRepository = socioRepository;
    }

    @GetMapping
    public String listarFacturas(Model model) {
        List<Factura> facturas = facturaRepository.findAll();
        model.addAttribute("facturas", facturas);
        return "admin/facturas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("factura", new Factura());
        model.addAttribute("socios", socioRepository.findAll());
        return "admin/facturas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute Factura factura) {
        facturaRepository.save(factura);
        return "redirect:/admin/facturas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        model.addAttribute("factura", factura);
        model.addAttribute("socios", socioRepository.findAll());
        return "admin/facturas/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaRepository.deleteById(id);
        return "redirect:/admin/facturas";
    }
}