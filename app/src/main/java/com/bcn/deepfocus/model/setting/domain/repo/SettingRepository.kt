package com.bcn.deepfocus.model.setting.domain.repo

import com.bcn.deepfocus.core.ui.theme.LanguageMode
import com.bcn.deepfocus.core.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun observeThemeMode(): Flow<ThemeMode>
    fun observeLanguageMode(): Flow<LanguageMode>
    suspend fun updateThemeMode(themeMode: ThemeMode)
    suspend fun updateLanguageMode(languageMode: LanguageMode)
}
