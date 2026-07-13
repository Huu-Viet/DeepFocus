package com.bcn.deepfocus.model.timer.domain.usecase

import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository

class StartTimerUseCase(
    private val timerRepo: TimerRepository
) {
    operator fun invoke(id: String, focusSeconds: Int, breakSeconds: Int) {
        timerRepo.start(id, focusSeconds, breakSeconds)
    }
}
