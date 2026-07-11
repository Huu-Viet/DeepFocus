package com.bcn.deepfocus.presentation.setting.viewmodel

import com.bcn.deepfocus.core.ui.theme.ThemeMode
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun themeMode_system_uses_system_dark_theme_value() {
        assertTrue(ThemeMode.SYSTEM.isDarkTheme(systemDarkTheme = true))
        assertFalse(ThemeMode.SYSTEM.isDarkTheme(systemDarkTheme = false))
    }

    @Test
    fun themeMode_explicit_values_override_system() {
        assertTrue(ThemeMode.DARK.isDarkTheme(systemDarkTheme = false))
        assertFalse(ThemeMode.LIGHT.isDarkTheme(systemDarkTheme = true))
    }
}
