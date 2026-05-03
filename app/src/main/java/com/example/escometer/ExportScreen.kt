package com.example.escometer

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.example.escometer.viewmodel.MultimeterViewModel
import java.io.File
import android.os.Environment

@Composable
fun ExportScreen(vm: MultimeterViewModel) {

    val context = LocalContext.current

    // 🔥 DATOS TIPADOS (FIX ERRORES)
    val voltage: List<Float> by vm.voltageData.collectAsState(initial = emptyList())
    val current: List<Float> by vm.currentData.collectAsState(initial = emptyList())
    val resistance: List<Float> by vm.resistanceData.collectAsState(initial = emptyList())

    var message by remember { mutableStateOf("") }

    val csvPreview: String = remember(voltage, current, resistance) {
        buildCSV(voltage, current, resistance)
    }

    Scaffold(
        containerColor = Color(0xFF0A0F14)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔥 HEADER
            Text(
                text = "Exportar Datos",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            // 📊 INFO
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111A21)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {

                    Text("Resumen", color = Color.Gray)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Voltaje: ${voltage.size} muestras", color = Color(0xFF00E676))
                    Text("Corriente: ${current.size} muestras", color = Color(0xFFFF5252))
                    Text("Resistencia: ${resistance.size} muestras", color = Color(0xFF40C4FF))
                }
            }

            // 👀 PREVIEW
            Text(
                "Preview CSV",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111A21)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .padding(12.dp)
                ) {

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = csvPreview,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = Color(0xFFB0BEC5)
                            )
                        }
                    }
                }
            }

            // 💾 BOTÓN
            Button(
                onClick = {
                    try {
                        val file: File = exportCSV(context, csvPreview)
                        message = "Exportado en:\n${file.absolutePath}"
                    } catch (e: Exception) {
                        message = "Error al exportar archivo"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Exportar CSV")
            }

            // 📢 MENSAJE
            if (message.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B5E20).copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(12.dp),
                        color = Color.Green
                    )
                }
            }
        }
    }
}

// 🔥 GENERADOR CSV (TIPADO CORRECTO)
fun buildCSV(
    voltage: List<Float>,
    current: List<Float>,
    resistance: List<Float>
): String {

    val maxSize: Int = listOf(
        voltage.size,
        current.size,
        resistance.size
    ).maxOrNull() ?: 0

    val builder = StringBuilder()
    builder.append("Index,Voltage,Current,Resistance\n")

    for (i in 0 until maxSize) {

        val v: Any = voltage.getOrNull(i) ?: ""
        val c: Any = current.getOrNull(i) ?: ""
        val r: Any = resistance.getOrNull(i) ?: ""

        builder.append("$i,$v,$c,$r\n")
    }

    return builder.toString()
}

// 💾 EXPORTACIÓN
fun exportCSV(context: Context, csv: String): File {

    val downloadsDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    )

    if (!downloadsDir.exists()) {
        downloadsDir.mkdirs()
    }

    val file = File(downloadsDir, "escometer_datos.csv")

    file.writeText(csv)

    return file
}