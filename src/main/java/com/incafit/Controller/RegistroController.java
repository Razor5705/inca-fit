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
                                   BindingResult result) {

        if (result.hasErrors()) {
            return "registro";
        }

        if (socioService.existeEmail(socio.getEmail())) {
            result.rejectValue("email", "error.socio", "El email ya está registrado");
            return "registro";
        }

        // Asignar rol
        socio.setRol(Rol.USUARIO);
        socioService.guardarSocio(socio);

        return "redirect:/login?registroExitoso";
    }
}