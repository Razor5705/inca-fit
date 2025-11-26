package com.incafit.Controller;

import com.incafit.Model.Socio;
import com.incafit.service.EmailService;
import com.incafit.service.SocioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Optional;

@Controller
public class PasswordResetController {

    private final SocioService socioService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    public PasswordResetController(SocioService socioService,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder) {
        this.socioService = socioService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/forgot-password")
    public String mostrarFormulario(Model model) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String procesarReset(@RequestParam("email") String email,
                                RedirectAttributes redirectAttributes) {
        Optional<Socio> socioOpt = socioService.findByEmail(email);
        if (socioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Si el email existe en nuestro sistema, enviaremos un correo con la nueva contraseña.");
            redirectAttributes.addFlashAttribute("tempPassword", null);
            return "redirect:/forgot-password";
        }

        Socio socio = socioOpt.get();
        String nuevaPassword = generarPasswordTemporal();
        socio.setPassword(passwordEncoder.encode(nuevaPassword));
        socioService.guardarSocio(socio);

        String cuerpo = "Hola " + socio.getNombre() + ",\n\n" +
                "Tu nueva contraseña temporal es: " + nuevaPassword + "\n\n" +
                "Por seguridad, cámbiala al iniciar sesión en tu perfil.\n\n" +
                "Equipo Inca Fit";
        try {
            emailService.sendEmail(socio.getEmail(), "Recuperación de contraseña", cuerpo);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Revisa tu correo. Enviamos una contraseña temporal si tu email está registrado.");
            redirectAttributes.addFlashAttribute("tempPassword",
                    "Contraseña temporal generada: " + nuevaPassword + " (solo para demo).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "No pudimos enviar el correo ahora. Inténtalo más tarde.");
            redirectAttributes.addFlashAttribute("tempPassword",
                    "Contraseña temporal generada: " + nuevaPassword + " (solo para demo).");
        }

        return "redirect:/forgot-password";
    }

    private String generarPasswordTemporal() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789@#";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
