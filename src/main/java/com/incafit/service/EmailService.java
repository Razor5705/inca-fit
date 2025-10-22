package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Model.Factura;
import com.incafit.Model.DetalleFactura;
import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void sendWelcomeEmail(Socio socio);
    void sendReservaConfirmacionEmail(Socio socio, String nombreClase, String fecha, String hora);
    
    // Nuevos métodos con templates HTML
    void sendWelcomeEmailHtml(Socio socio);
    void sendReservaConfirmacionEmailHtml(Socio socio, String nombreClase, String fecha, String hora);
    void sendCancelacionReservaEmail(Socio socio, String nombreClase, String fecha, String hora);
    void sendFacturaEmail(Socio socio, Factura factura, List<DetalleFactura> detalles);
    void sendRecordatorioMembresiaEmail(Socio socio, int diasRestantes);
}