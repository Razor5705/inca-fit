# Configuración de Email - Inca Fit

## Descripción General

El sistema de Inca Fit ahora incluye funcionalidad de envío automático de correos electrónicos a los socios en diferentes eventos:

1. **Email de Bienvenida**: Se envía automáticamente cuando un nuevo socio completa su registro
2. **Confirmación de Reserva**: Se envía cuando un socio reserva una clase

## Configuración en `application.properties`

La configuración de email ya está establecida en el archivo `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=incafit.soporte@gmail.com
spring.mail.password=incafitTFG
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
email.from=incafit.soporte@gmail.com
```

### Importante: Configuración de Gmail

Para que Gmail permita el envío de correos desde aplicaciones externas, necesitas:

1. **Opción 1: Usar contraseña de aplicación (Recomendado)**
   - Ve a tu cuenta de Google > Seguridad
   - Activa la verificación en dos pasos
   - Genera una "Contraseña de aplicación" específica para esta aplicación
   - Usa esa contraseña en lugar de tu contraseña de Gmail normal

2. **Opción 2: Permitir aplicaciones menos seguras** (No recomendado)
   - Ve a https://myaccount.google.com/lesssecureapps
   - Activa "Permitir aplicaciones menos seguras"

## Tipos de Emails Implementados

### 1. Email de Bienvenida

**Cuándo se envía**: Al completar el registro (paso 3) y crear exitosamente la cuenta del socio.

**Ubicación del código**: `RegistroController.java` (líneas 180-186)

**Contenido**:
- Saludo personalizado
- Confirmación de registro exitoso
- Detalles de la cuenta (nombre, email, DNI, membresía)
- Información sobre las funcionalidades disponibles
- Mensaje de bienvenida al gimnasio

**Ejemplo de implementación**:
```java
emailService.sendWelcomeEmail(nuevoSocio);
```

### 2. Email de Confirmación de Reserva

**Cuándo se envía**: Al crear exitosamente una reserva de clase.

**Ubicación del código**: `ReservaServiceImpl.java` (líneas 58-69)

**Contenido**:
- Saludo personalizado
- Confirmación de la reserva
- Detalles de la clase (nombre, fecha, hora)
- Recordatorio de llegar 10 minutos antes
- Política de cancelación

**Ejemplo de implementación**:
```java
emailService.sendReservaConfirmacionEmail(socio, nombreClase, fecha, hora);
```

## Arquitectura del Servicio de Email

### Interfaz: `EmailService.java`

Define los métodos disponibles:
- `sendEmail(String to, String subject, String text)`: Método genérico
- `sendWelcomeEmail(Socio socio)`: Email de bienvenida
- `sendReservaConfirmacionEmail(Socio socio, String nombreClase, String fecha, String hora)`: Confirmación de reserva

### Implementación: `EmailServiceImpl.java`

- Usa `JavaMailSender` de Spring Boot
- Incluye manejo de errores (try-catch) para no interrumpir el flujo principal
- Logs informativos en consola para debugging
- Configuración condicional (`@ConditionalOnProperty`) - solo se activa si está configurado `spring.mail.host`

## Manejo de Errores

El sistema está diseñado para que **el envío de emails no interrumpa el proceso principal**:

```java
try {
    emailService.sendWelcomeEmail(nuevoSocio);
    System.out.println("✅ Email enviado correctamente");
} catch (Exception e) {
    System.err.println("⚠️ Error al enviar email: " + e.getMessage());
    // El proceso continúa normalmente
}
```

Si hay un error al enviar el email:
- Se registra en los logs
- Se muestra un mensaje de advertencia en consola
- El registro/reserva se completa exitosamente de todas formas

## Testing

### Verificar Envío de Emails

1. **Registro de nuevo socio**:
   - Ir a `/registro`
   - Completar los 3 pasos del registro
   - Verificar que llegue el email de bienvenida

2. **Reserva de clase**:
   - Iniciar sesión como socio
   - Ir a `/socio/reservas/nueva`
   - Crear una reserva
   - Verificar que llegue el email de confirmación

### Logs en Consola

Los logs mostrarán:
```
✅ Email enviado correctamente a: usuario@ejemplo.com
✅ Email de bienvenida enviado a: usuario@ejemplo.com
✅ Email de confirmación de reserva enviado a: usuario@ejemplo.com
```

En caso de error:
```
❌ Error al enviar email a usuario@ejemplo.com: [mensaje de error]
⚠️ Error al enviar email de bienvenida: [mensaje de error]
```

## Dependencias Necesarias

Ya incluidas en `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

## Futuras Mejoras Sugeridas

1. **Templates HTML**: Usar plantillas HTML con Thymeleaf en lugar de texto plano
2. **Email de Cancelación**: Notificar cuando se cancela una reserva
3. **Recordatorios**: Enviar recordatorio 24h antes de una clase reservada
4. **Renovación de Membresía**: Avisar cuando esté próxima a vencer
5. **Facturas**: Enviar copia de factura por email al generar una
6. **Async**: Hacer el envío asíncrono para mejorar el rendimiento
7. **Queue**: Usar una cola de mensajes para reintentos automáticos

## Solución de Problemas

### Error: "Authentication failed"
- Verifica que la contraseña sea correcta
- Usa una contraseña de aplicación si tienes verificación en 2 pasos

### Error: "Connection timeout"
- Verifica que el puerto 587 no esté bloqueado por firewall
- Confirma que tienes conexión a internet

### Los emails no llegan
- Revisa la carpeta de spam
- Verifica que la dirección `email.from` esté correcta
- Comprueba los logs de la aplicación para ver si hay errores

### Email se envía pero con errores de formato
- Verifica que los datos del socio estén completos
- Asegúrate de que la membresía esté asignada correctamente

## Contacto de Soporte

Email configurado: `incafit.soporte@gmail.com`

Este email será el remitente de todas las notificaciones automáticas del sistema.

