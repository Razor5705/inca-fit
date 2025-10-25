package com.incafit.service;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Servicio programado para enviar emails automáticos
 * - Recordatorios de vencimiento de membresía
 * - Otros recordatorios periódicos
 */
@Service
public class EmailSchedulerService {

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Se ejecuta diariamente a las 9:00 AM
     * Revisa las membresías y envía recordatorios si están por vencer
     */
    @Scheduled(cron = "0 0 9 * * ?") // Cada día a las 9:00 AM
    public void verificarVencimientoMembresias() {
        System.out.println("🔔 Iniciando verificación de vencimiento de membresías...");
        
        LocalDate hoy = LocalDate.now();
        List<Socio> sociosActivos = socioRepository.findAll().stream()
                .filter(Socio::isActivo)
                .filter(s -> s.getFechaFinMembresia() != null)
                .toList();

        int emailsEnviados = 0;

        for (Socio socio : sociosActivos) {
            LocalDate fechaVencimiento = socio.getFechaFinMembresia();
            long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaVencimiento);

            // Enviar recordatorio si faltan 7, 3 o 1 día
            if (diasRestantes == 7 || diasRestantes == 3 || diasRestantes == 1) {
                try {
                    emailService.sendRecordatorioMembresiaEmail(socio, (int) diasRestantes);
                    emailsEnviados++;
                    System.out.println("📧 Recordatorio enviado a " + socio.getEmail() + " (" + diasRestantes + " días restantes)");
                } catch (Exception e) {
                    System.err.println("⚠️ Error al enviar recordatorio a " + socio.getEmail() + ": " + e.getMessage());
                }
            }

            // Avisar si ya venció
            if (diasRestantes < 0 && diasRestantes >= -1) {
                try {
                    enviarNotificacionVencida(socio);
                    emailsEnviados++;
                } catch (Exception e) {
                    System.err.println("⚠️ Error al enviar notificación de vencimiento a " + socio.getEmail());
                }
            }
        }

        System.out.println("✅ Verificación completada. Emails enviados: " + emailsEnviados);
    }

    /**
     * Se ejecuta cada lunes a las 10:00 AM
     * Envía un resumen semanal (opcional, para futuras implementaciones)
     */
    @Scheduled(cron = "0 0 10 ? * MON")
    public void enviarResumenSemanal() {
        System.out.println("📊 Preparando resumen semanal... (funcionalidad futura)");
        // Aquí se puede implementar un resumen de actividades de la semana
    }

    private void enviarNotificacionVencida(Socio socio) {
        String subject = "Tu Membresía Ha Vencido - Inca Fit";
        String text = String.format(
            "Hola %s,\n\n" +
            "Te informamos que tu membresía '%s' ha vencido.\n\n" +
            "Para continuar disfrutando de nuestras instalaciones y servicios, " +
            "es necesario que renueves tu membresía lo antes posible.\n\n" +
            "Ventajas de renovar:\n" +
            "- Acceso inmediato al gimnasio\n" +
            "- Mantén tu historial y progreso\n" +
            "- Promociones especiales por renovación\n\n" +
            "Visita nuestra recepción o renueva online desde tu panel de usuario.\n\n" +
            "¡Te esperamos!\n\n" +
            "Saludos cordiales,\n" +
            "El equipo de Inca Fit",
            socio.getNombre(),
            socio.getMembresia() != null ? socio.getMembresia().getNombre() : "tu membresía"
        );
        
        emailService.sendEmail(socio.getEmail(), subject, text);
        System.out.println("⚠️ Notificación de vencimiento enviada a: " + socio.getEmail());
    }
}


