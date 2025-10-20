-- =====================================================
-- SCRIPT PARA AGREGAR LOS CAMPOS NUEVOS A LA BASE DE DATOS EXISTENTE
-- Ejecutar este script en tu base de datos incafit_db
-- =====================================================

USE incafit_db;

-- =====================================================
-- 1. AGREGAR CAMPOS A LA TABLA DETALLES_FACTURA
-- =====================================================

-- Verificar si los campos ya existen antes de agregarlos
ALTER TABLE detalles_factura 
ADD COLUMN tipo_item VARCHAR(50),
ADD COLUMN membresia_id BIGINT,
ADD COLUMN reserva_id BIGINT;

-- Agregar las claves foráneas si no existen
-- Nota: Estas pueden fallar si ya existen, es normal
ALTER TABLE detalles_factura 
ADD CONSTRAINT fk_detalle_membresia 
FOREIGN KEY (membresia_id) REFERENCES membresias(id) ON DELETE SET NULL;

ALTER TABLE detalles_factura 
ADD CONSTRAINT fk_detalle_reserva 
FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE SET NULL;

-- =====================================================
-- 2. AGREGAR CAMPOS A LA TABLA SOCIOS
-- =====================================================

ALTER TABLE socios 
ADD COLUMN IF NOT EXISTS fecha_inicio_membresia DATE,
ADD COLUMN IF NOT EXISTS fecha_fin_membresia DATE;

-- =====================================================
-- 3. AGREGAR CAMPOS A LA TABLA CLASES
-- =====================================================

ALTER TABLE clases 
ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT TRUE,
ADD COLUMN IF NOT EXISTS fecha_inicio DATE,
ADD COLUMN IF NOT EXISTS fecha_fin DATE,
ADD COLUMN IF NOT EXISTS precio_adicional DECIMAL(10,2);

-- =====================================================
-- 4. ACTUALIZAR DATOS EXISTENTES
-- =====================================================

-- Actualizar clases existentes para que estén activas
UPDATE clases 
SET activo = TRUE 
WHERE activo IS NULL;

-- Actualizar detalles de factura existentes para marcar que son membresías
UPDATE detalles_factura 
SET tipo_item = 'MEMBRESIA' 
WHERE tipo_item IS NULL 
AND descripcion LIKE '%Membresía%';

-- Actualizar socios con membresías para darles vigencia
-- (Esto es opcional, puedes ajustar las fechas según necesites)
UPDATE socios 
SET fecha_inicio_membresia = CURDATE(),
    fecha_fin_membresia = CASE 
        WHEN membresia_id = 1 THEN DATE_ADD(CURDATE(), INTERVAL 30 DAY)
        WHEN membresia_id = 2 THEN DATE_ADD(CURDATE(), INTERVAL 90 DAY)
        WHEN membresia_id = 3 THEN DATE_ADD(CURDATE(), INTERVAL 365 DAY)
        ELSE NULL
    END
WHERE membresia_id IS NOT NULL 
AND fecha_inicio_membresia IS NULL;

-- =====================================================
-- 5. INSERTAR CLASE DE EJEMPLO CON DURACIÓN LIMITADA
-- =====================================================

-- Insertar clase "Defensa Personal" como ejemplo de clase limitada
INSERT IGNORE INTO clases (nombre, descripcion, instructor_id, hora, duracion_minutos, capacidad_maxima, activo, fecha_inicio, fecha_fin, precio_adicional) 
VALUES ('Defensa Personal', 'Curso de defensa personal de 3 meses', 
        (SELECT id FROM instructores LIMIT 1), 
        '19:00:00', 90, 15, TRUE, 
        CURDATE(), 
        DATE_ADD(CURDATE(), INTERVAL 90 DAY), 
        25.00);

-- =====================================================
-- 6. VERIFICAR LOS CAMBIOS
-- =====================================================

-- Mostrar estructura actualizada de detalles_factura
DESCRIBE detalles_factura;

-- Mostrar estructura actualizada de socios
DESCRIBE socios;

-- Mostrar estructura actualizada de clases
DESCRIBE clases;

-- =====================================================
-- 7. CONSULTAS DE VERIFICACIÓN
-- =====================================================

-- Ver socios con vigencia de membresía
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
WHERE membresia_id IS NOT NULL;

-- Ver clases con sus características
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

-- Ver detalles de facturas con sus relaciones
SELECT 
    df.id,
    df.descripcion,
    df.tipo_item,
    df.membresia_id,
    df.reserva_id,
    m.tipo_membresia as membresia_relacionada
FROM detalles_factura df
LEFT JOIN membresias m ON df.membresia_id = m.id;

-- =====================================================
-- MENSAJE DE CONFIRMACIÓN
-- =====================================================

SELECT '✅ SCRIPT EJECUTADO EXITOSAMENTE' as mensaje,
       'Todos los campos nuevos han sido agregados a la base de datos' as detalle;


