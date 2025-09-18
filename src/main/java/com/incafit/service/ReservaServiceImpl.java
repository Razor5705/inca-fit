package com.incafit.service;

import com.incafit.Model.Clase;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.ReservaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository, ClaseRepository claseRepository) {
        this.reservaRepository = reservaRepository;
        this.claseRepository = claseRepository;
    }

    @Override
    public Reserva crearReserva(Socio socio, Clase clase) throws Exception {
        // Refrescar el estado de la clase para obtener el número actual de reservas
        Clase claseActualizada = claseRepository.findById(clase.getId())
                .orElseThrow(() -> new Exception("La clase seleccionada ya no existe."));

        if (claseActualizada.getReservas().size() >= claseActualizada.getCapacidad()) {
            throw new Exception("No quedan plazas disponibles para esta clase.");
        }

        Reserva reserva = new Reserva();
        reserva.setSocio(socio);
        reserva.setClase(claseActualizada);
        reserva.setFechaHora(claseActualizada.getFechaHora());
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
}