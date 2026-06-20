package com.bcn.deepfocus.model.timer.domain.repo

import com.bcn.deepfocus.model.timer.domain.entity.Timer
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    val timerData: Flow<Timer>
    fun updateTime(remainingSeconds: Int)
    fun start(duration: Int)
    fun endSession()
    fun restart(duration: Int)
}