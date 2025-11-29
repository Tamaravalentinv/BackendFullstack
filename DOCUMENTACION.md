# Documentación Unificada – BackendFullstack (Java 21 / Spring Boot 3.2.12)

Esta es la guía única del proyecto. Reúne y reemplaza toda la documentación previa (README, front-back y reporte de verificación de servicios).

## Índice
- Objetivo y alcance
- Requisitos
- Arquitectura y seguridad
- Endpoints y permisos (alias)
- Preparación de Base de Datos (schema/seed)
- Configuración del backend (MySQL XAMPP)
- Colección Postman
- Integración Frontend (Axios, servicios, guardas)
- Pruebas automáticas con Axios (Node)
- Validación End-to-End
- Servicios y validaciones (detalle)
- Archivos relevantes
- Solución de problemas

## Objetivo y alcance
- Ejecutar el backend con MySQL (XAMPP) cargado por script.
- Probar autenticación JWT y endpoints de Productos/Órdenes/Usuarios (alias).
- Integrar un frontend (React/Vite) consumiendo el backend con CORS.

## Requisitos
- XAMPP con MySQL activo (phpMyAdmin en `http://localhost/phpmyadmin`).
- Java 21; Maven (usar `mvnw.cmd`).
- Postman (opcional para pruebas de API).
- Frontend en `http://localhost:3000` (CORS permitido en el backend).

## Arquitectura y seguridad
- Autenticación mediante JWT (Header `Authorization: Bearer <token>`).
- Roles: `ADMIN`, `VENDEDOR`, `CLIENTE`.
- Autorización con `@PreAuthorize` por controlador/operación.
- CORS global permite `http://localhost:3000`.

## Endpoints y permisos (alias)
Base URL: `http://localhost:8080/api/v1`

- Auth
  - `POST /auth/login`

- Productos (alias de Perfumes)
  - `GET /products` (ADMIN, VENDEDOR)
  - `GET /products/{id}` (ADMIN, VENDEDOR)
  - `POST /products` (ADMIN)
  - `PUT /products/{id}` (ADMIN)
  - `DELETE /products/{id}` (ADMIN)

- Órdenes (alias de Ventas)
  - `GET /orders` (ADMIN, VENDEDOR)
  - `GET /orders/{id}` (ADMIN, VENDEDOR)
  - `POST /orders` (ADMIN, VENDEDOR)
  - `PUT /orders/{id}` (ADMIN)
  - `DELETE /orders/{id}` (ADMIN)

- Usuarios
  - `GET /users` (ADMIN)
  - `POST /users` (ADMIN)
  - `GET /users/{username}` (ADMIN)

Swagger:
- UI: `http://localhost:8080/swagger-ui/index.html`
- Docs: `http://localhost:8080/v3/api-docs`

## Preparación de Base de Datos (schema/seed)
El repositorio ofrece dos caminos:
- A) Importar esquema listo: `db/schema.sql` (rápido).
- B) Generar el esquema desde el backend (perfil `schema`) y luego importarlo.

### Opción A: Importar `db/schema.sql` + seed
1) Abrir phpMyAdmin: `http://localhost/phpmyadmin`.
2) Crear BD: `backend_evaluacion`.
3) Seleccionar la BD → pestaña Importar → `db/schema.sql` → Ejecutar.
4) Importar datos de ejemplo: `db/evaluacion_seed.sql`.

### Opción B: Generar `target/schema.sql` con el backend
1) Generar y ejecutar el perfil (Windows CMD):
```bat
mvnw.cmd -q -DskipTests clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=schema
```
2) Se creará `target/schema.sql`.
3) Importar en phpMyAdmin como en la Opción A.
4) Importar `db/evaluacion_seed.sql`.

Notas:
- El seed incluye roles, categorías, marcas, perfumes y clientes.
- Los usuarios se crean vía API para almacenar la contraseña en BCrypt.

## Configuración del backend (MySQL XAMPP)
Ejemplo disponible en `src/main/resources/application-xampp.properties`.

Copiar/adaptar en `src/main/resources/application.properties`:
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

### Crear usuario ADMIN (BCrypt) vía API
- Endpoint: `POST /api/v1/users`
- Body de ejemplo:
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
- Requests incluidos: Login, Crear ADMIN, Listar Products/Orders/Users.

## Integración Frontend

### Variables de entorno (Frontend)
Crear `.env` con:
```
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

### Axios (`src/services/api.js`)
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  resp => resp,
  error => {
    if (error.response?.status === 401) {
      // Opcional: redirigir a login
    }
    return Promise.reject(error);
  }
);

export default api;
```

### Servicios Frontend
`src/services/authService.js`
```javascript
import api from './api';

export async function login(username, password) {
  const { data } = await api.post('/auth/login', { username, password });
  if (data.error) throw new Error(data.error);
  localStorage.setItem('token', data.token);
  localStorage.setItem('rol', data.rol);
  localStorage.setItem('usuario', data.usuario);
  return data;
}

export function logout() {
  ['token','rol','usuario'].forEach(k => localStorage.removeItem(k));
}
```

`src/services/productService.js`
```javascript
import api from './api';

export const listProducts   = async () => (await api.get('/products')).data;
export const getProduct     = async id => (await api.get(`/products/${id}`)).data;
export const createProduct  = async p  => (await api.post('/products', p)).data;
export const updateProduct  = async (id, p) => (await api.put(`/products/${id}`, p)).data;
export const deleteProduct  = async id => api.delete(`/products/${id}`);
```

`src/services/orderService.js`
```javascript
import api from './api';

export const listOrders   = async () => (await api.get('/orders')).data;
export const getOrder     = async id => (await api.get(`/orders/${id}`)).data;
export const createOrder  = async o  => (await api.post('/orders', o)).data;
export const updateOrder  = async (id, o) => (await api.put(`/orders/${id}`, o)).data;
export const deleteOrder  = async id => api.delete(`/orders/${id}`);
```

`src/services/userService.js`
```javascript
import api from './api';

export const listUsers   = async () => (await api.get('/users')).data;
export const createUser  = async u  => (await api.post('/users', u)).data;
export const getUser     = async username => (await api.get(`/users/${username}`)).data;
```

### Utilidad de roles y guardas
`src/utils/roleValidator.js`
```javascript
export function hasRole(roles) {
  const current = localStorage.getItem('rol');
  return !!current && roles.includes(current);
}
```

Ejemplo de guardas (React Router):
```jsx
import { hasRole } from '../utils/roleValidator';

export function AdminRoute({ children }) {
  return hasRole(['ADMIN']) ? children : <div>Acceso denegado</div>;
}

export function VendedorRoute({ children }) {
  return hasRole(['ADMIN','VENDEDOR']) ? children : <div>Acceso denegado</div>;
}
```

### Ejemplo de login (UI mínima)
```jsx
import { useState } from 'react';
import { login } from '../services/authService';

export default function LoginForm() {
  const [username,setU] = useState('');
  const [password,setP] = useState('');
  const [error,setError] = useState('');

  const submit = async e => {
    e.preventDefault();
    try {
      await login(username,password);
      const rol = localStorage.getItem('rol');
      if (rol === 'ADMIN') window.location = '/dashboard';
      else if (rol === 'VENDEDOR') window.location = '/orders';
      else window.location = '/store';
    } catch (e) {
      setError(e.message);
    }
  };

  return (
    <form onSubmit={submit}>
      <input value={username} onChange={e=>setU(e.target.value)} placeholder='Usuario'/>
      <input type='password' value={password} onChange={e=>setP(e.target.value)} placeholder='Contraseña'/>
      <button>Ingresar</button>
      {error && <p style={{color:'red'}}>{error}</p>}
    </form>
  );
}
```

## Pruebas automáticas con Axios (Node)
Carpeta: `tests/api` (incluye `package.json` y `run.js`).

Windows (cmd):
```bat
cd tests\api
npm install
set API_BASE_URL=http://localhost:8080/api/v1
set API_USER=admin
set API_PASS=Admin123!
npm run test
```
Resultados esperados: ✔ Products OK, ✔ Orders OK, ✔ Users OK, y “Todas las pruebas pasaron.”

## Validación End-to-End
1. Importar `db/schema.sql` y `db/evaluacion_seed.sql` en MySQL.
2. Configurar `application.properties` y arrancar backend.
3. Crear usuario ADMIN vía API (ver sección de configuración).
4. En el frontend, hacer login y validar listados/creación de órdenes.
5. Con Postman, verificar 200 y JWT válido en endpoints.

## Servicios y validaciones (detalle)
Resumen del “Reporte de Verificación de Servicios” integrado:

- Perfumes (`PerfumeService` / `PerfumeController` – `/api/v1/perfumes`)
  - CRUD completo; Validaciones: nombre requerido, precio > 0, stock >= 0, marca y categoría requeridas; Timestamps automáticos.

- Marcas (`MarcaService` / `MarcaController` – `/api/v1/marcas`)
  - CRUD completo; Validaciones: nombre y país requeridos; Timestamps.

- Categorías (`CategoriaService` / `CategoriaController` – `/api/v1/categorias`)
  - CRUD completo; Validaciones: nombre requerido; Timestamps.

- Clientes (`ClienteService` / `ClienteController` – `/api/v1/clientes`)
  - CRUD completo; Validaciones: nombre, apellido, email válido, teléfono, dirección, ciudad; Timestamps.

- Usuarios (`UsuarioService` / `AuthController` – `/api/v1/auth` para login)
  - CRUD + `loadUserByUsername`; BCrypt en persistencia; cambio de contraseña con validación; activación por defecto; Timestamps.

- Ventas (`VentaService` / `VentaController` – `/api/v1/ventas`)
  - CRUD + filtro por estado; BigDecimal para montos; estados: PENDIENTE/COMPLETADA/CANCELADA; Timestamps.

- Detalle de ventas (`DetalleVentaService` / `DetalleVentaController` – `/api/v1/detalles-ventas`)
  - CRUD + por venta; subtotal = cantidad × precioUnitario; BigDecimal; Timestamps.

- Roles (`RolService` / `RolController` – `/api/v1/roles`)
  - CRUD; Nombres permitidos: ADMIN/ VENDEDOR (case-insensitive); Normalización a mayúsculas.

Seguridad y permisos:
- GET: ADMIN o VENDEDOR (según recurso)
- POST/PUT/DELETE: principalmente ADMIN; algunas operaciones permiten VENDEDOR

Base de datos esperada (principales tablas):
- `perfume`, `marca`, `categoria`, `cliente`, `usuario`, `rol`, `venta`, `detalle_venta`.

## Archivos relevantes
- `db/schema.sql` (esquema listo)
- `db/evaluacion_seed.sql` (datos de ejemplo)
- `src/main/resources/application-xampp.properties` (guía conexión)
- `src/main/resources/application-schema.properties` (perfil export schema)
- `postman/BackendFullstack.postman_collection.json` (API tests)
- `tests/api` (pruebas Axios)

## Solución de problemas
- 401: token faltante/expirado → verificar login/interceptor.
- 403: rol sin permiso → revisar matriz de roles y guardas.
- CORS: origen distinto → agregarlo en configuración de backend.
- SQL: cadena/credenciales erróneas → revisar `application.properties`.
- 404: comprobar uso de alias `/products`, `/orders`, `/users`.
