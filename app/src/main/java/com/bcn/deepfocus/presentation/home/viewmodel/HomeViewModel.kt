package com.bcn.deepfocus.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcn.deepfocus.model.timer.domain.usecase.RestartTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.StartTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.StopTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.WatchTimerUseCase
import com.bcn.deepfocus.presentation.home.model.EFocusPreset
import com.bcn.deepfocus.presentation.home.model.TimerPhase
import com.bcn.deepfocus.presentation.home.model.TimerUIState
import com.bcn.deepfocus.presentation.service.TimerServiceManager
import com.bcn.deepfocus.utils.toSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val startTimerUseCase: StartTimerUseCase,
    private val restartTimerUseCase: RestartTimerUseCase,
    private val stopTimerUseCase: StopTimerUseCase,
    private val watchTimerUseCase: WatchTimerUseCase,
): ViewModel() {
    private val _focusPreset = MutableStateFlow(EFocusPreset.DEFAULT)
    val focusPreset = _focusPreset.asStateFlow()

    private val _timerState = MutableStateFlow(TimerUIState(
        totalSeconds = focusPreset.value.focusMinutes.toSecond(),
        remainingSeconds = focusPreset.value.focusMinutes.toSecond(),
        isRunning = false,
        progress = 0f,
        timerPhase = TimerPhase.FOCUS
    ))
    val timerState = _timerState.asStateFlow()

    init {
        watchTimerData()
    }

    private fun watchTimerData() = viewModelScope.launch {
        watchTimerUseCase().collect{ timerData ->
            if (timerData.remainingSeconds == 0) moveToNextPhase()
            _timerState.update {
                it.copy(
                    totalSeconds = timerData.totalSeconds,
                    remainingSeconds = timerData.remainingSeconds,
                    progress = 1f - (timerData.remainingSeconds.toFloat() / timerData.totalSeconds),
                    isRunning = timerData.isRunning
                )
            }
        }
    }

    private fun moveToNextPhase() {
        _timerState.update {
            when (it.timerPhase) {
                TimerPhase.FOCUS -> {
                    it.copy(
                        timerPhase = TimerPhase.BREAK,
                        totalSeconds = focusPreset.value.breakMinutes.toSecond(),
                        remainingSeconds = focusPreset.value.breakMinutes.toSecond(),
                        isRunning = false
                    )
                }

                TimerPhase.BREAK -> {
                    it.copy(
                        timerPhase = TimerPhase.FOCUS,
                        totalSeconds = focusPreset.value.focusMinutes.toSecond(),
                        remainingSeconds = focusPreset.value.focusMinutes.toSecond(),
                        isRunning = false
                    )
                }
            }
        }
        start()
    }

    fun start() {
        if (_timerState.value.isRunning) return

        _timerState.update {
            it.copy(
                isRunning = true
            )
        }

        startTimerUseCase(_timerState.value.totalSeconds)
    }

    fun endSession() {
        stopTimerUseCase()
        _timerState.update {
            it.copy(
                remainingSeconds = focusPreset.value.focusMinutes.toSecond(),
                isRunning = false,
                progress = 0f
            )
        }
    }

    fun reset() {
        restartTimerUseCase(_timerState.value.totalSeconds)
    }

    fun changeSetDuration(focusPreset: EFocusPreset) {
        if(_timerState.value.isRunning) endSession()
        _focusPreset.update {
            focusPreset
        }
        _timerState.update {
            it.copy(
                totalSeconds = focusPreset.focusMinutes.toSecond(),
                remainingSeconds = focusPreset.focusMinutes.toSecond(),
                isRunning = false,
                progress = 0f,
                timerPhase = TimerPhase.FOCUS
            )
        }
    }
}

