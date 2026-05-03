package com.example.escometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.escometer.ui.MultimeterScreen
import com.example.escometer.ui.theme.ESCOMeterTheme
import com.example.escometer.viewmodel.MultimeterViewModel

// 🔥 IMPORTS
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

class MainActivity : ComponentActivity() {

    // 🔥 ENUM ACTUALIZADO
    enum class Screen {
        MULTIMETER,
        EXPORT,
        DEVICES,
        WAVEFORM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ESCOMeterTheme {

                val vm: MultimeterViewModel = viewModel()

                var screen by remember { mutableStateOf(Screen.MULTIMETER) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {

                            NavigationBarItem(
                                selected = screen == Screen.MULTIMETER,
                                onClick = { screen = Screen.MULTIMETER },
                                label = { Text("Medición") },
                                icon = { Icon(Icons.Default.ShowChart, null) }
                            )

                            NavigationBarItem(
                                selected = screen == Screen.EXPORT,
                                onClick = { screen = Screen.EXPORT },
                                label = { Text("Exportar") },
                                icon = { Icon(Icons.Default.Download, null) }
                            )

                            NavigationBarItem(
                                selected = screen == Screen.DEVICES,
                                onClick = { screen = Screen.DEVICES },
                                label = { Text("USB") },
                                icon = { Icon(Icons.Default.Usb, null) }
                            )

                            // 🔥 NUEVA SECCIÓN: SEÑAL
                            NavigationBarItem(
                                selected = screen == Screen.WAVEFORM,
                                onClick = { screen = Screen.WAVEFORM },
                                label = { Text("Señal") },
                                icon = { Icon(Icons.Default.AutoGraph, null) }
                            )
                        }
                    }
                ) { padding ->

                    Box(Modifier.padding(padding)) {

                        when (screen) {
                            Screen.MULTIMETER -> MultimeterScreen(vm)
                            Screen.EXPORT -> ExportScreen(vm)
                            Screen.DEVICES -> DeviceScreen()
                            Screen.WAVEFORM -> WaveformScreen(vm)
                        }
                    }
                }
            }
        }
    }
}