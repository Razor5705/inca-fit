package com.incafit.Controller;


import com.incafit.Model.Socio;
import com.incafit.Model.Rol;
import com.incafit.service.SocioService;
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
                                   BindingResult result, Model model) {

        System.out.println("Iniciando proceso de registro para: " + socio.getEmail());

        if (result.hasErrors()) {
            System.out.println("Errores de validación: " + result.getAllErrors());
            return "registro";
        }

        if (socioService.existeEmail(socio.getEmail())) {
            System.out.println("Email ya existe: " + socio.getEmail());
            result.rejectValue("email", "error.socio", "El email ya está registrado");
            return "registro";
        }

        try {
            // asignar rol

            socio.setRol(Rol.USUARIO);
            socio.setFechaRegistro(LocalDate.now());
            socio.setActivo(true);

            System.out.println("Guardando socio en BD: " + socio.getEmail());
            Socio socioGuardado = socioService.guardarSocio(socio);
            System.out.println("Socio guardado con ID: " + socioGuardado.getId());

            return "redirect:/login?registroExitoso=true";

        } catch (Exception e) {
            System.err.println("Error al guardar socio: " + e.getMessage());
            e.printStackTrace();
            result.rejectValue("email", "error.socio", "Error en el registro. Intenta nuevamente.");
            return "registro";
        }
    }
}