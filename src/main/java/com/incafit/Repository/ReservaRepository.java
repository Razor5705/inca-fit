package com.incafit.Repository;

import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

// com.incafft.repository.ReservaRepository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findBySocio(Socio socio);
    // Método para encontrar reservas por fecha
    @Query("SELECT r FROM Reserva r WHERE DATE(r.fechaHora) = :fecha")
    List<Reserva> findByFecha(@Param("fecha") LocalDate fecha);
}