package com.example.escometer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.escometer.serial.SerialManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MultimeterViewModel(application: Application) : AndroidViewModel(application) {

    private val serialManager = SerialManager(application)

    // 📊 DATOS
    private val _voltageData = MutableStateFlow<List<Float>>(emptyList())
    val voltageData: StateFlow<List<Float>> = _voltageData.asStateFlow()

    private val _currentData = MutableStateFlow<List<Float>>(emptyList())
    val currentData: StateFlow<List<Float>> = _currentData.asStateFlow()

    private val _resistanceData = MutableStateFlow<List<Float>>(emptyList())
    val resistanceData: StateFlow<List<Float>> = _resistanceData.asStateFlow()

    // ⚡ POTENCIA (NUEVO)
    private val _powerData = MutableStateFlow<List<Float>>(emptyList())
    val powerData: StateFlow<List<Float>> = _powerData.asStateFlow()

    // ⚠️ ERRORES
    private val _errorFlow = MutableStateFlow<String?>(null)
    val errorFlow: StateFlow<String?> = _errorFlow.asStateFlow()

    private val maxPoints = 100

    init {
        start()
    }

    private fun start() {

        // 🔋 VOLTAJE
        viewModelScope.launch {
            serialManager.voltageFlow.collect { value ->
                _voltageData.value =
                    (_voltageData.value + value).takeLast(maxPoints)
            }
        }

        // ⚡ CORRIENTE
        viewModelScope.launch {
            serialManager.currentFlow.collect { value ->
                _currentData.value =
                    (_currentData.value + value).takeLast(maxPoints)
            }
        }

        // 🧱 RESISTENCIA
        viewModelScope.launch {
            serialManager.resistanceFlow.collect { value ->
                _resistanceData.value =
                    (_resistanceData.value + value).takeLast(maxPoints)
            }
        }

        // ⚡🔥 POTENCIA (V * I)
        viewModelScope.launch {
            combine(
                voltageData,
                currentData
            ) { voltageList, currentList ->

                val size = minOf(voltageList.size, currentList.size)

                List(size) { i ->
                    voltageList[i] * currentList[i]
                }
            }.collect { powerList ->

                _powerData.value =
                    powerList.takeLast(maxPoints)
            }
        }

        // ⚠️ ERRORES
        viewModelScope.launch {
            serialManager.errorFlow.collect { error ->
                _errorFlow.value = error
            }
        }

        // 🔌 INICIAR LECTURA
        viewModelScope.launch {
            try {
                serialManager.startReading()
            } catch (_: Exception) {
                _errorFlow.value = "Error iniciando lectura"
            }
        }
    }

    fun clearError() {
        _errorFlow.value = null
    }
}