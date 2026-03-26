@echo off
echo ========================================
echo  Git PUSH - UDP Project
echo ========================================
echo.

cd C:\Users\USER\IdeaProjects\UDP

echo Verificando estado del repositorio...
git status
echo.

set /p URL="Ingresa la URL del repositorio remoto (ej: https://github.com/usuario/UDP.git): "

echo.
echo Configurando repositorio remoto...
git remote add origin %URL%
if errorlevel 1 (
    echo Remote ya existe, actualizando...
    git remote set-url origin %URL%
)

echo.
echo Haciendo push...
git branch -M main
git push -u origin main

if errorlevel 1 (
    echo.
    echo ERROR: El push fallo. Verifica:
    echo - La URL del repositorio
    echo - Tus credenciales de Git
    echo - Conexion a internet
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Push completado exitosamente!
echo ========================================
pause
