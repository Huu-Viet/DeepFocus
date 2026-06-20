package com.bcn.deepfocus.model.timer.domain.usecase

import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository

class StopTimerUseCase(
    private val timerRepo: TimerRepository
) {
    operator fun invoke() {
        timerRepo.endSession()
    }
}