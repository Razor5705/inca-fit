package com.incafit.Controller;

import com.incafit.Model.Socio;
import com.incafit.service.SocioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9}$");

    private final SocioService socioService;
    private final PasswordEncoder passwordEncoder;

    public DashboardController(SocioService socioService, PasswordEncoder passwordEncoder) {
        this.socioService = socioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.obtenerSocioConReservasPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + email));

        model.addAttribute("socio", socio);
        return "dashboard";
    }

    @GetMapping("/perfil")
    public String verPerfil(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        return "perfil";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfil(@RequestParam String nombre,
                               @RequestParam String telefono,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        if (telefono == null || !PHONE_PATTERN.matcher(telefono).matches()) {
            redirectAttributes.addFlashAttribute("profileError", "El telefono debe contener 9 digitos sin espacios.");
            return "redirect:/dashboard/perfil";
        }

        socio.setNombre(nombre != null ? nombre.trim() : socio.getNombre());
        socio.setTelefono(telefono);
        socioService.guardarSocio(socio);

        redirectAttributes.addFlashAttribute("profileSuccess", "Perfil actualizado con exito.");
        return "redirect:/dashboard/perfil";
    }

    @PostMapping("/perfil/cambiar-password")
    public String cambiarPassword(@RequestParam("passwordActual") String passwordActual,
                                  @RequestParam("nuevaPassword") String nuevaPassword,
                                  @RequestParam("confirmarPassword") String confirmarPassword,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        if (passwordActual == null || !passwordEncoder.matches(passwordActual, socio.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "La contrasena actual no coincide.");
            return "redirect:/dashboard/perfil";
        }

        if (nuevaPassword == null || nuevaPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("passwordError", "La nueva contrasena debe tener al menos 8 caracteres.");
            return "redirect:/dashboard/perfil";
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "La confirmacion no coincide con la nueva contrasena.");
            return "redirect:/dashboard/perfil";
        }

        if (passwordEncoder.matches(nuevaPassword, socio.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "La nueva contrasena no puede ser igual a la actual.");
            return "redirect:/dashboard/perfil";
        }

        socio.setPassword(passwordEncoder.encode(nuevaPassword));
        socioService.guardarSocio(socio);
        redirectAttributes.addFlashAttribute("passwordSuccess", "Contrasena actualizada correctamente.");
        return "redirect:/dashboard/perfil";
    }

    @GetMapping("/membresia")
    public String verMembresia(Model model, Authentication authentication) {
        String email = authentication.getName();
        Socio socio = socioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        model.addAttribute("socio", socio);
        model.addAttribute("diasRestantes", calcularDiasRestantes(socio));
        return "membresia";
    }

    private long calcularDiasRestantes(Socio socio) {
        if (socio.getFechaFinMembresia() == null) {
            return 0;
        }
        LocalDate hoy = LocalDate.now();
        if (socio.getFechaFinMembresia().isBefore(hoy)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(hoy, socio.getFechaFinMembresia()) + 1;
    }
}
