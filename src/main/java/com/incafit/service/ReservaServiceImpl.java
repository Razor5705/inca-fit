// ReservaServiceImpl
package com.incafit.service;

import com.incafit.Model.Asistencia;
import com.incafit.Model.Clase;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import com.incafit.Repository.AsistenciaRepository;
import com.incafit.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.incafit.Repository.ClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final EmailService emailService;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository, 
                             ClaseRepository claseRepository,
                             AsistenciaRepository asistenciaRepository,
                             EmailService emailService) {
        this.reservaRepository = reservaRepository;
        this.claseRepository = claseRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.emailService = emailService;
    }

    @Override
    public Reserva crearReserva(Socio socio, Long claseId, LocalDateTime fechaHora) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada con ID: " + claseId));
        Reserva reserva = new Reserva();
        reserva.setSocio(socio);
        reserva.setClase(clase);
        reserva.setFechaHora(fechaHora);
        reserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(reserva);
        
        // Crear asistencia automáticamente cuando se confirma una reserva
        Asistencia asistencia = new Asistencia();
        asistencia.setSocio(socio);
        asistencia.setClase(clase);
        asistencia.setReserva(reservaGuardada);
        asistencia.setFecha(fechaHora.toLocalDate());
        asistenciaRepository.save(asistencia);

        // Enviar email de confirmación de reserva
        try {
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            String fecha = fechaHora.format(formatoFecha);
            String hora = fechaHora.format(formatoHora);
            emailService.sendReservaConfirmacionEmail(socio, clase.getNombre(), fecha, hora);
            System.out.println("✅ Email de confirmación de reserva enviado a: " + socio.getEmail());
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar email de confirmación de reserva: " + e.getMessage());
            // No detenemos el proceso si falla el email
        }

        return reservaGuardada;
    }

    @Override
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }

    @Override
    public List<Reserva> obtenerReservasPorSocio(Socio socio) {
        return reservaRepository.findBySocio(socio);
    }

    @Override
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Override
    public List<Reserva> obtenerTodasReservas() {
        return reservaRepository.findAll(); // Debe usar el repositorio para obtener todas las facturas
    }

    @Override
    public List<Reserva> obtenerReservasPorFecha(LocalDate fecha) {
        return reservaRepository.findByFecha(fecha);
    }
}