package com.incafit.Repository;

import com.incafit.Model.Reserva;
import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findBySocio(Socio socio);

    @Query("SELECT FUNCTION('MONTHNAME', r.fechaHora), COUNT(r) FROM Reserva r WHERE r.estado = 'CONFIRMADA' GROUP BY FUNCTION('MONTHNAME', r.fechaHora)")
    List<Object[]> findMonthlyAttendance();
}