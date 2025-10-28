package com.incafit.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuración para habilitar tareas programadas (scheduled tasks)
 * Permite que los servicios con @Scheduled se ejecuten automáticamente
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    // Esta clase habilita las anotaciones @Scheduled en toda la aplicación
}





