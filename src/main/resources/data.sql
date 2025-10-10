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

-- La inserción de socios ahora se maneja mediante DataInitializer.java para asegurar el correcto hasheo de contraseñas.