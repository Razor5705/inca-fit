/* ============================================================
   INCA FIT – ESQUEMA + DATOS DE DEMO
   ============================================================ */
CREATE DATABASE IF NOT EXISTS incafit_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE incafit_db;

/* -------------------
   TABLAS PRINCIPALES
   ------------------- */

CREATE TABLE IF NOT EXISTS membresias (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_membresia  VARCHAR(100) NOT NULL,
    precio          DECIMAL(10,2) NOT NULL,
    duracion_dias   INT NOT NULL,
    descripcion     TEXT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS instructores (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(255) NOT NULL,
    especialidad    VARCHAR(255),
    email           VARCHAR(255) NOT NULL UNIQUE,
    telefono        VARCHAR(30),
    experiencia     VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS clases (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(255) NOT NULL,
    descripcion       TEXT,
    capacidad_maxima  INT NOT NULL DEFAULT 1,
    instructor_id     BIGINT,
    hora              TIME,
    duracion_minutos  INT,
    activo            TINYINT(1) NOT NULL DEFAULT 1,
    fecha_inicio      DATE,
    fecha_fin         DATE,
    precio_adicional  DECIMAL(10,2),
    CONSTRAINT fk_clase_instructor
        FOREIGN KEY (instructor_id) REFERENCES instructores(id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS socios (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni                     VARCHAR(10) NOT NULL UNIQUE,
    nombre                  VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL UNIQUE,
    password                VARCHAR(255) NOT NULL,
    rol                     ENUM('ADMIN','USUARIO') NOT NULL DEFAULT 'USUARIO',
    activo                  TINYINT(1) NOT NULL DEFAULT 1,
    fecha_registro          DATE NOT NULL,
    telefono                VARCHAR(20),
    membresia_id            BIGINT,
    fecha_inicio_membresia  DATE,
    fecha_fin_membresia     DATE,
    CONSTRAINT fk_socio_membresia
        FOREIGN KEY (membresia_id) REFERENCES membresias(id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS asistencias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    socio_id    BIGINT NOT NULL,
    clase_id    BIGINT NOT NULL,
    fecha       DATE NOT NULL,
    presente    TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_asistencia_socio  FOREIGN KEY (socio_id) REFERENCES socios(id)  ON DELETE CASCADE,
    CONSTRAINT fk_asistencia_clase  FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reservas (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    socio_id  BIGINT NOT NULL,
    clase_id  BIGINT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado    VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA',
    CONSTRAINT fk_reserva_socio FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_clase FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS facturas (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    socio_id  BIGINT NOT NULL,
    fecha     DATE NOT NULL,
    total     DECIMAL(10,2) NOT NULL,
    estado    VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_factura_socio FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS detalles_factura (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id     BIGINT NOT NULL,
    descripcion    VARCHAR(500) NOT NULL,
    cantidad       INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal       DECIMAL(10,2) NOT NULL,
    tipo_item      VARCHAR(50),
    membresia_id   BIGINT,
    reserva_id     BIGINT,
    CONSTRAINT fk_detalle_factura   FOREIGN KEY (factura_id)   REFERENCES facturas(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_membresia FOREIGN KEY (membresia_id) REFERENCES membresias(id) ON DELETE SET NULL,
    CONSTRAINT fk_detalle_reserva   FOREIGN KEY (reserva_id)   REFERENCES reservas(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pagos (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id    BIGINT NOT NULL,
    fecha_pago    DATE NOT NULL,
    monto_pagado  DECIMAL(10,2) NOT NULL,
    metodo_pago   VARCHAR(100) NOT NULL,
    CONSTRAINT fk_pago_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

/* -------------------
   DATOS DE REFERENCIA
   ------------------- */

INSERT INTO membresias (id, tipo_membresia, precio, duracion_dias, descripcion) VALUES
    (1, 'Mensual',    45.00,  30, 'Acceso ilimitado 30 días'),
    (2, 'Trimestral', 120.00, 90, 'Acceso trimestral con descuento'),
    (3, 'Anual',      420.00,365, 'Acceso anual completo')
ON DUPLICATE KEY UPDATE
    precio = VALUES(precio),
    duracion_dias = VALUES(duracion_dias),
    descripcion = VALUES(descripcion);

INSERT INTO instructores (id, nombre_completo, especialidad, email, telefono, experiencia) VALUES
    (1, 'Carla Castillo', 'Funcional y HIIT', 'carla.castillo@incafit.com', '600000000', 'Coach certificada'),
    (2, 'Javier Martín',  'Yoga y Pilates',   'javier.martin@incafit.com',  '600000001', 'Instructor senior')
ON DUPLICATE KEY UPDATE
    especialidad = VALUES(especialidad),
    telefono = VALUES(telefono),
    experiencia = VALUES(experiencia);

INSERT INTO clases (id, nombre, descripcion, capacidad_maxima, instructor_id, hora, duracion_minutos, activo, fecha_inicio, fecha_fin, precio_adicional)
VALUES
    (1, 'Funcional Express', 'Sesión funcional de 50 minutos', 20, 1, '18:30:00', 50, 1, NULL, NULL, NULL),
    (2, 'HIIT Nocturno',     'Entrenamiento HIIT avanzado',    18, 1, '20:00:00', 45, 1, NULL, NULL, 8.50),
    (3, 'Defensa Personal',  'Curso intensivo de 3 meses',     15, 2, '19:00:00', 90, 1, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 2 MONTH), 25.00)
ON DUPLICATE KEY UPDATE
    descripcion = VALUES(descripcion),
    capacidad_maxima = VALUES(capacidad_maxima),
    instructor_id = VALUES(instructor_id),
    hora = VALUES(hora),
    duracion_minutos = VALUES(duracion_minutos),
    activo = VALUES(activo),
    fecha_inicio = VALUES(fecha_inicio),
    fecha_fin = VALUES(fecha_fin),
    precio_adicional = VALUES(precio_adicional);

/* ----------- SOCIOS (ADMIN + USUARIOS) ----------- */
INSERT INTO socios (
    id, dni, nombre, email, password, rol, activo,
    fecha_registro, telefono, membresia_id,
    fecha_inicio_membresia, fecha_fin_membresia
) VALUES
    -- Contraseña original: admin123
    (1, '00000001', 'Administrador IncaFit', 'admin@incafit.com',
        '$2a$10$F1dx7V1u2SMxjF5pXJ51GuMjY6h3JH4fiUowq3hhVz3VyoKoG8vNG',
        'ADMIN', 1, CURDATE(), '600111222', 1,
        CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY)),

    -- Contraseña original: TestGym!24
    (2, '00000002', 'Usuario Test', 'test@example.com',
        '$2a$10$gtT3BGJd4Y1RRG.Xcuv6HOpFwLtn86IExBPOQfi4X.gawjV9N3laK',
        'USUARIO', 0, DATE_SUB(CURDATE(), INTERVAL 180 DAY), '600123456', NULL, NULL, NULL),

    -- Contraseña original: nickyreny22
    (3, '00000003', 'Nikolas Adriano Medina Ricra', 'nikkmed805@gmail.com',
        '$2a$10$Q5PwWKpQ9ju7GZLaSGMS2OF5m2LgN1J53wda8opeNIF8Jx3Yse7u2',
        'ADMIN', 1, DATE_SUB(CURDATE(), INTERVAL 150 DAY), '600987654', 1,
        DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY)),

    -- Contraseña original: PruebaFit#24
    (4, '00000004', 'Prueba Prueba Prueba Prueba', 'prueba@yopmail.com',
        '$2a$10$2V.Zkng1bUfy61p2.Oowsu9F91eVCGgZcFQXH7mC4c8j/SRdF1.KK',
        'USUARIO', 1, DATE_SUB(CURDATE(), INTERVAL 120 DAY), '600888777', 1,
        DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 15 DAY)),

    -- Contraseña original: HolaGym#24
    (5, '00000005', 'prueba holaa', 'p@yopmail.com',
        '$2a$10$hyePxBkuqvY6dXy2Hh2HaOwOlYrGZ7rRlR2Y7biwiW7v3S63LaOHG',
        'USUARIO', 1, DATE_SUB(CURDATE(), INTERVAL 90 DAY), '599999999', NULL, NULL, NULL),

    -- Contraseña original: IncaHola#24
    (6, '00000006', 'Hola', 'holainca@yopmail.com',
        '$2a$10$3Z7h72fL9EKC62XsfBKtne5o6ZBFZlZp7jd0QxsuNn.7F5N7EuArK',
        'USUARIO', 1, DATE_SUB(CURDATE(), INTERVAL 60 DAY), '666999330', 2,
        DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 80 DAY))
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    password = VALUES(password),
    rol = VALUES(rol),
    activo = VALUES(activo),
    fecha_registro = VALUES(fecha_registro),
    telefono = VALUES(telefono),
    membresia_id = VALUES(membresia_id),
    fecha_inicio_membresia = VALUES(fecha_inicio_membresia),
    fecha_fin_membresia = VALUES(fecha_fin_membresia);

/* ----------- RESERVAS Y ASISTENCIAS ----------- */
INSERT INTO reservas (socio_id, clase_id, fecha_hora, estado) VALUES
    (3, 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY) + INTERVAL 18 HOUR + INTERVAL 30 MINUTE, 'CONFIRMADA'),
    (4, 2, DATE_ADD(CURDATE(), INTERVAL 3 DAY) + INTERVAL 20 HOUR, 'CONFIRMADA'),
    (5, 2, DATE_SUB(CURDATE(), INTERVAL 2 DAY) + INTERVAL 20 HOUR, 'CANCELADA'),
    (6, 3, DATE_ADD(CURDATE(), INTERVAL 5 DAY) + INTERVAL 19 HOUR, 'PENDIENTE')
ON DUPLICATE KEY UPDATE estado = VALUES(estado);

INSERT INTO asistencias (socio_id, clase_id, fecha, presente) VALUES
    (3, 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1),
    (4, 2, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1),
    (5, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 0)
ON DUPLICATE KEY UPDATE presente = VALUES(presente);

/* ----------- FACTURAS, DETALLES Y PAGOS ----------- */

-- Limpieza opcional de facturas de demo previas
DELETE FROM pagos          WHERE factura_id NOT IN (SELECT id FROM facturas);
DELETE FROM detalles_factura WHERE factura_id NOT IN (SELECT id FROM facturas);
DELETE FROM facturas WHERE id >= 1000;

INSERT INTO facturas (id, socio_id, fecha, total, estado) VALUES
    (1001, 3, DATE_SUB(CURDATE(), INTERVAL 5 MONTH),   39.90, 'PAGADA'),
    (1002, 4, DATE_SUB(CURDATE(), INTERVAL 4 MONTH),   45.00, 'PAGADA'),
    (1003, 5, DATE_SUB(CURDATE(), INTERVAL 3 MONTH),   52.50, 'PAGADA'),
    (1004, 6, DATE_SUB(CURDATE(), INTERVAL 2 MONTH),   58.50, 'PENDIENTE'),
    (1005, 3, DATE_SUB(CURDATE(), INTERVAL 1 MONTH),   68.00, 'PAGADA'),
    (1006, 3, CURDATE(),                               120.00,'PENDIENTE')
ON DUPLICATE KEY UPDATE
    socio_id = VALUES(socio_id),
    fecha = VALUES(fecha),
    total = VALUES(total),
    estado = VALUES(estado);

INSERT INTO detalles_factura (factura_id, descripcion, cantidad, precio_unitario, subtotal, tipo_item, membresia_id, reserva_id) VALUES
    (1001, 'Membresía Mensual',     1, 39.90, 39.90, 'MEMBRESIA', 1, NULL),
    (1002, 'Membresía Mensual',     1, 45.00, 45.00, 'MEMBRESIA', 1, NULL),
    (1003, 'Membresía + Clase HIIT',1, 52.50, 52.50, 'MIXTO',     1, NULL),
    (1004, 'Membresía Mensual',     1, 45.00, 45.00, 'MEMBRESIA', 1, NULL),
    (1004, 'Clase HIIT Nocturno',   1, 13.50, 13.50, 'CLASE',     NULL, 2),
    (1005, 'Bono Clases + Mensual', 1, 68.00, 68.00, 'MIXTO',     1, 1),
    (1006, 'Membresía Anual',       1,120.00,120.00, 'MEMBRESIA', 3, NULL)
ON DUPLICATE KEY UPDATE
    descripcion = VALUES(descripcion),
    cantidad = VALUES(cantidad),
    precio_unitario = VALUES(precio_unitario),
    subtotal = VALUES(subtotal),
    tipo_item = VALUES(tipo_item),
    membresia_id = VALUES(membresia_id),
    reserva_id = VALUES(reserva_id);

INSERT INTO pagos (factura_id, fecha_pago, monto_pagado, metodo_pago) VALUES
    (1001, DATE_SUB(CURDATE(), INTERVAL 5 MONTH), 39.90, 'TARJETA'),
    (1002, DATE_SUB(CURDATE(), INTERVAL 4 MONTH), 45.00, 'TRANSFERENCIA'),
    (1003, DATE_SUB(CURDATE(), INTERVAL 3 MONTH), 52.50, 'TARJETA'),
    (1005, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 68.00, 'BIZUM')
ON DUPLICATE KEY UPDATE
    fecha_pago   = VALUES(fecha_pago),
    monto_pagado = VALUES(monto_pagado),
    metodo_pago  = VALUES(metodo_pago);


/* Admin demo – contraseña: AdminDemo#24 */
INSERT INTO socios (
    dni, nombre, email, password, rol, activo,
    fecha_registro, telefono, membresia_id,
    fecha_inicio_membresia, fecha_fin_membresia
)
VALUES (
    '00000007',
    'Admin Demo',
    'admindemo@incafit.com',
    '$2a$12$1kuAJ7Ju7cl11mx6SaI9GOVbEw7ZcW6GlVRlJTMsKQdLWBnwD66wu', -- hash de AdminDemo#24
    'ADMIN',
    1,
    CURDATE(),
    '600777888',
    NULL,
    NULL,
    NULL
);

-- Deja al admin demo con la contraseña: admin123
UPDATE socios
SET password = '$2a$10$OGLqZw8GBl/yw5vmb3r7EOB6MRgVuLewqgi4pYh8v7pLmeU20acg.'
WHERE email = 'admindemo@incafit.com'
;

UPDATE socios
SET password = '$2a$10$M7AlYmPn1QIPDultONJ2XOq8c6PfIL1eM/jDOxKMK7gf/CIEvdOGO',
    rol = 'ADMIN',
    activo = 1
WHERE email = 'admindemo@incafit.com';

select * from socios;