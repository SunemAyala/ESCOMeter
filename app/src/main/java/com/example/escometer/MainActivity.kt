package com.example.escometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.escometer.ui.MultimeterScreen
import com.example.escometer.ui.theme.ESCOMeterTheme
import com.example.escometer.viewmodel.MultimeterViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ESCOMeterTheme {
                val vm: MultimeterViewModel = viewModel()
                MultimeterScreen(vm)
            }
        }
    }
}