package com.incafit.Controller;


import com.incafit.Model.Socio;
import com.incafit.Model.Rol;
import com.incafit.service.SocioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;


import java.time.LocalDate;


@Controller
public class RegistroController {

    private final SocioService socioService;
    private final PasswordEncoder passwordEncoder;

    public RegistroController(SocioService socioService,
                              PasswordEncoder passwordEncoder) {
        this.socioService = socioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("socio", new Socio()); // Ahora funciona con constructor vacío
        return "registro";
    }

    @PostMapping("/procesar-registro")
    public String procesarRegistro(@Valid @ModelAttribute("socio") Socio socio,
                                   BindingResult result,
                                   HttpServletRequest request) {

        System.out.println("=== 🚀 INICIANDO PROCESO DE REGISTRO ===");
        System.out.println("📧 Email recibido: " + socio.getEmail());
        System.out.println("👤 Nombre recibido: " + socio.getNombre());
        System.out.println("🆔 DNI recibido: " + socio.getDni());

        // Verificar si hay errores de validación
        if (result.hasErrors()) {
            System.out.println("❌ ERRORES DE VALIDACIÓN:");
            result.getAllErrors().forEach(error ->
                    System.out.println("   - " + error.getDefaultMessage()));
            return "registro";
        }

        // Verificar si el email ya existe
        if (socioService.existeEmail(socio.getEmail())) {
            System.out.println("❌ Email ya existe: " + socio.getEmail());
            result.rejectValue("email", "error.socio", "El email ya está registrado");
            return "registro";
        }


        try {
            // Codificar la contraseña
            String passwordCodificada = passwordEncoder.encode(socio.getPassword());
            socio.setPassword(passwordCodificada);

            // Asignar valores por defecto
            socio.setRol(Rol.USUARIO);
            socio.setFechaRegistro(LocalDate.now());
            socio.setActivo(true);

            System.out.println("✅ Contraseña codificada correctamente");
            System.out.println("💾 Guardando socio en base de datos...");

            // Guardar el socio
            Socio socioGuardado = socioService.guardarSocio(socio);

            System.out.println("🎉 REGISTRO EXITOSO:");
            System.out.println("   - ID: " + socioGuardado.getId());
            System.out.println("   - Email: " + socioGuardado.getEmail());
            System.out.println("   - DNI: " + socioGuardado.getDni());

            return "redirect:/login?registroExitoso=true";

        } catch (Exception e) {
            System.err.println("💥 ERROR CRÍTICO durante el registro:");
            e.printStackTrace();
            result.rejectValue("email", "error.socio",
                    "Error interno durante el registro. Por favor, intenta nuevamente.");
            return "registro";
        }
    }
}