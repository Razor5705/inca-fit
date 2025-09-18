package com.incafit.service;

import com.incafit.Model.Clase;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;

import java.util.List;

public interface ReservaService {
    Reserva crearReserva(Socio socio, Clase clase) throws Exception;
    void cancelarReserva(Long id);
    List<Reserva> obtenerReservasPorSocio(Socio socio);
    Reserva obtenerReservaPorId(Long id);
    List<com.incafit.dto.DataPointDTO> getMonthlyAttendance();
}