// src/main/java/com/incafit/controller/admin/FacturaController.java
package com.incafit.Controller.admin;

import com.incafit.Model.Factura;
import com.incafit.service.FacturaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/facturas")
public class FacturaController {
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public String listarFacturas(Model model) {
        List<Factura> facturas = facturaService.obtenerTodasFacturas();
        model.addAttribute("facturas", facturas);
        return "admin/facturas/lista";
    }

    @GetMapping("/{id}")
    public String verFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerFacturaPorId(id);
        model.addAttribute("factura", factura);
        return "admin/facturas/detalle";
    }

    @PostMapping("/{id}/marcar-pagada")
    public String marcarComoPagada(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            facturaService.pagarFactura(id);
            redirect.addFlashAttribute("success", "Factura marcada como pagada");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al actualizar factura: " + e.getMessage());
        }
        return "redirect:/admin/facturas";
    }
}