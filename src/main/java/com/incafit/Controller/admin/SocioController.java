package com.incafit.Controller.admin;

import com.incafit.Model.Socio;
import com.incafit.Model.Membresia;
import com.incafit.service.FacturaService;
import com.incafit.service.ReservaService;
import com.incafit.service.SocioService;
import com.incafit.service.MembresiaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/socios")
public class SocioController {

    private final SocioService socioService;
    private final MembresiaService membresiaService;
    private final ReservaService reservaService;
    private final FacturaService facturaService;

    public SocioController(SocioService socioService,
                           MembresiaService membresiaService,
                           ReservaService reservaService,
                           FacturaService facturaService) {
        this.socioService = socioService;
        this.membresiaService = membresiaService;
        this.reservaService = reservaService;
        this.facturaService = facturaService;
    }

    @GetMapping
    public String listarSocios(Model model) {
        model.addAttribute("socios", socioService.obtenerTodosSocios());
        return "admin/socios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("socio", new Socio());
        model.addAttribute("membresias", membresiaService.findAll());
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
        List<Membresia> membresias = membresiaService.findAll();
        
        model.addAttribute("socio", socio);
        model.addAttribute("membresias", membresias);
        return "admin/socios/formulario";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarSocio(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            socioService.eliminarSocio(id);
            redirect.addFlashAttribute("success", "Socio eliminado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar socio: " + e.getMessage());
        }
        return "redirect:/admin/socios";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam boolean activo,
                                RedirectAttributes redirect) {
        try {
            socioService.cambiarEstadoSocio(id, activo);
            String msg = activo ? "Socio activado correctamente" : "Socio desactivado correctamente";
            redirect.addFlashAttribute("success", msg);
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/socios";
    }

    // En tu SocioController actual
    @GetMapping("/{id}/reservas")
    public String verReservasSocio(@PathVariable Long id, Model model) {
        Socio socio = socioService.obtenerSocioPorId(id);
        model.addAttribute("socio", socio);
        model.addAttribute("reservas", reservaService.obtenerReservasPorSocio(socio));
        return "admin/socios/reservas";
    }

    @GetMapping("/{id}/facturas")
    public String verFacturasSocio(@PathVariable Long id, Model model) {
        Socio socio = socioService.obtenerSocioPorId(id);
        model.addAttribute("socio", socio);
        model.addAttribute("facturas", facturaService.obtenerFacturasPorSocio(socio));
        return "admin/socios/facturas";
    }


}