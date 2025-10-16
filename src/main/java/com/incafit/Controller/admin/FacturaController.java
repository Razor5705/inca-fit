package com.incafit.Controller.admin;

import com.incafit.Model.Factura;
import com.incafit.Repository.FacturaRepository;
import com.incafit.Repository.SocioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

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

    @PostMapping("/nueva")
    public String guardarFactura(@Valid @ModelAttribute Factura factura, 
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/facturas/formulario";
        }
        
        try {
            facturaRepository.save(factura);
            redirectAttributes.addFlashAttribute("successMessage", "Factura guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la factura: " + e.getMessage());
        }
        
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

    @PostMapping("/editar/{id}")
    public String actualizarFactura(@PathVariable Long id, 
                                   @Valid @ModelAttribute Factura factura,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/facturas/formulario";
        }
        
        try {
            factura.setId(id);
            facturaRepository.save(factura);
            redirectAttributes.addFlashAttribute("successMessage", "Factura actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la factura: " + e.getMessage());
        }
        
        return "redirect:/admin/facturas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            facturaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Factura eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la factura: " + e.getMessage());
        }
        return "redirect:/admin/facturas";
    }
}