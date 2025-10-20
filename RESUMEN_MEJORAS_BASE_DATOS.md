# Resumen de Mejoras en la Base de Datos - INCA FIT

## Feedback del Tutor Víctor

### Problemas Identificados:
1. **Facturación y pagos**: Falta una relación clara entre las facturas y los elementos facturados (membresías y clases)
2. **Membresías**: No se permite controlar la vigencia (inicio y fin de la membresía)
3. **Clases y Reservas**: No se contempla clases que tengan una duración limitada

---

## Soluciones Implementadas

### 1. ✅ Facturación y Pagos - Relación Clara con Elementos Facturados

**Modelo `DetalleFactura` actualizado:**

```java
// Campos nuevos agregados
private String tipoItem;           // "MEMBRESIA" o "CLASE"

@ManyToOne
@JoinColumn(name = "membresia_id")
private Membresia membresia;       // Relación directa con Membresía

@ManyToOne
@JoinColumn(name = "reserva_id")
private Reserva reserva;            // Relación directa con Reserva/Clase
```

**Beneficios:**
- Ahora cada detalle de factura puede vincularse directamente a una membresía o a una reserva de clase
- Se puede distinguir fácilmente qué tipo de elemento se está facturando (membresía vs clase)
- Las clases que generen un coste adicional pueden vincularse directamente a una factura mediante `reserva_id`
- Trazabilidad completa: se puede saber exactamente qué membresía o qué clase se facturó

**Ejemplo de uso:**
```java
// Facturar una membresía
DetalleFactura detalle1 = new DetalleFactura();
detalle1.setTipoItem("MEMBRESIA");
detalle1.setMembresia(membresiaMensual);
detalle1.setDescripcion("Membresía Mensual");
detalle1.setPrecioUnitario(BigDecimal.valueOf(50.00));

// Facturar una clase con coste adicional
DetalleFactura detalle2 = new DetalleFactura();
detalle2.setTipoItem("CLASE");
detalle2.setReserva(reservaDefensaPersonal);
detalle2.setDescripcion("Clase: Defensa Personal");
detalle2.setPrecioUnitario(BigDecimal.valueOf(25.00));
```

---

### 2. ✅ Membresías - Control de Vigencia

**Modelo `Socio` actualizado:**

```java
// Campos nuevos agregados
private LocalDate fechaInicioMembresia;   // Inicio de la vigencia
private LocalDate fechaFinMembresia;      // Fin de la vigencia

// Método de utilidad
public boolean isMembresiaActiva() {
    if (membresia == null || fechaInicioMembresia == null || fechaFinMembresia == null) {
        return false;
    }
    LocalDate hoy = LocalDate.now();
    return !hoy.isBefore(fechaInicioMembresia) && !hoy.isAfter(fechaFinMembresia);
}
```

**Beneficios:**
- Control preciso de la vigencia de cada membresía por socio
- Se puede verificar fácilmente si la membresía de un socio está activa
- Permite renovaciones: al renovar, simplemente se actualizan las fechas
- Facilita la gestión de vencimientos y notificaciones

**Ejemplo de uso:**
```java
// Asignar una membresía con vigencia
Socio socio = new Socio();
socio.setMembresia(membresiaMensual);
socio.setFechaInicioMembresia(LocalDate.now());
socio.setFechaFinMembresia(LocalDate.now().plusDays(30));

// Verificar si está activa
if (socio.isMembresiaActiva()) {
    // Permitir acceso al gimnasio
}
```

---

### 3. ✅ Clases con Duración Limitada y Precio Adicional

**Modelo `Clase` actualizado:**

```java
// Campos nuevos agregados
private LocalDate fechaInicio;              // Inicio de la clase (para clases limitadas)
private LocalDate fechaFin;                 // Fin de la clase (para clases limitadas)
private BigDecimal precioAdicional;         // Precio adicional por reserva

// Métodos de utilidad
public boolean isVigente() {
    if (fechaInicio == null || fechaFin == null) {
        return true; // Si no tiene fechas, es permanente
    }
    LocalDate hoy = LocalDate.now();
    return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
}

public boolean tienePrecioAdicional() {
    return precioAdicional != null && precioAdicional.compareTo(BigDecimal.ZERO) > 0;
}
```

**Beneficios:**
- **Clases permanentes**: Si `fechaInicio` y `fechaFin` son `null`, la clase es permanente (ej: Yoga, Spinning)
- **Clases limitadas**: Si tienen fechas, son cursos temporales (ej: Defensa Personal de 3 meses)
- **Clases con coste adicional**: Si `precioAdicional` tiene valor, se cobra extra por reserva
- Flexibilidad total: una clase puede ser permanente sin coste adicional, temporal con coste adicional, etc.

**Ejemplos de uso:**

```java
// Clase permanente sin coste adicional (Yoga)
Clase yoga = new Clase();
yoga.setNombre("Yoga");
yoga.setFechaInicio(null);        // Permanente
yoga.setFechaFin(null);           // Permanente
yoga.setPrecioAdicional(null);    // Sin coste adicional

// Clase limitada con coste adicional (Defensa Personal)
Clase defensaPersonal = new Clase();
defensaPersonal.setNombre("Defensa Personal");
defensaPersonal.setFechaInicio(LocalDate.now());
defensaPersonal.setFechaFin(LocalDate.now().plusDays(90));  // 3 meses
defensaPersonal.setPrecioAdicional(BigDecimal.valueOf(25.00));

// Verificar si está vigente
if (defensaPersonal.isVigente()) {
    // Permitir reservas
}

// Verificar si tiene coste adicional
if (defensaPersonal.tienePrecioAdicional()) {
    // Generar factura con el precio adicional
}
```

---

## Archivos Modificados

### Modelos (Entidades JPA)
1. **`DetalleFactura.java`**: Agregados campos para relacionar con membresías y reservas
2. **`Socio.java`**: Agregados campos de vigencia de membresía
3. **`Clase.java`**: Agregados campos de duración limitada y precio adicional

### Scripts SQL
4. **`incafit_migration_script.sql`**: Actualizado con los nuevos campos y estructura

### Configuración
5. **`DataInitializer.java`**: Actualizado para inicializar datos con los nuevos campos

---

## Migración de Base de Datos

Para aplicar los cambios en la base de datos, ejecutar el script SQL actualizado:

```sql
-- 1. Actualizar tabla SOCIOS
ALTER TABLE socios 
ADD COLUMN fecha_inicio_membresia DATE AFTER membresia_id,
ADD COLUMN fecha_fin_membresia DATE AFTER fecha_inicio_membresia;

-- 2. Actualizar tabla CLASES
ALTER TABLE clases 
ADD COLUMN fecha_inicio DATE AFTER activo,
ADD COLUMN fecha_fin DATE AFTER fecha_inicio,
ADD COLUMN precio_adicional DECIMAL(10,2) AFTER fecha_fin;

-- 3. Actualizar tabla DETALLES_FACTURA
ALTER TABLE detalles_factura 
ADD COLUMN tipo_item VARCHAR(50) AFTER subtotal,
ADD COLUMN membresia_id BIGINT AFTER tipo_item,
ADD COLUMN reserva_id BIGINT AFTER membresia_id,
ADD FOREIGN KEY (membresia_id) REFERENCES membresias(id) ON DELETE SET NULL,
ADD FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE SET NULL;
```

---

## Datos de Ejemplo Incluidos

### Membresías con Vigencia
```
- Juan Perez: Membresía Mensual (vigente por 30 días desde hoy)
- Maria Garcia: Membresía Trimestral (vigente por 90 días desde hoy)
- Pedro Lopez: Membresía Anual (vigente por 365 días desde hoy)
```

### Clases Permanentes
```
- Yoga (8:00, 60 min, sin coste adicional)
- Spinning (18:00, 45 min, sin coste adicional)
```

### Clases con Duración Limitada
```
- Defensa Personal (19:00, 90 min, vigente por 90 días, coste adicional: 25.00€)
```

### Facturas con Relaciones Directas
```
- Factura #1: Detalle vinculado a Membresía Mensual (membresia_id = 1)
- Factura #2: Detalle vinculado a Membresía Trimestral (membresia_id = 2)
```

---

## Compatibilidad con el Sistema Existente

**Los cambios son 100% compatibles con el código existente:**
- Todos los campos nuevos son opcionales (`null` permitido)
- No se eliminaron campos existentes
- Los métodos existentes siguen funcionando igual
- Se agregaron métodos de utilidad adicionales pero no se modificaron los existentes

---

## Próximos Pasos Recomendados

1. **Controladores**: Actualizar los controladores de facturación para usar las nuevas relaciones
2. **Servicios**: Implementar lógica de negocio para:
   - Generar facturas con vinculación automática a membresías/clases
   - Verificar vigencia de membresías antes de permitir reservas
   - Verificar vigencia de clases antes de permitir reservas
   - Calcular el monto total de facturas incluyendo precios adicionales de clases
3. **Vistas (Templates)**: Actualizar las plantillas HTML para:
   - Mostrar las fechas de vigencia de membresías
   - Mostrar clases limitadas con sus fechas de inicio/fin
   - Indicar qué clases tienen coste adicional
   - Mostrar en la factura qué elementos específicos se están cobrando

---

## Resumen Final

✅ **Problema 1 - Facturación**: RESUELTO con relaciones directas en `DetalleFactura`  
✅ **Problema 2 - Vigencia de Membresías**: RESUELTO con campos de fecha en `Socio`  
✅ **Problema 3 - Clases Limitadas**: RESUELTO con campos de fecha y precio en `Clase`

Todos los cambios están documentados, implementados y probados. El sistema ahora cumple con todos los requisitos mencionados por el tutor Víctor.


