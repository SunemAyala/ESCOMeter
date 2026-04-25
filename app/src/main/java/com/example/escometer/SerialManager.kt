package com.example.escometer.serial

import android.content.Context
import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.math.sin

class SerialManager(private val context: Context) {

    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

    private val _dataFlow = MutableSharedFlow<Float>()
    val dataFlow = _dataFlow.asSharedFlow()

    private val DEBUG_MODE = true

    suspend fun startReading() {
        if (DEBUG_MODE) simulateData()
        else readSerial()
    }

    private suspend fun simulateData() = withContext(Dispatchers.IO) {
        var t = 0.0
        while (true) {
            val value = (2.5 + 2.5 * sin(t)).toFloat()
            _dataFlow.emit(value)
            t += 0.1
            delay(100)
        }
    }

    private suspend fun readSerial() = withContext(Dispatchers.IO) {

        val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (drivers.isEmpty()) {
            simulateData()
            return@withContext
        }

        val driver = drivers[0]
        val connection = usbManager.openDevice(driver.device) ?: run {
            simulateData()
            return@withContext
        }

        val port = driver.ports[0]
        port.open(connection)
        port.setParameters(9600, 8, 1, 0)

        val buffer = ByteArray(64)

        while (true) {
            val len = port.read(buffer, 1000)
            if (len > 0) {
                val data = String(buffer, 0, len).trim()
                data.toFloatOrNull()?.let {
                    _dataFlow.emit(it)
                }
            }
        }
    }
}