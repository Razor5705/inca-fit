create database incafit_db;
use incafit_db;

-- Tabla de Membresías (primero por dependencias)
CREATE TABLE IF NOT EXISTS membresias (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    clases_incluidas INT,
    descripcion VARCHAR(255),
    nombre VARCHAR(255),
    precio_base DECIMAL(38,2),
    precio_clase_extra DECIMAL(38,2),
    tipo_cobro ENUM('CLASES_INCLUIDAS', 'CUOTA_FIJA', 'PAGO_POR_CLASE')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Socios
CREATE TABLE IF NOT EXISTS socios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dni VARCHAR(10) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    fecha_registro DATE,
    nombre VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'USUARIO') DEFAULT 'USUARIO',
    activo TINYINT(1) NOT NULL DEFAULT 1,
    membresia_id BIGINT,
    FOREIGN KEY (membresia_id) REFERENCES membresias(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Reservas
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    clase VARCHAR(255),
    estado VARCHAR(255),
    fecha_hora DATETIME(6),
    socio_id BIGINT,
    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Facturas
CREATE TABLE IF NOT EXISTS facturas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    socio_id BIGINT NOT NULL,
    fecha_emision DATE NOT NULL,
    total DECIMAL(38,2),
    estado VARCHAR(255),
    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Detalles de Factura
CREATE TABLE IF NOT EXISTS detalles_factura (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    factura_id BIGINT NOT NULL,
    concepto VARCHAR(255) NOT NULL,
    monto DECIMAL(38,2),
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
) ;


describe socios;
describe detalles_factura;
describe facturas;
describe membresias;
describe reservas;

INSERT INTO socios (nombre, email, password, dni, rol, fecha_registro, activo)
VALUES (
    'Usuario Test',
    'test@example.com',
    '$2a$10$r3k4I5q6w7e8r9t0y1u2vOcQdReSfTgUhViWjXkYlZmAnBoCpDqEs', -- contraseña: password123
    '12345678A',
    'USUARIO',
    CURDATE(),
    true
);

