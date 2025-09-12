package com.incafit.service;

import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import com.incafit.Repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void testCrearReserva() {
        // Given
        Socio socio = new Socio();
        socio.setId(1L);
        String clase = "Yoga";
        LocalDateTime fechaHora = LocalDateTime.now();

        Reserva reserva = new Reserva();
        reserva.setSocio(socio);
        reserva.setClase(clase);
        reserva.setFechaHora(fechaHora);
        reserva.setEstado("CONFIRMADA");

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // When
        Reserva reservaCreada = reservaService.crearReserva(socio, clase, fechaHora);

        // Then
        assertEquals("CONFIRMADA", reservaCreada.getEstado());
        assertEquals(socio, reservaCreada.getSocio());
        assertEquals(clase, reservaCreada.getClase());
    }

    @Test
    void testCancelarReserva() {
        // Given
        Long reservaId = 1L;
        Reserva reserva = new Reserva();
        reserva.setId(reservaId);
        reserva.setEstado("CONFIRMADA");

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        // When
        reservaService.cancelarReserva(reservaId);

        // Then
        assertEquals("CANCELADA", reserva.getEstado());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void testObtenerReservasPorSocio() {
        // Given
        Socio socio = new Socio();
        socio.setId(1L);
        Reserva reserva = new Reserva();
        reserva.setSocio(socio);

        when(reservaRepository.findBySocio(socio)).thenReturn(Collections.singletonList(reserva));

        // When
        List<Reserva> reservas = reservaService.obtenerReservasPorSocio(socio);

        // Then
        assertEquals(1, reservas.size());
        assertEquals(socio, reservas.get(0).getSocio());
    }
}