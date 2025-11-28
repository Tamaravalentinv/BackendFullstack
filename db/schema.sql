-- Esquema MySQL para evaluación basado en entidades JPA
-- Úsalo si no puedes generar `target/schema.sql` automáticamente

-- Ajustes iniciales
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  descripcion VARCHAR(500),
  fechaCreacion DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uq_roles_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(200) NOT NULL,
  nombre VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  rol_id BIGINT NOT NULL,
  activo TINYINT(1) DEFAULT 1,
  fechaCreacion DATETIME,
  fechaActualizacion DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uq_usuarios_username (username),
  UNIQUE KEY uq_usuarios_email (email),
  KEY idx_usuarios_rol (rol_id),
  CONSTRAINT fk_usuarios_roles FOREIGN KEY (rol_id) REFERENCES roles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS categorias (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  descripcion VARCHAR(500),
  fechaCreacion DATETIME,
  fechaActualizacion DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uq_categorias_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS marcas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  descripcion VARCHAR(500),
  pais VARCHAR(50),
  fechaCreacion DATETIME,
  fechaActualizacion DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uq_marcas_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS perfumes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  marca_id BIGINT NOT NULL,
  categoria_id BIGINT NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  descripcion VARCHAR(500),
  tamanio VARCHAR(50),
  tipo VARCHAR(20),
  fechaCreacion DATETIME,
  fechaActualizacion DATETIME,
  PRIMARY KEY (id),
  KEY idx_perfumes_marca (marca_id),
  KEY idx_perfumes_categoria (categoria_id),
  CONSTRAINT fk_perfumes_marcas FOREIGN KEY (marca_id) REFERENCES marcas (id),
  CONSTRAINT fk_perfumes_categorias FOREIGN KEY (categoria_id) REFERENCES categorias (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS clientes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  apellido VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  telefono VARCHAR(20),
  direccion VARCHAR(500),
  ciudad VARCHAR(20),
  fechaRegistro DATETIME,
  fechaActualizacion DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uq_clientes_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ventas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cliente_id BIGINT NOT NULL,
  vendedor_id BIGINT NOT NULL,
  montoTotal DECIMAL(10,2) NOT NULL,
  fecha DATETIME,
  estado VARCHAR(20),
  observaciones VARCHAR(500),
  fechaCreacion DATETIME,
  fechaActualizacion DATETIME,
  PRIMARY KEY (id),
  KEY idx_ventas_cliente (cliente_id),
  KEY idx_ventas_vendedor (vendedor_id),
  CONSTRAINT fk_ventas_clientes FOREIGN KEY (cliente_id) REFERENCES clientes (id),
  CONSTRAINT fk_ventas_usuarios FOREIGN KEY (vendedor_id) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS detalle_ventas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  venta_id BIGINT NOT NULL,
  perfume_id BIGINT NOT NULL,
  cantidad INT NOT NULL,
  precioUnitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  fechaCreacion DATETIME,
  PRIMARY KEY (id),
  KEY idx_detalle_ventas_venta (venta_id),
  KEY idx_detalle_ventas_perfume (perfume_id),
  CONSTRAINT fk_detalle_ventas_ventas FOREIGN KEY (venta_id) REFERENCES ventas (id),
  CONSTRAINT fk_detalle_ventas_perfumes FOREIGN KEY (perfume_id) REFERENCES perfumes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
