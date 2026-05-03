# Guía de Usuario - ESCOMeter

Esta guía explica cómo utilizar la aplicación **ESCOMeter** para realizar mediciones eléctricas de forma segura y eficiente.

## 1. Conexión del Dispositivo
1. Conecta tu microcontrolador (Arduino, ESP32, etc.) al sensor de medición.
2. Utiliza un cable **USB OTG** para conectar el microcontrolador a tu dispositivo Android.
3. Abre **ESCOMeter**.
4. Ve a la sección **USB** en la barra inferior.
5. Presiona el botón flotante de **Actualizar (↻)**.
6. Selecciona tu dispositivo de la lista. Verás una confirmación visual cuando esté seleccionado.

## 2. Realizando Mediciones
En la pantalla de **Medición**:
- Selecciona la magnitud que deseas observar (Voltaje, Corriente, Resistencia o Potencia).
- El valor central se actualizará en tiempo real.
- **Auto-rango:** En el modo Resistencia, la app cambiará automáticamente entre Ohmios (Ω), Kilo-Ohmios (kΩ) y Mega-Ohmios (MΩ).
- **Gráfica:** Puedes observar la estabilidad de la señal en la gráfica inferior.

## 3. Uso del Osciloscopio
Navega a la sección **Señal**:
- Verás una representación visual detallada de la forma de onda del voltaje.
- La aplicación analizará automáticamente la señal y mostrará una etiqueta indicando si es **Senoidal**, **Cuadrada** o **Triangular**.
- Esto es útil para verificar generadores de funciones o salidas de inversores.

## 4. Exportación de Datos
Para guardar tus mediciones:
- Ve a la sección **Exportar**.
- Revisa el resumen de muestras capturadas.
- Presiona el botón **Exportar CSV**.
- El archivo se guardará en tu carpeta de **Descargas** con el nombre `escometer_datos.csv`. Puedes abrirlo con Excel o Google Sheets.

## 5. Alertas de Seguridad
Mantente atento a las tarjetas de alerta en la parte superior:
- **Amarillo:** Advertencia de valores bajos o inestables.
- **Naranja:** Valores altos (Sobrevoltaje o Sobrecorriente).
- **Rojo:** Errores de comunicación o riesgos eléctricos críticos.
