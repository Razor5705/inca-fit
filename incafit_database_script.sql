-- =====================================================
-- SCRIPT SQL PARA INCA FIT - SISTEMA DE GESTIÓN DE GIMNASIO
-- Trabajo de Fin de Grado (TFG) - Campus FP
-- =====================================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS incafit_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE incafit_db;

-- =====================================================
-- CREACIÓN DE TABLAS
-- =====================================================

-- Tabla: MEMBRESIAS
-- Almacena los diferentes tipos de membresías disponibles
CREATE TABLE membresias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_membresia VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    duracion_dias INT NOT NULL,
    descripcion TEXT
);

-- Tabla: INSTRUCTORES
-- Almacena información de los instructores del gimnasio
CREATE TABLE instructores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(255) NOT NULL,
    especialidad VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Tabla: CLASES
-- Almacena información de las clases disponibles
CREATE TABLE clases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    capacidad_maxima INT NOT NULL DEFAULT 1,
    instructor_id BIGINT,
    hora TIME,
    duracion_minutos INT,
    FOREIGN KEY (instructor_id) REFERENCES instructores(id) ON DELETE SET NULL
);

-- Tabla: SOCIOS
-- Almacena información de los socios/miembros del gimnasio
CREATE TABLE socios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'USUARIO') NOT NULL DEFAULT 'USUARIO',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATE NOT NULL,
    telefono VARCHAR(20),
    membresia_id BIGINT,
    FOREIGN KEY (membresia_id) REFERENCES membresias(id) ON DELETE SET NULL
);

-- Tabla: RESERVAS
-- Almacena las reservas de clases por parte de los socios
CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    socio_id BIGINT NOT NULL,
    clase_id BIGINT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA',
    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE
);

-- Tabla: FACTURAS
-- Almacena las facturas generadas para los socios
CREATE TABLE facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    socio_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE
);

-- Tabla: DETALLES_FACTURA
-- Almacena los detalles de cada factura
CREATE TABLE detalles_factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
);

-- Tabla: PAGOS
-- Almacena información de los pagos realizados
CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    fecha_pago DATE NOT NULL,
    monto_pagado DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(100) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
);

-- Tabla: ASISTENCIAS
-- Almacena el registro de asistencia de socios a clases
CREATE TABLE asistencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    socio_id BIGINT NOT NULL,
    clase_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE
);

-- =====================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

-- Índices para mejorar el rendimiento de consultas frecuentes
CREATE INDEX idx_socios_email ON socios(email);
CREATE INDEX idx_socios_dni ON socios(dni);
CREATE INDEX idx_reservas_fecha ON reservas(fecha_hora);
CREATE INDEX idx_reservas_socio ON reservas(socio_id);
CREATE INDEX idx_reservas_clase ON reservas(clase_id);
CREATE INDEX idx_facturas_socio ON facturas(socio_id);
CREATE INDEX idx_facturas_fecha ON facturas(fecha);
CREATE INDEX idx_asistencias_fecha ON asistencias(fecha);
CREATE INDEX idx_asistencias_socio ON asistencias(socio_id);

-- =====================================================
-- INSERCIÓN DE DATOS DE PRUEBA
-- =====================================================

-- Insertar MEMBRESÍAS
INSERT INTO membresias (tipo_membresia, descripcion, precio, duracion_dias) VALUES
('Mensual', 'Acceso por 30 días', 50.00, 30),
('Trimestral', 'Acceso por 90 días', 135.00, 90),
('Anual', 'Acceso por 365 días', 500.00, 365);

-- Insertar INSTRUCTORES
INSERT INTO instructores (nombre_completo, especialidad, email) VALUES
('Carlos Gomez', 'Yoga y Pilates', 'carlos.gomez@incafit.com'),
('Ana Martinez', 'Spinning y HIIT', 'ana.martinez@incafit.com'),
('Miguel Rodriguez', 'Musculación y Crossfit', 'miguel.rodriguez@incafit.com'),
('Laura Sanchez', 'Zumba y Aeróbicos', 'laura.sanchez@incafit.com');

-- Insertar CLASES
INSERT INTO clases (nombre, descripcion, instructor_id, hora, duracion_minutos, capacidad_maxima) VALUES
('Yoga', 'Clase de relajación y flexibilidad', 1, '08:00:00', 60, 20),
('Spinning', 'Clase de ciclismo intenso', 2, '18:00:00', 45, 15),
('Pilates', 'Fortalecimiento del core y flexibilidad', 1, '10:00:00', 50, 12),
('HIIT', 'Entrenamiento de alta intensidad', 2, '19:30:00', 30, 10),
('Musculación', 'Entrenamiento con pesas', 3, '07:00:00', 90, 8),
('Zumba', 'Baile y cardio', 4, '20:00:00', 60, 25);

-- Insertar SOCIOS (Admin y usuarios de prueba)
-- Nota: Las contraseñas están hasheadas con BCrypt
-- admin123 -> $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi
-- user123 -> $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW
INSERT INTO socios (dni, nombre, email, password, rol, activo, fecha_registro, telefono, membresia_id) VALUES
('12345678', 'Admin', 'admin@incafit.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', TRUE, CURDATE(), '600123456', NULL),
('87654321', 'Juan Perez', 'juan.perez@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'USUARIO', TRUE, CURDATE(), '600987654', 1),
('11223344', 'Maria Garcia', 'maria.garcia@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'USUARIO', TRUE, CURDATE(), '600555666', 2),
('55667788', 'Pedro Lopez', 'pedro.lopez@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'USUARIO', TRUE, CURDATE(), '600777888', 3),
('99887766', 'Ana Torres', 'ana.torres@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'USUARIO', FALSE, CURDATE(), '600999000', 1);

-- Insertar RESERVAS de ejemplo
INSERT INTO reservas (socio_id, clase_id, fecha_hora, estado) VALUES
(2, 1, '2024-10-15 08:00:00', 'CONFIRMADA'),
(2, 2, '2024-10-16 18:00:00', 'CONFIRMADA'),
(3, 1, '2024-10-15 08:00:00', 'CONFIRMADA'),
(3, 3, '2024-10-17 10:00:00', 'CONFIRMADA'),
(4, 2, '2024-10-16 18:00:00', 'CANCELADA'),
(4, 4, '2024-10-18 19:30:00', 'CONFIRMADA');

-- Insertar FACTURAS de ejemplo
INSERT INTO facturas (socio_id, fecha, total, estado) VALUES
(2, '2024-10-01', 50.00, 'PAGADA'),
(3, '2024-10-01', 135.00, 'PAGADA'),
(4, '2024-10-01', 500.00, 'PENDIENTE'),
(2, '2024-11-01', 50.00, 'PENDIENTE');

-- Insertar DETALLES_FACTURA
INSERT INTO detalles_factura (factura_id, descripcion, cantidad, precio_unitario, subtotal) VALUES
(1, 'Membresía: Mensual', 1, 50.00, 50.00),
(2, 'Membresía: Trimestral', 1, 135.00, 135.00),
(3, 'Membresía: Anual', 1, 500.00, 500.00),
(4, 'Membresía: Mensual', 1, 50.00, 50.00);

-- Insertar PAGOS de ejemplo
INSERT INTO pagos (factura_id, fecha_pago, monto_pagado, metodo_pago) VALUES
(1, '2024-10-01', 50.00, 'TARJETA_CREDITO'),
(2, '2024-10-01', 135.00, 'TRANSFERENCIA_BANCARIA');

-- Insertar ASISTENCIAS de ejemplo
INSERT INTO asistencias (socio_id, clase_id, fecha) VALUES
(2, 1, '2024-10-15'),
(2, 2, '2024-10-16'),
(3, 1, '2024-10-15'),
(3, 3, '2024-10-17'),
(4, 4, '2024-10-18');

-- =====================================================
-- CONSULTAS DE VERIFICACIÓN
-- =====================================================

-- Verificar que los datos se insertaron correctamente
SELECT 'MEMBRESÍAS' as tabla, COUNT(*) as registros FROM membresias
UNION ALL
SELECT 'INSTRUCTORES', COUNT(*) FROM instructores
UNION ALL
SELECT 'CLASES', COUNT(*) FROM clases
UNION ALL
SELECT 'SOCIOS', COUNT(*) FROM socios
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
-- CONSULTAS DE EJEMPLO PARA EL TUTOR
-- =====================================================

-- 1. Socios activos con sus membresías
SELECT 
    s.nombre,
    s.email,
    s.telefono,
    m.tipo_membresia,
    m.precio,
    s.fecha_registro
FROM socios s
LEFT JOIN membresias m ON s.membresia_id = m.id
WHERE s.activo = TRUE;

-- 2. Clases con sus instructores
SELECT 
    c.nombre as clase,
    c.descripcion,
    c.hora,
    c.duracion_minutos,
    c.capacidad_maxima,
    i.nombre_completo as instructor,
    i.especialidad
FROM clases c
LEFT JOIN instructores i ON c.instructor_id = i.id
ORDER BY c.hora;

-- 3. Reservas confirmadas para hoy
SELECT 
    s.nombre as socio,
    c.nombre as clase,
    r.fecha_hora,
    r.estado
FROM reservas r
JOIN socios s ON r.socio_id = s.id
JOIN clases c ON r.clase_id = c.id
WHERE DATE(r.fecha_hora) = CURDATE()
AND r.estado = 'CONFIRMADA';

-- 4. Facturas pendientes de pago
SELECT 
    s.nombre as socio,
    f.fecha,
    f.total,
    f.estado,
    df.descripcion
FROM facturas f
JOIN socios s ON f.socio_id = s.id
LEFT JOIN detalles_factura df ON f.id = df.factura_id
WHERE f.estado = 'PENDIENTE';

-- 5. Asistencias por clase
SELECT 
    c.nombre as clase,
    COUNT(a.id) as total_asistencias,
    c.capacidad_maxima,
    ROUND((COUNT(a.id) / c.capacidad_maxima) * 100, 2) as porcentaje_ocupacion
FROM clases c
LEFT JOIN asistencias a ON c.id = a.clase_id
GROUP BY c.id, c.nombre, c.capacidad_maxima;

-- =====================================================
-- NOTAS IMPORTANTES PARA EL TUTOR
-- =====================================================

/*
ESTRUCTURA DE LA BASE DE DATOS:

1. MEMBRESÍAS: Define los tipos de membresías disponibles
2. INSTRUCTORES: Información de los entrenadores
3. CLASES: Clases disponibles con horarios y capacidad
4. SOCIOS: Usuarios del sistema (admin y usuarios normales)
5. RESERVAS: Reservas de clases por parte de los socios
6. FACTURAS: Facturación de membresías y servicios
7. DETALLES_FACTURA: Detalles de cada factura
8. PAGOS: Registro de pagos realizados
9. ASISTENCIAS: Control de asistencia a clases

RELACIONES:
- Un socio puede tener una membresía (ManyToOne)
- Un socio puede hacer múltiples reservas (OneToMany)
- Una clase pertenece a un instructor (ManyToOne)
- Una factura puede tener múltiples detalles (OneToMany)
- Un pago pertenece a una factura (ManyToOne)
- Una asistencia registra socio + clase + fecha (ManyToOne)

DATOS DE PRUEBA:
- 1 administrador (admin@incafit.com / admin123)
- 4 usuarios de prueba (user123 para todos)
- 3 tipos de membresías
- 4 instructores
- 6 clases diferentes
- Reservas, facturas y asistencias de ejemplo

CONTRASEÑAS:
Las contraseñas están hasheadas con BCrypt:
- admin123: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi
- user123: $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW

CONFIGURACIÓN DE SPRING BOOT:
- Base de datos: incafit_db
- Usuario: root
- Contraseña: admin1A (según application.properties)
- Puerto: 3306
- DDL: update (crea/actualiza tablas automáticamente)
*/
