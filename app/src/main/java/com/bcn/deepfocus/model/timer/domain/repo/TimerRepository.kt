package com.bcn.deepfocus.model.timer.domain.repo

import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.timer.domain.entity.Timer
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    val timerData: Flow<Timer>
    val activeSession: Flow<FocusSet>
    fun updateTimer(timer: Timer)
    fun updateActiveSession(currentSession: FocusSet)
    fun start(id: String, focusSeconds: Int, breakSeconds: Int)
    fun endSession()
    fun restart()
}
