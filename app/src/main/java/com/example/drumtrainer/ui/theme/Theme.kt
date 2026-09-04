package com.example.drumtrainer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DrumTrainerTheme(
    themeOption: ThemeOption = ThemeOption.DEFAULT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) themeOption.dark else themeOption.light,
        typography = Typography,
        content = content,
    )
}
