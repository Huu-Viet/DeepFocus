package com.bcn.deepfocus.model.setting.domain.usecase

import com.bcn.deepfocus.core.ui.theme.ThemeMode
import com.bcn.deepfocus.model.setting.domain.repo.SettingRepository
import kotlinx.coroutines.flow.Flow

class ObserveThemeModeUseCase(
    private val repository: SettingRepository
) {
    operator fun invoke(): Flow<ThemeMode> = repository.observeThemeMode()
}
