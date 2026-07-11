package com.bcn.deepfocus.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK;

    fun isDarkTheme(systemDarkTheme: Boolean): Boolean = when (this) {
        SYSTEM -> systemDarkTheme
        LIGHT -> false
        DARK -> true
    }
}

enum class LanguageMode {
    ENGLISH,
    VIETNAMESE
}

val LocalThemeMode = staticCompositionLocalOf { ThemeMode.SYSTEM }
