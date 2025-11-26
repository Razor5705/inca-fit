// FacturaService
package com.incafit.service;

import com.incafit.Model.Factura;
import com.incafit.Model.Membresia;
import com.incafit.Model.Socio;
import java.math.BigDecimal;
import java.util.List;

public interface FacturaService {
    Factura generarFactura(Socio socio, Membresia membresia);
    Factura generarFacturaPorReserva(Socio socio, BigDecimal monto, String concepto);
    void pagarFactura(Long id);
    void pagarFactura(Long id, String metodoPago);
    List<Factura> obtenerFacturasPorSocio(Socio socio);
    Factura obtenerFacturaPorId(Long id);

    List<Factura> obtenerTodasFacturas();
    List<Factura> obtenerFacturasPorEstado(String estado);
    List<Factura> obtenerFacturasPagadasEsteMes();
    List<Factura> obtenerFacturasPendientesVencidas();

}
