# Setup Completo - Backend + Base de Datos + Frontend

Guía paso a paso para ejecutar todo el proyecto desde cero.

---

## 1. Requisitos Previos

- **Java 21** instalado (Eclipse Adoptium jdk-21.0.9.10-hotspot)
- **MySQL** instalado y ejecutándose
- **Node.js** instalado (para el frontend)
- **Git** instalado

---

## 2. Configurar la Base de Datos

### 2.1 Iniciar MySQL (XAMPP)
1. Abre **XAMPP Control Panel**
2. Haz clic en **Start** en la fila de **MySQL**
3. Espera a que el estado cambie a **Running** (la fila se vuelve verde)

### 2.2 Crear la Base de Datos
1. Abre un navegador y ve a `http://localhost/phpmyadmin`
2. En la sección **SQL**, copia y ejecuta el contenido de:
   - Archivo: `db/schema.sql`
3. Luego ejecuta el contenido de:
   - Archivo: `db/evaluacion_seed.sql`

**Resultado:** La base de datos `evaluacion_bd` estará creada con tablas y datos de prueba.

---

## 3. Ejecutar el Backend

### 3.1 Navega a la carpeta del backend
```powershell
cd C:\Users\dafne\Downloads\vaina_fran_full\BackendFullstack
```

### 3.2 Configura JAVA_HOME (solo la primera vez o si hay errores)
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
```

### 3.3 Limpia y compila el proyecto
```powershell
.\mvnw clean compile
```

**Nota:** Esto compilará el proyecto con el flag `-parameters` necesario para que Spring resuelva los parámetros correctamente.

### 3.4 Inicia el backend
#### Opción PowerShell
```powershell
.\mvnw spring-boot:run
```

#### Opción cmd (Windows)
```bat
cd /d C:\Users\frang\Downloads\evalucion3\BackendFullstack
mvn -DskipTests spring-boot:run
```

Si necesitas establecer Java en esa terminal:
```bat
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
mvn -DskipTests spring-boot:run
```

**Resultado esperado:**
```
Started BackendEvaluacionApplication in X.XXX seconds (JVM running for X.XXX)
Tomcat started on port(s): 8080 (http)
```

**El backend estará disponible en:** `http://localhost:8080`

---

## 4. Ejecutar el Frontend

### 4.1 Navega a la carpeta del frontend
```powershell
cd C:\Users\dafne\Downloads\vaina_fran_full\frontend-evaluacion-parcial3
```

### 4.2 Instala las dependencias (solo la primera vez)
```powershell
npm install
```

### 4.3 Inicia el servidor de desarrollo
```powershell
npm run dev
```

**Resultado esperado:**
```
  VITE v7.2.4  ready in 123 ms

  ➜  Local:   http://localhost:5173/
  ➜  press h to show help
```

**El frontend estará disponible en:** `http://localhost:5173`

---

## 5. Acceder a la Aplicación

1. Abre tu navegador y ve a: `http://localhost:5173`
2. Verás una página de **Login**
3. Usa las credenciales de prueba:
   - **Usuario:** `admin`
   - **Contraseña:** `admin123`

---

## 6. Usuarios de Prueba

La base de datos incluye 3 usuarios de prueba:

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | ADMIN |
| `vendedor` | `vendedor123` | VENDEDOR |
| `cliente` | `cliente123` | CLIENTE |

---

## 7. Funcionalidades Disponibles

### Admin (admin/admin123)
- ✅ Ver lista de productos
- ✅ Crear nuevos productos
- ✅ Editar productos existentes
- ✅ Eliminar productos
- ✅ Ver marcas y categorías

### Vendedor (vendedor/vendedor123)
- ✅ Ver lista de productos
- ❌ No puede crear, editar ni eliminar

### Cliente (cliente/cliente123)
- ✅ Ver lista de productos
- ❌ No puede crear, editar ni eliminar

---

## 8. Solucionar Problemas

### Error: "JAVA_HOME not found"
**Solución:**
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
```

### Error: "Cannot find symbol: class ArrayList"
**Solución:** Recompila el proyecto:
```powershell
.\mvnw clean compile
```

### Error: "Connection refused" (Backend no responde)
**Solución:**
1. Verifica que el backend esté ejecutándose (debería ver "Tomcat started on port(s): 8080")
2. Espera a que termine de inicializar (puede tardar 30 segundos)
3. Recarga el navegador (F5)

### Error: "Cannot GET /" en frontend
**Solución:**
1. Asegúrate de estar en la carpeta `frontend-evaluacion-parcial3`
2. Ejecuta `npm run dev` (no `npm start`)
3. Accede a `http://localhost:5173` (no `localhost:5000` o `3000`)

### Error: "Database connection failed"
**Solución:**
1. Verifica que MySQL esté ejecutándose (ve a XAMPP y haz clic en **Start** para MySQL)
2. Verifica que la base de datos `evaluacion_bd` existe (chequea en phpmyadmin)
3. Verifica que los archivos `db/schema.sql` y `db/evaluacion_seed.sql` fueron ejecutados

---

## 9. Puertos Utilizados

- **Backend:** http://localhost:8080
- **Frontend:** http://localhost:5173
- **MySQL:** localhost:3306
- **phpMyAdmin:** http://localhost/phpmyadmin

---

## 10. Archivos Importantes

| Archivo | Descripción |
|---------|-------------|
| `pom.xml` | Configuración Maven del backend |
| `src/main/resources/application.properties` | Configuración de la BD |
| `src/main/java/com/example/backend_evaluacion/` | Código fuente del backend |
| `db/schema.sql` | Script de creación de tablas |
| `db/evaluacion_seed.sql` | Datos de prueba |
| `../frontend-evaluacion-parcial3/` | Código fuente del frontend |

---

## 11. Detener la Aplicación

### Detener Backend
En la terminal donde está corriendo el backend, presiona **Ctrl+C**

### Detener Frontend
En la terminal donde está corriendo el frontend, presiona **Ctrl+C**

### Detener MySQL
En **XAMPP Control Panel**, haz clic en **Stop** en la fila de MySQL

---

**¡Listo! El proyecto está completamente funcional.**

Si encuentras algún problema, revisa la sección "Solucionar Problemas" arriba.
