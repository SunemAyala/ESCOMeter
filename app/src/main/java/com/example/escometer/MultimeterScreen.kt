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

enum class Mode(val label: String, val unit: String, val color: Color) {
    VOLTAGE("Voltaje", "V", Color(0xFF00E676)),
    CURRENT("Corriente", "A", Color(0xFFFF5252)),
    RESISTANCE("Resistencia", "Ω", Color(0xFF40C4FF))
}

@Composable
fun MultimeterScreen(viewModel: MultimeterViewModel) {

    val dataPoints by viewModel.dataPoints.collectAsState()
    val currentValue = dataPoints.lastOrNull() ?: 0f

    var mode by remember { mutableStateOf(Mode.VOLTAGE) }

    val color = mode.color

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
                text = "ESCOMeter",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // 🎛 SELECTOR
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Mode.values().forEach {
                    ModeButton(
                        text = it.label,
                        selected = it == mode,
                        color = it.color,
                        onClick = { mode = it }
                    )
                }
            }

            // 🔢 DISPLAY PRO
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

            // 📊 GRÁFICA PRO (simple pero limpia)
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

                        val model = entryModelOf(*dataPoints.toTypedArray())

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

// 🔘 BOTÓN
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

// 🧠 FORMATO
fun formatValue(value: Float, mode: Mode): String {
    return when (mode) {
        Mode.VOLTAGE -> String.format("%.2f V", value)
        Mode.CURRENT -> String.format("%.3f A", value)
        Mode.RESISTANCE -> {
            when {
                value > 1_000_000 -> String.format("%.2f MΩ", value / 1_000_000)
                value > 1_000 -> String.format("%.2f kΩ", value / 1_000)
                else -> String.format("%.2f Ω", value)
            }
        }
    }
}