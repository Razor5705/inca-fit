// ReservaServiceImpl
package com.incafit.service;

import com.incafit.Model.Clase;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import com.incafit.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.incafit.Repository.ClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository, ClaseRepository claseRepository) {
        this.reservaRepository = reservaRepository;
        this.claseRepository = claseRepository;
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

        return reservaRepository.save(reserva);
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
        // This method might need adjustment if the date is now only in fechaHora
        // For now, assuming it's still needed and will be implemented in the repository.
        // return reservaRepository.findByFecha(fecha);
        throw new UnsupportedOperationException("La búsqueda de reservas por fecha aún no está implementada.");
    }
}