# 🎨 Ejemplos Visuales de Emails - Inca Fit

Este documento muestra cómo se ven los diferentes emails que el sistema envía.

---

## 📧 Email de Bienvenida (HTML)

### Características Visuales:
- **Color principal**: Gradiente púrpura (#667eea → #764ba2)
- **Icono**: 💪 (emoji de bienvenida)
- **Secciones**:
  1. Header con gradiente y título "¡Bienvenido a Inca Fit!"
  2. Cuadro informativo con datos del socio
  3. Lista de funcionalidades disponibles (fondo azul claro)
  4. Botón CTA "Iniciar Sesión Ahora"
  5. Consejos para empezar
  6. Footer con información de contacto

### Estructura:
```
┌─────────────────────────────────────┐
│     💪                              │
│  ¡Bienvenido a Inca Fit!           │ ← Header gradiente púrpura
└─────────────────────────────────────┘

Hola Juan Pérez,

¡Nos complace enormemente darte la bienvenida...

┌─────────────────────────────────────┐
│ 📋 Detalles de tu Cuenta           │
├─────────────────────────────────────┤
│ Nombre: Juan Pérez                  │
│ Email: juan@ejemplo.com             │
│ DNI: 12345678X                      │
│ Membresía: Premium                  │
│ Fecha de Registro: 21/10/2024       │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ 🎯 ¿Qué puedes hacer ahora?        │
├─────────────────────────────────────┤
│ ✓ Reservar Clases                   │
│ ✓ Ver tu Historial                  │
│ ✓ Gestionar tu Perfil               │
│ ✓ Consultar Facturas                │
│ ✓ Acceso 24/7                       │
└─────────────────────────────────────┘

    ┌──────────────────────┐
    │ Iniciar Sesión Ahora │ ← Botón CTA
    └──────────────────────┘

Consejos para empezar:
• Llega 10-15 minutos antes
• Trae botella de agua
• No olvides calentar
...

Footer: Inca Fit | incafit.soporte@gmail.com
```

---

## ✅ Email de Confirmación de Reserva (HTML)

### Características Visuales:
- **Color principal**: Gradiente verde (#28a745 → #20c997)
- **Icono**: ✅ (check de confirmación)
- **Secciones**:
  1. Header verde con "¡Reserva Confirmada!"
  2. Tarjeta destacada con detalles de la reserva (gradiente púrpura)
  3. Recordatorios importantes (fondo amarillo)
  4. Lista de qué traer (fondo azul claro)
  5. Política de cancelación (fondo rojo claro)
  6. Footer

### Estructura:
```
┌─────────────────────────────────────┐
│     ✅                              │
│  ¡Reserva Confirmada!               │ ← Header gradiente verde
└─────────────────────────────────────┘

Hola Juan Pérez,

Tu reserva ha sido confirmada...

┌─────────────────────────────────────┐
│     Yoga Matutino                   │ ← Gradiente púrpura
├─────────────────────────────────────┤
│ 📅 Fecha:         25/10/2024        │
│ 🕐 Hora:          10:00             │
│ 👤 Participante:  Juan Pérez        │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ ⏰ Recordatorio Importante          │ ← Fondo amarillo
├─────────────────────────────────────┤
│ Llega 10-15 minutos antes para:     │
│ • Cambiarte                          │
│ • Calentar                           │
│ • Consultar al instructor            │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ 📝 ¿Qué debes traer?                │ ← Fondo azul claro
├─────────────────────────────────────┤
│ ✓ Botella de agua                   │
│ ✓ Toalla personal                   │
│ ✓ Calzado deportivo                 │
│ ✓ Ropa cómoda                       │
│ ✓ Candado (opcional)                │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ ❌ Política de Cancelación          │ ← Fondo rojo claro
├─────────────────────────────────────┤
│ • Al menos 2 horas de anticipación  │
│ • Desde tu panel de reservas        │
│ • Las cancelaciones tardías pueden  │
│   afectar tu historial              │
└─────────────────────────────────────┘

    ¡Nos vemos en la clase! 🏋️‍♀️

Footer: Inca Fit | incafit.soporte@gmail.com
```

---

## 🚫 Email de Cancelación de Reserva (HTML)

### Características Visuales:
- **Color principal**: Gradiente rojo (#dc3545 → #c82333)
- **Icono**: 🚫 (señal de cancelación)
- **Secciones**:
  1. Header rojo con "Reserva Cancelada"
  2. Cuadro con detalles de reserva cancelada
  3. Sugerencias de próximos pasos (fondo azul claro)
  4. Botones para explorar más clases
  5. Footer

### Estructura:
```
┌─────────────────────────────────────┐
│     🚫                              │
│  Reserva Cancelada                  │ ← Header gradiente rojo
└─────────────────────────────────────┘

Hola Juan Pérez,

Tu reserva ha sido cancelada exitosamente.

┌─────────────────────────────────────┐
│ Detalles de la Reserva Cancelada    │ ← Borde rojo
├─────────────────────────────────────┤
│ Clase: CrossFit Avanzado            │
│ Fecha: 26/10/2024                   │
│ Hora: 18:00                         │
│ Cancelado el: 21/10/2024 09:00      │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ 🔄 ¿Qué hacer ahora?                │ ← Fondo azul claro
├─────────────────────────────────────┤
│ • Explora otras clases disponibles  │
│ • Reserva en otro horario           │
│ • Prueba nuevas disciplinas         │
│ • Consulta promociones              │
└─────────────────────────────────────┘

┌──────────────────┐  ┌──────────────┐
│ Ver Clases       │  │ Mis Reservas │ ← Botones
└──────────────────┘  └──────────────┘

ℹ️ Este espacio ahora está disponible...

Footer: Inca Fit | incafit.soporte@gmail.com
```

---

## 🧾 Email de Factura (HTML)

### Características Visuales:
- **Color principal**: Gradiente púrpura (#667eea → #764ba2)
- **Diseño**: Formato de factura profesional
- **Secciones**:
  1. Header con logo y número de factura
  2. Datos del gimnasio y del cliente
  3. Estado del pago (badge verde o amarillo)
  4. Tabla de items
  5. Totales (subtotal, IVA, total)
  6. Botón de pago (si está pendiente)
  7. Footer

### Estructura:
```
┌─────────────────────────────────────┐
│ 💪 INCA FIT          FACTURA #001   │
│ Tu Gimnasio...       Fecha: 21/10   │
└─────────────────────────────────────┘

┌────────────────────┐ ┌──────────────┐
│ 📍 Gimnasio        │ │ 👤 Cliente   │
├────────────────────┤ ├──────────────┤
│ Inca Fit S.L.      │ │ Juan Pérez   │
│ CIF: B-12345678    │ │ DNI: 12345   │
│ Av. Principal, 123 │ │ juan@ej.com  │
│ 28001 Madrid       │ │ Premium      │
└────────────────────┘ └──────────────┘

        ┌──────────────────┐
        │  ✓ PAGADA        │ ← Badge verde/amarillo
        └──────────────────┘

┌─────────────────────────────────────┐
│ Descripción │ Cant │ P.Unit │ Total │
├─────────────────────────────────────┤
│ Membresía   │  1   │ 50.00€ │ 50€   │
│ Premium     │      │        │       │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│                   Subtotal:  50.00€ │
│                   IVA (21%): 10.50€ │
│                   ──────────────────│
│                   TOTAL:     60.50€ │ ← Grande y destacado
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ 📝 Información Importante           │
├─────────────────────────────────────┤
│ • Factura electrónica válida        │
│ • Descarga PDF desde tu panel       │
│ • Contacta para consultas           │
└─────────────────────────────────────┘

   ┌──────────────┐
   │ Pagar Ahora  │ ← Si está pendiente
   └──────────────┘

Footer: Gracias por confiar en Inca Fit
```

---

## 🔔 Email de Recordatorio de Membresía (Texto)

### Características:
- **Formato**: Texto plano (sin HTML)
- **Cuando se envía**: Automáticamente a las 9:00 AM
- **Disparadores**: 7, 3 o 1 días antes del vencimiento

### Estructura:
```
Asunto: Recordatorio: Tu Membresía Vence Pronto - Inca Fit

Hola Juan Pérez,

Te recordamos que tu membresía 'Premium' vencerá en 7 días.

Fecha de vencimiento: 28/10/2024

Para evitar interrupciones en tu entrenamiento, te recomendamos 
renovar tu membresía antes de la fecha de vencimiento.

Puedes renovar fácilmente desde tu panel de usuario o 
contactándonos directamente.

Beneficios de renovar ahora:
- Sin interrupciones en tu acceso al gimnasio
- Mantén tu progreso y rutinas
- Posibles descuentos por renovación anticipada

Si ya has renovado, ignora este mensaje.

Gracias por ser parte de Inca Fit.

Saludos cordiales,
El equipo de Inca Fit
```

---

## 📱 Responsive Design

Todos los templates HTML están optimizados para:

### 📧 Clientes de Email Desktop
- ✅ Gmail (web)
- ✅ Outlook (web)
- ✅ Apple Mail
- ✅ Thunderbird

### 📱 Clientes de Email Mobile
- ✅ Gmail app (Android/iOS)
- ✅ Outlook app
- ✅ Apple Mail (iPhone/iPad)
- ✅ Samsung Email

### 🎨 Características Responsive
- Ancho máximo: 600px (estándar de email)
- Fuentes legibles: 14-16px
- Botones grandes (fácil clic en móvil)
- Espaciado adecuado
- Colores con buen contraste

---

## 🎨 Paleta de Colores Utilizada

### Colores Principales
```
Púrpura Principal:  #667eea → #764ba2 (gradiente)
Verde Confirmación: #28a745 → #20c997 (gradiente)
Rojo Cancelación:   #dc3545 → #c82333 (gradiente)
```

### Colores de Fondo
```
Fondo Blanco:       #ffffff
Fondo Gris Claro:   #f8f9fa
Fondo Azul Info:    #e7f3ff
Fondo Amarillo:     #fff3cd
Fondo Rojo Claro:   #f8d7da
Fondo Verde Claro:  #d4edda
```

### Colores de Texto
```
Texto Principal:    #333333
Texto Secundario:   #666666
Texto Claro:        #999999
```

---

## 📊 Comparación: Texto vs HTML

| Característica        | Texto Plano | HTML        |
|-----------------------|-------------|-------------|
| Diseño atractivo      | ❌          | ✅          |
| Compatibilidad        | ✅ 100%     | ✅ 95%      |
| Tamaño del archivo    | ✅ Pequeño  | ⚠️ Mayor   |
| Tiempo de carga       | ✅ Rápido   | ✅ Rápido   |
| Branding              | ❌          | ✅          |
| Call-to-Action        | ❌          | ✅          |
| Personalización       | ⚠️ Limitada | ✅ Total    |
| Filtros spam          | ✅ Mejor    | ⚠️ Mayor    |

### Recomendación:
- **Texto plano**: Para emails críticos (recuperación contraseña)
- **HTML**: Para emails de marketing y notificaciones (bienvenida, confirmaciones)

---

## 🧪 Vista Previa en Panel de Pruebas

Accede a `/admin/email-test` para:

1. ✅ Ver lista de todos los socios
2. ✅ Seleccionar destinatario
3. ✅ Elegir tipo de email (bienvenida, confirmación, etc.)
4. ✅ Elegir formato (HTML o texto)
5. ✅ Enviar email de prueba
6. ✅ Ver confirmación de envío

### Interfaz del Panel:
```
┌────────────────────────────────────────────┐
│ 🧪 Panel de Pruebas de Email              │
├────────────────────────────────────────────┤
│                                            │
│ Seleccionar Socio:                         │
│ [Dropdown: Juan Pérez (juan@ejemplo.com)]  │
│                                            │
│ ┌────────────────────────────────────────┐ │
│ │ ✉️ Email de Bienvenida                │ │
│ ├────────────────────────────────────────┤ │
│ │ [Enviar HTML] [Enviar Texto]           │ │
│ └────────────────────────────────────────┘ │
│                                            │
│ ┌────────────────────────────────────────┐ │
│ │ ✅ Confirmación de Reserva            │ │
│ ├────────────────────────────────────────┤ │
│ │ [Enviar HTML] [Enviar Texto]           │ │
│ └────────────────────────────────────────┘ │
│                                            │
│ ... (más opciones)                         │
└────────────────────────────────────────────┘
```

---

## 📸 Capturas Conceptuales

### Email de Bienvenida
```
┌──────────────────────────────────────┐
│ [Gradiente Púrpura con emoji 💪]    │
│                                      │
│  Contenido profesional y limpio      │
│  con secciones bien diferenciadas    │
│                                      │
│  [Botón destacado para acción]       │
│                                      │
│  Footer con info de contacto         │
└──────────────────────────────────────┘
```

### Email de Confirmación
```
┌──────────────────────────────────────┐
│ [Gradiente Verde con check ✅]      │
│                                      │
│  Tarjeta destacada con detalles      │
│  de la reserva en púrpura            │
│                                      │
│  Información útil en cajas de        │
│  colores (amarillo, azul, rojo)      │
│                                      │
│  Footer                              │
└──────────────────────────────────────┘
```

---

## ✨ Conclusión

Los templates HTML de Inca Fit ofrecen:

- ✅ **Diseño profesional** que refleja la marca
- ✅ **Colores coherentes** con identidad visual
- ✅ **Información clara** y bien organizada
- ✅ **Call-to-actions** destacados
- ✅ **Responsive** para todos los dispositivos
- ✅ **Fáciles de mantener** (Thymeleaf templates)

**¡Los emails se verán geniales en cualquier cliente de correo!** 🎉

