package com.example.escometer.serial

import android.content.Context
import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.driver.UsbSerialPort
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import org.json.JSONObject
import kotlin.math.sin

class SerialManager(context: Context) {

    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

    // 🔥 BUFFER para evitar bloqueos
    private val _voltageFlow = MutableSharedFlow<Float>(extraBufferCapacity = 64)
    val voltageFlow = _voltageFlow.asSharedFlow()

    private val _currentFlow = MutableSharedFlow<Float>(extraBufferCapacity = 64)
    val currentFlow = _currentFlow.asSharedFlow()

    private val _resistanceFlow = MutableSharedFlow<Float>(extraBufferCapacity = 64)
    val resistanceFlow = _resistanceFlow.asSharedFlow()

    private val _errorFlow = MutableSharedFlow<String>(extraBufferCapacity = 16)
    val errorFlow = _errorFlow.asSharedFlow()

    private val maxVoltage = 5.0f

    suspend fun startReading() = coroutineScope {
        try {
            readSerial()
        } catch (e: Exception) {
            _errorFlow.emit("⚠️ Error USB → modo simulación")
            simulateData()
        }
    }

    // 🔌 LECTURA REAL
    private suspend fun readSerial() = withContext(Dispatchers.IO) {

        val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)

        if (drivers.isEmpty()) {
            _errorFlow.emit("No hay dispositivos USB")
            simulateData()
            return@withContext
        }

        val driver = drivers.first()

        val connection = usbManager.openDevice(driver.device)
            ?: throw Exception("No se pudo abrir USB")

        val port = driver.ports.first()

        port.open(connection)

        port.setParameters(
            9600,
            8,
            UsbSerialPort.STOPBITS_1,
            UsbSerialPort.PARITY_NONE
        )

        val buffer = ByteArray(128)
        val builder = StringBuilder()

        while (isActive) {

            val len = port.read(buffer, 1000)

            if (len > 0) {

                val chunk = String(buffer, 0, len)
                builder.append(chunk)

                if (chunk.contains("\n")) {

                    val lines = builder.split("\n")

                    for (i in 0 until lines.size - 1) {
                        processJson(lines[i].trim())
                    }

                    builder.clear()
                    builder.append(lines.last())
                }

            } else {
                _errorFlow.emit("Sin datos → simulación")
                simulateData()
                return@withContext
            }
        }
    }

    // 🧠 PROCESAMIENTO JSON
    private suspend fun processJson(jsonString: String) {

        try {
            val json = JSONObject(jsonString)

            var valid = false

            json.optDouble("voltage", Double.NaN).takeIf { !it.isNaN() }?.let {
                handleVoltage(it.toFloat())
                valid = true
            }

            json.optDouble("current", Double.NaN).takeIf { !it.isNaN() }?.let {
                _currentFlow.emit(it.toFloat())
                valid = true
            }

            json.optDouble("resistance", Double.NaN).takeIf { !it.isNaN() }?.let {
                _resistanceFlow.emit(it.toFloat())
                valid = true
            }

            if (!valid) {
                _errorFlow.emit("JSON sin datos útiles")
            }

        } catch (e: Exception) {
            _errorFlow.emit("JSON inválido")
        }
    }

    // ⚠️ PROTECCIÓN
    private suspend fun handleVoltage(value: Float) {
        if (value > maxVoltage) {
            _errorFlow.emit("⚠️ Sobrevoltaje: ${"%.2f".format(value)} V")
        } else {
            _voltageFlow.emit(value)
        }
    }

    // 🧪 SIMULACIÓN SEGURA
    private suspend fun simulateData() = withContext(Dispatchers.IO) {

        var t = 0.0

        while (isActive) {

            val voltage = (0 + 10 * sin(t)).toFloat()
            val current = (0 + 10 * sin(t * 2)).toFloat()
            val resistance = (100 + 50 * sin(t)).toFloat()

            _voltageFlow.emit(voltage)
            _currentFlow.emit(current)
            _resistanceFlow.emit(resistance)

            t += 0.15
            delay(1000)
        }
    }
}