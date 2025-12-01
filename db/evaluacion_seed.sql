-- Datos de ejemplo para evaluación (no incluye usuarios por password en BCrypt)
-- Importar después de crear el esquema (schema.sql)

-- Roles base
INSERT INTO roles (nombre, descripcion, fechaCreacion) VALUES
 ('ADMIN','Acceso total', NOW()),
 ('VENDEDOR','Gestión de ventas y catálogo', NOW());

-- Categorías
INSERT INTO categorias (nombre, descripcion, fechaCreacion, fechaActualizacion) VALUES
 ('Masculino','Fragancias para hombre', NOW(), NOW()),
 ('Femenino','Fragancias para mujer', NOW(), NOW()),
 ('Unisex','Fragancias unisex', NOW(), NOW());

-- Marcas
INSERT INTO marcas (nombre, descripcion, pais, fechaCreacion, fechaActualizacion) VALUES
 ('Dior','Christian Dior', 'Francia', NOW(), NOW()),
 ('Chanel','Chanel SAS', 'Francia', NOW(), NOW()),
 ('Calvin Klein','CK', 'EEUU', NOW(), NOW());

-- Perfumes (asume ids: Dior=1, Chanel=2, CK=3; Masculino=1, Femenino=2, Unisex=3)
INSERT INTO perfumes (nombre, marca_id, categoria_id, precio, stock, descripcion, tamanio, tipo, imagen, fechaCreacion, fechaActualizacion) VALUES
 ('Sauvage', 1, 1, 89.99, 50, 'Aroma fresco y especiado', '100ml', 'EDT', 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=500&q=80', NOW(), NOW()),
 ('J''adore', 1, 2, 119.50, 30, 'Floral luminoso', '100ml', 'EDP', 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=500&q=80', NOW(), NOW()),
 ('Bleu de Chanel', 2, 1, 129.00, 40, 'Aromático amaderado', '100ml', 'EDP', 'https://images.unsplash.com/photo-1590080876-c9f5c6b8b5f5?w=500&q=80', NOW(), NOW()),
 ('CK One', 3, 3, 59.90, 100, 'Cítrico unisex', '100ml', 'EDT', 'https://images.unsplash.com/photo-1577720643272-265b0e0d7ca4?w=500&q=80', NOW(), NOW());

-- Clientes
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, fechaRegistro, fechaActualizacion) VALUES
 ('Juan','Pérez','juan.perez@example.com','+56 9 1111 1111','Av. Siempre Viva 123','Santiago', NOW(), NOW()),
 ('María','González','maria.gonzalez@example.com','+56 9 2222 2222','Calle Luna 456','Valparaíso', NOW(), NOW());

-- Nota: Para crear usuarios con contraseña segura (BCrypt), usar el endpoint POST /api/v1/users
-- Ejemplo JSON:
-- {
--   "username": "admin",
--   "password": "Admin123!",
--   "rol": "ADMIN",
--   "nombre": "Administrador",
--   "email": "admin@example.com"
-- }
