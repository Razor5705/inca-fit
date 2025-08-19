package com.incafit.Repository;

import com.incafit.Model.Factura;
import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findBySocio(Socio socio);
}
