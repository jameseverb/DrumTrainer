package com.example.drumtrainer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun DrumTrainerTheme(
    themeOption: ThemeOption = ThemeOption.DEFAULT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val lightColors = lightColorScheme(
        primary = themeOption.primary,
        secondary = themeOption.secondary,
        background = BackgroundLight,
        surface = SurfaceLight,
    )

    val darkColors = darkColorScheme(
        primary = themeOption.primary,
        secondary = themeOption.secondary,
        background = BackgroundDark,
        surface = SurfaceDark,
    )

    MaterialTheme(
        colorScheme = if (darkTheme) darkColors else lightColors,
        typography = Typography,
        content = content,
    )
}
