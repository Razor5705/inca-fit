# INSTRUCCIONES PARA GENERAR EL DIAGRAMA EER - INCA FIT

## 🎯 Objetivo
Crear un diagrama EER (Enhanced Entity Relationship) de MySQL para el sistema Inca Fit que puedas enviar a tu tutor.

---

## 📋 PASOS PARA GENERAR EL DIAGRAMA EER

### Método 1: Desde MySQL Workbench (Recomendado)

#### Paso 1: Preparar la Base de Datos
1. **Abrir MySQL Workbench**
2. **Conectar a tu servidor MySQL** (puerto 3306)
3. **Ejecutar el script completo:**
   ```sql
   -- Ejecutar el archivo: incafit_database_script.sql
   -- Esto creará la base de datos y todas las tablas
   ```

#### Paso 2: Generar el Diagrama EER
1. **Ir al menú:** `Database` → `Reverse Engineer...`
2. **Seleccionar conexión:** Tu conexión MySQL
3. **Seleccionar esquema:** `incafit_db`
4. **Seleccionar tablas:** Marcar todas las tablas:
   - ✅ membresias
   - ✅ instructores
   - ✅ clases
   - ✅ socios
   - ✅ reservas
   - ✅ facturas
   - ✅ detalles_factura
   - ✅ pagos
   - ✅ asistencias
5. **Hacer clic en:** `Execute`
6. **Revisar el diagrama generado**

#### Paso 3: Personalizar el Diagrama
1. **Organizar las tablas** arrastrándolas para mejor visualización
2. **Ajustar el zoom** para ver todas las relaciones
3. **Verificar que todas las relaciones** estén correctamente mostradas
4. **Añadir título:** "Sistema Inca Fit - Diagrama EER"

#### Paso 4: Exportar el Diagrama
1. **Ir al menú:** `File` → `Export` → `Export as PNG/PDF`
2. **Seleccionar resolución alta** (300 DPI recomendado)
3. **Guardar como:** `INCA_FIT_DIAGRAMA_EER.png`

---

### Método 2: Usando el Script EER Específico

#### Paso 1: Ejecutar Script EER
```sql
-- Ejecutar el archivo: incafit_eer_diagram.sql
-- Este archivo contiene comentarios detallados sobre la estructura EER
```

#### Paso 2: Generar Diagrama
1. **Ir a:** `Database` → `Reverse Engineer...`
2. **Seguir los mismos pasos** del Método 1

---

## 🔍 VERIFICACIÓN DEL DIAGRAMA

### Elementos que DEBE incluir el diagrama:

#### ✅ Entidades (9 tablas)
- [ ] MEMBRESIAS
- [ ] INSTRUCTORES  
- [ ] CLASES
- [ ] SOCIOS
- [ ] RESERVAS
- [ ] FACTURAS
- [ ] DETALLES_FACTURA
- [ ] PAGOS
- [ ] ASISTENCIAS

#### ✅ Relaciones (10 relaciones)
- [ ] MEMBRESIAS (1) ←→ (N) SOCIOS
- [ ] INSTRUCTORES (1) ←→ (N) CLASES
- [ ] SOCIOS (1) ←→ (N) RESERVAS
- [ ] CLASES (1) ←→ (N) RESERVAS
- [ ] SOCIOS (1) ←→ (N) FACTURAS
- [ ] FACTURAS (1) ←→ (N) DETALLES_FACTURA
- [ ] FACTURAS (1) ←→ (N) PAGOS
- [ ] SOCIOS (1) ←→ (N) ASISTENCIAS
- [ ] CLASES (1) ←→ (N) ASISTENCIAS
- [ ] RESERVAS (1) ←→ (1) ASISTENCIAS

#### ✅ Atributos Clave
- [ ] Claves Primarias (PK) marcadas
- [ ] Claves Foráneas (FK) marcadas
- [ ] Claves Únicas (UK) marcadas
- [ ] Tipos de datos visibles

---

## 📊 CONSULTAS DE VALIDACIÓN

### Ejecutar estas consultas para verificar:

```sql
-- 1. Verificar que todas las tablas existen
SHOW TABLES;

-- 2. Verificar relaciones (claves foráneas)
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = 'incafit_db'
ORDER BY TABLE_NAME;

-- 3. Verificar estructura de tablas
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_KEY
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'incafit_db'
ORDER BY TABLE_NAME, ORDINAL_POSITION;

-- 4. Verificar datos de prueba
SELECT 'MEMBRESÍAS' as tabla, COUNT(*) as registros FROM membresias
UNION ALL
SELECT 'INSTRUCTORES', COUNT(*) FROM instructores
UNION ALL
SELECT 'CLASES', COUNT(*) FROM clases
UNION ALL
SELECT 'SOCIOS', COUNT(*) FROM socios
UNION ALL
SELECT 'RESERVAS', COUNT(*) FROM reservas
UNION ALL
SELECT 'FACTURAS', COUNT(*) FROM facturas
UNION ALL
SELECT 'DETALLES_FACTURA', COUNT(*) FROM detalles_factura
UNION ALL
SELECT 'PAGOS', COUNT(*) FROM pagos
UNION ALL
SELECT 'ASISTENCIAS', COUNT(*) FROM asistencias;
```

---

## 🎨 MEJORAS VISUALES DEL DIAGRAMA

### Consejos para un diagrama profesional:

1. **Organización:**
   - Colocar entidades principales en el centro
   - Agrupar entidades relacionadas
   - Usar colores diferentes para diferentes tipos de entidades

2. **Etiquetas:**
   - Añadir título: "Sistema Inca Fit - Diagrama EER"
   - Incluir tu nombre y fecha
   - Añadir nota: "TFG - Campus FP"

3. **Relaciones:**
   - Verificar que todas las líneas de relación estén visibles
   - Añadir etiquetas de cardinalidad si es posible
   - Usar colores diferentes para diferentes tipos de relaciones

4. **Formato:**
   - Exportar en alta resolución (300 DPI)
   - Usar formato PNG o PDF
   - Tamaño mínimo: 1920x1080 píxeles

---

## 📁 ARCHIVOS GENERADOS

Después de completar el proceso, deberías tener:

1. **`INCA_FIT_DIAGRAMA_EER.png`** - Diagrama visual principal
2. **`incafit_eer_diagram.sql`** - Script SQL con estructura EER
3. **`INCA_FIT_DIAGRAMA_EER.md`** - Documentación detallada
4. **`INSTRUCCIONES_DIAGRAMA_EER.md`** - Este archivo de instrucciones

---

## 🚨 SOLUCIÓN DE PROBLEMAS

### Problema: No se ven las relaciones
**Solución:** Verificar que las claves foráneas estén correctamente definidas en el script SQL.

### Problema: Diagrama muy pequeño
**Solución:** Ajustar el zoom y exportar en mayor resolución.

### Problema: Tablas desordenadas
**Solución:** Arrastrar las tablas para reorganizarlas manualmente en MySQL Workbench.

### Problema: Faltan datos de prueba
**Solución:** Ejecutar la sección "INSERCIÓN DE DATOS DE PRUEBA" del script SQL.

---

## ✅ CHECKLIST FINAL

Antes de enviar al tutor, verificar:

- [ ] ✅ Diagrama EER generado correctamente
- [ ] ✅ Todas las 9 entidades visibles
- [ ] ✅ Todas las 10 relaciones mostradas
- [ ] ✅ Claves primarias y foráneas marcadas
- [ ] ✅ Diagrama exportado en alta calidad
- [ ] ✅ Título y metadatos incluidos
- [ ] ✅ Consultas de validación ejecutadas
- [ ] ✅ Datos de prueba cargados
- [ ] ✅ Documentación completa incluida

---

## 📞 CONTACTO

Si tienes problemas generando el diagrama EER, verifica:

1. **Conexión a MySQL:** ¿Está funcionando?
2. **Script SQL:** ¿Se ejecutó sin errores?
3. **Permisos:** ¿Tienes permisos para crear bases de datos?
4. **Versión MySQL Workbench:** ¿Es compatible?

¡Con estos pasos deberías tener un diagrama EER profesional listo para enviar a tu tutor! 🎯
