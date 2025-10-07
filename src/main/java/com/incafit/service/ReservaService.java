// ReservaService
package com.incafit.service;

import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.incafit.Model.Clase;

public interface ReservaService {
    Reserva crearReserva(Socio socio, Long claseId, LocalDateTime fechaHora);
    void cancelarReserva(Long id);
    List<Reserva> obtenerReservasPorSocio(Socio socio);
    Reserva obtenerReservaPorId(Long id);

    List<Reserva> obtenerTodasReservas();
    List<Reserva> obtenerReservasPorFecha(LocalDate fecha);


}