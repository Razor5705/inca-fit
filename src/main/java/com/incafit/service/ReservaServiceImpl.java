// ReservaServiceImpl
package com.incafit.service;

import com.incafit.Model.Asistencia;
import com.incafit.Model.Clase;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import com.incafit.Repository.AsistenciaRepository;
import com.incafit.Repository.ReservaRepository;
import com.incafit.Repository.ClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final EmailService emailService;
    private final ClaseHorarioService claseHorarioService;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              ClaseRepository claseRepository,
                              AsistenciaRepository asistenciaRepository,
                              EmailService emailService,
                              ClaseHorarioService claseHorarioService) {
        this.reservaRepository = reservaRepository;
        this.claseRepository = claseRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.emailService = emailService;
        this.claseHorarioService = claseHorarioService;
    }

    @Override
    public Reserva crearReserva(Socio socio, Long claseId, LocalDateTime fechaHora) {
        validarSocio(socio);

        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada con ID: " + claseId));
        validarClaseYHorario(clase, fechaHora);
        validarCapacidad(clase, fechaHora);
        if (reservaRepository.existsReservaActiva(socio, claseId, fechaHora)) {
            throw new IllegalArgumentException("Ya tienes una reserva activa para esta clase y fecha.");
        }

        Reserva reserva = new Reserva();
        reserva.setSocio(socio);
        reserva.setClase(clase);
        reserva.setFechaHora(fechaHora);
        reserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(reserva);

        // Crear asistencia automaticamente cuando se confirma una reserva
        Asistencia asistencia = new Asistencia();
        asistencia.setSocio(socio);
        asistencia.setClase(clase);
        asistencia.setReserva(reservaGuardada);
        asistencia.setFecha(fechaHora.toLocalDate());
        asistenciaRepository.save(asistencia);

        // Enviar email de confirmacion de reserva en formato HTML
        try {
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            String fecha = fechaHora.format(formatoFecha);
            String hora = fechaHora.format(formatoHora);
            emailService.sendReservaConfirmacionEmailHtml(socio, clase.getNombre(), fecha, hora);
            System.out.println("[INFO] Email HTML de confirmacion de reserva enviado a: " + socio.getEmail());
        } catch (Exception e) {
            System.err.println("[WARN] Error al enviar email HTML de confirmacion de reserva: " + e.getMessage());
            // No detenemos el proceso si falla el email
        }

        return reservaGuardada;
    }

    private void validarSocio(Socio socio) {
        if (socio == null) {
            throw new IllegalArgumentException("Socio no encontrado.");
        }
        if (!socio.isActivo()) {
            throw new IllegalStateException("Tu cuenta esta inactiva. Reactivala para reservar.");
        }
        if (socio.getMembresia() == null || !socio.isMembresiaActiva()) {
            throw new IllegalStateException("Necesitas una membresia vigente para reservar esta clase.");
        }
    }

    private void validarClaseYHorario(Clase clase, LocalDateTime fechaHora) {
        if (clase == null) {
            throw new IllegalArgumentException("Clase no encontrada.");
        }
        if (!clase.isActivo()) {
            throw new IllegalStateException("La clase no esta activa.");
        }
        if (!clase.isVigente()) {
            throw new IllegalStateException("La clase no esta vigente.");
        }
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora de la reserva son obligatorias.");
        }
        if (fechaHora.toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No puedes reservar en una fecha pasada.");
        }
        if (clase.getFechaInicio() != null && fechaHora.toLocalDate().isBefore(clase.getFechaInicio())) {
            throw new IllegalArgumentException("La clase seleccionada aun no ha comenzado.");
        }
        if (clase.getFechaFin() != null && fechaHora.toLocalDate().isAfter(clase.getFechaFin())) {
            throw new IllegalArgumentException("La clase seleccionada ya finalizo.");
        }
        if (clase.getHora() != null && !clase.getHora().equals(fechaHora.toLocalTime())) {
            throw new IllegalArgumentException("La hora seleccionada no coincide con el horario de la clase.");
        }
        Set<DayOfWeek> diasPermitidos = claseHorarioService.obtenerDiasPermitidos(clase.getId());
        if (diasPermitidos != null && !diasPermitidos.isEmpty() && !diasPermitidos.contains(fechaHora.getDayOfWeek())) {
            throw new IllegalArgumentException("La clase no se imparte el dia seleccionado.");
        }
    }

    private void validarCapacidad(Clase clase, LocalDateTime fechaHora) {
        if (clase.getCapacidadMaxima() <= 0) {
            return;
        }
        long ocupadas = reservaRepository.countActivasByClaseAndFechaHora(clase.getId(), fechaHora);
        if (ocupadas >= clase.getCapacidadMaxima()) {
            throw new IllegalStateException("No quedan plazas disponibles para esta clase en la fecha seleccionada.");
        }
    }

    @Override
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);

        // Enviar email de cancelacion de reserva sin interrumpir el flujo
        try {
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime fechaHora = reserva.getFechaHora();
            String fecha = fechaHora != null ? fechaHora.format(formatoFecha) : "N/A";
            String hora = fechaHora != null ? fechaHora.format(formatoHora) : "N/A";

            emailService.sendCancelacionReservaEmail(
                    reserva.getSocio(),
                    reserva.getClase() != null ? reserva.getClase().getNombre() : "Clase",
                    fecha,
                    hora
            );
            System.out.println("[INFO] Email de cancelacion de reserva enviado a: "
                    + reserva.getSocio().getEmail());
        } catch (Exception e) {
            System.err.println("[WARN] Error al enviar email de cancelacion de reserva: "
                    + e.getMessage());
        }
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
        return reservaRepository.findAll();
    }

    @Override
    public List<Reserva> obtenerReservasPorFecha(LocalDate fecha) {
        return reservaRepository.findByFecha(fecha);
    }

    @Override
    public boolean existeReservaActiva(Socio socio, Long claseId, LocalDateTime fechaHora) {
        return reservaRepository.existsReservaActiva(socio, claseId, fechaHora);
    }
}
