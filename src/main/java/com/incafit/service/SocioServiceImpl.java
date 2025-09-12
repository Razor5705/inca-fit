package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SocioServiceImpl implements SocioService {

    private static final Logger log = LoggerFactory.getLogger(SocioServiceImpl.class);
    private final SocioRepository socioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public SocioServiceImpl(SocioRepository socioRepository,
                            PasswordEncoder passwordEncoder,
                            EmailService emailService) {
        this.socioRepository = socioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    @Override
    public List<Socio> obtenerTodosSocios() {
        return socioRepository.findAll();
    }

    @Override
    public Socio obtenerSocioPorId(Long id) {
        return socioRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Socio guardarSocio(Socio socio) {
        boolean isNewSocio = socio.getId() == null;

        // Encriptar contraseña si es nuevo o si se ha proporcionado una nueva contraseña.
        if (isNewSocio || (socio.getPassword() != null && !socio.getPassword().isEmpty())) {
            socio.setPassword(passwordEncoder.encode(socio.getPassword()));
        } else {
            // Si es un socio existente y la contraseña está vacía, mantener la actual.
            socioRepository.findById(socio.getId())
                    .ifPresent(socioExistente -> socio.setPassword(socioExistente.getPassword()));
        }

        Socio socioGuardado = socioRepository.save(socio);

        // Enviar email de bienvenida si es un nuevo socio.
        // Se envuelve en un try-catch para que un fallo en el envío de email no cancele el registro.
        if (isNewSocio) {
            try {
                String subject = "¡Bienvenido a Inca Fit!";
                String text = "Hola " + socioGuardado.getNombre() + ",\n\n" +
                        "Gracias por registrarte en Inca Fit. ¡Estamos muy contentos de tenerte con nosotros!\n\n" +
                        "Tu cuenta ha sido creada exitosamente.\n\n" +
                        "Saludos,\n" +
                        "El equipo de Inca Fit";
                emailService.sendEmail(socioGuardado.getEmail(), subject, text);
            } catch (Exception e) {
                log.error("Error al enviar el email de bienvenida al socio {}: {}", socioGuardado.getEmail(), e.getMessage());
                // No relanzamos la excepción para no revertir la transacción del usuario.
            }
        }
        return socioGuardado;
    }

    @Override
    @Transactional
    public void eliminarSocio(Long id) {
        socioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void cambiarEstadoSocio(Long id, boolean estado) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socio no encontrado"));
        socio.setActivo(estado);
        socioRepository.save(socio);
    }

    @Override
    public boolean existeEmail(String email) {
        return socioRepository.existsByEmail(email);
    }

    @Override
    public Optional<Socio> obtenerSocioPorEmail(String email) {
        return socioRepository.findByEmail(email);
    }
}