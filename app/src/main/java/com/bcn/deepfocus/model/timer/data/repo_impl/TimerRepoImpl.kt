package com.bcn.deepfocus.model.timer.data.repo_impl

import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.usecase.SaveFocusSessionUseCase
import com.bcn.deepfocus.model.timer.domain.entity.Timer
import com.bcn.deepfocus.model.timer.domain.entity.TimerPhase
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import com.bcn.deepfocus.model.timer.data.service.TimerServiceManager
import com.bcn.deepfocus.presentation.home.model.EFocusPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimerRepoImpl(
    private val timerServiceManager: TimerServiceManager,
): TimerRepository {
    private val _timerData = MutableStateFlow(
            Timer(
                focusSeconds = 25 * 60,
                breakSeconds = 5 * 60,
                totalSeconds = 25 * 60,
                remainingSeconds = 25 * 60,
                isRunning = false,
                timerPhase = TimerPhase.FOCUS,
                completedCycles = 0
            )
    )
    override val timerData = _timerData.asStateFlow()

    private val _activeSession = MutableStateFlow(
        FocusSet(
            id = EFocusPreset.DEFAULT.name,
            focusMinute = EFocusPreset.DEFAULT.focusMinutes,
            breakMinute = EFocusPreset.DEFAULT.breakMinutes
        )
    )
    override val activeSession = _activeSession.asStateFlow()

    override fun updateTimer(timer: Timer) {
        _timerData.update { timer }
    }

    override fun updateActiveSession(currentSession: FocusSet) {
        _activeSession.update { currentSession }
    }

    override fun start(id: String, focusSeconds: Int, breakSeconds: Int) {
        _timerData.update {
            it.copy(
                focusSeconds = focusSeconds,
                breakSeconds = breakSeconds,
                totalSeconds = focusSeconds,
                remainingSeconds = focusSeconds,
                isRunning = true,
                timerPhase = TimerPhase.FOCUS,
                completedCycles = 0
            )
        }
        timerServiceManager.startTimer(id, focusSeconds, breakSeconds)
    }

    override fun endSession() {
        _timerData.update { it.copy(
            remainingSeconds = it.totalSeconds,
            isRunning = false
        ) }
        timerServiceManager.stopTimer()
    }

    override fun restart() {
        timerServiceManager.restart()
    }
}
