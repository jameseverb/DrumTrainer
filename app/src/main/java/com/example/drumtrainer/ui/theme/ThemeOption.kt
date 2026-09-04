package com.example.drumtrainer.ui.theme

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.drumtrainer.R

/**
 * 可选颜色主题（primary 主色 + secondary 强调色）。
 * 深浅色模式跟随系统，仅切换配色。
 */
enum class ThemeOption(
    @StringRes val labelRes: Int,
    val primary: Color,
    val secondary: Color,
) {
    BLUE(R.string.theme_blue, Color(0xFF1E88E5), Color(0xFFFF8F00)),
    RED(R.string.theme_red, Color(0xFFE53935), Color(0xFFFFB300)),
    PURPLE(R.string.theme_purple, Color(0xFF8E24AA), Color(0xFFFFD54F)),
    GREEN(R.string.theme_green, Color(0xFF2E7D32), Color(0xFFFFCA28)),
    ORANGE(R.string.theme_orange, Color(0xFFEF6C00), Color(0xFF26A69A)),
    TEAL(R.string.theme_teal, Color(0xFF00897B), Color(0xFFFF7043)),
    ;

    companion object {
        /** 默认主题 */
        val DEFAULT = BLUE

        /** 从持久化的字符串安全还原，非法值回退默认 */
        fun fromName(name: String?): ThemeOption =
            entries.firstOrNull { it.name == name } ?: DEFAULT
    }
}
