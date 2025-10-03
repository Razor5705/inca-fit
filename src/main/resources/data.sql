-- Inserción de Instructores
INSERT INTO instructores (nombre_completo, especialidad, email) VALUES
('Carlos Rivera', 'Yoga y Pilates', 'carlos.rivera@incafit.com'),
('Ana Torres', 'Entrenamiento de Fuerza', 'ana.torres@incafit.com'),
('Luis Gomez', 'Cardio y HIIT', 'luis.gomez@incafit.com');

-- Inserción de Clases
INSERT INTO clases (nombre, descripcion, capacidad_maxima, instructor_id) VALUES
('Yoga para principiantes', 'Una clase suave para empezar con el yoga.', 20, 1),
('Levantamiento de pesas 101', 'Aprende los fundamentos del levantamiento de pesas.', 15, 2),
('HIIT', 'Entrenamiento de intervalos de alta intensidad para quemar calorías.', 25, 3);

-- Inserción de Membresías
INSERT INTO membresias (nombre, descripcion, tipo_cobro, precio_base, clases_incluidas, precio_clase_extra) VALUES
('Básica', 'Acceso al gimnasio y 8 clases al mes.', 'CUOTA_FIJA', 29.99, 8, 5.00),
('Premium', 'Acceso completo, incluyendo todas las clases.', 'CUOTA_FIJA', 49.99, NULL, NULL),
('Anual', 'Acceso premium por un año a un precio reducido.', 'CUOTA_FIJA', 499.99, NULL, NULL);

-- Inserción de Socios de prueba (sin reservas iniciales)
-- Contraseña para todos es 'password' (sin encriptar para H2, Spring Security se encargará en la app)
INSERT INTO socios (dni, nombre, email, password, telefono, membresia_id, activo, fecha_registro, rol) VALUES
('11223344A', 'Elena Navarro', 'elena.navarro@test.com', 'password123', '611223344', 1, true, CURDATE(), 'USUARIO'),
('55667788B', 'Javier Martin', 'javier.martin@test.com', 'password123', '655667788', 2, true, CURDATE(), 'USUARIO'),
('99887766C', 'Admin User', 'admin@incafit.com', 'adminpass', '699887766', 2, true, CURDATE(), 'ADMIN');

-- No insertar reservas para usuarios nuevos para cumplir con la Tarea 3
-- Las reservas de prueba se pueden añadir manualmente si es necesario para los administradores.
-- Ejemplo de reserva para un admin (asumiendo que el socio con id 3 es el admin)
-- INSERT INTO reservas (socio_id, clase_id, fecha_hora, estado) VALUES
-- (3, 1, '2024-10-15 18:00:00', 'CONFIRMADA');