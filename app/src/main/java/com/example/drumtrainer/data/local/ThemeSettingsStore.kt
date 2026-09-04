package com.example.drumtrainer.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.drumtrainer.ui.theme.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore(name = "theme_settings")

/**
 * 主题选择的本地持久化（DataStore Preferences）。
 * 读：Flow 响应式，切换主题立即生效；写：suspend。
 */
class ThemeSettingsStore(private val context: Context) {

    private val themeKey = stringPreferencesKey("theme_option")

    val themeFlow: Flow<ThemeOption> = context.themeDataStore.data
        .map { prefs -> ThemeOption.fromName(prefs[themeKey]) }

    suspend fun setTheme(option: ThemeOption) {
        context.themeDataStore.edit { prefs ->
            prefs[themeKey] = option.name
        }
    }
}
