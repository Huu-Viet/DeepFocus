package com.bcn.deepfocus.model.timer.domain.usecase

import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import kotlinx.coroutines.flow.Flow

class WatchActiveSessionUseCase(
    private val timerRepo: TimerRepository
) {
    operator fun invoke(): Flow<FocusSet> {
        return timerRepo.activeSession
    }
}