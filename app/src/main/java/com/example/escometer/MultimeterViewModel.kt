package com.example.escometer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.escometer.serial.SerialManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MultimeterViewModel(application: Application) : AndroidViewModel(application) {

    private val serialManager = SerialManager(application)

    private val _dataPoints = MutableStateFlow<List<Float>>(emptyList())
    val dataPoints: StateFlow<List<Float>> = _dataPoints

    init {
        start()
    }

    private fun start() {
        viewModelScope.launch {
            serialManager.dataFlow.collect { value ->
                _dataPoints.value = (_dataPoints.value + value).takeLast(100)
            }
        }

        viewModelScope.launch {
            serialManager.startReading()
        }
    }
}