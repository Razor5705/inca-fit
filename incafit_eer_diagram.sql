-- =====================================================
-- DIAGRAMA EER (Enhanced Entity Relationship) - INCA FIT
-- Sistema de Gestión de Gimnasio
-- Trabajo de Fin de Grado (TFG) - Campus FP
-- =====================================================

-- Este archivo contiene la estructura EER de la base de datos
-- Para visualizar en MySQL Workbench o herramientas similares

-- =====================================================
-- ENTIDADES PRINCIPALES
-- =====================================================

-- 1. ENTIDAD: MEMBRESIAS
-- Descripción: Tipos de membresías disponibles en el gimnasio
-- Atributos: id (PK), tipo_membresia, precio, duracion_dias, descripcion
-- Cardinalidad: 1:N con SOCIOS

-- 2. ENTIDAD: INSTRUCTORES  
-- Descripción: Entrenadores del gimnasio
-- Atributos: id (PK), nombre_completo, especialidad, email (UK)
-- Cardinalidad: 1:N con CLASES

-- 3. ENTIDAD: CLASES
-- Descripción: Clases disponibles en el gimnasio
-- Atributos: id (PK), nombre, descripcion, capacidad_maxima, instructor_id (FK), hora, duracion_minutos
-- Cardinalidad: N:1 con INSTRUCTORES, 1:N con RESERVAS, 1:N con ASISTENCIAS

-- 4. ENTIDAD: SOCIOS
-- Descripción: Miembros del gimnasio (usuarios del sistema)
-- Atributos: id (PK), dni (UK), nombre, email (UK), password, rol, activo, fecha_registro, telefono, membresia_id (FK)
-- Cardinalidad: N:1 con MEMBRESIAS, 1:N con RESERVAS, 1:N con FACTURAS, 1:N con ASISTENCIAS

-- 5. ENTIDAD: RESERVAS
-- Descripción: Reservas de clases por parte de los socios
-- Atributos: id (PK), socio_id (FK), clase_id (FK), fecha_hora, estado
-- Cardinalidad: N:1 con SOCIOS, N:1 con CLASES, 1:1 con ASISTENCIAS

-- 6. ENTIDAD: FACTURAS
-- Descripción: Facturas generadas para los socios
-- Atributos: id (PK), socio_id (FK), fecha, total, estado
-- Cardinalidad: N:1 con SOCIOS, 1:N con DETALLES_FACTURA, 1:N con PAGOS

-- 7. ENTIDAD: DETALLES_FACTURA
-- Descripción: Detalles de cada factura
-- Atributos: id (PK), factura_id (FK), descripcion, cantidad, precio_unitario, subtotal
-- Cardinalidad: N:1 con FACTURAS

-- 8. ENTIDAD: PAGOS
-- Descripción: Pagos realizados por los socios
-- Atributos: id (PK), factura_id (FK), fecha_pago, monto_pagado, metodo_pago
-- Cardinalidad: N:1 con FACTURAS

-- 9. ENTIDAD: ASISTENCIAS
-- Descripción: Registro de asistencia de socios a clases
-- Atributos: id (PK), socio_id (FK), clase_id (FK), reserva_id (FK, UK), fecha
-- Cardinalidad: N:1 con SOCIOS, N:1 con CLASES, 1:1 con RESERVAS

-- =====================================================
-- RELACIONES ENTRE ENTIDADES
-- =====================================================

/*
RELACIONES IDENTIFICADAS:

1. MEMBRESIAS (1) ←→ (N) SOCIOS
   - Un socio puede tener una membresía
   - Una membresía puede ser asignada a múltiples socios
   - FK: socios.membresia_id → membresias.id

2. INSTRUCTORES (1) ←→ (N) CLASES
   - Un instructor puede impartir múltiples clases
   - Una clase tiene un instructor asignado
   - FK: clases.instructor_id → instructores.id

3. SOCIOS (1) ←→ (N) RESERVAS
   - Un socio puede hacer múltiples reservas
   - Una reserva pertenece a un socio
   - FK: reservas.socio_id → socios.id

4. CLASES (1) ←→ (N) RESERVAS
   - Una clase puede tener múltiples reservas
   - Una reserva es para una clase específica
   - FK: reservas.clase_id → clases.id

5. SOCIOS (1) ←→ (N) FACTURAS
   - Un socio puede tener múltiples facturas
   - Una factura pertenece a un socio
   - FK: facturas.socio_id → socios.id

6. FACTURAS (1) ←→ (N) DETALLES_FACTURA
   - Una factura puede tener múltiples detalles
   - Un detalle pertenece a una factura
   - FK: detalles_factura.factura_id → facturas.id

7. FACTURAS (1) ←→ (N) PAGOS
   - Una factura puede tener múltiples pagos
   - Un pago pertenece a una factura
   - FK: pagos.factura_id → facturas.id

8. SOCIOS (1) ←→ (N) ASISTENCIAS
   - Un socio puede tener múltiples asistencias
   - Una asistencia pertenece a un socio
   - FK: asistencias.socio_id → socios.id

9. CLASES (1) ←→ (N) ASISTENCIAS
   - Una clase puede tener múltiples asistencias
   - Una asistencia es para una clase específica
   - FK: asistencias.clase_id → clases.id

10. RESERVAS (1) ←→ (1) ASISTENCIAS
    - Una reserva puede generar una asistencia
    - Una asistencia está vinculada a una reserva
    - FK: asistencias.reserva_id → reservas.id (UNIQUE)
*/

-- =====================================================
-- ATRIBUTOS ESPECIALES Y RESTRICCIONES
-- =====================================================

/*
ATRIBUTOS CLAVE:
- PK: Primary Key (Clave Primaria)
- FK: Foreign Key (Clave Foránea)
- UK: Unique Key (Clave Única)
- NN: Not Null (No Nulo)

RESTRICCIONES DE INTEGRIDAD:
- ON DELETE CASCADE: Eliminación en cascada
- ON DELETE SET NULL: Establecer NULL al eliminar
- DEFAULT: Valores por defecto
- CHECK: Validaciones de datos

ÍNDICES PARA OPTIMIZACIÓN:
- Índices en campos de búsqueda frecuente
- Índices en claves foráneas
- Índices en campos de fecha para consultas temporales
*/

-- =====================================================
-- DIAGRAMA EER VISUAL (Para MySQL Workbench)
-- =====================================================

/*
INSTRUCCIONES PARA CREAR EL DIAGRAMA EER EN MYSQL WORKBENCH:

1. Abrir MySQL Workbench
2. Conectar a la base de datos incafit_db
3. Ir a Database → Reverse Engineer
4. Seleccionar la base de datos incafit_db
5. Seleccionar todas las tablas
6. Generar el diagrama EER automáticamente

ALTERNATIVAMENTE, usar el script SQL completo para crear las tablas
y luego generar el diagrama EER desde el modelo de base de datos.
*/

-- =====================================================
-- RESUMEN DEL MODELO EER
-- =====================================================

/*
CARACTERÍSTICAS DEL MODELO:

1. NORMALIZACIÓN:
   - Tercera forma normal (3NF)
   - Eliminación de redundancias
   - Separación de responsabilidades

2. INTEGRIDAD REFERENCIAL:
   - Claves foráneas bien definidas
   - Restricciones de eliminación apropiadas
   - Validaciones de datos

3. ESCALABILIDAD:
   - Estructura preparada para crecimiento
   - Índices para optimización
   - Campos apropiados para cada tipo de dato

4. FUNCIONALIDAD:
   - Gestión completa de socios
   - Sistema de reservas y asistencias
   - Facturación y pagos
   - Control de membresías
   - Gestión de instructores y clases

5. SEGURIDAD:
   - Contraseñas hasheadas
   - Roles de usuario (ADMIN/USUARIO)
   - Campos de estado para control de acceso
*/

-- =====================================================
-- CONSULTAS PARA VALIDAR EL MODELO EER
-- =====================================================

-- Verificar integridad referencial
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = 'incafit_db'
ORDER BY TABLE_NAME, COLUMN_NAME;

-- Verificar índices creados
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'incafit_db'
ORDER BY TABLE_NAME, INDEX_NAME;

-- Verificar estructura de tablas
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_KEY
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'incafit_db'
ORDER BY TABLE_NAME, ORDINAL_POSITION;
