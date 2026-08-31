package com.example.drumtrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.drumtrainer.ui.navigation.AppNavHost
import com.example.drumtrainer.ui.theme.DrumTrainerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrumTrainerTheme {
                AppNavHost()
            }
        }
    }
}
