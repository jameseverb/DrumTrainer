package com.example.drumtrainer.ui.theme

import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.example.drumtrainer.R

/**
 * 可选颜色主题。每套提供完整的 light/dark ColorScheme，
 * 覆盖 primary / primaryContainer / secondary / secondaryContainer / surfaceVariant 等
 * 关键色槽，切换主题时按钮、卡片高亮、字谱背景等"色块"会统一变化。
 * 深浅色模式仍跟随系统。
 */
enum class ThemeOption(
    @StringRes val labelRes: Int,
    val light: ColorScheme,
    val dark: ColorScheme,
) {
    BLUE(
        labelRes = R.string.theme_blue,
        light = lightScheme(
            primary = Color(0xFF1E88E5), onPrimary = Color.White,
            primaryContainer = Color(0xFFD1E4FF), onPrimaryContainer = Color(0xFF001D36),
            secondary = Color(0xFF8A5100), onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFDDB3), onSecondaryContainer = Color(0xFF2A1700),
            surfaceVariant = Color(0xFFE0E2EC), onSurfaceVariant = Color(0xFF43474E),
        ),
        dark = darkScheme(
            primary = Color(0xFF9ECAFF), onPrimary = Color(0xFF003258),
            primaryContainer = Color(0xFF00497D), onPrimaryContainer = Color(0xFFD1E4FF),
            secondary = Color(0xFFFFB877), onSecondary = Color(0xFF4A2800),
            secondaryContainer = Color(0xFF693C00), onSecondaryContainer = Color(0xFFFFDDB3),
            surfaceVariant = Color(0xFF43474E), onSurfaceVariant = Color(0xFFC3C7CF),
        ),
    ),
    RED(
        labelRes = R.string.theme_red,
        light = lightScheme(
            primary = Color(0xFFD32F2F), onPrimary = Color.White,
            primaryContainer = Color(0xFFFFDAD6), onPrimaryContainer = Color(0xFF410002),
            secondary = Color(0xFF7C5800), onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFDDB3), onSecondaryContainer = Color(0xFF2A1700),
            surfaceVariant = Color(0xFFF5DDDB), onSurfaceVariant = Color(0xFF534339),
        ),
        dark = darkScheme(
            primary = Color(0xFFFFB4AB), onPrimary = Color(0xFF690005),
            primaryContainer = Color(0xFF930008), onPrimaryContainer = Color(0xFFFFDAD6),
            secondary = Color(0xFFFFB77E), onSecondary = Color(0xFF4C2600),
            secondaryContainer = Color(0xFF693C00), onSecondaryContainer = Color(0xFFFFDDB3),
            surfaceVariant = Color(0xFF534339), onSurfaceVariant = Color(0xFFC8BDB6),
        ),
    ),
    PURPLE(
        labelRes = R.string.theme_purple,
        light = lightScheme(
            primary = Color(0xFF8E24AA), onPrimary = Color.White,
            primaryContainer = Color(0xFFFFD8E7), onPrimaryContainer = Color(0xFF3B0029),
            secondary = Color(0xFF7A5800), onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFDDB3), onSecondaryContainer = Color(0xFF2A1700),
            surfaceVariant = Color(0xFFE8E0F5), onSurfaceVariant = Color(0xFF4A4458),
        ),
        dark = darkScheme(
            primary = Color(0xFFFFADF5), onPrimary = Color(0xFF560050),
            primaryContainer = Color(0xFF712977), onPrimaryContainer = Color(0xFFFFD8E7),
            secondary = Color(0xFFFFB77E), onSecondary = Color(0xFF4C2600),
            secondaryContainer = Color(0xFF693C00), onSecondaryContainer = Color(0xFFFFDDB3),
            surfaceVariant = Color(0xFF4A4458), onSurfaceVariant = Color(0xFFCDC8D8),
        ),
    ),
    GREEN(
        labelRes = R.string.theme_green,
        light = lightScheme(
            primary = Color(0xFF2E7D32), onPrimary = Color.White,
            primaryContainer = Color(0xFFB8F5B0), onPrimaryContainer = Color(0xFF002106),
            secondary = Color(0xFF7C5800), onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFDDB3), onSecondaryContainer = Color(0xFF2A1700),
            surfaceVariant = Color(0xFFDCE8DC), onSurfaceVariant = Color(0xFF3F5040),
        ),
        dark = darkScheme(
            primary = Color(0xFF9CD698), onPrimary = Color(0xFF003907),
            primaryContainer = Color(0xFF115D17), onPrimaryContainer = Color(0xFFB8F5B0),
            secondary = Color(0xFFFFB77E), onSecondary = Color(0xFF4C2600),
            secondaryContainer = Color(0xFF693C00), onSecondaryContainer = Color(0xFFFFDDB3),
            surfaceVariant = Color(0xFF3F5040), onSurfaceVariant = Color(0xFFBFCDB8),
        ),
    ),
    ORANGE(
        labelRes = R.string.theme_orange,
        light = lightScheme(
            primary = Color(0xFFEF6C00), onPrimary = Color.White,
            primaryContainer = Color(0xFFFFDCC2), onPrimaryContainer = Color(0xFF501F00),
            secondary = Color(0xFF00696E), onSecondary = Color.White,
            secondaryContainer = Color(0xFF9CF0FF), onSecondaryContainer = Color(0xFF001F22),
            surfaceVariant = Color(0xFFF5E5D0), onSurfaceVariant = Color(0xFF534030),
        ),
        dark = darkScheme(
            primary = Color(0xFFFFB77E), onPrimary = Color(0xFF4C2800),
            primaryContainer = Color(0xFF693C00), onPrimaryContainer = Color(0xFFFFDCC2),
            secondary = Color(0xFF80D3DD), onSecondary = Color(0xFF00373B),
            secondaryContainer = Color(0xFF004F53), onSecondaryContainer = Color(0xFF9CF0FF),
            surfaceVariant = Color(0xFF534030), onSurfaceVariant = Color(0xFFC8BDB6),
        ),
    ),
    TEAL(
        labelRes = R.string.theme_teal,
        light = lightScheme(
            primary = Color(0xFF00897B), onPrimary = Color.White,
            primaryContainer = Color(0xFF9CF0E4), onPrimaryContainer = Color(0xFF001F1B),
            secondary = Color(0xFF8A5100), onSecondary = Color.White,
            secondaryContainer = Color(0xFFFFDDB3), onSecondaryContainer = Color(0xFF2A1700),
            surfaceVariant = Color(0xFFD0E8E5), onSurfaceVariant = Color(0xFF38504A),
        ),
        dark = darkScheme(
            primary = Color(0xFF80D4C3), onPrimary = Color(0xFF003730),
            primaryContainer = Color(0xFF005046), onPrimaryContainer = Color(0xFF9CF0E4),
            secondary = Color(0xFFFFB877), onSecondary = Color(0xFF4A2800),
            secondaryContainer = Color(0xFF693C00), onSecondaryContainer = Color(0xFFFFDDB3),
            surfaceVariant = Color(0xFF38504A), onSurfaceVariant = Color(0xFFBCC9C4),
        ),
    ),
    ;

    companion object {
        /** 默认主题 */
        val DEFAULT = BLUE

        /** 从持久化的字符串安全还原，非法值回退默认 */
        fun fromName(name: String?): ThemeOption =
            entries.firstOrNull { it.name == name } ?: DEFAULT
    }
}

/** light 主题：每套传入主色相关色槽，中性背景/表面共用 */
private fun lightScheme(
    primary: Color, onPrimary: Color,
    primaryContainer: Color, onPrimaryContainer: Color,
    secondary: Color, onSecondary: Color,
    secondaryContainer: Color, onSecondaryContainer: Color,
    surfaceVariant: Color, onSurfaceVariant: Color,
): ColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightBackground,
    onSurface = LightOnBackground,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    outline = LightOutline,
)

/** dark 主题：每套传入主色相关色槽，中性背景/表面共用 */
private fun darkScheme(
    primary: Color, onPrimary: Color,
    primaryContainer: Color, onPrimaryContainer: Color,
    secondary: Color, onSecondary: Color,
    secondaryContainer: Color, onSecondaryContainer: Color,
    surfaceVariant: Color, onSurfaceVariant: Color,
): ColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkBackground,
    onSurface = DarkOnBackground,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    outline = DarkOutline,
)
