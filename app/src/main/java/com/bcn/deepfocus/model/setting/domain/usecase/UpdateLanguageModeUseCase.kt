package com.bcn.deepfocus.model.setting.domain.usecase

import com.bcn.deepfocus.core.ui.theme.LanguageMode
import com.bcn.deepfocus.model.setting.domain.repo.SettingRepository

class UpdateLanguageModeUseCase(
    private val repository: SettingRepository
) {
    suspend operator fun invoke(languageMode: LanguageMode) {
        repository.updateLanguageMode(languageMode)
    }
}
