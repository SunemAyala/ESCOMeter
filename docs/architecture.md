# Arquitectura del Proyecto

El proyecto **ESCOMeter** utiliza una arquitectura robusta basada en **MVVM (Model-View-ViewModel)** y programación reactiva con **Kotlin Flows**.

## 🏗️ Capas del Sistema

### 1. Gestión de Datos (`SerialManager.kt`)
Esta capa se encarga de la comunicación de bajo nivel con el hardware:
- **Comunicación USB:** Utiliza la librería `usb-serial-for-android` para gestionar la conexión física.
- **Procesamiento de Tramas:** Implementa un `StringBuilder` para reconstruir mensajes JSON completos que llegan fragmentados por el puerto serial.
- **Parsing JSON:** Utiliza `JSONObject` para extraer valores de `voltage`, `current` y `resistance`.
- **Sistema de Alertas:** Incluye una lógica de protección que emite errores al `errorFlow` si se detectan valores peligrosos (ej. sobrevoltaje > 5V).
- **Modo Simulación:** Si el hardware se desconecta o no se encuentra, el sistema conmuta automáticamente a un generador de señales sintéticas (ondas senoidales) para no interrumpir la experiencia de usuario.

### 2. Lógica de Negocio (`MultimeterViewModel.kt`)
- **Estado de UI:** Expone `StateFlow` a las pantallas de Compose.
- **Histórico de Datos:** Almacena los últimos 120 puntos de medición para alimentar las gráficas de **Vico**.
- **Cálculos en Tiempo Real:** Calcula la Potencia (W) dinámicamente (`P = V * I`).

### 3. Interfaz de Usuario (Jetpack Compose)
- **Navegación:** Un único `MainActivity` gestiona el cambio entre pantallas mediante un `Scaffold` con `NavigationBar`.
- **Reactividad:** La UI se suscribe a los flujos del ViewModel y se redibuja automáticamente ante cualquier cambio en las mediciones.

## 🔄 Flujo de Información
1. **Hardware:** Envía `{"voltage": 3.3, "current": 0.5}`.
2. **SerialManager:** Procesa el JSON y emite a `voltageFlow` y `currentFlow`.
3. **ViewModel:** Recibe los valores, los añade a la lista y notifica a la UI.
4. **UI:** Actualiza el display numérico y la gráfica en tiempo real.
