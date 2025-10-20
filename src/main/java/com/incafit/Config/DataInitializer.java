package com.incafit.Config;

import com.incafit.Model.*;
import com.incafit.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

        // Crear Membresías solo si no existen
        if (membresiaRepository.count() == 0) {
            System.out.println("🔧 DataInitializer: Creando membresías...");
            try {
                Membresia mensual = new Membresia("Mensual", "Acceso por 30 días", 50.00, 30);
                Membresia trimestral = new Membresia("Trimestral", "Acceso por 90 días", 135.00, 90);
                Membresia anual = new Membresia("Anual", "Acceso por 365 días", 500.00, 365);
                membresiaRepository.saveAll(Arrays.asList(mensual, trimestral, anual));
                System.out.println("✅ DataInitializer: Membresías creadas exitosamente");
            } catch (Exception e) {
                System.out.println("⚠️ DataInitializer: Error creando membresías: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ DataInitializer: Membresías ya existen, saltando creación");
        }

        // Crear Instructores solo si no existen
        if (instructorRepository.count() == 0) {
            System.out.println("🔧 DataInitializer: Creando instructores...");
            try {
                Instructor instructor1 = new Instructor("Carlos Gomez", "Yoga y Pilates", "carlos.gomez@incafit.com");
                Instructor instructor2 = new Instructor("Ana Martinez", "Spinning y HIIT", "ana.martinez@incafit.com");
                instructorRepository.saveAll(Arrays.asList(instructor1, instructor2));
                System.out.println("✅ DataInitializer: Instructores creados exitosamente");
            } catch (Exception e) {
                System.out.println("⚠️ DataInitializer: Error creando instructores: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ DataInitializer: Instructores ya existen, saltando creación");
        }

        if (claseRepository.count() == 0) {
            System.out.println("🔧 DataInitializer: Creando clases...");
            try {
                // Crear Clases
                Instructor instructor1 = instructorRepository.findByEmail("carlos.gomez@incafit.com").orElse(null);
                Instructor instructor2 = instructorRepository.findByEmail("ana.martinez@incafit.com").orElse(null);
                
                if (instructor1 != null && instructor2 != null) {
                    // Clases permanentes (sin fecha de inicio/fin)
                    Clase yoga = new Clase("Yoga", "Clase de relajación y flexibilidad", instructor1, LocalTime.of(8, 0), 60, 20, true);
                    Clase spinning = new Clase("Spinning", "Clase de ciclismo intenso", instructor2, LocalTime.of(18, 0), 45, 15, true);
                    
                    // Clase con duración limitada y precio adicional (ejemplo: Defensa Personal)
                    Clase defensaPersonal = new Clase("Defensa Personal", "Curso de defensa personal de 3 meses", instructor2, LocalTime.of(19, 0), 90, 15, true);
                    defensaPersonal.setFechaInicio(LocalDate.now());
                    defensaPersonal.setFechaFin(LocalDate.now().plusDays(90));
                    defensaPersonal.setPrecioAdicional(BigDecimal.valueOf(25.00));
                    
                    claseRepository.saveAll(Arrays.asList(yoga, spinning, defensaPersonal));
                    System.out.println("✅ DataInitializer: Clases creadas exitosamente");
                }
            } catch (Exception e) {
                System.out.println("⚠️ DataInitializer: Error creando clases: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ DataInitializer: Clases ya existen, saltando creación");
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

            // Crear cuenta de administrador adicional
            Socio adminNiko = new Socio();
            adminNiko.setNombre("Niko Admin");
            adminNiko.setDni("11223344");
            adminNiko.setEmail("nikkmed805@gmail.com");
            adminNiko.setPassword(passwordEncoder.encode("admin123"));
            adminNiko.setRol(Rol.ADMIN);
            adminNiko.setActivo(true);
            adminNiko.setFechaRegistro(LocalDate.now());
            socioRepository.save(adminNiko);

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
            
            // Establecer vigencia de la membresía (30 días desde hoy)
            if (mensual != null) {
                usuario.setFechaInicioMembresia(LocalDate.now());
                usuario.setFechaFinMembresia(LocalDate.now().plusDays(mensual.getDuracionDias()));
            }
            
            usuario.setActivo(true);
            usuario.setFechaRegistro(LocalDate.now());
            socioRepository.save(usuario);
            
            System.out.println("✅ DataInitializer: Usuarios creados exitosamente");
        }

        // Crear Asistencias solo si no existen
        if (asistenciaRepository.count() == 0) {
            System.out.println("ℹ️ DataInitializer: No se crearon asistencias iniciales");
        }
    }
}