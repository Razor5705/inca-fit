package com.incafit.Repository;

import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// com.incafft.repository.ReservaRepository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findBySocio(Socio socio);
    
    @Query("SELECT r FROM Reserva r WHERE DATE(r.fechaHora) = :fecha")
    List<Reserva> findByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.socio = :socio AND r.clase.id = :claseId AND r.fechaHora = :fechaHora AND r.estado <> 'CANCELADA'")
    boolean existsReservaActiva(@Param("socio") Socio socio,
                                @Param("claseId") Long claseId,
                                @Param("fechaHora") LocalDateTime fechaHora);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.clase.id = :claseId AND r.fechaHora = :fechaHora AND r.estado <> 'CANCELADA'")
    long countActivasByClaseAndFechaHora(@Param("claseId") Long claseId,
                                         @Param("fechaHora") LocalDateTime fechaHora);
}
