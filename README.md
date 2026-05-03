# ESCOMeter ⚡

**ESCOMeter** es una solución integral para la medición y visualización de señales eléctricas en Android, actuando como multímetro digital y osciloscopio portátil mediante comunicación USB Serial.

## 🚀 Funcionalidades Principales
- **Multímetro Pro:** Medición de Voltaje, Corriente, Resistencia y Potencia con detección de rangos.
- **Osciloscopio:** Visualización de ondas y detección automática de tipo de señal (Senoidal, Cuadrada, Triangular).
- **Gestión USB:** Interfaz para escanear y conectar dispositivos mediante USB OTG.
- **Exportación:** Herramienta para generar archivos CSV con los datos recolectados.
- **Modo Debug:** Simulación de señales integrada para pruebas de desarrollo.

## 📂 Documentación Completa

Para información detallada sobre el funcionamiento técnico y la implementación, consulta la carpeta `/docs`:

1.  [**Arquitectura del Proyecto**](./docs/architecture.md): Detalles sobre el patrón MVVM, manejo de flujos reactivos con Coroutines y estructura de clases.
2.  [**Guía de Pantallas**](./docs/screens.md): Recorrido por la interfaz de usuario y funcionalidades de cada sección.
3.  [**Hardware y Protocolo**](./docs/hardware.md): Especificaciones del protocolo JSON y ejemplos para Arduino/ESP32.

## 🛠️ Stack Tecnológico
- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Gráficas:** Vico Charts
- **Conectividad:** USB Serial for Android
- **Arquitectura:** MVVM + Flows/Coroutines

## 📋 Requisitos de Uso
- Dispositivo con Android 8.0+ y soporte USB OTG.
- Hardware externo compatible que envíe tramas JSON por serial a 9600 bps.

---
*Proyecto desarrollado para la comunidad de ESCOM.*
