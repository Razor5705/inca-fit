# Prompt General - TFG Inca Fit - Sistema de Gestión de Gimnasio

## 📋 Contexto del Proyecto

**Proyecto**: Trabajo de Fin de Grado (TFG) - Campus FP  
**Aplicación**: Inca Fit - Sistema de Gestión de Gimnasio  
**Lenguaje**: Java 21  
**Framework**: Spring Boot 3.5.3  
**Base de Datos**: MySQL 8.0  
**Tipo**: Aplicación Web Full-Stack

---

## 🎯 Objetivo del Sistema

Inca Fit es una **aplicación web completa de gestión de gimnasio** que permite:
- Gestión de socios y membresías
- Reserva de clases grupales
- Control de asistencias
- Facturación y pagos
- Gestión de instructores
- Sistema de notificaciones por email

---

## 🛠️ Stack Tecnológico

### Backend
- **Java 21** (JDK 21)
- **Spring Boot 3.5.3**
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Acceso a datos
- **Hibernate** - ORM
- **Spring Mail** - Envío de emails
- **Spring Scheduling** - Tareas programadas
- **Maven** - Gestión de dependencias

### Frontend
- **Thymeleaf** - Motor de plantillas
- **Bootstrap 5.3.0** (WebJars)
- **HTML5/CSS3/JavaScript**
- **Thymeleaf Layout Dialect** - Layouts reutilizables

### Base de Datos
- **MySQL 8.0**
- **9 tablas** principales
- **Índices optimizados** para consultas frecuentes

### Herramientas
- **IntelliJ IDEA** (desarrollo)
- **MySQL Workbench** (base de datos)
- **Maven** (construcción)
- **Git/GitHub** (control de versiones)

---

## 📊 Estructura de la Base de Datos

### 9 Entidades Principales

1. **SOCIOS** - Miembros del gimnasio (usuarios del sistema)
   - Campos: id, dni (UK), nombre, email (UK), password, rol, activo, fecha_registro, telefono
   - Relaciones: N:1 con MEMBRESIAS, 1:N con RESERVAS, 1:N con FACTURAS, 1:N con ASISTENCIAS

2. **MEMBRESIAS** - Tipos de membresías disponibles
   - Campos: id, tipo_membresia, precio, duracion_dias, descripcion
   - Relaciones: 1:N con SOCIOS

3. **INSTRUCTORES** - Entrenadores del gimnasio
   - Campos: id, nombre_completo, especialidad, email (UK), telefono, experiencia
   - Relaciones: 1:N con CLASES

4. **CLASES** - Clases disponibles
   - Campos: id, nombre, descripcion, capacidad_maxima, instructor_id (FK), hora, duracion_minutos, activo
   - Relaciones: N:1 con INSTRUCTORES, 1:N con RESERVAS, 1:N con ASISTENCIAS

5. **RESERVAS** - Reservas de clases por socios
   - Campos: id, socio_id (FK), clase_id (FK), fecha_hora, estado (CONFIRMADA/CANCELADA)
   - Relaciones: N:1 con SOCIOS, N:1 con CLASES, 1:1 con ASISTENCIAS

6. **FACTURAS** - Facturación de socios
   - Campos: id, socio_id (FK), fecha, total, estado (PENDIENTE/PAGADA/CANCELADA)
   - Relaciones: N:1 con SOCIOS, 1:N con DETALLES_FACTURA, 1:N con PAGOS

7. **DETALLES_FACTURA** - Detalles de cada factura
   - Campos: id, factura_id (FK), descripcion, cantidad, precio_unitario, subtotal, tipo_item, membresia_id (FK), reserva_id (FK)
   - Relaciones: N:1 con FACTURAS, N:1 con MEMBRESIAS, N:1 con RESERVAS

8. **PAGOS** - Pagos realizados
   - Campos: id, factura_id (FK), fecha_pago, monto_pagado, metodo_pago
   - Relaciones: N:1 con FACTURAS

9. **ASISTENCIAS** - Control de asistencia a clases
   - Campos: id, socio_id (FK), clase_id (FK), reserva_id (FK, UK), fecha
   - Relaciones: N:1 con SOCIOS, N:1 con CLASES, 1:1 con RESERVAS

### Relaciones Clave
- Un socio → Una membresía → Múltiples reservas
- Una clase → Un instructor → Múltiples reservas
- Una reserva → Genera asistencia automáticamente
- Una factura → Múltiples detalles → Puede incluir membresía o reservas
- Una factura → Múltiples pagos

---

## 📁 Estructura del Proyecto

```
inca-fit/
├── src/
│   ├── main/
│   │   ├── java/com/incafit/
│   │   │   ├── Config/                    # Configuraciones
│   │   │   │   ├── DataInitializer.java  # Datos iniciales
│   │   │   │   ├── SecurityConfig.java    # Configuración de seguridad
│   │   │   │   └── SchedulingConfig.java  # Tareas programadas
│   │   │   ├── Controller/                # Controladores
│   │   │   │   ├── RegistroController.java
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── SocioReservaController.java
│   │   │   │   ├── SocioMembresiaController.java
│   │   │   │   └── admin/
│   │   │   │       ├── SocioController.java
│   │   │   │       ├── MembresiaController.java
│   │   │   │       ├── ClaseController.java
│   │   │   │       ├── InstructorController.java
│   │   │   │       ├── ReservaController.java
│   │   │   │       ├── AsistenciaController.java
│   │   │   │       ├── FacturaController.java
│   │   │   │       └── EmailTestController.java
│   │   │   ├── Model/                     # Entidades JPA
│   │   │   │   ├── Socio.java
│   │   │   │   ├── Membresia.java
│   │   │   │   ├── Instructor.java
│   │   │   │   ├── Clase.java
│   │   │   │   ├── Reserva.java
│   │   │   │   ├── Factura.java
│   │   │   │   ├── DetalleFactura.java
│   │   │   │   ├── Pago.java
│   │   │   │   ├── Asistencia.java
│   │   │   │   └── Rol.java
│   │   │   ├── Repository/                # Repositorios JPA
│   │   │   │   ├── SocioRepository.java
│   │   │   │   ├── MembresiaRepository.java
│   │   │   │   ├── InstructorRepository.java
│   │   │   │   ├── ClaseRepository.java
│   │   │   │   ├── ReservaRepository.java
│   │   │   │   ├── FacturaRepository.java
│   │   │   │   ├── DetalleFacturaRepository.java
│   │   │   │   ├── PagoRepository.java
│   │   │   │   └── AsistenciaRepository.java
│   │   │   ├── service/                   # Lógica de negocio
│   │   │   │   ├── SocioService.java
│   │   │   │   ├── SocioServiceImpl.java
│   │   │   │   ├── ReservaService.java
│   │   │   │   ├── ReservaServiceImpl.java
│   │   │   │   ├── FacturaService.java
│   │   │   │   ├── EmailService.java
│   │   │   │   ├── EmailServiceImpl.java
│   │   │   │   └── EmailSchedulerService.java
│   │   │   ├── dto/                       # DTOs para transferencia
│   │   │   │   ├── RegistroSocioDto.java
│   │   │   │   ├── BasicInfo.java
│   │   │   │   └── PaymentInfo.java
│   │   │   └── IncaFitApplication.java    # Clase principal
│   │   └── resources/
│   │       ├── application.properties     # Configuración
│   │       ├── static/css/
│   │       │   └── custom.css            # Estilos personalizados
│   │       └── templates/                 # Plantillas Thymeleaf
│   │           ├── fragments/            # Fragmentos reutilizables
│   │           ├── admin/                # Vistas de admin
│   │           ├── socio/                # Vistas de socio
│   │           └── email/                # Templates de emails
│   └── test/                             # Tests
└── pom.xml                               # Maven POM
```

---

## 🔑 Funcionalidades Principales

### Para Socios (USUARIO)
- ✅ Registro y autenticación
- ✅ Dashboard personalizado
- ✅ Ver información de membresía
- ✅ Reservar clases
- ✅ Ver/Cancelar reservas
- ✅ Ver historial de facturas
- ✅ Pagar facturas pendientes

### Para Administradores (ADMIN)
- ✅ Gestión completa de socios
- ✅ Gestión de membresías
- ✅ Gestión de instructores
- ✅ Gestión de clases
- ✅ Visualizar todas las reservas
- ✅ Control de asistencias
- ✅ Gestión de facturas
- ✅ Panel de pruebas de emails

### Sistema de Emails
- ✅ Email de bienvenida al registrarse
- ✅ Confirmación de reserva
- ✅ Cancelación de reserva (pendiente integración)
- ✅ Facturas (pendiente integración)
- ✅ Recordatorios automáticos de membresías vencidas

---

## 🔧 Configuración Actual

### application.properties
```properties
# Aplicación
spring.application.name=inca-fit
server.port=8080

# Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/incafit_db
spring.datasource.username=root
spring.datasource.password=admin1A
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false

# Email (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=incafit.soporte@gmail.com
spring.mail.password=eppizetwrjaxeoxb
```

### Dependencias en pom.xml
```xml
<!-- Spring Boot Starters -->
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-thymeleaf
- spring-boot-starter-mail
- spring-boot-starter-validation
- spring-boot-starter-test

<!-- Base de Datos -->
- mysql-connector-j

<!-- Security & Thymeleaf -->
- thymeleaf-extras-springsecurity6
- thymeleaf-layout-dialect

<!-- Frontend -->
- bootstrap (5.3.0)
- webjars-locator-core
```

---

## 🔒 Sistema de Seguridad

### Spring Security Config
- **Autenticación**: Basada en formularios
- **Encriptación**: BCrypt para contraseñas
- **Roles**: ADMIN, USUARIO
- **Rutas protegidas**:
  - `/admin/**` → Solo ADMIN
  - `/socio/**` → USUARIO o ADMIN
  - `/`, `/registro/**`, `/login` → Públicas

### Usuarios por Defecto
- **Admin**: admin@incafit.com / admin (Rol: ADMIN)
- **Socio de prueba**: usuario@ejemplo.com / password (Rol: USUARIO)

---

## 📝 Funcionalidades Implementadas

### ✅ Completadas
1. Sistema de autenticación y roles
2. Registro de socios en 3 pasos
3. Gestión completa CRUD de todas las entidades
4. Reservas de clases con estado
5. Control de asistencias automático
6. Facturación completa
7. Sistema de emails automáticos
8. Recordatorios programados
9. Dashboard para socios y admin
10. Interfaz responsive con Bootstrap 5

### ⚠️ Pendientes de Integración
1. **Email de cancelación** - Método existe, falta llamarlo en `ReservaServiceImpl.cancelarReserva()`
2. **Email de factura** - Método existe, falta integrarlo en el flujo de facturación
3. **Cambiar emails a HTML** - Cambiar `sendWelcomeEmail` y `sendReservaConfirmacionEmail` por sus versiones HTML

---

## 🚨 Puntos Importantes para Continuar

### Patrón Arquitectónico
- **Controller** → **Service** → **Repository** → **Database**
- Servicios con lógica de negocio separada
- Repositorios JPA para acceso a datos
- DTOs para transferencia de datos complejos

### Convenciones de Código
- Nombres de métodos descriptivos
- Manejo de errores con try-catch
- Validaciones con `@Valid` y `@NotBlank`, `@Email`, etc.
- Logs informativos (✅ para éxito, ⚠️ para warnings, ❌ para errores)

### Configuración de Email
- SMTP configurado para Gmail
- Contraseña de aplicación: `eppizetwrjaxeoxb`
- Templates HTML en `src/main/resources/templates/email/`
- Servicio de emails con manejo de errores no bloqueante

### Tareas Programadas
- `EmailSchedulerService` ejecuta diariamente a las 9:00 AM
- Verifica vencimiento de membresías
- Envía recordatorios automáticamente

---

## 📚 Archivos de Documentación Disponibles

1. **README.md** - Descripción general del proyecto
2. **PROMPT_CONTINUACION_CODEX.md** - Guía específica para sistema de emails
3. **RESUMEN_IMPLEMENTACION_EMAILS.md** - Resumen del sistema de emails
4. **CONFIGURACION_EMAIL.md** - Guía de configuración de emails
5. **FUNCIONALIDADES_EMAIL_COMPLETAS.md** - Documentación técnica de emails
6. **INCA_FIT_DIAGRAMA_EER.md** - Diagrama EER de la base de datos
7. **incafit_database_script.sql** - Script SQL completo
8. **incafit_eer_diagram.sql** - Diagrama EER en SQL

---

## 🎯 Tareas Pendientes Sugeridas

### Prioridad Alta
1. Integrar email de cancelación en `ReservaServiceImpl.cancelarReserva()`
2. Integrar email de factura en el servicio de facturación
3. Cambiar llamadas de email de texto plano a HTML

### Prioridad Media
4. Crear template HTML para recordatorios de membresía
5. Agregar validaciones adicionales en formularios
6. Mejorar manejo de errores en controladores

### Prioridad Baja
7. Agregar tests unitarios
8. Implementar paginación en listados
9. Agregar búsqueda y filtros avanzados
10. Implementar exportación a PDF/Excel

---

## 🔍 Información de Desarrollo

### Base de Datos
- **Nombre**: `incafit_db`
- **Usuario**: `root`
- **Contraseña**: `admin1A`
- **Puerto**: 3306
- **Charset**: utf8mb4_unicode_ci

### Puerto de la Aplicación
- **Puerto**: 8080
- **URL**: http://localhost:8080

### Credenciales de Acceso
- **Admin**: admin@incafit.com / admin
- **Usuario de prueba**: usuario@ejemplo.com / password

---

## 💡 Consideraciones para Desarrollo

### Al modificar código:
1. **NO interrumpir flujos principales** con emails
2. **Usar try-catch** para manejar errores de email
3. **Agregar logs** informativos para debugging
4. **Validar formularios** con anotaciones de Jakarta Validation
5. **Mantener coherencia** con el patrón Controller-Service-Repository

### Al trabajar con la base de datos:
1. **No usar DROP TABLE** a menos que sea absolutamente necesario
2. **Usar ALTER TABLE** para agregar campos
3. **Probar consultas** antes de implementar
4. **Verificar claves foráneas** al modificar relaciones

### Al trabajar con emails:
1. **Probar en panel de admin** primero (`/admin/email-test`)
2. **Usar try-catch** para no bloquear flujos
3. **Seguir el patrón** de formateo de fechas (dd/MM/yyyy)
4. **Verificar logs** en consola después de envíos

---

## 📞 Endpoints Importantes

### Públicos
- `GET /` - Página de inicio
- `GET /login` - Login
- `GET /registro/paso1` - Inicio de registro
- `POST /registro/paso3` - Procesamiento de registro

### Socios
- `GET /dashboard` - Dashboard del socio
- `GET /socio/reservas` - Mis reservas
- `POST /socio/reservas/guardar` - Crear reserva
- `POST /socio/reservas/{id}/cancelar` - Cancelar reserva
- `GET /socio/facturas` - Mis facturas
- `GET /socio/membresia` - Mi membresía

### Admin
- `GET /admin/dashboard` - Dashboard de admin
- `GET /admin/socios` - Lista de socios
- `GET /admin/membresias` - Gestión de membresías
- `GET /admin/clases` - Gestión de clases
- `GET /admin/instructores` - Gestión de instructores
- `GET /admin/reservas` - Todas las reservas
- `GET /admin/asistencias` - Control de asistencias
- `GET /admin/facturas` - Gestión de facturas
- `GET /admin/email-test` - Panel de pruebas de emails

---

## ✅ Estado del Proyecto

**Completitud General**: ~90%

**Funcionalidades Core**: ✅ 100% completas  
**Sistema de Emails**: ⚠️ 80% completas (falta integración)  
**Interfaz de Usuario**: ✅ 100% completas  
**Base de Datos**: ✅ 100% implementada  
**Seguridad**: ✅ 100% implementada

---

**Última actualización**: Octubre 2024  
**Versión del proyecto**: 0.0.1-SNAPSHOT  
**Estado**: En desarrollo activo
