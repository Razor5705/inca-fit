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

    public SocioServiceImpl(SocioRepository socioRepository,
                            PasswordEncoder passwordEncoder) {
        this.socioRepository = socioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<Socio> obtenerTodosSocios() {
        return socioRepository.findAll();
    }

    @Override
    public List<Socio> obtenerTodosSociosConMembresia() {
        return socioRepository.findAllWithMembresia();
    }

    @Override
    public Socio obtenerSocioPorId(Long id) {
        Optional<Socio> socio = socioRepository.findById(id);
        return socio.orElse(null);
    }

    @Override
    public void guardarSocio(Socio socio) {
        // Si es un socio existente y la contraseña está vacía, mantener la actual
        if (socio.getId() != null) {
            Socio socioExistente = socioRepository.findById(socio.getId()).orElse(null);
            if (socioExistente != null &&
                    (socio.getPassword() == null || socio.getPassword().isEmpty())) {
                socio.setPassword(socioExistente.getPassword());
            } else {
                socio.setPassword(passwordEncoder.encode(socio.getPassword()));
            }
        } else {
            // Nuevo socio: encriptar contraseña
            socio.setPassword(passwordEncoder.encode(socio.getPassword()));
        }
        socioRepository.save(socio);
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