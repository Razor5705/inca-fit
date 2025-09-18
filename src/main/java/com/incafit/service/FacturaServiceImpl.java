// FacturaServiceImpl
package com.incafit.service;

import com.incafit.Model.Factura;
import com.incafit.Model.DetalleFactura;
import com.incafit.Model.Membresia;
import com.incafit.Model.Socio;
import com.incafit.Repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;

    @Autowired
    public FacturaServiceImpl(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    public Factura generarFactura(Socio socio, Membresia membresia) {
        Factura factura = new Factura();
        factura.setSocio(socio);
        factura.setFechaEmision(LocalDate.now());
        factura.setTotal(membresia.getPrecioBase());
        factura.setEstado("PENDIENTE");

        // Crear detalle de factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setConcepto("Membresía: " + membresia.getNombre());
        detalle.setMonto(membresia.getPrecioBase());
        detalle.setFactura(factura);

        factura.getDetalles().add(detalle);

        return facturaRepository.save(factura);
    }

    @Override
    public Factura generarFacturaPorReserva(Socio socio, BigDecimal monto, String concepto) {
        Factura factura = new Factura();
        factura.setSocio(socio);
        factura.setFechaEmision(LocalDate.now());
        factura.setTotal(monto);
        factura.setEstado("PENDIENTE");

        // Crear detalle de factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setConcepto(concepto);
        detalle.setMonto(monto);
        detalle.setFactura(factura);

        factura.getDetalles().add(detalle);

        return facturaRepository.save(factura);
    }

    @Override
    public void pagarFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        factura.setEstado("PAGADA");
        facturaRepository.save(factura);
    }

    @Override
    public List<Factura> obtenerFacturasPorSocio(Socio socio) {
        return facturaRepository.findBySocio(socio);
    }

    @Override
    public Factura obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }

    @Override
    public List<com.incafit.dto.DataPointDTO> getMonthlyRevenue() {
        return facturaRepository.findMonthlyRevenue().stream()
                .map(result -> new com.incafit.dto.DataPointDTO((String) result[0], (Number) result[1]))
                .collect(java.util.stream.Collectors.toList());
    }
}