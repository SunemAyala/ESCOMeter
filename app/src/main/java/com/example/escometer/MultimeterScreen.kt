package com.example.escometer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.escometer.viewmodel.MultimeterViewModel
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.util.Locale

enum class Mode(val label: String, val unit: String, val color: Color) {
    VOLTAGE("Voltaje", "V", Color(0xFF00E676)),
    CURRENT("Corriente", "A", Color(0xFFFF5252)),
    RESISTANCE("Resistencia", "Ω", Color(0xFF40C4FF)),
    POWER("Potencia", "W", Color(0xFFFFC107))
}

@Composable
fun MultimeterScreen(viewModel: MultimeterViewModel) {

    val voltage: List<Float> by viewModel.voltageData.collectAsState(initial = emptyList())
    val current: List<Float> by viewModel.currentData.collectAsState(initial = emptyList())
    val resistance: List<Float> by viewModel.resistanceData.collectAsState(initial = emptyList())
    val power: List<Float> by viewModel.powerData.collectAsState(initial = emptyList())

    val errorMessage: String? by viewModel.errorFlow.collectAsState(initial = null)

    var mode by remember { mutableStateOf(Mode.VOLTAGE) }

    val dataPoints: List<Float> = when (mode) {
        Mode.VOLTAGE -> voltage
        Mode.CURRENT -> current
        Mode.RESISTANCE -> resistance
        Mode.POWER -> power
    }

    val currentValue = dataPoints.lastOrNull() ?: 0f
    val color = mode.color

    // 🔥 ALERTAS INTELIGENTES
    val alert = remember(currentValue, mode) {
        when (mode) {
            Mode.VOLTAGE -> {
                when {
                    currentValue > 5f -> "⚠️ Sobrevoltaje"
                    currentValue < 1f -> "⚠️ Voltaje muy bajo"
                    else -> null
                }
            }

            Mode.CURRENT -> {
                when {
                    currentValue > 2f -> "⚠️ Sobrecorriente"
                    currentValue < 0.1f -> "⚠️ Corriente muy baja"
                    else -> null
                }
            }

            Mode.RESISTANCE -> {
                when {
                    currentValue > 1_000_000 -> "⚠️ Resistencia muy alta"
                    currentValue < 10 -> "⚠️ Posible corto circuito"
                    else -> null
                }
            }

            Mode.POWER -> {
                when {
                    currentValue > 10f -> "⚠️ Alta potencia"
                    currentValue > 20f -> "🚨 Riesgo eléctrico"
                    else -> null
                }
            }
        }
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

            // HEADER
            Text(
                text = "ESCOMeter",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // SELECTOR
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Mode.entries.forEach {
                    ModeButton(
                        text = it.label,
                        selected = it == mode,
                        color = it.color,
                        onClick = { mode = it }
                    )
                }
            }

            // ⚠️ ALERTA DE SISTEMA (errores)
            errorMessage?.let {
                AlertCard(it, Color.Red)
            }

            // 🚨 ALERTA DE MEDICIÓN
            alert?.let {
                AlertCard(it, Color(0xFFFFA000))
            }

            // DISPLAY
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111A21)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(mode.label, color = Color.Gray)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatValue(currentValue, mode),
                        style = MaterialTheme.typography.displayLarge,
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // GRÁFICA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111A21)
                )
            ) {
                Box(Modifier.padding(16.dp)) {

                    if (dataPoints.isNotEmpty()) {

                        val model = entryModelOf(
                            *dataPoints.takeLast(120).toTypedArray()
                        )

                        Chart(
                            chart = lineChart(),
                            model = model,
                            modifier = Modifier.fillMaxSize()
                        )

                    } else {
                        Text(
                            "Esperando datos...",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

// 🔥 COMPONENTE DE ALERTA
@Composable
fun AlertCard(text: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            color = color,
            modifier = Modifier.padding(12.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

// BOTÓN
@Composable
fun ModeButton(
    text: String,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (selected) color else Color(0xFF1C2A33),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.Black else Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

// FORMATO
fun formatValue(value: Float, mode: Mode): String {
    return when (mode) {
        Mode.VOLTAGE -> String.format(Locale.US, "%.2f V", value)
        Mode.CURRENT -> String.format(Locale.US, "%.3f A", value)
        Mode.POWER -> String.format(Locale.US, "%.2f W", value)
        Mode.RESISTANCE -> {
            when {
                value > 1_000_000 -> String.format(Locale.US, "%.2f MΩ", value / 1_000_000)
                value > 1_000 -> String.format(Locale.US, "%.2f kΩ", value / 1_000)
                else -> String.format(Locale.US, "%.2f Ω", value)
            }
        }
    }
}