-- ========================================
-- BASE DE DATOS COMPLETA Y DEFINITIVA
-- Sistema de Ventas de Perfumes
-- Incluye: Estructura + Datos + Usuarios con contraseñas válidas
-- ========================================

DROP DATABASE IF EXISTS evaluacion_bd;
CREATE DATABASE evaluacion_bd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE evaluacion_bd;

-- ========================================
-- ESTRUCTURA DE TABLAS
-- ========================================

-- Tabla: roles
CREATE TABLE roles (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  descripcion VARCHAR(500),
  fecha_creacion DATETIME(6),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: usuarios
CREATE TABLE usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  rol_id BIGINT NOT NULL,
  activo BOOLEAN DEFAULT TRUE,
  fecha_creacion DATETIME(6),
  fecha_actualizacion DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_usuario_username (username),
  KEY idx_usuario_email (email),
  CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) REFERENCES roles (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: clientes
CREATE TABLE clientes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  telefono VARCHAR(20),
  direccion VARCHAR(500),
  ciudad VARCHAR(100),
  fecha_registro DATETIME(6),
  fecha_actualizacion DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_cliente_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: categorias
CREATE TABLE categorias (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(500),
  fecha_creacion DATETIME(6),
  fecha_actualizacion DATETIME(6),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: marcas
CREATE TABLE marcas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(500),
  pais VARCHAR(50),
  fecha_creacion DATETIME(6),
  fecha_actualizacion DATETIME(6),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: perfumes
CREATE TABLE perfumes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(200) NOT NULL,
  marca_id BIGINT NOT NULL,
  categoria_id BIGINT NOT NULL,
  precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
  stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
  descripcion VARCHAR(500),
  tamanio VARCHAR(50),
  tipo VARCHAR(20),
  imagen VARCHAR(1000),
  fecha_creacion DATETIME(6),
  fecha_actualizacion DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_perfume_marca (marca_id),
  KEY idx_perfume_categoria (categoria_id),
  CONSTRAINT fk_perfumes_marca FOREIGN KEY (marca_id) REFERENCES marcas (id) ON DELETE RESTRICT,
  CONSTRAINT fk_perfumes_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: ventas
CREATE TABLE ventas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cliente_id BIGINT NOT NULL,
  vendedor_id BIGINT,
  monto_total DECIMAL(10,2) NOT NULL CHECK (monto_total >= 0),
  fecha DATETIME(6),
  estado VARCHAR(20) DEFAULT 'COMPLETADA',
  observaciones VARCHAR(500),
  fecha_creacion DATETIME(6),
  fecha_actualizacion DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_venta_cliente (cliente_id),
  KEY idx_venta_vendedor (vendedor_id),
  KEY idx_venta_fecha (fecha),
  CONSTRAINT fk_ventas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id) ON DELETE RESTRICT,
  CONSTRAINT fk_ventas_vendedor FOREIGN KEY (vendedor_id) REFERENCES usuarios (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: detalle_ventas
CREATE TABLE detalle_ventas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  venta_id BIGINT NOT NULL,
  perfume_id BIGINT NOT NULL,
  cantidad INT NOT NULL CHECK (cantidad > 0),
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  fecha_creacion DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_detalle_venta (venta_id),
  KEY idx_detalle_perfume (perfume_id),
  CONSTRAINT fk_detalle_ventas FOREIGN KEY (venta_id) REFERENCES ventas (id) ON DELETE CASCADE,
  CONSTRAINT fk_detalle_perfumes FOREIGN KEY (perfume_id) REFERENCES perfumes (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- DATOS INICIALES
-- ========================================

-- Roles del sistema (ADMIN, VENDEDOR, CLIENTE)
INSERT INTO roles (nombre, descripcion, fecha_creacion) VALUES
('ADMIN', 'Administrador del sistema con acceso completo', NOW()),
('VENDEDOR', 'Vendedor que puede crear y gestionar ventas', NOW()),
('CLIENTE', 'Cliente que puede realizar compras', NOW());

-- Usuarios con contraseñas (admin123, vendedor123, cliente123)
-- Hash BCrypt generado con BCryptPasswordEncoder de Spring Security
INSERT INTO usuarios (username, password, nombre, email, rol_id, activo, fecha_creacion, fecha_actualizacion) VALUES
('admin', '$2a$10$DYv4ybOudmhSXGMMtILvD.7Y8iL/TQEExFNLTdsGxenn3AdI82yS2', 'Administrador Sistema', 'admin@perfumeria.com', 1, 1, NOW(), NOW()),
('vendedor', '$2a$10$aFJHxcTG2kktvB1qXsAvlOGVEbqdJpNIN9WtwOy/247AN5LOmB6HG', 'Vendedor Principal', 'vendedor@perfumeria.com', 2, 1, NOW(), NOW()),
('cliente', '$2a$10$KtbnBTor1AvW72IBjMCOUebxj6Zue6foUBeCKzdR39/5aj4L3kf5a', 'Cliente Test', 'cliente@perfumeria.com', 3, 1, NOW(), NOW());

-- Clientes
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, fecha_registro, fecha_actualizacion) VALUES
('Juan', 'Pérez', 'juan.perez@email.com', '555-0101', 'Av. Principal 123', 'Santiago', NOW(), NOW()),
('María', 'González', 'maria.gonzalez@email.com', '555-0102', 'Calle Central 456', 'Santiago', NOW(), NOW()),
('Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '555-0103', 'Av. Libertador 789', 'Valparaíso', NOW(), NOW()),
('Ana', 'Martínez', 'ana.martinez@email.com', '555-0104', 'Calle Norte 321', 'Concepción', NOW(), NOW()),
('Pedro', 'López', 'pedro.lopez@email.com', '555-0105', 'Av. Sur 654', 'Santiago', NOW(), NOW());

-- Categorías de perfumes
INSERT INTO categorias (nombre, descripcion, fecha_creacion, fecha_actualizacion) VALUES
('Masculino', 'Fragancias para hombre', NOW(), NOW()),
('Femenino', 'Fragancias para mujer', NOW(), NOW()),
('Unisex', 'Fragancias para todos', NOW(), NOW()),
('Oriental', 'Fragancias con notas especiadas y cálidas', NOW(), NOW()),
('Floral', 'Fragancias con predominio de notas florales', NOW(), NOW()),
('Fresco', 'Fragancias ligeras y refrescantes', NOW(), NOW());

-- Marcas de perfumes
INSERT INTO marcas (nombre, descripcion, pais, fecha_creacion, fecha_actualizacion) VALUES
('Dior', 'Casa de moda y perfumería francesa de lujo', 'Francia', NOW(), NOW()),
('Chanel', 'Marca de alta costura y perfumería francesa', 'Francia', NOW(), NOW()),
('Gucci', 'Casa de moda italiana de lujo', 'Italia', NOW(), NOW()),
('Paco Rabanne', 'Marca de moda y perfumería española', 'España', NOW(), NOW()),
('Carolina Herrera', 'Diseñadora venezolana-estadounidense', 'Estados Unidos', NOW(), NOW()),
('Armani', 'Casa de moda italiana fundada por Giorgio Armani', 'Italia', NOW(), NOW()),
('Versace', 'Marca de moda italiana de lujo', 'Italia', NOW(), NOW()),
('Calvin Klein', 'Marca estadounidense de moda', 'Estados Unidos', NOW(), NOW()),
('Hugo Boss', 'Marca alemana de moda', 'Alemania', NOW(), NOW()),
('Dolce & Gabbana', 'Casa de moda italiana de lujo', 'Italia', NOW(), NOW());

-- Perfumes (productos disponibles)
INSERT INTO perfumes (nombre, marca_id, categoria_id, precio, stock, descripcion, tamanio, tipo, imagen, fecha_creacion, fecha_actualizacion) VALUES
('Sauvage', 1, 1, 89990, 25, 'Fragancia masculina fresca y especiada', '100ml', 'EDT', 'https://images.unsplash.com/photo-1541643600914-78b084683601', NOW(), NOW()),
('J\'adore', 1, 2, 95990, 30, 'Fragancia femenina floral y luminosa', '100ml', 'EDP', 'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539', NOW(), NOW()),
('Chanel No. 5', 2, 2, 125990, 20, 'El perfume más icónico del mundo', '100ml', 'EDP', 'https://images.unsplash.com/photo-1590736969955-71cc94901144', NOW(), NOW()),
('Bleu de Chanel', 2, 1, 119990, 18, 'Fragancia masculina aromática y amaderada', '100ml', 'EDP', 'https://images.unsplash.com/photo-1585386959984-a4155224a1ad', NOW(), NOW()),
('Guilty', 3, 3, 79990, 35, 'Fragancia unisex oriental y floral', '90ml', 'EDT', 'https://images.unsplash.com/photo-1563170351-be82bc888aa4', NOW(), NOW()),
('1 Million', 4, 1, 84990, 28, 'Fragancia masculina especiada y dulce', '100ml', 'EDT', 'https://images.unsplash.com/photo-1587017539504-67cfbddac569', NOW(), NOW()),
('Lady Million', 4, 2, 84990, 32, 'Fragancia femenina floral y frutal', '80ml', 'EDP', 'https://images.unsplash.com/photo-1588405748879-d5a88f753ce0', NOW(), NOW()),
('Good Girl', 5, 2, 99990, 22, 'Fragancia femenina con notas de tubereosa y jazmín', '80ml', 'EDP', 'https://images.unsplash.com/photo-1594035910387-fea47794261f', NOW(), NOW()),
('Acqua Di Gio', 6, 1, 89990, 40, 'Fragancia masculina acuática y fresca', '100ml', 'EDT', 'https://images.unsplash.com/photo-1523293182086-7651a899d37f', NOW(), NOW()),
('Si', 6, 2, 94990, 27, 'Fragancia femenina chypre frutal', '100ml', 'EDP', 'https://images.unsplash.com/photo-1595425970377-c9703cf48b6d', NOW(), NOW()),
('Bright Crystal', 7, 2, 79990, 33, 'Fragancia femenina floral y frutal', '90ml', 'EDT', 'https://images.unsplash.com/photo-1547887538-047f814bfb64', NOW(), NOW()),
('Eros', 7, 1, 89990, 29, 'Fragancia masculina fresca y amaderada', '100ml', 'EDT', 'https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261', NOW(), NOW()),
('CK One', 8, 3, 49990, 50, 'Fragancia unisex cítrica y fresca', '200ml', 'EDT', 'https://images.unsplash.com/photo-1557170334-a9632e77c6e4', NOW(), NOW()),
('Eternity', 8, 2, 69990, 38, 'Fragancia femenina floral y romántica', '100ml', 'EDP', 'https://images.unsplash.com/photo-1594035910387-fea47794261f', NOW(), NOW()),
('Boss Bottled', 9, 1, 74990, 36, 'Fragancia masculina amaderada y especiada', '100ml', 'EDT', 'https://images.unsplash.com/photo-1563170351-be82bc888aa4', NOW(), NOW()),
('The Scent', 9, 2, 79990, 31, 'Fragancia femenina oriental y floral', '100ml', 'EDP', 'https://images.unsplash.com/photo-1587017539504-67cfbddac569', NOW(), NOW()),
('Light Blue', 10, 2, 84990, 34, 'Fragancia femenina fresca y frutal', '100ml', 'EDT', 'https://images.unsplash.com/photo-1541643600914-78b084683601', NOW(), NOW()),
('The One', 10, 1, 89990, 26, 'Fragancia masculina oriental y especiada', '100ml', 'EDP', 'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539', NOW(), NOW()),
('Pour Homme', 7, 1, 79990, 37, 'Fragancia masculina aromática y fresca', '100ml', 'EDT', 'https://images.unsplash.com/photo-1590736969955-71cc94901144', NOW(), NOW()),
('Scandal', 4, 2, 89990, 24, 'Fragancia femenina floral y dulce', '80ml', 'EDP', 'https://images.unsplash.com/photo-1585386959984-a4155224a1ad', NOW(), NOW());

-- Ventas de ejemplo (para testing y reportes)
INSERT INTO ventas (cliente_id, vendedor_id, monto_total, fecha, estado, observaciones, fecha_creacion, fecha_actualizacion) VALUES
(1, 2, 179980.00, '2024-11-15 10:30:00', 'COMPLETADA', 'Venta en tienda física', NOW(), NOW()),
(2, 2, 125990.00, '2024-11-18 14:45:00', 'COMPLETADA', 'Pago con tarjeta de crédito', NOW(), NOW()),
(3, 2, 254980.00, '2024-11-20 16:20:00', 'COMPLETADA', 'Cliente frecuente', NOW(), NOW()),
(4, 2, 89990.00, '2024-11-22 11:15:00', 'COMPLETADA', 'Compra online', NOW(), NOW()),
(5, 2, 169980.00, '2024-11-25 09:30:00', 'COMPLETADA', 'Regalo corporativo', NOW(), NOW());

-- Detalles de ventas
INSERT INTO detalle_ventas (venta_id, perfume_id, cantidad, precio_unitario, subtotal, fecha_creacion) VALUES
-- Venta 1: Cliente Juan Pérez
(1, 1, 2, 89990.00, 179980.00, NOW()),
-- Venta 2: Cliente María González
(2, 3, 1, 125990.00, 125990.00, NOW()),
-- Venta 3: Cliente Carlos Rodríguez
(3, 4, 1, 119990.00, 119990.00, NOW()),
(3, 8, 1, 99990.00, 99990.00, NOW()),
(3, 11, 1, 79990.00, 79990.00, NOW()),
-- Venta 4: Cliente Ana Martínez
(4, 9, 1, 89990.00, 89990.00, NOW()),
-- Venta 5: Cliente Pedro López
(5, 6, 2, 84990.00, 169980.00, NOW());

-- ========================================
-- VERIFICACIÓN DE DATOS
-- ========================================

-- Mostrar resumen de datos insertados
SELECT 'Roles' AS Tabla, COUNT(*) AS Total FROM roles
UNION ALL
SELECT 'Usuarios', COUNT(*) FROM usuarios
UNION ALL
SELECT 'Clientes', COUNT(*) FROM clientes
UNION ALL
SELECT 'Categorías', COUNT(*) FROM categorias
UNION ALL
SELECT 'Marcas', COUNT(*) FROM marcas
UNION ALL
SELECT 'Perfumes', COUNT(*) FROM perfumes
UNION ALL
SELECT 'Ventas', COUNT(*) FROM ventas
UNION ALL
SELECT 'Detalles de Venta', COUNT(*) FROM detalle_ventas;

-- ========================================
-- INFORMACIÓN DE USUARIOS
-- ========================================
-- Usuarios disponibles:
--   admin      / admin123     (ROL: ADMIN)
--   vendedor   / vendedor123  (ROL: VENDEDOR)
--   cliente    / cliente123   (ROL: CLIENTE)
-- ========================================
