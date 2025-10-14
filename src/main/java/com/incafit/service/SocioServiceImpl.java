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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class SocioServiceImpl implements SocioService {

    private static final Logger log = LoggerFactory.getLogger(SocioServiceImpl.class);
    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public List<Socio> obtenerTodosSocios() {
        log.info("Obteniendo todos los socios");
        try {
            List<Socio> socios = socioRepository.findAll();
            log.info("Se encontraron {} socios", socios.size());
            return socios;
        } catch (Exception e) {
            log.error("Error al obtener todos los socios: {}", e.getMessage());
            throw new RuntimeException("Error al obtener la lista de socios", e);
        }
    }

    @Override
    public Socio obtenerSocioPorId(Long id) {
        log.info("Buscando socio por ID: {}", id);
        try {
            Optional<Socio> socio = socioRepository.findById(id);
            if (socio.isPresent()) {
                log.info("Socio encontrado: {} - {}", socio.get().getId(), socio.get().getEmail());
                return socio.get();
            } else {
                log.warn("No se encontró socio con ID: {}", id);
                throw new RuntimeException("Socio no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error al obtener socio por ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al obtener el socio", e);
        }
    }
    @Override
    @Transactional
    public Socio guardarSocio(Socio socio) {
        log.info("🔄 Guardando socio en la base de datos...");
        log.info("   - Email: {}", socio.getEmail());
        log.info("   - DNI: {}", socio.getDni());
        log.info("   - Rol: {}", socio.getRol());

        try {
            Socio socioGuardado = socioRepository.save(socio);
            log.info("✅ Socio guardado exitosamente con ID: {}", socioGuardado.getId());

            // Verificar que realmente se guardó
            long totalSocios = socioRepository.count();
            log.info("📊 Total de socios en BD: {}", totalSocios);

            return socioGuardado;
        } catch (Exception e) {
            log.error("❌ Error al guardar socio: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar el socio en la base de datos", e);
        }
    }

    @Override
    public boolean existeEmail(String email) {
        boolean existe = socioRepository.existsByEmail(email);
        log.info("🔍 Verificando email '{}': {}", email, existe ? "EXISTE" : "NO EXISTE");
        return existe;
    }

    @Override
    public Optional<Socio> findByEmail(String email) {
        log.info("🔍 Buscando socio por email: {}", email);
        Optional<Socio> socio = socioRepository.findByEmail(email);
        log.info("   - Resultado: {}", socio.isPresent() ? "ENCONTRADO" : "NO ENCONTRADO");
        return socio;
    }

    @Override
    public boolean existeDni(String dni) {
        boolean existe = socioRepository.existsByDni(dni);
        log.info("🔍 Verificando DNI '{}': {}", dni, existe ? "EXISTE" : "NO EXISTE");
        return existe;
    }

    @Override
    public Optional<Socio> obtenerSocioConReservasPorEmail(String email) {
        log.info("🔍 Buscando socio con reservas por email: {}", email);
        Optional<Socio> socioOpt = socioRepository.findByEmail(email);
        socioOpt.ifPresent(socio -> {
            log.info("   - Socio encontrado. Inicializando reservas...");
            // Forzar la inicialización de la colección de reservas dentro de la transacción
            socio.getReservas().size();
            log.info("   - Reservas inicializadas. Total: {}", socio.getReservas().size());
        });
        return socioOpt;
    }



    @Override
    @Transactional
    public void eliminarSocio(Long id) {
        socioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void cambiarEstadoSocio(Long id, boolean estado) {
        log.info("Cambiando estado del socio ID: {} a {}", id, estado);
        try {
            Socio socio = socioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + id));
            socio.setActivo(estado);
            socioRepository.save(socio);
            log.info("Estado del socio actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al cambiar estado del socio ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al cambiar estado del socio", e);
        }
    }



}