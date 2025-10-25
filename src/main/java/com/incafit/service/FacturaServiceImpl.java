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
import java.time.YearMonth;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final EmailService emailService;

    @Autowired
    public FacturaServiceImpl(FacturaRepository facturaRepository,
                              EmailService emailService) {
        this.facturaRepository = facturaRepository;
        this.emailService = emailService;
    }

    @Override
    public Factura generarFactura(Socio socio, Membresia membresia) {
        Factura factura = new Factura();
        factura.setSocio(socio);
        factura.setFecha(LocalDate.now());
        factura.setTotal(membresia.getPrecio());
        factura.setEstado("PENDIENTE");

        // Crear detalle de factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setDescripcion("Membresía: " + membresia.getNombre());
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(membresia.getPrecio());
        detalle.setSubtotal(membresia.getPrecio());
        detalle.setFactura(factura);

        factura.getDetalles().add(detalle);

        Factura facturaGuardada = facturaRepository.save(factura);
        notificarFacturaPorEmail(socio, facturaGuardada);
        return facturaGuardada;
    }

    @Override
    public Factura generarFacturaPorReserva(Socio socio, BigDecimal monto, String concepto) {
        Factura factura = new Factura();
        factura.setSocio(socio);
        factura.setFecha(LocalDate.now());
        factura.setTotal(monto);
        factura.setEstado("PENDIENTE");

        // Crear detalle de factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setDescripcion(concepto);
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(monto);
        detalle.setSubtotal(monto);
        detalle.setFactura(factura);

        factura.getDetalles().add(detalle);

        Factura facturaGuardada = facturaRepository.save(factura);
        notificarFacturaPorEmail(socio, facturaGuardada);
        return facturaGuardada;
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

    public class FacturaNoEncontradaException extends RuntimeException {
        public FacturaNoEncontradaException(Long id) {
            super("Factura no encontrada con ID: " + id);
        }
    }

    @Override
    public Factura obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }

    @Override
    public List<Factura> obtenerTodasFacturas() {
        return facturaRepository.findAll(); // Debe usar el repositorio para obtener todas las facturas
    }


    @Override
    public List<Factura> obtenerFacturasPorEstado(String estado) {
        return facturaRepository.findByEstado(estado); // Debe usar el repositorio para filtrar por estado
    }

    @Override
    public List<Factura> obtenerFacturasPagadasEsteMes() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now().withDayOfMonth(YearMonth.now().lengthOfMonth());

        return facturaRepository.findByEstadoAndFechaBetween(
                "PAGADA", inicioMes, finMes); // Filtra por estado y rango de fechas
    }

    private void notificarFacturaPorEmail(Socio socio, Factura factura) {
        if (socio == null || factura == null) {
            return;
        }
        try {
            emailService.sendFacturaEmail(socio, factura, factura.getDetalles());
            System.out.println("[INFO] Email de factura enviado a: " + socio.getEmail());
        } catch (Exception e) {
            System.err.println("[WARN] Error al enviar email de factura: " + e.getMessage());
        }
    }
}
