package com.example.escometer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import com.example.escometer.viewmodel.MultimeterViewModel
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlin.math.abs

@Composable
fun WaveformScreen(vm: MultimeterViewModel) {

    // 🔥 USAMOS VOLTAJE (no dataPoints)
    val data: List<Float> by vm.voltageData.collectAsState(initial = emptyList())

    val waveform = remember(data) {
        detectWaveform(data)
    }

    val color = when (waveform) {
        "Senoidal" -> Color(0xFF00E676)
        "Cuadrada" -> Color(0xFFFF5252)
        "Triangular" -> Color(0xFF40C4FF)
        else -> Color.Gray
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

            // 🔥 HEADER PRO
            Text(
                text = "Osciloscopio",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // 🧠 BADGE DE SEÑAL
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = color.copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = "Señal detectada: $waveform",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }

            // 📊 GRÁFICA PRO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111A21)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {

                    if (data.isNotEmpty()) {

                        val safeData = data.takeLast(120)

                        val model = entryModelOf(*safeData.toTypedArray())

                        Chart(
                            chart = lineChart(),
                            model = model,
                            modifier = Modifier.fillMaxSize()
                        )

                    } else {
                        Text(
                            text = "Esperando señal...",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // 📉 INFO EXTRA PRO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Muestras: ${data.size}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Último: ${data.lastOrNull()?.let { "%.2f".format(it) } ?: "--"} V",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun detectWaveform(data: List<Float>): String {

    if (data.size < 20) return "Analizando..."

    val diffs = data.zipWithNext { a, b -> b - a }

    val signChanges = diffs.zipWithNext { a, b ->
        (a > 0 && b < 0) || (a < 0 && b > 0)
    }.count { it }

    val flatZones = diffs.count { abs(it) < 0.01 }

    return when {
        flatZones > data.size * 0.3 -> "Cuadrada"
        signChanges > data.size * 0.4 -> "Senoidal"
        else -> "Triangular"
    }
}