package com.incafit.dto;

import java.math.BigDecimal;

/**
 * Representa el desglose del pago simulado (subtotal + impuestos + descuentos + total final).
 * Se usa tanto en el backend como en la vista para mantener consistencia en los cálculos.
 */
public class ResumenPago {

    private final BigDecimal subtotal;
    private final BigDecimal impuesto;
    private final BigDecimal descuento;
    private final BigDecimal total;

    public ResumenPago(BigDecimal subtotal, BigDecimal impuesto, BigDecimal descuento, BigDecimal total) {
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.descuento = descuento;
        this.total = total;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
