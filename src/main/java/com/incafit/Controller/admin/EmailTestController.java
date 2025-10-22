package com.incafit.Controller.admin;

import com.incafit.Model.Socio;
import com.incafit.service.EmailService;
import com.incafit.service.SocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para pruebas y envío manual de emails
 * Solo accesible por administradores
 */
@Controller
@RequestMapping("/admin/email-test")
@PreAuthorize("hasRole('ADMIN')")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SocioService socioService;

    @GetMapping
    public String mostrarPanelPruebas(Model model) {
        model.addAttribute("socios", socioService.obtenerTodosSocios());
        return "admin/email-test";
    }

    /**
     * Enviar email de bienvenida de prueba
     */
    @PostMapping("/enviar-bienvenida")
    public String enviarBienvenida(@RequestParam("socioId") Long socioId, 
                                    @RequestParam(value = "html", defaultValue = "false") boolean html,
                                    RedirectAttributes redirect) {
        try {
            Socio socio = socioService.obtenerSocioPorId(socioId);
            if (socio == null) {
                redirect.addFlashAttribute("error", "Socio no encontrado");
                return "redirect:/admin/email-test";
            }
            
            if (html) {
                emailService.sendWelcomeEmailHtml(socio);
                redirect.addFlashAttribute("success", "Email de bienvenida HTML enviado a " + socio.getEmail());
            } else {
                emailService.sendWelcomeEmail(socio);
                redirect.addFlashAttribute("success", "Email de bienvenida (texto) enviado a " + socio.getEmail());
            }
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al enviar email: " + e.getMessage());
        }
        return "redirect:/admin/email-test";
    }

    /**
     * Enviar email de confirmación de reserva de prueba
     */
    @PostMapping("/enviar-confirmacion-reserva")
    public String enviarConfirmacionReserva(@RequestParam("socioId") Long socioId,
                                             @RequestParam(value = "html", defaultValue = "false") boolean html,
                                             RedirectAttributes redirect) {
        try {
            Socio socio = socioService.obtenerSocioPorId(socioId);
            if (socio == null) {
                redirect.addFlashAttribute("error", "Socio no encontrado");
                return "redirect:/admin/email-test";
            }
            
            // Datos de prueba
            String nombreClase = "Yoga Matutino";
            String fecha = "25/10/2024";
            String hora = "10:00";
            
            if (html) {
                emailService.sendReservaConfirmacionEmailHtml(socio, nombreClase, fecha, hora);
                redirect.addFlashAttribute("success", "Email de confirmación HTML enviado a " + socio.getEmail());
            } else {
                emailService.sendReservaConfirmacionEmail(socio, nombreClase, fecha, hora);
                redirect.addFlashAttribute("success", "Email de confirmación (texto) enviado a " + socio.getEmail());
            }
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al enviar email: " + e.getMessage());
        }
        return "redirect:/admin/email-test";
    }

    /**
     * Enviar email de cancelación de reserva de prueba
     */
    @PostMapping("/enviar-cancelacion")
    public String enviarCancelacion(@RequestParam("socioId") Long socioId,
                                     RedirectAttributes redirect) {
        try {
            Socio socio = socioService.obtenerSocioPorId(socioId);
            if (socio == null) {
                redirect.addFlashAttribute("error", "Socio no encontrado");
                return "redirect:/admin/email-test";
            }
            
            // Datos de prueba
            String nombreClase = "CrossFit Avanzado";
            String fecha = "26/10/2024";
            String hora = "18:00";
            
            emailService.sendCancelacionReservaEmail(socio, nombreClase, fecha, hora);
            redirect.addFlashAttribute("success", "Email de cancelación enviado a " + socio.getEmail());
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al enviar email: " + e.getMessage());
        }
        return "redirect:/admin/email-test";
    }

    /**
     * Enviar recordatorio de membresía de prueba
     */
    @PostMapping("/enviar-recordatorio")
    public String enviarRecordatorio(@RequestParam("socioId") Long socioId,
                                      @RequestParam(value = "dias", defaultValue = "7") int dias,
                                      RedirectAttributes redirect) {
        try {
            Socio socio = socioService.obtenerSocioPorId(socioId);
            if (socio == null) {
                redirect.addFlashAttribute("error", "Socio no encontrado");
                return "redirect:/admin/email-test";
            }
            emailService.sendRecordatorioMembresiaEmail(socio, dias);
            redirect.addFlashAttribute("success", "Recordatorio de membresía enviado a " + socio.getEmail());
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al enviar email: " + e.getMessage());
        }
        return "redirect:/admin/email-test";
    }

    /**
     * Enviar email personalizado
     */
    @PostMapping("/enviar-personalizado")
    public String enviarPersonalizado(@RequestParam("socioId") Long socioId,
                                       @RequestParam("asunto") String asunto,
                                       @RequestParam("mensaje") String mensaje,
                                       RedirectAttributes redirect) {
        try {
            Socio socio = socioService.obtenerSocioPorId(socioId);
            if (socio == null) {
                redirect.addFlashAttribute("error", "Socio no encontrado");
                return "redirect:/admin/email-test";
            }
            emailService.sendEmail(socio.getEmail(), asunto, mensaje);
            redirect.addFlashAttribute("success", "Email personalizado enviado a " + socio.getEmail());
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al enviar email: " + e.getMessage());
        }
        return "redirect:/admin/email-test";
    }
}

