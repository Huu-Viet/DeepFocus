package com.bcn.deepfocus.model.setting.data.repo_impl

import android.content.Context
import com.bcn.deepfocus.core.ui.theme.LanguageMode
import com.bcn.deepfocus.core.ui.theme.ThemeMode
import com.bcn.deepfocus.model.setting.domain.repo.SettingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

private const val PREFS_NAME = "deepfocus_settings"
private const val KEY_THEME_MODE = "theme_mode"
private const val KEY_LANGUAGE_MODE = "language_mode"

class SettingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingRepository {
    private val prefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val themeModeFlow = MutableStateFlow(getStoredThemeMode())
    private val languageModeFlow = MutableStateFlow(getStoredLanguageMode())

    override fun observeThemeMode(): Flow<ThemeMode> = themeModeFlow

    override fun observeLanguageMode(): Flow<LanguageMode> = languageModeFlow

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        prefs.edit()
            .putString(KEY_THEME_MODE, themeMode.name)
            .apply()
        themeModeFlow.value = themeMode
    }

    override suspend fun updateLanguageMode(languageMode: LanguageMode) {
        prefs.edit()
            .putString(KEY_LANGUAGE_MODE, languageMode.name)
            .apply()
        languageModeFlow.value = languageMode
    }

    private fun getStoredThemeMode(): ThemeMode {
        val stored = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(stored ?: ThemeMode.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    private fun getStoredLanguageMode(): LanguageMode {
        val stored = prefs.getString(KEY_LANGUAGE_MODE, LanguageMode.ENGLISH.name)
        return try {
            LanguageMode.valueOf(stored ?: LanguageMode.ENGLISH.name)
        } catch (e: IllegalArgumentException) {
            LanguageMode.ENGLISH
        }
    }
}
