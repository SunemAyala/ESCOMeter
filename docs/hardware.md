# Conectividad y Hardware

**ESCOMeter** se comunica con dispositivos externos a través de una conexión Serial por USB (usando un adaptador OTG si es necesario).

## 🔌 Requisitos de Conexión
- **Puerto:** USB Serial (CDC, FTDI, CH34x, CP210x, etc.).
- **Baudrate:** 9600 bps (configurable en `SerialManager.kt`).
- **Formato de datos:** JSON.

## 📝 Protocolo de Datos
El dispositivo de hardware debe enviar cadenas de texto en formato JSON terminadas con un carácter de nueva línea (`\n`). 

### Ejemplo de Mensaje
```json
{"voltage": 3.45, "current": 0.125, "resistance": 1200}
```

### Parámetros Soportados
- `voltage`: Valor numérico (Float) representando los voltios.
- `current`: Valor numérico (Float) representando los amperios.
- `resistance`: Valor numérico (Float) representando los ohmios.

> **Nota:** No es necesario enviar todas las llaves en cada mensaje. El `SerialManager` procesará lo que esté disponible.

## 🛠️ Ejemplo con Arduino
```cpp
void setup() {
  Serial.begin(9600);
}

void loop() {
  float v = analogRead(A0) * (5.0 / 1023.0);
  Serial.print("{\"voltage\": ");
  Serial.print(v);
  Serial.println("}");
  delay(100);
}
```

## ⚠️ Protección de Voltaje
La aplicación tiene un límite de seguridad definido en `maxVoltage = 5.0f`. Si se recibe un valor superior, se activará una alerta visual en la interfaz para prevenir daños simulados o advertir al usuario.
