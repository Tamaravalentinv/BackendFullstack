# Guía de Integración Frontend ↔ Backend (Java 21 / Spring Boot 3.2.12)
Nota: Esta guía está unificada en `README.md` (sección Evaluación). Aquí se mantiene el detalle de integración y ejemplos.

## Descripción
React + Vite consumiendo backend Spring Boot (Java 21) con JWT y control de roles (ADMIN, VENDEDOR, CLIENTE). El backend expone alias para endpoints genéricos del frontend: `/products`, `/orders`, `/users`.

## Base URL Backend
- `http://localhost:8080/api/v1`

## Variables de entorno (Frontend)
Crear `.env`:
```
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

## Autenticación
- `POST /api/v1/auth/login` body: `{ "username": "...", "password": "..." }`
- Respuesta:
```json
{
  "token": "<jwt>",
  "usuario": "nombreUsuario",
  "rol": "ADMIN|VENDEDOR|CLIENTE"
}
```
- Guardar `token`, `rol`, `usuario` en `localStorage`.
- Header requerido: `Authorization: Bearer <token>`
- No hay refresh de token (eliminar referencias a "renovacion automatica").

## Endpoints disponibles

### Auth
- `POST /auth/login`

### Productos (alias de Perfumes)
- `GET /products` (ADMIN, VENDEDOR)
- `GET /products/{id}` (ADMIN, VENDEDOR)
- `POST /products` (ADMIN)
- `PUT /products/{id}` (ADMIN)
- `DELETE /products/{id}` (ADMIN)

### Órdenes (alias de Ventas)
- `GET /orders` (ADMIN, VENDEDOR)
- `GET /orders/{id}` (ADMIN, VENDEDOR)
- `POST /orders` (ADMIN, VENDEDOR)
- `PUT /orders/{id}` (ADMIN)
- `DELETE /orders/{id}` (ADMIN)

### Usuarios
- `GET /users` (ADMIN)
- `POST /users` (ADMIN)
- `GET /users/{username}` (ADMIN)

## Matriz de permisos
- ADMIN: acceso total.
- VENDEDOR: ver productos y ordenes, crear ordenes.
- CLIENTE: acceso a tienda publica.

## CORS
El backend permite `http://localhost:3000`. Si cambias el origen, se debe ajustar la configuración en el servidor.

## Configuración de Axios (`src/services/api.js`)
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
      // window.location = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

## Servicios

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

## Utilidad de roles (`src/utils/roleValidator.js`)
```javascript
export function hasRole(roles) {
  const current = localStorage.getItem('rol');
  return !!current && roles.includes(current);
}
```

## Ejemplo de login
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

## Swagger
- UI: `http://localhost:8080/swagger-ui/index.html`
- Docs: `http://localhost:8080/v3/api-docs`

## Seguridad
- Backend usa BCrypt para contraseñas.
- Frontend guarda solo token/rol/usuario (evitar almacenar contraseña).
- En produccion considerar cookies httpOnly.

## Errores comunes
- 401: token no enviado/expirado -> revisar interceptor y `localStorage`.
- 403: rol sin permiso -> revisar matriz de permisos y guardas.
- CORS: origen distinto -> solicitar agregarlo en backend.
- 404: rutas -> confirmar uso de alias `/products`, `/orders`, `/users`.

## Checklist de integración
- [ ] `.env` con `VITE_API_BASE_URL`
- [ ] Servicios actualizados a endpoints `/auth`, `/products`, `/orders`, `/users`
- [ ] Interceptor Axios activo
- [ ] Manejo de errores de login implementado
- [ ] Redirecciones basadas en rol
- [ ] Uso de `hasRole(['ADMIN'])` en rutas protegidas

## Futuras mejoras (opcional)
 
## Ejemplos de payloads

### Crear usuario (ADMIN)
`POST /api/v1/users`
```json
{
  "username": "juan",
  "password": "secreta123",
  "rol": "VENDEDOR"
}
```

### Crear orden (ADMIN, VENDEDOR)
`POST /api/v1/orders`
```json
{
  "clienteId": 1,
  "fecha": "2025-11-28",
  "detalles": [
    { "perfumeId": 10, "cantidad": 2, "precio": 59.99 },
    { "perfumeId": 12, "cantidad": 1, "precio": 79.50 }
  ]
}
```

Nota: Los nombres de campos deben alinearse con los DTOs del backend. Si tu UI usa otros nombres, adapta el mapeo antes de enviar.

## Filtros opcionales de catálogo
Si tu frontend lista por categorías o marcas, usa:
- `GET /api/v1/categorias` para obtener categorías
- `GET /api/v1/marcas` para obtener marcas
- `GET /api/v1/products?categoriaId=...&marcaId=...` si implementas filtros por query (opcional)

## Guardas de rutas (React Router)
Ejemplo simple usando `hasRole`:
```jsx
import { hasRole } from '../utils/roleValidator';

export function AdminRoute({ children }) {
  return hasRole(['ADMIN']) ? children : <div>Acceso denegado</div>;
}

export function VendedorRoute({ children }) {
  return hasRole(['ADMIN','VENDEDOR']) ? children : <div>Acceso denegado</div>;
}
```
- Endpoint refresh JWT
- Paginacion en listados
- Manejo centralizado de errores (toasts)
- Mensajes multilanguage

## Generar schema SQL desde el backend (paso a paso)
Este proyecto incluye un perfil `schema` para exportar el esquema de tablas a un archivo SQL sin tocar tu base de datos.

1) ¿Qué hice por ti?
- Añadí `src/main/resources/application-schema.properties` con configuración para:
  - levantar H2 en memoria (solo para inicializar JPA),
  - generar `target/schema.sql` con dialecto MySQL,
  - evitar ejecutar DDL contra una BD real,
  - arrancar sin servidor web.

2) ¿Cómo generar el archivo `schema.sql`?
- Desde la raíz del proyecto (Windows CMD):
```bat
mvnw.cmd -q -DskipTests clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=schema
```
- Al iniciar, se crea `target/schema.sql`. Puedes detener con `Ctrl+C` cuando lo veas generado.

Si prefieres no ejecutar el perfil, también puedes usar el esquema listo en `db/schema.sql` (incluido en el repo) e importarlo directamente.

3) ¿Cómo usar el archivo en XAMPP (phpMyAdmin)?
- Abre `http://localhost/phpmyadmin`.
- Crea (si no existe) la BD: `backend_evaluacion`.
- Entra a esa BD → pestaña Importar → selecciona `target/schema.sql` → Ejecutar.

4) ¿Cómo saber si está bien?
- En phpMyAdmin, verifica que existen tablas como `usuario`, `rol`, `perfume`, `venta`, `detalle_venta`, `categoria`, `marca`, `cliente`.
- Si importó sin errores y aparecen las tablas, el script es válido.

5) Siguientes pasos recomendados
- Configura `src/main/resources/application.properties` con tu MySQL de XAMPP (host, puerto, usuario, contraseña).
- Arranca el backend normal: `mvnw.cmd spring-boot:run`.
- (Opcional) Inserta datos iniciales (usuarios, categorías, marcas) vía Postman o SQL.
- Abre el frontend (puerto 3000) y valida login, listado de productos y creación de órdenes.

## Importar datos de ejemplo (seed)
Incluí un script con datos de ejemplo para evaluación: `db/evaluacion_seed.sql`.

Paso a paso:
- Importa primero el esquema `target/schema.sql` (sección anterior).
- Luego importa `db/evaluacion_seed.sql` en la misma base (`backend_evaluacion`).
- Crea el usuario ADMIN vía API para tener contraseña en BCrypt:
  - `POST http://localhost:8080/api/v1/auth/login` no crea usuario; usa
  - `POST http://localhost:8080/api/v1/users` con body:
```json
{
  "username": "admin",
  "password": "Admin123!",
  "rol": "ADMIN",
  "nombre": "Administrador",
  "email": "admin@example.com"
}
```
- Luego inicia sesión en el front/back usando ese usuario.

## Colección de Postman
Incluí `postman/BackendFullstack.postman_collection.json` con los requests básicos:
- Auth: Login
- Users: Crear ADMIN
- Products: Listar
- Orders: Listar
- Users: Listar

Cómo usarla:
- Importa el archivo en Postman.
- Define la variable `baseUrl` si cambia (por defecto `http://localhost:8080/api/v1`).
- Ejecuta el login y coloca el `token` en la variable `token`.
- Lanza las demás peticiones (incluyen el header `Authorization: Bearer {{token}}`).

## Configuración ejemplo para XAMPP
Se incluye `src/main/resources/application-xampp.properties` como guía de conexión a MySQL local.
- Copia los valores a tu `application.properties` y ajusta usuario/contraseña.
- Reinicia el backend y valida que arranca sin errores de conexión.