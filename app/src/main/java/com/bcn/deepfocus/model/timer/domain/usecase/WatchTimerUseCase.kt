package com.bcn.deepfocus.model.timer.domain.usecase

import com.bcn.deepfocus.model.timer.domain.entity.Timer
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import kotlinx.coroutines.flow.Flow

class WatchTimerUseCase(
    private val timerRepo: TimerRepository
) {
    operator fun invoke(): Flow<Timer> {
        return timerRepo.timerData
    }
}