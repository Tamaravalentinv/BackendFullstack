# README de Evaluación – BackendFullstack (Java 21 / Spring Boot 3.2.12)

Este documento unifica la guía de integración Frontend ↔ Backend y los pasos para preparar la base de datos vía scripts, orientado al evaluador.

## Objetivo
- Verificar que el backend funciona con una base de datos MySQL (XAMPP) cargada por script.
- Probar autenticación JWT y endpoints de Productos, Órdenes y Usuarios.
- Validar que el frontend consume el backend correctamente.

## Requisitos
- XAMPP con MySQL activo (phpMyAdmin en `http://localhost/phpmyadmin`).
- Java 21, Maven (usa `mvnw.cmd`).
- Postman (opcional para pruebas rápidas de API).
- Frontend en `http://localhost:3000` (CORS habilitado).

## Preparar Base de Datos (sin BD física)
El repo incluye dos formas:
- A) Importar esquema listo: `db/schema.sql` (recomendado por rapidez).
- B) Generar esquema automáticamente: perfil `schema` → `target/schema.sql`.

### Opción A: Importar `db/schema.sql`
1. Abrir phpMyAdmin: `http://localhost/phpmyadmin`.
2. Crear base: `backend_evaluacion`.
3. Seleccionar BD → pestaña Importar → elegir `db/schema.sql` → Ejecutar.
4. Importar datos de ejemplo: `db/evaluacion_seed.sql`.

### Opción B: Generar `target/schema.sql` con el backend
1. Comandos en Windows (cmd):
```bat
mvnw.cmd -q -DskipTests clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=schema
```
2. Se creará `target/schema.sql`.
3. Importar en phpMyAdmin como en la opción A.
4. Importar `db/evaluacion_seed.sql`.

Notas:
- `db/evaluacion_seed.sql` incluye roles, categorías, marcas, perfumes y clientes.
- Los usuarios se crean vía API (para que la contraseña quede en BCrypt).

## Configurar Backend (MySQL XAMPP)
Ejemplo disponible: `src/main/resources/application-xampp.properties`.
Copia/adapta en `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:mysql://localhost:3306/backend_evaluacion?useSSL=false&serverTimezone=UTC
spring.datasource.username=app_user
spring.datasource.password=AppPass123!
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

Arranque del backend:
```bat
mvnw.cmd clean spring-boot:run
```

## Crear Usuario ADMIN (BCrypt) vía API
- Endpoint: `POST /api/v1/users`
- Body:
```json
{
  "username": "admin",
  "password": "Admin123!",
  "rol": "ADMIN",
  "nombre": "Administrador",
  "email": "admin@example.com"
}
```

## Colección Postman
Archivo: `postman/BackendFullstack.postman_collection.json`.
- Variables:
  - `baseUrl`: por defecto `http://localhost:8080/api/v1`
  - `token`: coloca el JWT tras el login
- Requests incluidos:
  - Auth: Login (`POST /auth/login`)
  - Users: Crear ADMIN (`POST /users`)
  - Products: Listar (`GET /products`)
  - Orders: Listar (`GET /orders`)
  - Users: Listar (`GET /users`)

## Guía Frontend ↔ Backend (resumen)
Base URL: `http://localhost:8080/api/v1`
- Auth: `POST /auth/login` → devuelve `token`, `usuario`, `rol`
- Productos: `GET/POST/PUT/DELETE /products`
- Órdenes: `GET/POST/PUT/DELETE /orders`
- Usuarios (ADMIN): `GET/POST /users`, `GET /users/{username}`

CORS: permitido `http://localhost:3000`. Frontend debe enviar `Authorization: Bearer <token>`.

Axios (interceptor): añade el header con JWT desde `localStorage`.

Roles:
- ADMIN: acceso total.
- VENDEDOR: ver/crear órdenes, ver productos.
- CLIENTE: acceso tienda pública.

Más detalle en `front-back.md` (servicios Axios, guardas de rutas, payloads, errores comunes).

## Validación End-to-End
1. Importar esquema + seed en MySQL.
2. Configurar `application.properties` y arrancar backend.
3. Crear usuario ADMIN vía API.
4. Login en frontend y validar:
   - Listado de productos y órdenes.
   - Creación de órdenes y reflejo en MySQL (phpMyAdmin).
5. Postman: respuestas 200 con datos y JWT válido.

## Solución de Problemas (comunes)
- 401: token faltante/expirado → verificar interceptor y login.
- 403: rol sin permiso → revisar matriz de roles y guardas.
- CORS: origen distinto → agregar en config de backend.
- SQL: cadena de conexión/credenciales incorrectas → revisar `application.properties`.

## Archivos relevantes
- `db/schema.sql` (esquema listo)
- `db/evaluacion_seed.sql` (datos ejemplo)
- `src/main/resources/application-xampp.properties` (ejemplo conexión)
- `src/main/resources/application-schema.properties` (perfil export schema)
- `postman/BackendFullstack.postman_collection.json` (API tests)
- `front-back.md` (guía de integración detallada)
