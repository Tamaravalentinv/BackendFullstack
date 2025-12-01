-- Datos iniciales para tests
INSERT INTO roles (id, nombre, descripcion, fecha_creacion) VALUES 
(1, 'ADMIN', 'Administrador del sistema', NOW()),
(2, 'VENDEDOR', 'Vendedor', NOW()),
(3, 'CLIENTE', 'Cliente', NOW());

-- Usuario admin para tests (password: admin123)
INSERT INTO usuarios (id, username, password, nombre, email, rol_id, activo, fecha_creacion, fecha_actualizacion) VALUES
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin Test', 'admin@test.com', 1, true, NOW(), NOW());
