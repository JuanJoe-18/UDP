@echo off
echo ========================================
echo  Git Commands para UDP Project
echo ========================================
echo.

cd C:\Users\USER\IdeaProjects\UDP

echo [1/4] Inicializando repositorio Git...
git init
if errorlevel 1 goto error

echo.
echo [2/4] Agregando archivos...
git add .
if errorlevel 1 goto error

echo.
echo [3/4] Creando commit...
git commit -m "feat: Implement UDP communication with Sender, Receiver and Main classes" -m "- Implement UDPConnection singleton class with Thread support" -m "- Create Receiver class for receiving UDP messages" -m "- Create Sender class for sending UDP messages" -m "- Create Main class with complete interface" -m "- Configure Maven for executable JAR generation" -m "- Add comprehensive README with usage instructions" -m "- Configure .gitignore for Maven project" -m "" -m "Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"
if errorlevel 1 goto error

echo.
echo [4/4] Estado del repositorio:
git status

echo.
echo ========================================
echo  Commit creado exitosamente!
echo ========================================
echo.
echo Para hacer PUSH, necesitas:
echo 1. Crear un repositorio en GitHub
echo 2. Ejecutar: git remote add origin [URL]
echo 3. Ejecutar: git push -u origin main
echo.
pause
goto end

:error
echo.
echo ERROR: Algo salio mal. Verifica que Git este instalado.
pause

:end
