package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Model.Factura;
import com.incafit.Model.DetalleFactura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

@Service
@ConditionalOnProperty(name = "spring.mail.host")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${email.from}")
    private String emailFrom;

    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("âœ… Email enviado correctamente a: " + to);
        } catch (Exception e) {
            System.err.println("âŒ Error al enviar email a " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendWelcomeEmail(Socio socio) {
        String subject = "Â¡Bienvenido a Inca Fit!";
        String text = String.format(
            "Hola %s,\n\n" +
            "Â¡Bienvenido a Inca Fit!\n\n" +
            "Nos complace confirmar que tu registro se ha completado exitosamente.\n\n" +
            "Detalles de tu cuenta:\n" +
            "- Nombre: %s\n" +
            "- Email: %s\n" +
            "- DNI: %s\n" +
            "- MembresÃ­a: %s\n\n" +
            "Ahora puedes acceder a nuestra plataforma con tu email y contraseÃ±a para:\n" +
            "- Reservar clases\n" +
            "- Ver tu historial de asistencias\n" +
            "- Gestionar tu perfil\n" +
            "- Consultar tus facturas\n\n" +
            "Si tienes alguna pregunta, no dudes en contactarnos.\n\n" +
            "Â¡Esperamos verte pronto en el gimnasio!\n\n" +
            "Saludos cordiales,\n" +
            "El equipo de Inca Fit",
            socio.getNombre(),
            socio.getNombre(),
            socio.getEmail(),
            socio.getDni(),
            socio.getMembresia() != null ? socio.getMembresia().getNombre() : "No asignada"
        );
        
        sendEmail(socio.getEmail(), subject, text);
    }

    @Override
    public void sendReservaConfirmacionEmail(Socio socio, String nombreClase, String fecha, String hora) {
        String subject = "ConfirmaciÃ³n de Reserva - Inca Fit";
        String text = String.format(
            "Hola %s,\n\n" +
            "Tu reserva ha sido confirmada exitosamente.\n\n" +
            "Detalles de la clase:\n" +
            "- Clase: %s\n" +
            "- Fecha: %s\n" +
            "- Hora: %s\n\n" +
            "Te esperamos en el gimnasio. Recuerda llegar 10 minutos antes del inicio de la clase.\n\n" +
            "Si necesitas cancelar tu reserva, por favor hazlo con al menos 2 horas de anticipaciÃ³n.\n\n" +
            "Saludos cordiales,\n" +
            "El equipo de Inca Fit",
            socio.getNombre(),
            nombreClase,
            fecha,
            hora
        );
        
        sendEmail(socio.getEmail(), subject, text);
    }

    // ==================== NUEVOS MÃ‰TODOS CON HTML ====================
    
    @Override
    public void sendWelcomeEmailHtml(Socio socio) {
        try {
            Context context = new Context();
            context.setVariable("socio", socio);
            context.setVariable("loginUrl", appBaseUrl + "/login");

            String htmlContent = templateEngine.process("email/bienvenida", context);
            sendHtmlEmail(socio.getEmail(), "Â¡Bienvenido a Inca Fit!", htmlContent);
            System.out.println("âœ… Email HTML de bienvenida enviado a: " + socio.getEmail());
        } catch (Exception e) {
            System.err.println("âš ï¸ Error al enviar email HTML de bienvenida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendReservaConfirmacionEmailHtml(Socio socio, String nombreClase, String fecha, String hora) {
        try {
            Context context = new Context();
            context.setVariable("socio", socio);
            context.setVariable("loginUrl", appBaseUrl + "/login");

            context.setVariable("nombreClase", nombreClase);
            context.setVariable("fecha", fecha);
            context.setVariable("hora", hora);
            
            String htmlContent = templateEngine.process("email/confirmacion-reserva", context);
            sendHtmlEmail(socio.getEmail(), "ConfirmaciÃ³n de Reserva - Inca Fit", htmlContent);
            System.out.println("âœ… Email HTML de confirmaciÃ³n de reserva enviado a: " + socio.getEmail());
        } catch (Exception e) {
            System.err.println("âš ï¸ Error al enviar email HTML de confirmaciÃ³n: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendCancelacionReservaEmail(Socio socio, String nombreClase, String fecha, String hora) {
        try {
            Context context = new Context();
            context.setVariable("socio", socio);
            context.setVariable("loginUrl", appBaseUrl + "/login");

            context.setVariable("nombreClase", nombreClase);
            context.setVariable("fecha", fecha);
            context.setVariable("hora", hora);
            context.setVariable("fechaCancelacion", LocalDateTime.now());
            
            String htmlContent = templateEngine.process("email/cancelacion-reserva", context);
            sendHtmlEmail(socio.getEmail(), "Reserva Cancelada - Inca Fit", htmlContent);
            System.out.println("âœ… Email de cancelaciÃ³n enviado a: " + socio.getEmail());
        } catch (Exception e) {
            System.err.println("âš ï¸ Error al enviar email de cancelaciÃ³n: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendFacturaEmail(Socio socio, Factura factura, List<DetalleFactura> detalles) {
        try {
            Context context = new Context();
            context.setVariable("socio", socio);
            context.setVariable("loginUrl", appBaseUrl + "/login");

            context.setVariable("factura", factura);
            context.setVariable("detalles", detalles);
            
            String htmlContent = templateEngine.process("email/factura", context);
            String subject = "Factura #" + factura.getId() + " - Inca Fit";
            sendHtmlEmail(socio.getEmail(), subject, htmlContent);
            System.out.println("âœ… Email de factura enviado a: " + socio.getEmail());
        } catch (Exception e) {
            System.err.println("âš ï¸ Error al enviar email de factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendRecordatorioMembresiaEmail(Socio socio, int diasRestantes) {
        String subject = "Recordatorio: Tu MembresÃ­a Vence Pronto - Inca Fit";
        String text = String.format(
            "Hola %s,\n\n" +
            "Te recordamos que tu membresÃ­a '%s' vencerÃ¡ en %d dÃ­as.\n\n" +
            "Fecha de vencimiento: %s\n\n" +
            "Para evitar interrupciones en tu entrenamiento, te recomendamos renovar tu membresÃ­a " +
            "antes de la fecha de vencimiento.\n\n" +
            "Puedes renovar fÃ¡cilmente desde tu panel de usuario o contactÃ¡ndonos directamente.\n\n" +
            "Beneficios de renovar ahora:\n" +
            "- Sin interrupciones en tu acceso al gimnasio\n" +
            "- MantÃ©n tu progreso y rutinas\n" +
            "- Posibles descuentos por renovaciÃ³n anticipada\n\n" +
            "Si ya has renovado, ignora este mensaje.\n\n" +
            "Gracias por ser parte de Inca Fit.\n\n" +
            "Saludos cordiales,\n" +
            "El equipo de Inca Fit",
            socio.getNombre(),
            socio.getMembresia() != null ? socio.getMembresia().getNombre() : "tu membresÃ­a",
            diasRestantes,
            socio.getFechaFinMembresia() != null ? socio.getFechaFinMembresia().toString() : "N/A"
        );
        
        sendEmail(socio.getEmail(), subject, text);
    }

    // MÃ©todo auxiliar para enviar emails HTML
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indica que es HTML
            
            mailSender.send(message);
            System.out.println("âœ… Email HTML enviado correctamente a: " + to);
        } catch (MessagingException e) {
            System.err.println("âŒ Error al enviar email HTML a " + to + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al enviar email HTML", e);
        }
    }
}

