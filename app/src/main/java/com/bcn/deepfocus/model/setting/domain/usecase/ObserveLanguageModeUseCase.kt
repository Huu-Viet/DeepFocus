package com.bcn.deepfocus.model.setting.domain.usecase

import com.bcn.deepfocus.core.ui.theme.LanguageMode
import com.bcn.deepfocus.model.setting.domain.repo.SettingRepository
import kotlinx.coroutines.flow.Flow

class ObserveLanguageModeUseCase(
    private val repository: SettingRepository
) {
    operator fun invoke(): Flow<LanguageMode> = repository.observeLanguageMode()
}
