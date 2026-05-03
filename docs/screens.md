# Guía de Pantallas

**ESCOMeter** cuenta con cuatro secciones principales accesibles desde la barra de navegación inferior.

## 1. Pantalla de Medición (MultimeterScreen)
Es la vista principal del dispositivo.
- **Selector de Modo:** Permite cambiar entre Voltaje (V), Corriente (A), Resistencia (Ω) y Potencia (W).
- **Display Digital:** Muestra el valor actual con un formato legible y colores temáticos.
- **Alertas Inteligentes:** 
    - ⚠️ **Sobrevoltaje / Sobrecorriente:** Detecta valores fuera de rango.
    - 🚨 **Riesgo Eléctrico:** Alerta visual cuando la potencia es muy alta.
    - 🔍 **Auto-rango:** La resistencia cambia automáticamente entre Ω, kΩ y MΩ.
- **Gráfica Dinámica:** Visualización de la tendencia histórica de la magnitud seleccionada.

## 2. Osciloscopio (WaveformScreen)
Visualización avanzada de la señal eléctrica.
- **Gráfica en Tiempo Real:** Muestra la forma de onda del voltaje.
- **Detección de Señal:** Utiliza algoritmos de análisis de cambios de signo y zonas planas para identificar si la señal es:
    - 🟢 **Senoidal**
    - 🔴 **Cuadrada**
    - 🔵 **Triangular**

## 3. Dispositivos USB (DeviceScreen)
Gestión de la conectividad física.
- **Escaneo:** Lista todos los dispositivos USB conectados al terminal Android.
- **Información Técnica:** Muestra el Vendor ID, Product ID e interfaces de cada dispositivo.
- **Selección:** Permite elegir qué hardware utilizar para la lectura.

## 4. Exportación (ExportScreen)
Gestión de datos históricos.
- **Resumen:** Muestra el conteo de muestras capturadas de cada magnitud.
- **Vista Previa:** Muestra el formato CSV generado antes de guardarlo.
- **Guardado:** Exporta los datos a la carpeta de Descargas del dispositivo como `escometer_datos.csv`.
