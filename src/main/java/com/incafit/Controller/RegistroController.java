package com.incafit.Controller;

import com.incafit.Model.*;
import com.incafit.Repository.DetalleFacturaRepository;
import com.incafit.Repository.FacturaRepository;
import com.incafit.service.MembresiaService;
import com.incafit.service.SocioService;
import com.incafit.dto.RegistroSocioDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/registro")
@SessionAttributes("registroDto")
public class RegistroController {

    private final SocioService socioService;
    private final PasswordEncoder passwordEncoder;
    private final MembresiaService membresiaService;
    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;

    public RegistroController(SocioService socioService,
                              PasswordEncoder passwordEncoder,
                              MembresiaService membresiaService,
                              FacturaRepository facturaRepository,
                              DetalleFacturaRepository detalleFacturaRepository) {
        this.socioService = socioService;
        this.passwordEncoder = passwordEncoder;
        this.membresiaService = membresiaService;
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    @ModelAttribute("registroDto")
    public RegistroSocioDto registroSocioDto() {
        return new RegistroSocioDto();
    }

    @GetMapping
    public String mostrarPaso1(Model model) {
        model.addAttribute("registroDto", new RegistroSocioDto());
        return "registro-paso1";
    }

    @PostMapping("/paso1")
    public String procesarPaso1(@Valid @ModelAttribute("registroDto") RegistroSocioDto registroDto,
                                BindingResult result) {
        if (registroDto.getPassword() != null && !registroDto.getPassword().equals(registroDto.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.socio", "Las contraseñas no coinciden");
        }
        if (socioService.existeDni(registroDto.getDni())) {
            result.rejectValue("dni", "error.socio", "El DNI ya está registrado en el sistema");
        }
        if (socioService.existeEmail(registroDto.getEmail())) {
            result.rejectValue("email", "error.socio", "El email ya está registrado");
        }

        if (result.hasErrors()) {
            return "registro-paso1";
        }

        return "redirect:/registro/paso2";
    }

    @GetMapping("/paso2")
    public String mostrarPaso2(Model model) {
        List<Membresia> membresias = membresiaService.findAll();
        model.addAttribute("membresias", membresias);
        // El DTO se obtiene de la sesión
        return "registro-paso2";
    }

    @PostMapping("/paso2")
    public String procesarPaso2(@ModelAttribute("registroDto") RegistroSocioDto registroDto,
                                @RequestParam("membresiaId") Long membresiaId) {

        registroDto.setMembresiaId(membresiaId);
        return "redirect:/registro/paso3";
    }

    @GetMapping("/paso3")
    public String mostrarPaso3(Model model) {
        // El DTO se obtiene de la sesión
        return "registro-paso3";
    }

    @PostMapping("/paso3")
    public String procesarPaso3(@Valid @ModelAttribute("registroDto") RegistroSocioDto registroDto,
                                BindingResult result,
                                SessionStatus status) {

        if (result.hasErrors()) {
            return "registro-paso3";
        }

        Membresia membresiaSeleccionada = membresiaService.findById(registroDto.getMembresiaId())
                .orElse(null);
        if (membresiaSeleccionada == null) {
            return "redirect:/registro/paso2?error=true";
        }

        // Aquí se simula el procesamiento del pago. En una aplicación real,
        // se integraría con una pasarela de pago.

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

        socioService.guardarSocio(nuevoSocio);

        // Crear la factura
        Factura factura = new Factura();
        factura.setSocio(nuevoSocio);
        factura.setFecha(LocalDate.now());
        factura.setTotal(membresiaSeleccionada.getPrecio());
        facturaRepository.save(factura);

        // Crear el detalle de la factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setFactura(factura);
        detalle.setDescripcion("Membresía " + membresiaSeleccionada.getNombre());
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(membresiaSeleccionada.getPrecio());
        detalle.setSubtotal(membresiaSeleccionada.getPrecio());
        detalleFacturaRepository.save(detalle);


        status.setComplete(); // Limpiar la sesión
        return "redirect:/login?registroExitoso=true";
    }
}