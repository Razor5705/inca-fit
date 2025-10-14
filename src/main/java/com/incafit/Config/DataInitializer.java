package com.incafit.Config;

import com.incafit.Model.*;
import com.incafit.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;
    private final InstructorRepository instructorRepository;
    private final ClaseRepository claseRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(SocioRepository socioRepository,
                           MembresiaRepository membresiaRepository,
                           InstructorRepository instructorRepository,
                           ClaseRepository claseRepository,
                           AsistenciaRepository asistenciaRepository,
                           PasswordEncoder passwordEncoder) {
        this.socioRepository = socioRepository;
        this.membresiaRepository = membresiaRepository;
        this.instructorRepository = instructorRepository;
        this.claseRepository = claseRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo crear datos si no existen
        if (membresiaRepository.count() == 0) {
            // Crear Membresías
            Membresia mensual = new Membresia("Mensual", "Acceso por 30 días", 50.00, 30);
            Membresia trimestral = new Membresia("Trimestral", "Acceso por 90 días", 135.00, 90);
            Membresia anual = new Membresia("Anual", "Acceso por 365 días", 500.00, 365);
            membresiaRepository.saveAll(Arrays.asList(mensual, trimestral, anual));
        }

        if (instructorRepository.count() == 0) {
            // Crear Instructores
            Instructor instructor1 = new Instructor("Carlos Gomez", "Yoga y Pilates", "carlos.gomez@incafit.com");
            Instructor instructor2 = new Instructor("Ana Martinez", "Spinning y HIIT", "ana.martinez@incafit.com");
            instructorRepository.saveAll(Arrays.asList(instructor1, instructor2));
        }

        if (claseRepository.count() == 0) {
            // Crear Clases
            Instructor instructor1 = instructorRepository.findByEmail("carlos.gomez@incafit.com").orElse(null);
            Instructor instructor2 = instructorRepository.findByEmail("ana.martinez@incafit.com").orElse(null);
            
            if (instructor1 != null && instructor2 != null) {
                Clase yoga = new Clase("Yoga", "Clase de relajación y flexibilidad", instructor1, LocalTime.of(8, 0), 60, 20);
                Clase spinning = new Clase("Spinning", "Clase de ciclismo intenso", instructor2, LocalTime.of(18, 0), 45, 15);
                claseRepository.saveAll(Arrays.asList(yoga, spinning));
            }
        }

        if (socioRepository.count() == 0) {
            // Crear Socios (Admin y Usuario)
            Socio admin = new Socio();
            admin.setNombre("Admin");
            admin.setDni("12345678");
            admin.setEmail("admin@incafit.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            admin.setActivo(true);
            admin.setFechaRegistro(LocalDate.now());
            socioRepository.save(admin);

            Socio usuario = new Socio();
            usuario.setNombre("Juan Perez");
            usuario.setDni("87654321");
            usuario.setEmail("juan.perez@example.com");
            usuario.setPassword(passwordEncoder.encode("user123"));
            usuario.setRol(Rol.USUARIO);
            
            // Buscar membresía mensual
            Membresia mensual = membresiaRepository.findAll().stream()
                .filter(m -> "Mensual".equals(m.getTipoMembresia()))
                .findFirst()
                .orElse(null);
            usuario.setMembresia(mensual);
            
            usuario.setActivo(true);
            usuario.setFechaRegistro(LocalDate.now());
            socioRepository.save(usuario);
        }

        // Crear Asistencias solo si no existen
        if (asistenciaRepository.count() == 0) {
            Socio usuario = socioRepository.findByEmail("juan.perez@example.com").orElse(null);
            Clase yoga = claseRepository.findAll().stream()
                .filter(c -> "Yoga".equals(c.getNombre()))
                .findFirst()
                .orElse(null);
            
            if (usuario != null && yoga != null) {
                Asistencia asistencia1 = new Asistencia(usuario, yoga, LocalDate.now());
                asistenciaRepository.save(asistencia1);
            }
        }
    }
}