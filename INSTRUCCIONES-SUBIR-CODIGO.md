# 🔐 Instrucciones para Subir Código de Forma Segura

## ✅ Estado Actual
- ✅ Credenciales de Azure Blob Storage **removidas** del `application.properties.example`
- ✅ `application.properties` local con credenciales reales **protegido** por `.gitignore`
- ✅ Scripts de limpieza del historial de Git **creados**

## 🚀 Pasos para Subir tu Código

### 1. Limpiar el Historial de Git (Recomendado)
Si ya has hecho commits con las credenciales, ejecuta el script de limpieza:

**En Windows:**
```cmd
cleanup-secrets-windows.bat
```

**En Linux/Mac:**
```bash
./cleanup-secrets.sh
```

### 2. Verificar que Git No Detecte Credenciales
```bash
git status
git add .
git commit -m "Código listo para subir - credenciales removidas"
```

### 3. Verificar antes de Push
Antes de hacer `git push`, verifica que no hay secretos:
```bash
# Verificar que application.properties no se subirá
git ls-files | grep application.properties
```

## 📁 Archivos Configurados

### ✅ Archivos que SÍ se subirán al repositorio:
- `application.properties.example` - Plantilla con placeholders
- `BlobStorageService.java` - Código del servicio (sin credenciales)
- Todo el resto del código fuente

### 🔒 Archivos que NO se subirán (protegidos por .gitignore):
- `application.properties` - Tu archivo local con credenciales reales
- Archivos de configuración local
- Archivos del IDE
- Logs y archivos temporales

## 🔧 Configuración para Desarrollo Local

Tu archivo `application.properties` local ya tiene:
- ✅ Credenciales de Azure Blob Storage (cuenta: `anypost`)
- ✅ Configuración completa de la aplicación
- ✅ Permisos para crear contenedores automáticamente

## ⚠️ Seguridad Importante

1. **Nunca subas credenciales reales** al repositorio
2. **Usa `application.properties.example`** como referencia
3. **Configura variables de entorno** en producción
4. **Considera usar Azure Key Vault** para producción

## 🛠️ Comandos Git Útiles

```bash
# Ver qué archivos Git va a subir
git status

# Ver historial sin credenciales
git log --oneline

# Ver archivos que contienen credenciales (debe estar vacío)
git grep "AccountKey="

# Limpiar cache de Git si es necesario
git rm -r --cached .
git add .
git commit -m "Limpieza de cache"
```

## 📞 Si Tienes Problemas

Si Git sigue detectando credenciales:
1. Ejecuta el script de limpieza
2. Revisa el historial con `git log --all`
3. Considera crear un nuevo repositorio desde cero
4. Contacta al soporte si necesitas ayuda adicional

---
**Fecha:** 2025-11-10  
**Estado:** ✅ Listo para subir código