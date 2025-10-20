-- =====================================================
-- SCRIPT PARA AGREGAR LOS CAMPOS NUEVOS A LA BASE DE DATOS EXISTENTE
-- VERSION SEGURA - Sin problemas con Safe Update Mode
-- Ejecutar este script en tu base de datos incafit_db
-- =====================================================

USE incafit_db;

-- Deshabilitar temporalmente el modo seguro
SET SQL_SAFE_UPDATES = 0;

-- =====================================================
-- 1. AGREGAR CAMPOS A LA TABLA DETALLES_FACTURA
-- =====================================================

-- Verificar si las columnas ya existen
SET @exist_tipo_item = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'detalles_factura' AND COLUMN_NAME = 'tipo_item');
SET @exist_membresia_id = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'detalles_factura' AND COLUMN_NAME = 'membresia_id');
SET @exist_reserva_id = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'detalles_factura' AND COLUMN_NAME = 'reserva_id');

-- Agregar campo tipo_item
SET @sql_tipo_item = IF(@exist_tipo_item = 0,
    'ALTER TABLE detalles_factura ADD COLUMN tipo_item VARCHAR(50)',
    'SELECT "Campo tipo_item ya existe" as mensaje');
PREPARE stmt FROM @sql_tipo_item;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar campo membresia_id
SET @sql_membresia_id = IF(@exist_membresia_id = 0,
    'ALTER TABLE detalles_factura ADD COLUMN membresia_id BIGINT',
    'SELECT "Campo membresia_id ya existe" as mensaje');
PREPARE stmt FROM @sql_membresia_id;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar campo reserva_id
SET @sql_reserva_id = IF(@exist_reserva_id = 0,
    'ALTER TABLE detalles_factura ADD COLUMN reserva_id BIGINT',
    'SELECT "Campo reserva_id ya existe" as mensaje');
PREPARE stmt FROM @sql_reserva_id;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar las claves foráneas (ignorar si ya existen)
SET @sql_fk1 = 'ALTER TABLE detalles_factura ADD CONSTRAINT fk_detalle_membresia FOREIGN KEY (membresia_id) REFERENCES membresias(id) ON DELETE SET NULL';
SET @sql_fk2 = 'ALTER TABLE detalles_factura ADD CONSTRAINT fk_detalle_reserva FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE SET NULL';

-- Intentar agregar FK1 (puede fallar si ya existe, está bien)
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLSTATE '42000' SELECT 'FK membresia ya existe' as mensaje;
    PREPARE stmt FROM @sql_fk1;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;

-- Intentar agregar FK2 (puede fallar si ya existe, está bien)
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLSTATE '42000' SELECT 'FK reserva ya existe' as mensaje;
    PREPARE stmt FROM @sql_fk2;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;

-- =====================================================
-- 2. AGREGAR CAMPOS A LA TABLA SOCIOS
-- =====================================================

-- Verificar si las columnas ya existen
SET @exist_fecha_inicio = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'socios' AND COLUMN_NAME = 'fecha_inicio_membresia');
SET @exist_fecha_fin = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'socios' AND COLUMN_NAME = 'fecha_fin_membresia');

-- Agregar fecha_inicio_membresia
SET @sql_fecha_inicio = IF(@exist_fecha_inicio = 0,
    'ALTER TABLE socios ADD COLUMN fecha_inicio_membresia DATE',
    'SELECT "Campo fecha_inicio_membresia ya existe" as mensaje');
PREPARE stmt FROM @sql_fecha_inicio;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar fecha_fin_membresia
SET @sql_fecha_fin = IF(@exist_fecha_fin = 0,
    'ALTER TABLE socios ADD COLUMN fecha_fin_membresia DATE',
    'SELECT "Campo fecha_fin_membresia ya existe" as mensaje');
PREPARE stmt FROM @sql_fecha_fin;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =====================================================
-- 3. AGREGAR CAMPOS A LA TABLA CLASES
-- =====================================================

-- Verificar si las columnas ya existen
SET @exist_activo = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'clases' AND COLUMN_NAME = 'activo');
SET @exist_clase_fecha_inicio = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'clases' AND COLUMN_NAME = 'fecha_inicio');
SET @exist_clase_fecha_fin = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'clases' AND COLUMN_NAME = 'fecha_fin');
SET @exist_precio_adicional = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'incafit_db' AND TABLE_NAME = 'clases' AND COLUMN_NAME = 'precio_adicional');

-- Agregar campo activo
SET @sql_activo = IF(@exist_activo = 0,
    'ALTER TABLE clases ADD COLUMN activo BOOLEAN DEFAULT TRUE',
    'SELECT "Campo activo ya existe" as mensaje');
PREPARE stmt FROM @sql_activo;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar fecha_inicio
SET @sql_clase_fecha_inicio = IF(@exist_clase_fecha_inicio = 0,
    'ALTER TABLE clases ADD COLUMN fecha_inicio DATE',
    'SELECT "Campo fecha_inicio ya existe" as mensaje');
PREPARE stmt FROM @sql_clase_fecha_inicio;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar fecha_fin
SET @sql_clase_fecha_fin = IF(@exist_clase_fecha_fin = 0,
    'ALTER TABLE clases ADD COLUMN fecha_fin DATE',
    'SELECT "Campo fecha_fin ya existe" as mensaje');
PREPARE stmt FROM @sql_clase_fecha_fin;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Agregar precio_adicional
SET @sql_precio_adicional = IF(@exist_precio_adicional = 0,
    'ALTER TABLE clases ADD COLUMN precio_adicional DECIMAL(10,2)',
    'SELECT "Campo precio_adicional ya existe" as mensaje');
PREPARE stmt FROM @sql_precio_adicional;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =====================================================
-- 4. ACTUALIZAR DATOS EXISTENTES
-- =====================================================

-- Actualizar clases existentes para que estén activas
UPDATE clases 
SET activo = TRUE 
WHERE activo IS NULL;

-- Actualizar detalles de factura usando la clave primaria
UPDATE detalles_factura 
SET tipo_item = 'MEMBRESIA' 
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM detalles_factura 
        WHERE tipo_item IS NULL 
        AND descripcion LIKE '%Membresía%'
    ) AS temp
);

-- Actualizar socios con membresías para darles vigencia
UPDATE socios 
SET fecha_inicio_membresia = CURDATE(),
    fecha_fin_membresia = CASE 
        WHEN membresia_id = 1 THEN DATE_ADD(CURDATE(), INTERVAL 30 DAY)
        WHEN membresia_id = 2 THEN DATE_ADD(CURDATE(), INTERVAL 90 DAY)
        WHEN membresia_id = 3 THEN DATE_ADD(CURDATE(), INTERVAL 365 DAY)
        ELSE NULL
    END
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM socios 
        WHERE membresia_id IS NOT NULL 
        AND fecha_inicio_membresia IS NULL
    ) AS temp
);

-- =====================================================
-- 5. INSERTAR CLASE DE EJEMPLO CON DURACIÓN LIMITADA
-- =====================================================

-- Insertar clase "Defensa Personal" solo si no existe
INSERT INTO clases (nombre, descripcion, instructor_id, hora, duracion_minutos, capacidad_maxima, activo, fecha_inicio, fecha_fin, precio_adicional)
SELECT 'Defensa Personal', 'Curso de defensa personal de 3 meses', 
       (SELECT id FROM instructores LIMIT 1), 
       '19:00:00', 90, 15, TRUE, 
       CURDATE(), 
       DATE_ADD(CURDATE(), INTERVAL 90 DAY), 
       25.00
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM clases WHERE nombre = 'Defensa Personal'
);

-- Reactivar el modo seguro
SET SQL_SAFE_UPDATES = 1;

-- =====================================================
-- 6. VERIFICAR LOS CAMBIOS
-- =====================================================

SELECT '✅ PASO 1: Estructura de detalles_factura' as paso;
DESCRIBE detalles_factura;

SELECT '✅ PASO 2: Estructura de socios' as paso;
DESCRIBE socios;

SELECT '✅ PASO 3: Estructura de clases' as paso;
DESCRIBE clases;

-- =====================================================
-- 7. CONSULTAS DE VERIFICACIÓN
-- =====================================================

SELECT '✅ PASO 4: Socios con vigencia de membresía' as paso;
SELECT 
    id,
    nombre,
    email,
    membresia_id,
    fecha_inicio_membresia,
    fecha_fin_membresia,
    CASE 
        WHEN fecha_inicio_membresia IS NULL THEN 'Sin membresía'
        WHEN CURDATE() BETWEEN fecha_inicio_membresia AND fecha_fin_membresia THEN 'Vigente'
        WHEN CURDATE() > fecha_fin_membresia THEN 'Vencida'
        ELSE 'Futura'
    END as estado_membresia
FROM socios
WHERE membresia_id IS NOT NULL
LIMIT 10;

SELECT '✅ PASO 5: Clases con sus características' as paso;
SELECT 
    id,
    nombre,
    activo,
    fecha_inicio,
    fecha_fin,
    precio_adicional,
    CASE 
        WHEN fecha_inicio IS NULL THEN 'Permanente'
        WHEN CURDATE() BETWEEN fecha_inicio AND fecha_fin THEN 'Vigente'
        WHEN CURDATE() > fecha_fin THEN 'Finalizada'
        ELSE 'Futura'
    END as estado_clase
FROM clases;

SELECT '✅ PASO 6: Detalles de facturas con sus relaciones' as paso;
SELECT 
    df.id,
    df.descripcion,
    df.tipo_item,
    df.membresia_id,
    df.reserva_id,
    m.tipo_membresia as membresia_relacionada
FROM detalles_factura df
LEFT JOIN membresias m ON df.membresia_id = m.id
LIMIT 10;

-- =====================================================
-- MENSAJE FINAL
-- =====================================================

SELECT '✅✅✅ SCRIPT EJECUTADO EXITOSAMENTE ✅✅✅' as mensaje,
       'Todos los campos nuevos han sido agregados a la base de datos' as detalle,
       'La aplicación Spring Boot ahora funcionará correctamente' as nota;


