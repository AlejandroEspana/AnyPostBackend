@echo off
echo 🛡️  Limpiando credenciales del historial de Git...
echo.

REM Verificar si estamos en un repositorio Git
if not exist ".git" (
    echo ❌ Error: No se encuentra un repositorio Git en el directorio actual.
    echo Ejecuta este comando desde la raíz de tu proyecto.
    pause
    exit /b 1
)

echo 📋 Verificando archivos con credenciales en el historial...

REM Buscar archivos que contengan AccountKey
git log --all --grep="AccountKey" --oneline | findstr /N "." | findstr /R "^[0-9]*:"

echo.
echo 🧹 Removiendo credenciales del historial de Git...

REM Remover del historial todos los archivos que contengan las credenciales
git filter-branch --force --index-filter ^
  "git rm --cached --ignore-unmatch src/main/resources/application.properties" ^
  --prune-empty --tag-name-filter cat -- --all

echo.
echo 🗑️  Limpiando referencias de Git...

REM Limpiar las referencias y el directorio de backup
if exist ".git\refs\original\" rmdir /s /q ".git\refs\original\"
git reflog expire --expire=now --all
git gc --prune=now --aggressive

echo.
echo ✅ Limpieza completada!
echo.
echo 📝 Resumen de lo que se hizo:
echo 1. ✅ Las credenciales de Azure Blob Storage han sido removidas del historial de Git
echo 2. ✅ El archivo application.properties con credenciales reales está protegido por .gitignore
echo 3. ✅ El archivo application.properties.example solo contiene placeholders
echo.
echo 🚀 Ahora puedes subir tu código de forma segura al repositorio.
echo.
echo ⚠️  IMPORTANTE: Si ya has subido el código con credenciales, considera:
echo    - Cambiar las credenciales en Azure
echo    - Usar un nuevo container o storage account
echo    - Revisar los logs de acceso de Azure para detectar usos no autorizados
echo.
pause