package com.incafit.Repository;

import com.incafit.Model.Factura;
import com.incafit.Model.Factura;
import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findBySocio(Socio socio);

    @Query("SELECT FUNCTION('MONTHNAME', f.fechaEmision), SUM(f.total) FROM Factura f WHERE f.estado = 'PAGADA' GROUP BY FUNCTION('MONTHNAME', f.fechaEmision)")
    List<Object[]> findMonthlyRevenue();
}
