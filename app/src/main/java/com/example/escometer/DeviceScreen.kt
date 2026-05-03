package com.example.escometer

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DeviceScreen() {

    val context = LocalContext.current
    val usbManager = context.getSystemService(UsbManager::class.java)

    // 🔥 LISTA REACTIVA
    var devices by remember { mutableStateOf<List<UsbDevice>>(emptyList()) }

    var selectedDevice by remember { mutableStateOf<String?>(null) }

    // 🔄 FUNCIÓN PARA ESCANEAR
    fun refreshDevices() {
        devices = usbManager.deviceList.values.toList()
    }

    // 🚀 PRIMERA CARGA
    LaunchedEffect(Unit) {
        refreshDevices()
    }

    Scaffold(
        containerColor = Color(0xFF0A0F14),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { refreshDevices() },
                containerColor = Color(0xFF00E676)
            ) {
                Text("↻", color = Color.Black)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 🔥 HEADER
            Text(
                text = "Dispositivos USB",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Escanea y selecciona un dispositivo",
                color = Color.Gray
            )

            // 📊 CONTADOR
            Text(
                text = "Dispositivos encontrados: ${devices.size}",
                color = Color(0xFF00E676),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (devices.isEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111A21)
                    )
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "No hay dispositivos USB conectados",
                            color = Color.Gray
                        )
                    }
                }

            } else {

                // 🔌 LISTA EFICIENTE
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(devices) { device ->

                        val isSelected = selectedDevice == device.deviceName

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    Color(0xFF00E676).copy(alpha = 0.2f)
                                else
                                    Color(0xFF111A21)
                            ),
                            elevation = CardDefaults.cardElevation(6.dp),
                            onClick = {
                                selectedDevice = device.deviceName
                            }
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {

                                // 🔵 NOMBRE
                                Text(
                                    text = device.deviceName,
                                    color = if (isSelected)
                                        Color(0xFF00E676)
                                    else Color.White,
                                    fontWeight = FontWeight.Bold
                                )

                                // 🔧 INFO
                                Text(
                                    text = "Vendor ID: ${device.vendorId}",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = "Product ID: ${device.productId}",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = "Interfaces: ${device.interfaceCount}",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                // ✅ SELECCIÓN
                                if (isSelected) {
                                    Text(
                                        text = "✔ Dispositivo seleccionado",
                                        color = Color(0xFF00E676),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}