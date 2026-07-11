package com.bcn.deepfocus.presentation.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcn.deepfocus.core.ui.theme.LanguageMode
import com.bcn.deepfocus.core.ui.theme.ThemeMode
import com.bcn.deepfocus.model.setting.domain.usecase.ObserveLanguageModeUseCase
import com.bcn.deepfocus.model.setting.domain.usecase.ObserveThemeModeUseCase
import com.bcn.deepfocus.model.setting.domain.usecase.UpdateLanguageModeUseCase
import com.bcn.deepfocus.model.setting.domain.usecase.UpdateThemeModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    observeThemeModeUseCase: ObserveThemeModeUseCase,
    observeLanguageModeUseCase: ObserveLanguageModeUseCase,
    private val updateThemeModeUseCase: UpdateThemeModeUseCase,
    private val updateLanguageModeUseCase: UpdateLanguageModeUseCase
) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = observeThemeModeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val languageMode: StateFlow<LanguageMode> = observeLanguageModeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LanguageMode.ENGLISH)

    fun onThemeModeChange(themeMode: ThemeMode) {
        viewModelScope.launch {
            updateThemeModeUseCase(themeMode)
        }
    }

    fun onLanguageModeChange(languageMode: LanguageMode) {
        viewModelScope.launch {
            updateLanguageModeUseCase(languageMode)
        }
    }
}
