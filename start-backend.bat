@echo off
REM Establece JAVA_HOME
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot

REM Compila y ejecuta el backend
echo Iniciando backend...
echo.
call mvnw.cmd spring-boot:run -DskipTests

REM Keep the window open
pause
