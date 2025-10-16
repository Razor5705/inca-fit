# DIAGRAMA EER - SISTEMA INCA FIT
## Sistema de Gestión de Gimnasio

---

## 📋 INFORMACIÓN GENERAL

**Proyecto:** Trabajo de Fin de Grado (TFG) - Campus FP  
**Sistema:** Inca Fit - Gestión de Gimnasio  
**Base de Datos:** MySQL  
**Tipo de Diagrama:** EER (Enhanced Entity Relationship)  

---

## 🏗️ ESTRUCTURA DEL MODELO EER

### ENTIDADES PRINCIPALES (9 Tablas)

#### 1. **MEMBRESIAS**
```
┌─────────────────────────────────────┐
│            MEMBRESIAS               │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│    | tipo_membresia        | VARCHAR│
│    | precio                | DECIMAL│
│    | duracion_dias         | INT    │
│    | descripcion           | TEXT   │
└─────────────────────────────────────┘
```
**Descripción:** Define los tipos de membresías disponibles  
**Relaciones:** 1:N con SOCIOS  

#### 2. **INSTRUCTORES**
```
┌─────────────────────────────────────┐
│           INSTRUCTORES              │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│    | nombre_completo       | VARCHAR│
│    | especialidad          | VARCHAR│
│ UK | email                 | VARCHAR│
└─────────────────────────────────────┘
```
**Descripción:** Información de los entrenadores  
**Relaciones:** 1:N con CLASES  

#### 3. **CLASES**
```
┌─────────────────────────────────────┐
│             CLASES                  │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│    | nombre                | VARCHAR│
│    | descripcion           | TEXT   │
│    | capacidad_maxima      | INT    │
│ FK | instructor_id         | BIGINT │
│    | hora                  | TIME   │
│    | duracion_minutos      | INT    │
└─────────────────────────────────────┘
```
**Descripción:** Clases disponibles con horarios  
**Relaciones:** N:1 con INSTRUCTORES, 1:N con RESERVAS, 1:N con ASISTENCIAS  

#### 4. **SOCIOS**
```
┌─────────────────────────────────────┐
│             SOCIOS                  │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│ UK | dni                   | VARCHAR│
│    | nombre                | VARCHAR│
│ UK | email                 | VARCHAR│
│    | password              | VARCHAR│
│    | rol                   | ENUM   │
│    | activo                | BOOLEAN│
│    | fecha_registro        | DATE   │
│    | telefono              | VARCHAR│
│ FK | membresia_id          | BIGINT │
└─────────────────────────────────────┘
```
**Descripción:** Miembros del gimnasio (usuarios del sistema)  
**Relaciones:** N:1 con MEMBRESIAS, 1:N con RESERVAS, 1:N con FACTURAS, 1:N con ASISTENCIAS  

#### 5. **RESERVAS**
```
┌─────────────────────────────────────┐
│            RESERVAS                 │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│ FK | socio_id              | BIGINT │
│ FK | clase_id              | BIGINT │
│    | fecha_hora            | DATETIME│
│    | estado                | VARCHAR│
└─────────────────────────────────────┘
```
**Descripción:** Reservas de clases por parte de los socios  
**Relaciones:** N:1 con SOCIOS, N:1 con CLASES, 1:1 con ASISTENCIAS  

#### 6. **FACTURAS**
```
┌─────────────────────────────────────┐
│            FACTURAS                 │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│ FK | socio_id              | BIGINT │
│    | fecha                 | DATE   │
│    | total                 | DECIMAL│
│    | estado                | VARCHAR│
└─────────────────────────────────────┘
```
**Descripción:** Facturas generadas para los socios  
**Relaciones:** N:1 con SOCIOS, 1:N con DETALLES_FACTURA, 1:N con PAGOS  

#### 7. **DETALLES_FACTURA**
```
┌─────────────────────────────────────┐
│         DETALLES_FACTURA            │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│ FK | factura_id            | BIGINT │
│    | descripcion           | VARCHAR│
│    | cantidad              | INT    │
│    | precio_unitario       | DECIMAL│
│    | subtotal              | DECIMAL│
└─────────────────────────────────────┘
```
**Descripción:** Detalles de cada factura  
**Relaciones:** N:1 con FACTURAS  

#### 8. **PAGOS**
```
┌─────────────────────────────────────┐
│             PAGOS                   │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│ FK | factura_id            | BIGINT │
│    | fecha_pago            | DATE   │
│    | monto_pagado          | DECIMAL│
│    | metodo_pago           | VARCHAR│
└─────────────────────────────────────┘
```
**Descripción:** Pagos realizados por los socios  
**Relaciones:** N:1 con FACTURAS  

#### 9. **ASISTENCIAS**
```
┌─────────────────────────────────────┐
│           ASISTENCIAS               │
├─────────────────────────────────────┤
│ PK | id                    | BIGINT │
│ FK | socio_id              | BIGINT │
│ FK | clase_id              | BIGINT │
│ FK | reserva_id            | BIGINT │
│    | fecha                 | DATE   │
└─────────────────────────────────────┘
```
**Descripción:** Registro de asistencia de socios a clases  
**Relaciones:** N:1 con SOCIOS, N:1 con CLASES, 1:1 con RESERVAS  

---

## 🔗 RELACIONES ENTRE ENTIDADES

### Diagrama de Relaciones

```
MEMBRESIAS (1) ──────────────── (N) SOCIOS
     │                              │
     │                              ├── (1) ──── (N) RESERVAS
     │                              │              │
     │                              │              ├── (N) ──── (1) CLASES
     │                              │              │              │
     │                              │              │              ├── (1) ──── (N) INSTRUCTORES
     │                              │              │              │
     │                              │              └── (1) ──── (1) ASISTENCIAS
     │                              │
     │                              └── (1) ──── (N) FACTURAS
     │                                           │
     │                                           ├── (1) ──── (N) DETALLES_FACTURA
     │                                           │
     │                                           └── (1) ──── (N) PAGOS
```

### Descripción de Relaciones

| Relación | Tipo | Descripción |
|----------|------|-------------|
| MEMBRESIAS ↔ SOCIOS | 1:N | Un socio tiene una membresía, una membresía puede ser de múltiples socios |
| INSTRUCTORES ↔ CLASES | 1:N | Un instructor imparte múltiples clases, una clase tiene un instructor |
| SOCIOS ↔ RESERVAS | 1:N | Un socio hace múltiples reservas, una reserva pertenece a un socio |
| CLASES ↔ RESERVAS | 1:N | Una clase tiene múltiples reservas, una reserva es para una clase |
| SOCIOS ↔ FACTURAS | 1:N | Un socio tiene múltiples facturas, una factura pertenece a un socio |
| FACTURAS ↔ DETALLES_FACTURA | 1:N | Una factura tiene múltiples detalles, un detalle pertenece a una factura |
| FACTURAS ↔ PAGOS | 1:N | Una factura puede tener múltiples pagos, un pago pertenece a una factura |
| SOCIOS ↔ ASISTENCIAS | 1:N | Un socio tiene múltiples asistencias, una asistencia pertenece a un socio |
| CLASES ↔ ASISTENCIAS | 1:N | Una clase tiene múltiples asistencias, una asistencia es para una clase |
| RESERVAS ↔ ASISTENCIAS | 1:1 | Una reserva puede generar una asistencia, una asistencia está vinculada a una reserva |

---

## 🔑 CLAVES Y RESTRICCIONES

### Claves Primarias (PK)
- Todas las entidades tienen un campo `id` como clave primaria autoincremental

### Claves Foráneas (FK)
- `socios.membresia_id` → `membresias.id`
- `clases.instructor_id` → `instructores.id`
- `reservas.socio_id` → `socios.id`
- `reservas.clase_id` → `clases.id`
- `facturas.socio_id` → `socios.id`
- `detalles_factura.factura_id` → `facturas.id`
- `pagos.factura_id` → `facturas.id`
- `asistencias.socio_id` → `socios.id`
- `asistencias.clase_id` → `clases.id`
- `asistencias.reserva_id` → `reservas.id`

### Claves Únicas (UK)
- `socios.dni` - DNI único por socio
- `socios.email` - Email único por socio
- `instructores.email` - Email único por instructor
- `asistencias.reserva_id` - Una reserva solo puede generar una asistencia

### Restricciones de Integridad
- **ON DELETE CASCADE:** Eliminación en cascada para relaciones fuertes
- **ON DELETE SET NULL:** Establecer NULL al eliminar para relaciones opcionales
- **DEFAULT:** Valores por defecto para campos como `activo = TRUE`, `estado = 'PENDIENTE'`

---

## 📊 ÍNDICES PARA OPTIMIZACIÓN

### Índices Creados
```sql
-- Índices en campos de búsqueda frecuente
CREATE INDEX idx_socios_email ON socios(email);
CREATE INDEX idx_socios_dni ON socios(dni);

-- Índices en claves foráneas
CREATE INDEX idx_reservas_socio ON reservas(socio_id);
CREATE INDEX idx_reservas_clase ON reservas(clase_id);
CREATE INDEX idx_facturas_socio ON facturas(socio_id);
CREATE INDEX idx_asistencias_socio ON asistencias(socio_id);
CREATE INDEX idx_asistencias_reserva ON asistencias(reserva_id);

-- Índices en campos de fecha para consultas temporales
CREATE INDEX idx_reservas_fecha ON reservas(fecha_hora);
CREATE INDEX idx_facturas_fecha ON facturas(fecha);
CREATE INDEX idx_asistencias_fecha ON asistencias(fecha);
```

---

## 🎯 CARACTERÍSTICAS DEL MODELO

### Normalización
- **Tercera Forma Normal (3NF):** Eliminación de redundancias y dependencias transitivas
- **Separación de responsabilidades:** Cada entidad tiene una función específica
- **Integridad referencial:** Claves foráneas bien definidas

### Escalabilidad
- **Estructura preparada para crecimiento:** Campos apropiados para cada tipo de dato
- **Índices para optimización:** Mejora del rendimiento en consultas frecuentes
- **Flexibilidad:** Estructura adaptable a nuevos requerimientos

### Funcionalidad
- **Gestión completa de socios:** Registro, membresías, estado
- **Sistema de reservas y asistencias:** Control de clases y participación
- **Facturación y pagos:** Sistema completo de cobros
- **Gestión de instructores y clases:** Organización del personal y actividades

### Seguridad
- **Contraseñas hasheadas:** Uso de BCrypt para seguridad
- **Roles de usuario:** ADMIN/USUARIO para control de acceso
- **Campos de estado:** Control de activación/desactivación

---

## 📋 DATOS DE PRUEBA INCLUIDOS

### Usuarios
- **1 Administrador:** admin@incafit.com (admin123)
- **4 Usuarios de prueba:** user123 para todos

### Membresías
- **Mensual:** €50.00 (30 días)
- **Trimestral:** €135.00 (90 días)
- **Anual:** €500.00 (365 días)

### Instructores
- **4 Instructores** con diferentes especialidades
- **6 Clases** diferentes con horarios variados

### Datos de Ejemplo
- Reservas, facturas, pagos y asistencias de ejemplo
- Datos realistas para pruebas del sistema

---

## 🛠️ INSTRUCCIONES PARA GENERAR EL DIAGRAMA EER

### En MySQL Workbench
1. Abrir MySQL Workbench
2. Conectar a la base de datos `incafit_db`
3. Ir a **Database → Reverse Engineer**
4. Seleccionar la base de datos `incafit_db`
5. Seleccionar todas las tablas
6. Generar el diagrama EER automáticamente

### Alternativa
1. Ejecutar el script SQL completo (`incafit_database_script.sql`)
2. Generar el diagrama EER desde el modelo de base de datos
3. Usar las consultas de validación incluidas

---

## ✅ VALIDACIÓN DEL MODELO

### Consultas de Verificación
```sql
-- Verificar integridad referencial
SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, 
       REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = 'incafit_db';

-- Verificar índices creados
SELECT TABLE_NAME, INDEX_NAME, COLUMN_NAME
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'incafit_db';

-- Verificar estructura de tablas
SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'incafit_db';
```

---

## 📝 NOTAS FINALES

Este diagrama EER representa un modelo de base de datos completo y bien estructurado para un sistema de gestión de gimnasio. Incluye todas las entidades necesarias, sus relaciones, restricciones de integridad y optimizaciones para garantizar un rendimiento adecuado.

El modelo está diseñado para ser escalable, mantenible y funcional, cumpliendo con las mejores prácticas de diseño de bases de datos relacionales.
