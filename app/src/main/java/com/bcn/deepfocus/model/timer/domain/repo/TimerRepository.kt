package com.bcn.deepfocus.model.timer.domain.repo

import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.timer.domain.entity.Timer
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    val timerData: Flow<Timer>
    fun updateTimer(timer: Timer)
    fun start(focusSeconds: Int, breakSeconds: Int)
    fun endSession()
    fun restart()
}
