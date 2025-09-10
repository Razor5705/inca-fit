package com.incafit.Repository;

import com.incafit.Model.Factura;
import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findBySocio(Socio socio);

    // Métodos adicionales necesarios
    List<Factura> findByEstado(String estado);

    @Query("SELECT f FROM Factura f WHERE f.estado = :estado AND f.fechaEmision BETWEEN :inicio AND :fin")
    List<Factura> findByEstadoAndFechaEmisionBetween(
            @Param("estado") String estado,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin);
}

