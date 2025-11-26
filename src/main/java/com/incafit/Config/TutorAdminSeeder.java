package com.incafit.Config;

import com.incafit.Model.Rol;
import com.incafit.Model.Socio;
import com.incafit.service.SocioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * Crea un usuario ADMIN de prueba para el tutor si no existe.
 */
@Configuration
@Profile("dev")
public class TutorAdminSeeder {

    private static final Logger log = LoggerFactory.getLogger(TutorAdminSeeder.class);
    private static final String TUTOR_EMAIL = "tutor.admin@test.com";

    @Bean
    public ApplicationRunner seedTutorAdmin(SocioService socioService, PasswordEncoder passwordEncoder) {
        return args -> {
            if (socioService.existeEmail(TUTOR_EMAIL)) {
                return;
            }
            Socio admin = new Socio();
            admin.setDni("ADMINTEST");
            admin.setNombre("Tutor Admin");
            admin.setEmail(TUTOR_EMAIL);
            admin.setTelefono("000000000");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            admin.setActivo(true);
            admin.setFechaRegistro(LocalDate.now());
            log.info("Creando usuario de prueba para tutor: {}", TUTOR_EMAIL);
            socioService.guardarSocio(admin);
        };
    }
}
