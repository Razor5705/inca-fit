package com.incafit.Repository;

import com.incafit.Model.Reserva;
import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findBySocio(Socio socio);
}