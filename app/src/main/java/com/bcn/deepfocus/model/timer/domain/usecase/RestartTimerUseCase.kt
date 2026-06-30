package com.bcn.deepfocus.model.timer.domain.usecase

import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository

class RestartTimerUseCase(
    private val timerRepo: TimerRepository
) {
    operator fun invoke() {
        timerRepo.restart()
    }
}
