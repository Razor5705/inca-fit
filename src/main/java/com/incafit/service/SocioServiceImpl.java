// SocioServiceImpl.java
package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SocioServiceImpl implements SocioService {

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
        Optional<Socio> socio = socioRepository.findById(id);
        return socio.orElse(null);
    }

    @Override
    @Transactional
    public Socio guardarSocio(Socio socio) {
        boolean isNewSocio = socio.getId() == null;

        // Encriptar contraseña si es necesario
        if (isNewSocio || (socio.getPassword() != null && !socio.getPassword().isEmpty())) {
            socio.setPassword(passwordEncoder.encode(socio.getPassword()));
        } else {
            // Mantener la contraseña existente si no se proporciona una nueva
            socioRepository.findById(socio.getId())
                    .ifPresent(socioExistente -> socio.setPassword(socioExistente.getPassword()));
        }

        Socio socioGuardado = socioRepository.save(socio);

        // Enviar email de bienvenida si es un nuevo socio
        if (isNewSocio) {
            String subject = "¡Bienvenido a Inca Fit!";
            String text = "Hola " + socioGuardado.getNombre() + ",\n\n" +
                    "Gracias por registrarte en Inca Fit. ¡Estamos muy contentos de tenerte con nosotros!\n\n" +
                    "Tu cuenta ha sido creada exitosamente.\n\n" +
                    "Saludos,\n" +
                    "El equipo de Inca Fit";
            emailService.sendEmail(socioGuardado.getEmail(), subject, text);
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