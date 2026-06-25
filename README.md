# IncaFit - Plataforma de gestion para gimnasios

[![Java](https://img.shields.io/badge/Java-21-red.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)]()
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue.svg)]()
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Server%20Side-green.svg)]()

IncaFit es una aplicacion web desarrollada como Trabajo de Fin de Grado para la gestion integral de gimnasios pequenos y medianos. La plataforma centraliza en una unica herramienta los procesos principales del negocio: registro de socios, membresias, clases, reservas, asistencias, facturacion, pagos simulados, notificaciones por email y panel administrativo.

El objetivo del proyecto es reducir la gestion manual con hojas de calculo, llamadas o mensajeria, evitando errores como dobles reservas, sobreaforos, membresias caducadas sin control o facturas pendientes sin seguimiento.

## Tabla de contenidos

- [Funcionalidades principales](#funcionalidades-principales)
- [Roles de usuario](#roles-de-usuario)
- [Arquitectura](#arquitectura)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Modelo de datos](#modelo-de-datos)
- [Flujos principales](#flujos-principales)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Instalacion y ejecucion](#instalacion-y-ejecucion)
- [Configuracion](#configuracion)
- [Base de datos](#base-de-datos)
- [Pruebas](#pruebas)
- [Seguridad](#seguridad)
- [Documentacion incluida](#documentacion-incluida)
- [Lineas futuras](#lineas-futuras)
- [Autor](#autor)

## Funcionalidades principales

### Area publica

- Pagina de inicio de la aplicacion.
- Paginas informativas de contacto y acerca del proyecto.
- Login personalizado con Spring Security.
- Registro de socio en varios pasos.
- Validacion de DNI, email, contrasena y datos de pago simulado.

### Area del socio

- Dashboard personal tras iniciar sesion.
- Consulta y edicion de datos de perfil.
- Cambio de contrasena.
- Visualizacion del estado de la membresia.
- Renovacion o cambio de membresia.
- Calculo automatico de fechas de inicio y fin de vigencia.
- Consulta de clases disponibles.
- Horario interactivo de clases.
- Reserva de plazas con validacion de aforo.
- Bloqueo de reservas si la membresia no esta vigente.
- Cancelacion de reservas.
- Consulta de facturas propias.
- Registro de pago simulado.
- Descarga de facturas en PDF.

### Area de administracion

- Dashboard con metricas principales del gimnasio.
- Gestion CRUD de socios.
- Activacion y desactivacion de socios.
- Gestion CRUD de membresias.
- Gestion CRUD de instructores.
- Gestion CRUD de clases.
- Activacion y desactivacion de clases.
- Gestion de reservas.
- Gestion de asistencias.
- Gestion de facturas.
- Consulta de facturas pendientes y vencidas.
- Panel de pruebas de emails.
- Graficas con Chart.js para apoyar la toma de decisiones.

### Emails y notificaciones

- Email HTML de bienvenida.
- Email de confirmacion de reserva.
- Email de cancelacion de reserva.
- Email de factura.
- Recordatorios programados de vencimiento de membresia.
- Plantillas Thymeleaf para emails.

## Roles de usuario

La aplicacion distingue dos perfiles principales:

| Rol | Acceso | Descripcion |
| --- | --- | --- |
| `USUARIO` | `/socio/**` | Socio del gimnasio. Puede gestionar su membresia, reservas y facturas. |
| `ADMIN` | `/admin/**` y `/socio/**` | Personal administrador. Puede gestionar socios, clases, reservas, facturas, asistencias y estadisticas. |

La autenticacion y autorizacion se gestionan con Spring Security. Las contrasenas se almacenan cifradas con BCrypt.

## Arquitectura

IncaFit sigue una arquitectura MVC y una separacion por capas:

- **Capa de presentacion**: plantillas Thymeleaf, Bootstrap y CSS propio.
- **Capa de controladores**: clases `@Controller` que reciben peticiones HTTP y preparan las vistas.
- **Capa de negocio**: servicios `@Service` con reglas de membresias, reservas, facturas, emails y estadisticas.
- **Capa de persistencia**: repositorios Spring Data JPA.
- **Base de datos**: MySQL con entidades relacionales.

Flujo general:

1. El usuario realiza una accion desde el navegador.
2. Spring Security valida permisos segun la ruta y el rol.
3. Un controlador recibe la peticion.
4. El controlador delega en servicios de negocio.
5. Los servicios consultan o modifican entidades mediante repositorios JPA.
6. Thymeleaf renderiza la vista final con los datos del modelo.

## Tecnologias utilizadas

### Backend

- Java 21
- Spring Boot 3.5.3
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate
- Jakarta Validation
- Spring Mail
- Apache PDFBox

### Frontend

- Thymeleaf
- Thymeleaf Layout Dialect
- Bootstrap 5
- Chart.js
- HTML5
- CSS3
- JavaScript

### Base de datos y herramientas

- MySQL 8.0+
- Maven Wrapper
- Docker y Docker Compose
- Git y GitHub

## Modelo de datos

Entidades principales:

- `Socio`: datos personales, credenciales, rol, estado activo y membresia asociada.
- `Membresia`: plan contratado, descripcion, precio y duracion.
- `Instructor`: datos del instructor.
- `Clase`: actividad, horario, instructor, aforo, dias permitidos, vigencia y precio adicional.
- `Reserva`: relacion entre socio, clase, fecha/hora y estado.
- `Asistencia`: registro de asistencia asociado a una reserva.
- `Factura`: factura emitida a un socio.
- `DetalleFactura`: conceptos incluidos en una factura.
- `Pago`: pagos registrados sobre una factura.

Relaciones destacadas:

- Un socio puede tener muchas reservas.
- Un socio puede tener muchas facturas.
- Una clase pertenece a un instructor.
- Una reserva pertenece a un socio y a una clase.
- Una factura puede tener varios detalles y pagos.
- Una asistencia puede estar vinculada a una reserva.

## Flujos principales

### Registro de socio

1. El socio introduce datos personales.
2. El sistema valida DNI, email y contrasena.
3. El socio selecciona una membresia.
4. El socio introduce datos de pago simulado.
5. Se crea el socio con rol `USUARIO`.
6. Se asigna la membresia y su periodo de vigencia.
7. Se genera la factura inicial.
8. Se registra el pago simulado.
9. Se envia email de bienvenida.

### Reserva de clase

1. El socio accede al horario de clases.
2. Selecciona clase y fecha.
3. El sistema valida que la cuenta este activa.
4. El sistema valida que la membresia este vigente.
5. El sistema comprueba que la clase este activa y vigente.
6. El sistema valida dia, hora y capacidad disponible.
7. Se crea la reserva en estado `CONFIRMADA`.
8. Se genera automaticamente una asistencia asociada.
9. Se envia email de confirmacion.

### Renovacion de membresia

1. El socio accede a la pantalla de membresia.
2. Selecciona plan y metodo de pago.
3. El sistema calcula subtotal, impuestos, descuentos y total.
4. Se calcula la nueva fecha de inicio y fin.
5. Se genera factura.
6. Si el pago es inmediato, se registra como `PAGADA`.
7. Si el pago queda pendiente, la factura queda en estado `PENDIENTE`.

### Facturacion

- Las facturas pueden generarse por alta, renovacion o concepto asociado.
- Cada factura puede tener detalles y pagos.
- El socio puede consultar su historial.
- El socio puede descargar facturas en PDF.
- El administrador puede revisar facturas pendientes, pagadas o vencidas.

## Estructura del proyecto

```text
inca-fit/
├── BBDD/                         # Dumps SQL por tabla
├── src/
│   ├── main/
│   │   ├── java/com/incafit/
│   │   │   ├── Config/           # Seguridad, datos iniciales y scheduling
│   │   │   ├── Controller/       # Controladores web
│   │   │   │   └── admin/        # Controladores del panel administrador
│   │   │   ├── Model/            # Entidades JPA y enums
│   │   │   ├── Repository/       # Repositorios Spring Data JPA
│   │   │   ├── dto/              # DTOs y objetos auxiliares
│   │   │   └── service/          # Logica de negocio
│   │   └── resources/
│   │       ├── static/css/       # Estilos personalizados
│   │       ├── templates/        # Vistas Thymeleaf
│   │       │   ├── admin/        # Vistas del administrador
│   │       │   ├── socio/        # Vistas del socio
│   │       │   ├── email/        # Plantillas HTML de correo
│   │       │   └── fragments/    # Fragmentos comunes
│   │       └── application.properties
│   └── test/                     # Pruebas unitarias y MVC
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## Instalacion y ejecucion

### Requisitos previos

- JDK 21
- MySQL 8.0 o superior
- Git
- Maven Wrapper incluido en el proyecto
- Opcional: Docker y Docker Compose

Comprobar Java:

```bash
java -version
javac -version
```

El proyecto requiere un JDK, no solo un JRE.

### Clonar el repositorio

```bash
git clone https://github.com/Razor5705/inca-fit.git
cd inca-fit
```

### Crear la base de datos

```sql
DROP DATABASE IF EXISTS incafit_db;
CREATE DATABASE incafit_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE incafit_db;
```

Despues importa los dumps de la carpeta `BBDD/` o usa el script completo `BBDD-DEFINIDO-TFG.sql`.

### Ejecutar en local

Windows PowerShell:

```powershell
.\mvnw.cmd clean spring-boot:run
```

Linux/macOS:

```bash
./mvnw clean spring-boot:run
```

La aplicacion queda disponible en:

```text
http://localhost:8080
```

## Configuracion

La configuracion principal esta en:

```text
src/main/resources/application.properties
```

Ejemplo recomendado para entorno local:

```properties
spring.application.name=inca-fit
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/incafit_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
email.from=${MAIL_FROM}
```

> Nota: no se recomienda subir credenciales reales al repositorio. Usa variables de entorno o perfiles externos para datos sensibles.

## Base de datos

Importacion recomendada por orden:

```text
BBDD/incafit_db_membresias.sql
BBDD/incafit_db_instructores.sql
BBDD/incafit_db_clases.sql
BBDD/incafit_db_socios.sql
BBDD/incafit_db_reservas.sql
BBDD/incafit_db_asistencias.sql
BBDD/incafit_db_facturas.sql
BBDD/incafit_db_detalles_factura.sql
BBDD/incafit_db_pagos.sql
```

Tras importar, puedes comprobar cada tabla:

```sql
SELECT COUNT(*) FROM socios;
SELECT COUNT(*) FROM membresias;
SELECT COUNT(*) FROM clases;
SELECT COUNT(*) FROM reservas;
SELECT COUNT(*) FROM facturas;
```

## Usuarios de prueba

El proyecto incluye inicializadores de datos para facilitar pruebas locales cuando la base de datos esta vacia.

Ejemplos habituales:

| Usuario | Contrasena | Rol | Observaciones |
| --- | --- | --- | --- |
| `admin@incafit.com` | `admin123` | `ADMIN` | Usuario demo creado por `DataInitializer` si no existen socios. |
| `juan.perez@example.com` | `user123` | `USUARIO` | Socio demo creado por `DataInitializer`. |
| `tutor.admin@incafit.com` | `Tutor123*` | `ADMIN` | Disponible solo con perfil `dev`. |

Estas cuentas son solo para desarrollo o demostracion. En un despliegue real deben eliminarse o cambiarse.

## Pruebas

Ejecutar tests:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

La suite incluye pruebas sobre reservas y controladores MVC. Para que funcione correctamente se necesita JDK 21 configurado en `JAVA_HOME`.

Casos de prueba manuales recomendados:

- Registro completo de socio.
- Login y acceso al dashboard.
- Reserva con membresia vigente y aforo disponible.
- Bloqueo de reserva con membresia caducada.
- Cancelacion de reserva.
- Renovacion de membresia con factura asociada.
- Pago simulado de factura.
- Descarga de factura en PDF.
- Creacion y edicion de clases desde administracion.
- Revision de estadisticas del dashboard admin.

## Seguridad

Medidas implementadas:

- Autenticacion con Spring Security.
- Autorizacion por roles.
- Cifrado de contrasenas con BCrypt.
- Validaciones de formularios en servidor.
- Separacion de rutas publicas, de socio y de administrador.

Recomendaciones para produccion:

- Activar HTTPS.
- Mover credenciales a variables de entorno.
- Usar perfiles `dev`, `test` y `prod`.
- Desactivar logs SQL y trazas DEBUG.
- Sustituir `spring.jpa.hibernate.ddl-auto=update` por migraciones controladas con Flyway o Liquibase.
- Revisar permisos de operaciones por ID para evitar acceso a recursos ajenos.
- Mantener CSRF activo en formularios.
- Eliminar usuarios demo o limitar su creacion a perfil de desarrollo.

## Docker

El proyecto incluye `Dockerfile` y `docker-compose.yml`.

Ejemplo:

```bash
docker compose up --build
```

Revisa las variables de entorno y la configuracion de MySQL antes de usar Docker en otro equipo.

## Documentacion incluida

El repositorio contiene documentacion adicional:

- `RUN-INSTRUCTIONS.txt`: guia rapida de ejecucion.
- `CONFIGURACION_EMAIL.md`: configuracion del sistema de correo.
- `FUNCIONALIDADES_EMAIL_COMPLETAS.md`: detalle de emails implementados.
- `GUIA_RAPIDA_EMAILS.md`: guia breve de uso de emails.
- `INDICE_EMAILS.md`: indice de documentacion de correos.
- `INCA_FIT_DIAGRAMA_EER.md`: descripcion del modelo EER.
- `INSTRUCCIONES_DIAGRAMA_EER.md`: instrucciones para diagramas.
- `RESUMEN_MEJORAS_BASE_DATOS.md`: mejoras realizadas en base de datos.
- `RESUMEN_IMPLEMENTACION_EMAILS.md`: resumen de implementacion del modulo de emails.
- `REPORTE_PRUEBAS.md`: reporte de pruebas.

## Lineas futuras

Mejoras propuestas para evolucionar IncaFit:

- Integracion con pasarela de pago real.
- Verificacion de email en el registro.
- Recuperacion de contrasena.
- Lista de espera para clases completas.
- Penalizacion o control de cancelaciones tardias.
- Check-in de asistencia mediante QR.
- Informes avanzados de ocupacion, ingresos y renovaciones.
- Exportacion de datos a CSV, Excel o PDF.
- API REST para futuras apps moviles.
- Panel responsive optimizado para uso en recepcion.
- Migraciones de base de datos con Flyway o Liquibase.
- Despliegue en servidor cloud con HTTPS.

## Autor

**Nikolas Medina Ricra**  
Trabajo de Fin de Grado - Desarrollo de Aplicaciones Multiplataforma  
Campus FP  
Tutor: Victor Colomo Gomez

## Repositorio

```text
https://github.com/Razor5705/inca-fit
```
