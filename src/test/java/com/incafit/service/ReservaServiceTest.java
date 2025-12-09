package com.incafit.service;

import com.incafit.Model.Clase;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import com.incafit.Model.Membresia;
import com.incafit.Repository.ClaseRepository;
import com.incafit.Repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClaseRepository claseRepository;

    @Mock
    private ClaseHorarioService claseHorarioService;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void testCrearReserva() {
        // Given
        Socio socio = new Socio();
        socio.setId(1L);
        Membresia membresia = new Membresia("Mensual", "Plan mensual", 30.0, 30);
        socio.setMembresia(membresia);
        socio.setFechaInicioMembresia(LocalDate.now().minusDays(1));
        socio.setFechaFinMembresia(LocalDate.now().plusDays(10));

        Long claseId = 1L;
        Clase clase = new Clase();
        clase.setId(claseId);
        clase.setNombre("Yoga");
        clase.setHora(LocalTime.of(10, 0));
        clase.setDuracionMinutos(60);
        clase.setCapacidadMaxima(10);

        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);

        Reserva reserva = new Reserva();
        reserva.setSocio(socio);
        reserva.setClase(clase);
        reserva.setFechaHora(fechaHora);
        reserva.setEstado("CONFIRMADA");

        when(claseRepository.findById(claseId)).thenReturn(Optional.of(clase));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);
        when(reservaRepository.countActivasByClaseAndFechaHora(claseId, fechaHora)).thenReturn(0L);
        when(claseHorarioService.obtenerDiasPermitidos(claseId)).thenReturn(Set.of(fechaHora.getDayOfWeek()));

        // When
        Reserva reservaCreada = reservaService.crearReserva(socio, claseId, fechaHora);

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
