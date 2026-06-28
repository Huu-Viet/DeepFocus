package com.bcn.deepfocus.model.timer.data.repo_impl

import com.bcn.deepfocus.model.timer.domain.entity.Timer
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import com.bcn.deepfocus.model.timer.data.service.TimerServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimerRepoImpl(
    private val timerServiceManager: TimerServiceManager
): TimerRepository {
    private val _timerData = MutableStateFlow(
            Timer(
                totalSeconds = 25 * 60,
                remainingSeconds = 25 * 60,
                isRunning = false
            )
    )
    override val timerData = _timerData.asStateFlow()

    override fun updateTime(remainingSeconds: Int) {
        _timerData.update {
            it.copy(
                remainingSeconds = remainingSeconds,
                isRunning = remainingSeconds > 0
            )
        }
    }

    override fun start(duration: Int) {
        _timerData.update {
            it.copy(
                totalSeconds = duration,
                remainingSeconds = duration,
                isRunning = true
            )
        }
        timerServiceManager.startTimer(duration)
    }

    override fun endSession() {
        _timerData.update { it.copy(
            remainingSeconds = it.totalSeconds,
            isRunning = false
        ) }
        timerServiceManager.stopTimer()
    }

    override fun restart(duration: Int) {
        timerServiceManager.restart(duration)
    }
}