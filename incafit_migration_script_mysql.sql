-- =====================================================
-- SCRIPT DE MIGRACIÓN PARA INCA FIT - ACTUALIZACIÓN DE TABLAS
-- Trabajo de Fin de Grado (TFG) - Campus FP
-- Compatible con MySQL 8.0+
-- =====================================================

USE incafit_db;

-- =====================================================
-- ACTUALIZACIÓN DE TABLAS EXISTENTES
-- =====================================================

-- 1. ACTUALIZAR TABLA SOCIOS
-- Verificar y agregar campos faltantes
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'socios' 
     AND COLUMN_NAME = 'telefono') = 0,
    'ALTER TABLE socios ADD COLUMN telefono VARCHAR(20) AFTER email',
    'SELECT "Campo telefono ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'socios' 
     AND COLUMN_NAME = 'fecha_registro') = 0,
    'ALTER TABLE socios ADD COLUMN fecha_registro DATE AFTER activo',
    'SELECT "Campo fecha_registro ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Modificar campos existentes
ALTER TABLE socios 
MODIFY COLUMN dni VARCHAR(10) NOT NULL,
MODIFY COLUMN nombre VARCHAR(255) NOT NULL,
MODIFY COLUMN email VARCHAR(255) NOT NULL,
MODIFY COLUMN password VARCHAR(255) NOT NULL,
MODIFY COLUMN rol ENUM('ADMIN', 'USUARIO') NOT NULL DEFAULT 'USUARIO',
MODIFY COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;

-- 2. ACTUALIZAR TABLA MEMBRESIAS
-- Agregar campos faltantes
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'membresias' 
     AND COLUMN_NAME = 'tipo_membresia') = 0,
    'ALTER TABLE membresias ADD COLUMN tipo_membresia VARCHAR(255) NOT NULL AFTER id',
    'SELECT "Campo tipo_membresia ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'membresias' 
     AND COLUMN_NAME = 'precio') = 0,
    'ALTER TABLE membresias ADD COLUMN precio DECIMAL(10,2) NOT NULL AFTER tipo_membresia',
    'SELECT "Campo precio ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'membresias' 
     AND COLUMN_NAME = 'duracion_dias') = 0,
    'ALTER TABLE membresias ADD COLUMN duracion_dias INT NOT NULL AFTER precio',
    'SELECT "Campo duracion_dias ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'membresias' 
     AND COLUMN_NAME = 'descripcion') = 0,
    'ALTER TABLE membresias ADD COLUMN descripcion TEXT AFTER duracion_dias',
    'SELECT "Campo descripcion ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. ACTUALIZAR TABLA CLASES
-- Agregar campos faltantes
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'clases' 
     AND COLUMN_NAME = 'hora') = 0,
    'ALTER TABLE clases ADD COLUMN hora TIME AFTER instructor_id',
    'SELECT "Campo hora ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'clases' 
     AND COLUMN_NAME = 'duracion_minutos') = 0,
    'ALTER TABLE clases ADD COLUMN duracion_minutos INT AFTER hora',
    'SELECT "Campo duracion_minutos ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Modificar campos existentes
ALTER TABLE clases 
MODIFY COLUMN descripcion TEXT,
MODIFY COLUMN capacidad_maxima INT NOT NULL DEFAULT 1;

-- 4. ACTUALIZAR TABLA FACTURAS
-- Cambiar nombre de campo fecha_emision a fecha
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'facturas' 
     AND COLUMN_NAME = 'fecha_emision') > 0,
    'ALTER TABLE facturas CHANGE fecha_emision fecha DATE NOT NULL',
    'SELECT "Campo fecha_emision no existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 5. ACTUALIZAR TABLA DETALLES_FACTURA
-- Agregar campos faltantes
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'detalles_factura' 
     AND COLUMN_NAME = 'cantidad') = 0,
    'ALTER TABLE detalles_factura ADD COLUMN cantidad INT NOT NULL DEFAULT 1 AFTER concepto',
    'SELECT "Campo cantidad ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'detalles_factura' 
     AND COLUMN_NAME = 'precio_unitario') = 0,
    'ALTER TABLE detalles_factura ADD COLUMN precio_unitario DECIMAL(10,2) NOT NULL AFTER cantidad',
    'SELECT "Campo precio_unitario ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'detalles_factura' 
     AND COLUMN_NAME = 'subtotal') = 0,
    'ALTER TABLE detalles_factura ADD COLUMN subtotal DECIMAL(10,2) NOT NULL AFTER precio_unitario',
    'SELECT "Campo subtotal ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Cambiar nombre de campos existentes
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'detalles_factura' 
     AND COLUMN_NAME = 'concepto') > 0,
    'ALTER TABLE detalles_factura CHANGE concepto descripcion VARCHAR(500) NOT NULL',
    'SELECT "Campo concepto no existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 6. ACTUALIZAR TABLA ASISTENCIAS
-- Eliminar tabla actual y recrearla con nueva estructura
DROP TABLE IF EXISTS asistencias;

CREATE TABLE asistencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    socio_id BIGINT NOT NULL,
    clase_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE
);

-- 7. ACTUALIZAR TABLA INSTRUCTORES
-- Asegurar que email sea NOT NULL
ALTER TABLE instructores 
MODIFY COLUMN email VARCHAR(255) NOT NULL;

-- =====================================================
-- ACTUALIZAR DATOS EXISTENTES
-- =====================================================

-- Actualizar fechas de registro para usuarios existentes
UPDATE socios 
SET fecha_registro = CURDATE() 
WHERE fecha_registro IS NULL;

-- Actualizar membresías para que tengan los campos requeridos
UPDATE membresias 
SET tipo_membresia = COALESCE(nombre, 'Mensual'),
    precio = COALESCE(precio_base, 50.00),
    duracion_dias = COALESCE(duracion_dias, 30),
    descripcion = COALESCE(descripcion, 'Membresía estándar')
WHERE tipo_membresia IS NULL OR precio IS NULL;

-- =====================================================
-- INSERTAR DATOS FALTANTES
-- =====================================================

-- Insertar instructores si no existen
INSERT IGNORE INTO instructores (nombre_completo, especialidad, email) VALUES
('Carlos Gomez', 'Yoga y Pilates', 'carlos.gomez@incafit.com'),
('Ana Martinez', 'Spinning y HIIT', 'ana.martinez@incafit.com'),
('Miguel Rodriguez', 'Musculación y Crossfit', 'miguel.rodriguez@incafit.com'),
('Laura Sanchez', 'Zumba y Aeróbicos', 'laura.sanchez@incafit.com');

-- Insertar clases si no existen
INSERT IGNORE INTO clases (nombre, descripcion, instructor_id, hora, duracion_minutos, capacidad_maxima) VALUES
('Yoga', 'Clase de relajación y flexibilidad', 1, '08:00:00', 60, 20),
('Spinning', 'Clase de ciclismo intenso', 2, '18:00:00', 45, 15),
('Pilates', 'Fortalecimiento del core y flexibilidad', 1, '10:00:00', 50, 12),
('HIIT', 'Entrenamiento de alta intensidad', 2, '19:30:00', 30, 10),
('Musculación', 'Entrenamiento con pesas', 3, '07:00:00', 90, 8),
('Zumba', 'Baile y cardio', 4, '20:00:00', 60, 25);

-- Insertar membresías si no existen
INSERT IGNORE INTO membresias (tipo_membresia, descripcion, precio, duracion_dias) VALUES
('Mensual', 'Acceso por 30 días', 50.00, 30),
('Trimestral', 'Acceso por 90 días', 135.00, 90),
('Anual', 'Acceso por 365 días', 500.00, 365);

-- =====================================================
-- ACTUALIZAR USUARIOS EXISTENTES
-- =====================================================

-- Actualizar usuarios existentes con datos completos
UPDATE socios 
SET telefono = '600123456',
    membresia_id = 1,
    rol = 'USUARIO',
    activo = TRUE
WHERE email = 'test@example.com';

UPDATE socios 
SET telefono = '600987654',
    membresia_id = 2,
    rol = 'USUARIO',
    activo = TRUE
WHERE email = 'nikkmed805@gmail.com';

-- =====================================================
-- INSERTAR DATOS DE PRUEBA ADICIONALES
-- =====================================================

-- Insertar más usuarios de prueba
INSERT IGNORE INTO socios (dni, nombre, email, password, rol, activo, fecha_registro, telefono, membresia_id) VALUES
('11223344', 'Maria Garcia', 'maria.garcia@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'USUARIO', TRUE, CURDATE(), '600555666', 2),
('55667788', 'Pedro Lopez', 'pedro.lopez@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'USUARIO', TRUE, CURDATE(), '600777888', 3);

-- Insertar reservas de ejemplo
INSERT IGNORE INTO reservas (socio_id, clase_id, fecha_hora, estado) VALUES
(1, 1, '2024-10-15 08:00:00', 'CONFIRMADA'),
(1, 2, '2024-10-16 18:00:00', 'CONFIRMADA'),
(2, 1, '2024-10-15 08:00:00', 'CONFIRMADA'),
(2, 3, '2024-10-17 10:00:00', 'CONFIRMADA'),
(3, 2, '2024-10-16 18:00:00', 'CANCELADA'),
(3, 4, '2024-10-18 19:30:00', 'CONFIRMADA');

-- Insertar facturas de ejemplo
INSERT IGNORE INTO facturas (socio_id, fecha, total, estado) VALUES
(1, '2024-10-01', 50.00, 'PAGADA'),
(2, '2024-10-01', 135.00, 'PAGADA'),
(3, '2024-10-01', 500.00, 'PENDIENTE'),
(1, '2024-11-01', 50.00, 'PENDIENTE');

-- Insertar detalles de factura
INSERT IGNORE INTO detalles_factura (factura_id, descripcion, cantidad, precio_unitario, subtotal) VALUES
(1, 'Membresía: Mensual', 1, 50.00, 50.00),
(2, 'Membresía: Trimestral', 1, 135.00, 135.00),
(3, 'Membresía: Anual', 1, 500.00, 500.00),
(4, 'Membresía: Mensual', 1, 50.00, 50.00);

-- Insertar pagos de ejemplo
INSERT IGNORE INTO pagos (factura_id, fecha_pago, monto_pagado, metodo_pago) VALUES
(1, '2024-10-01', 50.00, 'TARJETA_CREDITO'),
(2, '2024-10-01', 135.00, 'TRANSFERENCIA_BANCARIA');

-- Insertar asistencias de ejemplo
INSERT IGNORE INTO asistencias (socio_id, clase_id, fecha) VALUES
(1, 1, '2024-10-15'),
(1, 2, '2024-10-16'),
(2, 1, '2024-10-15'),
(2, 3, '2024-10-17'),
(3, 4, '2024-10-18');

-- =====================================================
-- CREAR ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

-- Crear índices solo si no existen
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'socios' 
     AND INDEX_NAME = 'idx_socios_email') = 0,
    'CREATE INDEX idx_socios_email ON socios(email)',
    'SELECT "Índice idx_socios_email ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'socios' 
     AND INDEX_NAME = 'idx_socios_dni') = 0,
    'CREATE INDEX idx_socios_dni ON socios(dni)',
    'SELECT "Índice idx_socios_dni ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'reservas' 
     AND INDEX_NAME = 'idx_reservas_fecha') = 0,
    'CREATE INDEX idx_reservas_fecha ON reservas(fecha_hora)',
    'SELECT "Índice idx_reservas_fecha ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'reservas' 
     AND INDEX_NAME = 'idx_reservas_socio') = 0,
    'CREATE INDEX idx_reservas_socio ON reservas(socio_id)',
    'SELECT "Índice idx_reservas_socio ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'reservas' 
     AND INDEX_NAME = 'idx_reservas_clase') = 0,
    'CREATE INDEX idx_reservas_clase ON reservas(clase_id)',
    'SELECT "Índice idx_reservas_clase ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'facturas' 
     AND INDEX_NAME = 'idx_facturas_socio') = 0,
    'CREATE INDEX idx_facturas_socio ON facturas(socio_id)',
    'SELECT "Índice idx_facturas_socio ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'facturas' 
     AND INDEX_NAME = 'idx_facturas_fecha') = 0,
    'CREATE INDEX idx_facturas_fecha ON facturas(fecha)',
    'SELECT "Índice idx_facturas_fecha ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'asistencias' 
     AND INDEX_NAME = 'idx_asistencias_fecha') = 0,
    'CREATE INDEX idx_asistencias_fecha ON asistencias(fecha)',
    'SELECT "Índice idx_asistencias_fecha ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = 'incafit_db' 
     AND TABLE_NAME = 'asistencias' 
     AND INDEX_NAME = 'idx_asistencias_socio') = 0,
    'CREATE INDEX idx_asistencias_socio ON asistencias(socio_id)',
    'SELECT "Índice idx_asistencias_socio ya existe" as mensaje'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- =====================================================
-- VERIFICACIÓN FINAL
-- =====================================================

-- Verificar estructura de tablas actualizada
SELECT 'ESTRUCTURA DE TABLAS ACTUALIZADA' as mensaje;
DESCRIBE socios;
DESCRIBE membresias;
DESCRIBE clases;
DESCRIBE facturas;
DESCRIBE detalles_factura;
DESCRIBE asistencias;

-- Verificar datos insertados
SELECT 'DATOS INSERTADOS' as mensaje;
SELECT 'SOCIOS' as tabla, COUNT(*) as registros FROM socios
UNION ALL
SELECT 'MEMBRESÍAS', COUNT(*) FROM membresias
UNION ALL
SELECT 'INSTRUCTORES', COUNT(*) FROM instructores
UNION ALL
SELECT 'CLASES', COUNT(*) FROM clases
UNION ALL
SELECT 'RESERVAS', COUNT(*) FROM reservas
UNION ALL
SELECT 'FACTURAS', COUNT(*) FROM facturas
UNION ALL
SELECT 'DETALLES_FACTURA', COUNT(*) FROM detalles_factura
UNION ALL
SELECT 'PAGOS', COUNT(*) FROM pagos
UNION ALL
SELECT 'ASISTENCIAS', COUNT(*) FROM asistencias;

-- =====================================================
-- CONSULTAS DE VERIFICACIÓN PARA EL TUTOR
-- =====================================================

-- 1. Verificar usuarios existentes con sus datos completos
SELECT 'USUARIOS CON DATOS COMPLETOS' as titulo;
SELECT 
    s.id,
    s.nombre,
    s.email,
    s.telefono,
    s.rol,
    s.activo,
    s.fecha_registro,
    m.tipo_membresia,
    m.precio
FROM socios s
LEFT JOIN membresias m ON s.membresia_id = m.id
ORDER BY s.id;

-- 2. Verificar estructura de clases
SELECT 'CLASES CON INSTRUCTORES' as titulo;
SELECT 
    c.id,
    c.nombre,
    c.descripcion,
    c.hora,
    c.duracion_minutos,
    c.capacidad_maxima,
    i.nombre_completo as instructor
FROM clases c
LEFT JOIN instructores i ON c.instructor_id = i.id
ORDER BY c.hora;

-- 3. Verificar facturas y detalles
SELECT 'FACTURAS CON DETALLES' as titulo;
SELECT 
    f.id,
    s.nombre as socio,
    f.fecha,
    f.total,
    f.estado,
    df.descripcion,
    df.cantidad,
    df.precio_unitario,
    df.subtotal
FROM facturas f
JOIN socios s ON f.socio_id = s.id
LEFT JOIN detalles_factura df ON f.id = df.factura_id
ORDER BY f.fecha DESC;

SELECT 'MIGRACIÓN COMPLETADA EXITOSAMENTE' as resultado;
