package com.bcn.deepfocus.presentation.custom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.custom.domain.usecase.SaveFocusSetUseCase
import com.bcn.deepfocus.model.custom.domain.usecase.UpdateFocusSetUseCase
import com.bcn.deepfocus.presentation.custom.model.CustomTimerUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TimeCustomViewModel @Inject constructor(
    private val saveFocusSetUseCase: SaveFocusSetUseCase,
    private val updateFocusSetUseCase: UpdateFocusSetUseCase,
) : ViewModel() {

    companion object {
        private const val MAX_FOCUS = 180
        private const val MIN_VALUE = 1
        private const val BREAK_RATIO = 0.3f
    }

    private val _uiState = MutableStateFlow(CustomTimerUIState())
    val uiState = _uiState.asStateFlow()

    private fun maxBreakAllowed(): Int {
        return (_uiState.value.focusMinute * BREAK_RATIO).toInt().coerceAtLeast(MIN_VALUE)
    }

    fun onFocusMinuteChange(minute: Int) {
        val clamped = minute.coerceIn(MIN_VALUE, MAX_FOCUS)
        val newMaxBreak = (clamped * BREAK_RATIO).toInt().coerceAtLeast(MIN_VALUE)
        val adjustedBreak = _uiState.value.breakMinute.coerceAtMost(newMaxBreak)

        _uiState.update {
            it.copy(
                focusMinute = clamped,
                breakMinute = adjustedBreak,
                warningMessage = null
            )
        }
    }

    fun onBreakMinuteChange(minute: Int) {
        val maxBreak = maxBreakAllowed()
        if (minute > maxBreak) {
            _uiState.update {
                it.copy(
                    breakMinute = maxBreak,
                    warningMessage = "Easy on the breaks! Max ${maxBreak}m for this focus session"
                )
            }
            return
        }
        val clamped = minute.coerceIn(MIN_VALUE, maxBreak)
        _uiState.update {
            it.copy(
                breakMinute = clamped,
                warningMessage = null
            )
        }
    }

    fun incrementFocusMinute() {
        val current = _uiState.value.focusMinute
        if (current < MAX_FOCUS) {
            onFocusMinuteChange(current + 1)
        }
    }

    fun decrementFocusMinute() {
        val current = _uiState.value.focusMinute
        if (current > MIN_VALUE) {
            onFocusMinuteChange(current - 1)
        }
    }

    fun incrementBreakMinute() {
        val current = _uiState.value.breakMinute
        val maxBreak = maxBreakAllowed()
        if (current >= maxBreak) {
            _uiState.update {
                it.copy(
                    warningMessage = "Easy on the breaks! Max ${maxBreak}m for this focus session"
                )
            }
            return
        }
        onBreakMinuteChange(current + 1)
    }

    fun decrementBreakMinute() {
        val current = _uiState.value.breakMinute
        if (current > MIN_VALUE) {
            onBreakMinuteChange(current - 1)
        }
    }

    fun dismissWarning() {
        _uiState.update { it.copy(warningMessage = null) }
    }

    fun saveCustomFocusSet() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val focusSet = FocusSet(
                id = UUID.randomUUID().toString(),
                focusMinute = _uiState.value.focusMinute,
                breakMinute = _uiState.value.breakMinute
            )

            saveFocusSetUseCase(focusSet)

            _uiState.update {
                it.copy(
                    isSaving = false,
                    isSaved = true
                )
            }
        }
    }

    fun updateCustomFocusSet(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val focusSet = FocusSet(
                id = id,
                focusMinute = _uiState.value.focusMinute,
                breakMinute = _uiState.value.breakMinute
            )

            updateFocusSetUseCase(focusSet)
        }
    }

    fun resetSavedState() {
        _uiState.update { it.copy(isSaved = false) }
    }
}
