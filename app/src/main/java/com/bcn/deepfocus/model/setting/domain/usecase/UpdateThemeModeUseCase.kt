package com.bcn.deepfocus.model.setting.domain.usecase

import com.bcn.deepfocus.core.ui.theme.ThemeMode
import com.bcn.deepfocus.model.setting.domain.repo.SettingRepository

class UpdateThemeModeUseCase(
    private val repository: SettingRepository
) {
    suspend operator fun invoke(themeMode: ThemeMode) {
        repository.updateThemeMode(themeMode)
    }
}
