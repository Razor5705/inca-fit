/* ============================================================
   INCA FIT – ESQUEMA + DATOS DE DEMO (export 2025-11-13)
   Basado en los dumps de la carpeta BBDD/
   ============================================================ */

DROP DATABASE IF EXISTS incafit_db;
CREATE DATABASE incafit_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE incafit_db;

/* -------------------
   TABLAS PRINCIPALES
   ------------------- */

CREATE TABLE membresias (
  id BIGINT NOT NULL AUTO_INCREMENT,
  clases_incluidas INT DEFAULT NULL,
  descripcion VARCHAR(1000) DEFAULT NULL,
  nombre VARCHAR(255) DEFAULT NULL,
  precio_base DECIMAL(38,2) DEFAULT NULL,
  precio_clase_extra DECIMAL(38,2) DEFAULT NULL,
  tipo_cobro ENUM('CLASES_INCLUIDAS','CUOTA_FIJA','PAGO_POR_CLASE') DEFAULT NULL,
  duracion_dias INT NOT NULL,
  precio DECIMAL(38,2) NOT NULL,
  tipo_membresia VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE instructores (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre_completo VARCHAR(255) NOT NULL,
  especialidad VARCHAR(255) DEFAULT NULL,
  email VARCHAR(255) NOT NULL,
  experiencia VARCHAR(255) DEFAULT NULL,
  telefono VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE clases (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  descripcion VARCHAR(255) DEFAULT NULL,
  capacidad_maxima INT NOT NULL DEFAULT 1,
  instructor_id BIGINT DEFAULT NULL,
  hora TIME DEFAULT NULL,
  duracion_minutos INT DEFAULT NULL,
  dias_semana VARCHAR(255) DEFAULT NULL,
  activo BIT(1) NOT NULL,
  fecha_inicio DATE DEFAULT NULL,
  fecha_fin DATE DEFAULT NULL,
  precio_adicional DECIMAL(38,2) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY instructor_id (instructor_id),
  CONSTRAINT clases_ibfk_1 FOREIGN KEY (instructor_id) REFERENCES instructores (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE socios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  dni VARCHAR(10) NOT NULL,
  email VARCHAR(255) NOT NULL,
  telefono VARCHAR(255) DEFAULT NULL,
  fecha_registro DATE DEFAULT NULL,
  nombre VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  rol ENUM('ADMIN','USUARIO') NOT NULL DEFAULT 'USUARIO',
  activo TINYINT(1) NOT NULL DEFAULT 1,
  membresia_id BIGINT DEFAULT NULL,
  fecha_inicio_membresia DATE DEFAULT NULL,
  fecha_fin_membresia DATE DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY dni (dni),
  UNIQUE KEY email (email),
  KEY FK_socio_membresia (membresia_id),
  CONSTRAINT FK_socio_membresia FOREIGN KEY (membresia_id) REFERENCES membresias (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE reservas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  estado VARCHAR(255) DEFAULT NULL,
  fecha_hora DATETIME(6) DEFAULT NULL,
  socio_id BIGINT DEFAULT NULL,
  clase_id BIGINT DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_reserva_socio (socio_id),
  KEY fk_reservas_clase (clase_id),
  CONSTRAINT fk_reservas_clase FOREIGN KEY (clase_id) REFERENCES clases (id) ON DELETE CASCADE,
  CONSTRAINT FK_reserva_socio FOREIGN KEY (socio_id) REFERENCES socios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE asistencias (
  id BIGINT NOT NULL AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  clase_id BIGINT NOT NULL,
  reserva_id BIGINT NOT NULL,
  fecha DATE NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY reserva_id (reserva_id),
  KEY socio_id (socio_id),
  KEY clase_id (clase_id),
  CONSTRAINT asistencias_ibfk_1 FOREIGN KEY (socio_id) REFERENCES socios (id) ON DELETE CASCADE,
  CONSTRAINT asistencias_ibfk_2 FOREIGN KEY (clase_id) REFERENCES clases (id) ON DELETE CASCADE,
  CONSTRAINT asistencias_ibfk_3 FOREIGN KEY (reserva_id) REFERENCES reservas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE facturas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  fecha DATE NOT NULL,
  total DECIMAL(38,2) DEFAULT NULL,
  estado VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY socio_id (socio_id),
  CONSTRAINT facturas_ibfk_1 FOREIGN KEY (socio_id) REFERENCES socios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE detalles_factura (
  id BIGINT NOT NULL AUTO_INCREMENT,
  factura_id BIGINT NOT NULL,
  descripcion VARCHAR(255) DEFAULT NULL,
  cantidad INT NOT NULL DEFAULT 1,
  precio_unitario DECIMAL(38,2) DEFAULT NULL,
  subtotal DECIMAL(38,2) DEFAULT NULL,
  monto DECIMAL(38,2) DEFAULT NULL,
  tipo_item VARCHAR(255) DEFAULT NULL,
  membresia_id BIGINT DEFAULT NULL,
  reserva_id BIGINT DEFAULT NULL,
  PRIMARY KEY (id),
  KEY factura_id (factura_id),
  KEY fk_detalle_membresia (membresia_id),
  KEY fk_detalle_reserva (reserva_id),
  CONSTRAINT detalles_factura_ibfk_1 FOREIGN KEY (factura_id) REFERENCES facturas (id),
  CONSTRAINT fk_detalle_membresia FOREIGN KEY (membresia_id) REFERENCES membresias (id) ON DELETE SET NULL,
  CONSTRAINT fk_detalle_reserva FOREIGN KEY (reserva_id) REFERENCES reservas (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE pagos (
  id BIGINT NOT NULL AUTO_INCREMENT,
  factura_id BIGINT NOT NULL,
  fecha_pago DATE NOT NULL,
  monto_pagado DOUBLE NOT NULL,
  metodo_pago VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  KEY factura_id (factura_id),
  CONSTRAINT pagos_ibfk_1 FOREIGN KEY (factura_id) REFERENCES facturas (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* -------------------
   DATOS DE REFERENCIA
   ------------------- */

INSERT INTO membresias (id, clases_incluidas, descripcion, nombre, precio_base, precio_clase_extra, tipo_cobro, duracion_dias, precio, tipo_membresia) VALUES
  (1, 10, 'Acceso por 30 dias', NULL, NULL, 5.00, 'CLASES_INCLUIDAS', 30, 50.00, 'Mensual'),
  (2, 30, 'Acceso por 90 dias', NULL, NULL, 4.50, 'CLASES_INCLUIDAS', 90, 135.00, 'Trimestral'),
  (3, 120, 'Acceso por 365 dias', NULL, NULL, 4.00, 'CLASES_INCLUIDAS', 365, 500.00, 'Anual');

INSERT INTO instructores (id, nombre_completo, especialidad, email, experiencia, telefono) VALUES
  (2, 'Luis Martinez', 'Spinning', 'luis.martinez@incafit.com', '', '123123123'),
  (3, 'Miguel Rodriguez', 'Musculacion', 'miguel.rodriguez@incafit.com', NULL, NULL),
  (4, 'Laura Santos', 'Zumba', 'laura.santos@incafit.com', '', ''),
  (6, 'Pol Fernandez', 'Pilates', 'pol.fernandez@incafit.com', '', '');

INSERT INTO clases (id, nombre, descripcion, capacidad_maxima, instructor_id, hora, duracion_minutos, dias_semana, activo, fecha_inicio, fecha_fin, precio_adicional) VALUES
  (1, 'Yoga', 'Clase de relajacion y flexibilidad', 20, 6, '15:45:00', 60, 'MONDAY,WEDNESDAY,FRIDAY', b'1', NULL, NULL, NULL),
  (2, 'Spinning', 'Clase de ciclismo intenso', 15, 2, '18:00:00', 45, 'TUESDAY,THURSDAY', b'1', NULL, NULL, NULL),
  (3, 'Pilates', 'Fortalecimiento del core y flexibilidad', 12, 6, '10:00:00', 50, 'MONDAY,WEDNESDAY', b'1', NULL, NULL, NULL),
  (4, 'HIIT', 'Entrenamiento de alta intensidad', 10, 2, '19:30:00', 30, 'TUESDAY,THURSDAY', b'1', NULL, NULL, NULL),
  (5, 'Musculacion', 'Entrenamiento con pesas', 8, 3, '07:00:00', 90, 'MONDAY,WEDNESDAY,FRIDAY', b'1', NULL, NULL, NULL),
  (7, 'Defensa Personal', 'Curso de defensa personal de 3 meses', 15, 2, '19:00:00', 90, 'MONDAY,WEDNESDAY,FRIDAY', b'1', '2025-10-19', '2026-01-17', 25.00),
  (8, 'Yoga', 'Clase de relajacion y flexibilidad', 20, 6, '18:30:00', 60, 'TUESDAY,THURSDAY', b'1', NULL, NULL, NULL),
  (9, 'Spinning', 'Clase de ciclismo intenso', 15, 2, '18:00:00', 45, 'MONDAY,WEDNESDAY,FRIDAY', b'1', NULL, NULL, NULL),
  (10, 'Pilates', 'Fortalecimiento del core y flexibilidad', 12, 6, '10:00:00', 50, 'TUESDAY,THURSDAY', b'1', NULL, NULL, NULL),
  (11, 'HIIT', 'Entrenamiento de alta intensidad', 10, 2, '19:30:00', 30, 'MONDAY,WEDNESDAY,FRIDAY', b'1', NULL, NULL, NULL),
  (12, 'Musculacion', 'Entrenamiento con pesas', 8, 3, '07:00:00', 90, 'TUESDAY,THURSDAY', b'1', NULL, NULL, NULL),
  (13, 'Zumba', 'Baile y cardio', 25, 4, '20:00:00', 60, 'MONDAY,WEDNESDAY,FRIDAY', b'1', NULL, NULL, NULL),
  (14, 'Defensa Personal', 'Curso de defensa personal de 3 meses', 15, 3, '19:00:00', 90, 'MONDAY,WEDNESDAY,FRIDAY', b'1', '2025-10-19', '2026-01-17', 25.00);

INSERT INTO socios (id, dni, email, telefono, fecha_registro, nombre, password, rol, activo, membresia_id, fecha_inicio_membresia, fecha_fin_membresia) VALUES
  (2, '12345678A', 'test@example.com', '600123456', '2025-09-10', 'Usuario Test', '$2a$10$r3k4I5q6w7e8r9t0y1u2vOcQdReSfTgUhViWjXkYlZmAnBoCpDqEs', 'USUARIO', 1, 1, '2025-10-19', '2025-11-18'),
  (3, '74085564', 'nikkmed805@gmail.com', '600987653', '2025-09-29', 'Nikolas Adriano Medina Ricra', '$2a$10$4DxZXb5Ys4y9chw2tIClFuuP7tZlafoAwyiQUTicTy0fsYHjGqX2u', 'ADMIN', 1, 2, '2025-10-19', '2026-01-17'),
  (4, '12345678', 'prueba@yopmail.com', NULL, '2025-10-14', 'Prueba Prueba Prueba Pruebaa', '123456', 'USUARIO', 1, 2, NULL, NULL),
  (6, '123456777', 'holainca@yopmail.com', '666999330', '2025-10-21', 'Hola', '$2a$10$JucMYn4Jc1anijZraDtKh..yqx0Q5j4YXlRwu8F2GzrM/pL0Zyrji', 'USUARIO', 1, 1, '2025-12-10', '2026-01-08'),
  (8, '740855643', 'testeo@yopmail.com', '357357758', '2025-10-30', 'TESTEO testeo', '$2a$10$wsajuKnoiPOrnZxXZOGZcO.WIFCWDpzkKun3fK16jt9DPdqWu8nfS', 'USUARIO', 1, 3, NULL, NULL),
  (9, '74085562', 'holatest@yopmail.com', '603100222', '2025-10-30', 'HOLA', '$2a$10$i8hc3k2AYYbb.uJPKP1YkuQZxDbxrbGtqqotkEDH0FjgVP1PmAJYu', 'USUARIO', 1, 3, NULL, NULL),
  (11, '3914119241', 'final@yopmail.com', NULL, '2025-11-04', 'final final', '12345678', 'USUARIO', 1, 1, NULL, NULL),
  (12, '00000007', 'admindemo@incafit.com', '600777888', '2025-11-04', 'Admin Demo', '$2a$10$M7AlYmPn1QIPDultONJ2XOq8c6PfIL1eM/jDOxKMK7gf/CIEvdOGO', 'ADMIN', 1, NULL, NULL, NULL),
  (13, '123456781', 'Hola@yopmail.com', '603030242', '2025-11-07', 'Holaa', '$2a$10$Sgd5LDMladLyh172ElTGoeDjyFLx7kngIswQrWRfx3NvKWk4Mt.yG', 'USUARIO', 1, 1, NULL, NULL),
  (14, '74085533', 'finaltest@yopmail.com', '123456711', '2025-11-10', 'Final', '$2a$10$Pt7skOOm2pfPv9HO7dojKeojs8qzEHQ5PqUULOnJ77j0smDNJLBJW', 'USUARIO', 1, 1, NULL, NULL),
  (15, '12341412A', 'nikkmed@hotmail.com', '666303444', '2025-11-13', 'Niko', '$2a$10$m.O5PHvOLJXwuI9xSu3qPe6f1wQdr61Bbp5shnEckm8NXyDa8TAxu', 'USUARIO', 1, 1, NULL, NULL);

INSERT INTO reservas (id, estado, fecha_hora, socio_id, clase_id) VALUES
  (5, 'CONFIRMADA', '2025-11-12 12:00:00', 3, 7),
  (8, 'CONFIRMADA', '2025-11-19 14:45:00', 3, 1),
  (9, 'CANCELADA', '2025-11-13 18:30:00', 6, 4),
  (10, 'CONFIRMADA', '2025-11-17 19:00:00', 3, 13),
  (11, 'CONFIRMADA', '2025-11-19 19:00:00', 3, 13),
  (12, 'CANCELADA', '2025-11-19 19:00:00', 3, 13);

INSERT INTO facturas (id, socio_id, fecha, total, estado) VALUES
  (2, 3, '2025-10-15', 135.00, 'PAGADA'),
  (3, 4, '2025-10-15', 50.00, 'PAGADA'),
  (6, 3, '2025-10-28', 135.00, 'PAGADA'),
  (13, 6, '2025-11-10', 50.00, 'PAGADA'),
  (14, 6, '2025-11-10', 50.00, 'PAGADA'),
  (15, 15, '2025-11-13', 50.00, NULL);

INSERT INTO detalles_factura (id, factura_id, descripcion, cantidad, precio_unitario, subtotal, monto, tipo_item, membresia_id, reserva_id) VALUES
  (2, 2, 'Renovacion de Membresia Trimestral', 1, 135.00, 135.00, NULL, NULL, NULL, NULL),
  (12, 13, 'Renovacion de membresia Mensual', 1, 50.00, 50.00, NULL, 'MEMBRESIA', 1, NULL),
  (13, 14, 'Renovacion de membresia Mensual', 1, 50.00, 50.00, NULL, 'MEMBRESIA', 1, NULL),
  (14, 15, 'Membresia Mensual', 1, 50.00, 50.00, NULL, NULL, NULL, NULL);

INSERT INTO asistencias (id, socio_id, clase_id, reserva_id, fecha) VALUES
  (1, 3, 7, 5, '2025-10-30'),
  (3, 3, 1, 8, '2025-11-19'),
  (4, 6, 4, 9, '2025-11-13'),
  (5, 3, 13, 10, '2025-11-17'),
  (6, 3, 13, 11, '2025-11-19'),
  (7, 3, 13, 12, '2025-11-19');

INSERT INTO pagos (id, factura_id, fecha_pago, monto_pagado, metodo_pago) VALUES
  (2, 2, '2024-10-01', 135.00, 'TRANSFERENCIA_BANCARIA');
