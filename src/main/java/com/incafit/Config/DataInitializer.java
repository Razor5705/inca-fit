package com.incafit.Config;

import com.incafit.Model.Membresia;
import com.incafit.Model.Rol;
import com.incafit.Model.Socio;
import com.incafit.Repository.MembresiaRepository;
import com.incafit.Repository.SocioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(SocioRepository socioRepository, MembresiaRepository membresiaRepository, PasswordEncoder passwordEncoder) {
        this.socioRepository = socioRepository;
        this.membresiaRepository = membresiaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar si no hay socios
        if (socioRepository.count() == 0) {
            Membresia membresiaBasica = membresiaRepository.findById(1L).orElse(null);
            Membresia membresiaPremium = membresiaRepository.findById(2L).orElse(null);

            // Crear socio USUARIO
            Socio usuario = new Socio();
            usuario.setDni("11223344A");
            usuario.setNombre("Elena Navarro");
            usuario.setEmail("elena.navarro@test.com");
            usuario.setPassword(passwordEncoder.encode("password123"));
            usuario.setTelefono("611223344");
            usuario.setMembresia(membresiaBasica);
            usuario.setRol(Rol.USUARIO);
            usuario.setFechaRegistro(LocalDate.now());
            usuario.setActivo(true);
            socioRepository.save(usuario);

            // Crear socio ADMIN
            Socio admin = new Socio();
            admin.setDni("99887766C");
            admin.setNombre("Admin User");
            admin.setEmail("admin@incafit.com");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setTelefono("699887766");
            admin.setMembresia(membresiaPremium);
            admin.setRol(Rol.ADMIN);
            admin.setFechaRegistro(LocalDate.now());
            admin.setActivo(true);
            socioRepository.save(admin);
        }
    }
}