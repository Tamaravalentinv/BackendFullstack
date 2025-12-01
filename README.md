# Backend Fullstack - Guía de Ejecución

## Inicio Rápido

### 1. Base de Datos
```bash
# Asegúrate que MySQL/XAMPP esté corriendo

# Abre http://localhost/phpmyadmin y:
# 1. Crea la BD: backend_evaluacion
# 2. Importa: db/schema.sql
# 3. Importa: db/evaluacion_seed.sql
```

### 2. Backend (Windows)
```bash
# Opción A: Ejecutar con script (RECOMENDADO)
cd BackendFullstack
start-backend.bat

# Opción B: Ejecutar directamente
cd BackendFullstack
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot
mvnw.cmd spring-boot:run -DskipTests
```

El backend estará en: **http://localhost:8080**

### 3. Usuarios Automáticos
El backend crea 3 usuarios automáticamente al iniciar:
- **admin / admin123** (Rol: ADMIN)
- **vendedor / vendedor123** (Rol: VENDEDOR)
- **cliente / cliente123** (Rol: CLIENTE)

### 4. Frontend (desde otra terminal)
```bash
cd FrontendFullstack
npm install  # si es primera vez
npm run dev
```

El frontend estará en: **http://localhost:3000**

## Login desde Frontend

1. Abre http://localhost:3000
2. Usa cualquiera de los usuarios arriba
3. Verás el dashboard según el rol

## Endpoints Principales

- `POST /api/v1/auth/login` - Autenticación
- `GET /api/v1/perfumes` - Listar productos
- `GET /api/v1/ordenes` - Listar órdenes
- `GET /api/v1/usuarios` - Listar usuarios (solo ADMIN)

## Documentación API

- Swagger: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs

## Solución de Problemas

**Q: El backend dice "JAVA_HOME not found"**
- Edita `start-backend.bat` y ajusta la ruta de Java según tu instalación

**Q: "Can't connect to MySQL"**
- Verifica que XAMPP está corriendo
- Verifica que la BD `backend_evaluacion` existe

**Q: "Port 8080 already in use"**
- Cierra procesos Java: `taskkill /IM java.exe /F`

**Q: Frontend no se conecta al backend**
- Verifica CORS: El backend permite localhost:3000 y localhost:3001
- Comprueba que el backend está corriendo en puerto 8080
