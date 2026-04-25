# ESCOMeter ⚡

**ESCOMeter** es una aplicación de multímetro digital para Android que permite visualizar mediciones en tiempo real a través de una conexión USB Serial. El proyecto está diseñado con una interfaz moderna utilizando **Jetpack Compose** y ofrece representación gráfica de los datos.

## ✨ Características

- **Monitoreo en Tiempo Real:** Visualización instantánea de magnitudes eléctricas.
- **Modos de Medición:**
  - 🟢 **Voltaje (V):** Medición de tensión eléctrica.
  - 🔴 **Corriente (A):** Medición de flujo de electrones.
  - 🔵 **Resistencia (Ω):** Medición de oposición al flujo, con auto-rango (Ω, kΩ, MΩ).
- **Gráficas en Vivo:** Gráfico de líneas dinámico que muestra los últimos 100 puntos de datos utilizando la librería **Vico**.
- **Comunicación Serial:** Soporte para dispositivos USB seriales (Arduino, ESP32, etc.) mediante la librería `usb-serial-for-android`.
- **Modo Simulación:** Incluye un modo de depuración que simula datos (onda senoidal) cuando no hay un dispositivo físico conectado.

## 🛠️ Tecnologías Utilizadas

- **Kotlin:** Lenguaje principal.
- **Jetpack Compose:** Para una interfaz de usuario moderna y reactiva.
- **Kotlin Coroutines & Flow:** Manejo eficiente de datos asíncronos en tiempo real.
- **ViewModel:** Arquitectura MVVM para la gestión de estados.
- **Vico:** Librería de gráficas para Compose.
- **USB Serial for Android:** Para la comunicación con hardware externo.

## 🚀 Instalación y Uso

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/SunemAyala/ESCOMeter.git
   ```
2. **Abrir en Android Studio:** Importa el proyecto y deja que Gradle sincronice las dependencias.
3. **Hardware:**
   - Si tienes un sensor conectado por Serial (USB OTG), asegúrate de que envíe los datos como texto (strings con números flotantes) terminados en salto de línea.
   - El baudrate predeterminado es **9600**.
4. **Ejecutar:** Compila y despliega en tu dispositivo Android.

