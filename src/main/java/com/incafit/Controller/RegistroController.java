package com.incafit.Controller;

import com.incafit.Model.Factura;
import com.incafit.Model.Membresia;
import com.incafit.Model.Rol;
import com.incafit.Model.Socio;
import com.incafit.service.EmailService;
import com.incafit.service.FacturaService;
import com.incafit.service.MembresiaService;
import com.incafit.service.SocioService;
import com.incafit.dto.BasicInfo;
import com.incafit.dto.PaymentInfo;
import com.incafit.dto.RegistroSocioDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/registro")
@SessionAttributes("registroDto")
public class RegistroController {

    private final SocioService socioService;
    private final PasswordEncoder passwordEncoder;
    private final MembresiaService membresiaService;
    private final FacturaService facturaService;
    private final EmailService emailService;

    public RegistroController(SocioService socioService,
                              PasswordEncoder passwordEncoder,
                              MembresiaService membresiaService,
                              FacturaService facturaService,
                              EmailService emailService) {
        this.socioService = socioService;
        this.passwordEncoder = passwordEncoder;
        this.membresiaService = membresiaService;
        this.facturaService = facturaService;
        this.emailService = emailService;
    }

    @ModelAttribute("registroDto")
    public RegistroSocioDto registroSocioDto() {
        return new RegistroSocioDto();
    }

    @GetMapping
    public String mostrarPaso1(@RequestParam(value = "trial", required = false) Boolean trial,
                               Model model) {
        RegistroSocioDto dto = new RegistroSocioDto();
        if (Boolean.TRUE.equals(trial)) {
            dto.setTrial(true);
        }
        model.addAttribute("registroDto", dto);
        return "registro-paso1";
    }

    @PostMapping("/paso1")
    public String procesarPaso1(@Validated(BasicInfo.class) @ModelAttribute("registroDto") RegistroSocioDto registroDto,
                                BindingResult result) {

        System.out.println("[DEBUG] Datos recibidos:");
        System.out.println("DNI: " + registroDto.getDni());
        System.out.println("Nombre: " + registroDto.getNombre());
        System.out.println("Email: " + registroDto.getEmail());
        System.out.println("Telefono: " + registroDto.getTelefono());
        System.out.println("Password: " + (registroDto.getPassword() != null ? "***" : "null"));
        System.out.println("PasswordConfirm: " + (registroDto.getPasswordConfirm() != null ? "***" : "null"));

        System.out.println("[DEBUG] Errores de validacion:");
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println("Error: " + error.getDefaultMessage()));
        }

        if (registroDto.getPassword() != null && !registroDto.getPassword().equals(registroDto.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.socio", "Las contrasenas no coinciden");
        }
        if (socioService.existeDni(registroDto.getDni())) {
            result.rejectValue("dni", "error.socio", "El DNI ya esta registrado en el sistema");
        }
        if (socioService.existeEmail(registroDto.getEmail())) {
            result.rejectValue("email", "error.socio", "El email ya esta registrado");
        }

        System.out.println("[DEBUG] Errores finales:");
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println("Error final: " + error.getDefaultMessage()));
            return "registro-paso1";
        }

        System.out.println("[DEBUG] Redirigiendo a paso 2");
        return "redirect:/registro/paso2";
    }

    @GetMapping("/paso2")
    public String mostrarPaso2(Model model) {
        List<Membresia> membresias = membresiaService.findAll();
        model.addAttribute("membresias", membresias);
        return "registro-paso2";
    }

    @PostMapping("/paso2")
    public String procesarPaso2(@ModelAttribute("registroDto") RegistroSocioDto registroDto,
                                @RequestParam("membresiaId") Long membresiaId) {

        registroDto.setMembresiaId(membresiaId);
        if (registroDto.isTrial()) {
            return "redirect:/registro/trial/confirmar";
        }
        return "redirect:/registro/paso3";
    }

    @GetMapping("/paso3")
    public String mostrarPaso3(Model model) {
        return "registro-paso3";
    }

    @PostMapping("/paso3")
    public String procesarPaso3(@Validated({BasicInfo.class, PaymentInfo.class}) @ModelAttribute("registroDto") RegistroSocioDto registroDto,
                                BindingResult result,
                                SessionStatus status,
                                RedirectAttributes redirectAttributes) {

        System.out.println("[DEBUG] Procesando Paso 3:");
        System.out.println("Nombre Tarjeta: " + registroDto.getNombreTarjeta());
        System.out.println("Numero Tarjeta: " + (registroDto.getNumeroTarjeta() != null && registroDto.getNumeroTarjeta().length() >= 4
                ? "***" + registroDto.getNumeroTarjeta().substring(registroDto.getNumeroTarjeta().length() - 4)
                : "null"));
        System.out.println("Fecha Caducidad: " + registroDto.getFechaCaducidad());
        System.out.println("CVV: " + (registroDto.getCvv() != null ? "***" : "null"));
        System.out.println("Membresia ID: " + registroDto.getMembresiaId());

        validarDatosTarjeta(registroDto, result);

        if (result.hasErrors()) {
            System.out.println("[DEBUG] Errores de validacion en Paso 3:");
            result.getAllErrors().forEach(error -> System.out.println("Error: " + error.getDefaultMessage()));
            return "registro-paso3";
        }

        Membresia membresiaSeleccionada = membresiaService.findById(registroDto.getMembresiaId())
                .orElse(null);
        if (membresiaSeleccionada == null) {
            return "redirect:/registro/paso2?error=true";
        }

        Socio nuevoSocio = new Socio();
        nuevoSocio.setDni(registroDto.getDni());
        nuevoSocio.setNombre(registroDto.getNombre());
        nuevoSocio.setEmail(registroDto.getEmail());
        nuevoSocio.setTelefono(registroDto.getTelefono());
        nuevoSocio.setPassword(passwordEncoder.encode(registroDto.getPassword()));
        nuevoSocio.setMembresia(membresiaSeleccionada);
        nuevoSocio.setRol(Rol.USUARIO);
        nuevoSocio.setFechaRegistro(LocalDate.now());
        nuevoSocio.setActivo(true);
        // fechas de vigencia para evitar tener la membresía sin asignación tras el alta
        LocalDate inicio = LocalDate.now();
        int duracion = membresiaSeleccionada.getDuracionDias() != null && membresiaSeleccionada.getDuracionDias() > 0
                ? membresiaSeleccionada.getDuracionDias() : 30;
        nuevoSocio.setFechaInicioMembresia(inicio);
        nuevoSocio.setFechaFinMembresia(inicio.plusDays(duracion - 1L));

        socioService.guardarSocio(nuevoSocio);

        Factura factura = facturaService.generarFactura(nuevoSocio, membresiaSeleccionada);
        facturaService.pagarFactura(factura.getId(), "Tarjeta de registro");

        try {
            emailService.sendWelcomeEmailHtml(nuevoSocio);
            System.out.println("[INFO] Email HTML de bienvenida enviado a: " + nuevoSocio.getEmail());
        } catch (Exception e) {
            System.err.println("[WARN] Error al enviar email HTML de bienvenida: " + e.getMessage());
        }

        status.setComplete();
        redirectAttributes.addFlashAttribute("registroEmail", nuevoSocio.getEmail());
        return "redirect:/registro/exito";
    }

    @GetMapping("/trial/confirmar")
    public String finalizarTrial(@ModelAttribute("registroDto") RegistroSocioDto registroDto,
                                 SessionStatus status,
                                 RedirectAttributes redirectAttributes) {
        Membresia membresiaSeleccionada = membresiaService.findById(registroDto.getMembresiaId())
                .orElse(null);
        if (membresiaSeleccionada == null) {
            return "redirect:/registro/paso2?error=true";
        }

        Socio nuevoSocio = new Socio();
        nuevoSocio.setDni(registroDto.getDni());
        nuevoSocio.setNombre(registroDto.getNombre());
        nuevoSocio.setEmail(registroDto.getEmail());
        nuevoSocio.setTelefono(registroDto.getTelefono());
        nuevoSocio.setPassword(passwordEncoder.encode(registroDto.getPassword()));
        nuevoSocio.setMembresia(membresiaSeleccionada);
        nuevoSocio.setRol(Rol.USUARIO);
        nuevoSocio.setFechaRegistro(LocalDate.now());
        nuevoSocio.setActivo(true);
        LocalDate inicio = LocalDate.now();
        nuevoSocio.setFechaInicioMembresia(inicio);
        nuevoSocio.setFechaFinMembresia(inicio.plusDays(7 - 1L));

        socioService.guardarSocio(nuevoSocio);

        try {
            emailService.sendWelcomeEmailHtml(nuevoSocio);
        } catch (Exception e) {
            System.err.println("[WARN] Error al enviar email HTML de bienvenida (trial): " + e.getMessage());
        }

        status.setComplete();
        redirectAttributes.addFlashAttribute("registroEmail", nuevoSocio.getEmail());
        redirectAttributes.addFlashAttribute("trialActivo", true);
        return "redirect:/registro/exito";
    }

    private void validarDatosTarjeta(RegistroSocioDto registroDto, BindingResult result) {
        if (registroDto.getNumeroTarjeta() != null) {
            String normalizada = registroDto.getNumeroTarjeta().replaceAll("\\s", "");
            registroDto.setNumeroTarjeta(normalizada);
            if (!normalizada.matches("\\d{16}")) {
                result.rejectValue("numeroTarjeta", "tarjeta.formato", "El numero de tarjeta debe tener 16 digitos.");
            }
        }

        if (registroDto.getFechaCaducidad() != null && !registroDto.getFechaCaducidad().isBlank()) {
            if (tarjetaCaducada(registroDto.getFechaCaducidad())) {
                result.rejectValue("fechaCaducidad", "tarjeta.caducada", "La tarjeta indicada esta caducada.");
            }
        }
    }

    private boolean tarjetaCaducada(String fechaCaducidad) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expira = YearMonth.parse(fechaCaducidad, formatter);
            return expira.isBefore(YearMonth.now());
        } catch (DateTimeParseException ex) {
            return true;
        }
    }

    @GetMapping("/exito")
    public String mostrarConfirmacion() {
        return "registro-exito";
    }

    @GetMapping("/check")
    @ResponseBody
    public Map<String, Boolean> comprobarDisponibilidad(@RequestParam(value = "dni", required = false) String dni,
                                                        @RequestParam(value = "email", required = false) String email) {
        Map<String, Boolean> resultado = new HashMap<>();
        if (dni != null && !dni.isBlank()) {
            resultado.put("dniExists", socioService.existeDni(dni));
        }
        if (email != null && !email.isBlank()) {
            resultado.put("emailExists", socioService.existeEmail(email));
        }
        return resultado;
    }
}
