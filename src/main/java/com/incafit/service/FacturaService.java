package com.incafit.service;

import com.incafit.Model.Factura;
import com.incafit.Model.Membresia;
import com.incafit.Model.Socio;
import com.incafit.dto.DataPointDTO;
import java.math.BigDecimal;
import java.util.List;

public interface FacturaService {
    Factura generarFactura(Socio socio, Membresia membresia);
    Factura generarFacturaPorReserva(Socio socio, BigDecimal monto, String concepto);
    void pagarFactura(Long id);
    List<Factura> obtenerFacturasPorSocio(Socio socio);
    Factura obtenerFacturaPorId(Long id);
    List<DataPointDTO> getMonthlyRevenue();
}