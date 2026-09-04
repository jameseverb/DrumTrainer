package com.example.drumtrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.drumtrainer.ui.navigation.AppNavHost
import com.example.drumtrainer.ui.theme.DrumTrainerTheme
import com.example.drumtrainer.ui.theme.ThemeOption

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as DrumTrainerApp
        setContent {
            // 读取持久化的主题选择，切换后立即生效
            val themeOption by app.container.themeSettings.themeFlow
                .collectAsStateWithLifecycle(initialValue = ThemeOption.DEFAULT)

            DrumTrainerTheme(themeOption = themeOption) {
                AppNavHost()
            }
        }
    }
}
