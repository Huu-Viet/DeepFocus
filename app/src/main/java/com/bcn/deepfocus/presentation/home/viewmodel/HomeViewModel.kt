package com.bcn.deepfocus.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.custom.domain.usecase.DeleteFocusSetUseCase
import com.bcn.deepfocus.model.custom.domain.usecase.WatchAllFocusSetsUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.RestartTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.StartTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.StopTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.WatchTimerUseCase
import com.bcn.deepfocus.model.timer.domain.entity.TimerPhase as DomainTimerPhase
import com.bcn.deepfocus.presentation.home.model.EFocusPreset
import com.bcn.deepfocus.presentation.home.model.TimerPhase
import com.bcn.deepfocus.presentation.home.model.TimerUIState
import com.bcn.deepfocus.utils.toSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val startTimerUseCase: StartTimerUseCase,
    private val restartTimerUseCase: RestartTimerUseCase,
    private val stopTimerUseCase: StopTimerUseCase,
    private val watchTimerUseCase: WatchTimerUseCase,
    private val watchAllFocusSetsUseCase: WatchAllFocusSetsUseCase,
    private val deleteFocusSetUseCase: DeleteFocusSetUseCase,
): ViewModel() {

    // Single source of truth for active focus/break duration
    private val _activeFocusSet = MutableStateFlow(
        FocusSet(
            id = EFocusPreset.DEFAULT.name,
            focusMinute = EFocusPreset.DEFAULT.focusMinutes,
            breakMinute = EFocusPreset.DEFAULT.breakMinutes
        )
    )
    val activeFocusSet = _activeFocusSet.asStateFlow()

    val customFocusSets = watchAllFocusSetsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _timerState = MutableStateFlow(TimerUIState(
        totalSeconds = _activeFocusSet.value.focusMinute.toSecond(),
        remainingSeconds = _activeFocusSet.value.focusMinute.toSecond(),
        isRunning = false,
        progress = 0f,
        timerPhase = TimerPhase.FOCUS
    ))
    val timerState = _timerState.asStateFlow()

    init {
        watchTimerData()
    }

    private fun watchTimerData() = viewModelScope.launch {
        watchTimerUseCase().collect { timerData ->
            _timerState.update {
                it.copy(
                    totalSeconds = timerData.totalSeconds,
                    remainingSeconds = timerData.remainingSeconds,
                    progress = 1f - (timerData.remainingSeconds.toFloat() / timerData.totalSeconds),
                    isRunning = timerData.isRunning,
                    timerPhase = timerData.timerPhase.toUiPhase()
                )
            }
        }
    }

    fun start() {
        if (_timerState.value.isRunning) return

        _timerState.update {
            it.copy(isRunning = true)
        }

        startTimerUseCase(
            _activeFocusSet.value.focusMinute.toSecond(),
            _activeFocusSet.value.breakMinute.toSecond()
        )
    }

    fun endSession() {
        stopTimerUseCase()
    }

    fun reset() {
        restartTimerUseCase()
    }

    fun changeSetDuration(focusPreset: EFocusPreset) {
        selectFocusSet(
            FocusSet(
                id = focusPreset.name,
                focusMinute = focusPreset.focusMinutes,
                breakMinute = focusPreset.breakMinutes
            )
        )
    }

    fun selectFocusSet(focusSet: FocusSet) {
        if (_timerState.value.isRunning) endSession()
        _activeFocusSet.update { focusSet }
        _timerState.update {
            it.copy(
                totalSeconds = focusSet.focusMinute.toSecond(),
                remainingSeconds = focusSet.focusMinute.toSecond(),
                isRunning = false,
                progress = 0f,
                timerPhase = TimerPhase.FOCUS
            )
        }
    }

    fun deleteFocusSet(id: String) = viewModelScope.launch{
        deleteFocusSetUseCase(id = id)
    }

    private fun DomainTimerPhase.toUiPhase(): TimerPhase {
        return when (this) {
            DomainTimerPhase.FOCUS -> TimerPhase.FOCUS
            DomainTimerPhase.BREAK -> TimerPhase.BREAK
        }
    }
}
