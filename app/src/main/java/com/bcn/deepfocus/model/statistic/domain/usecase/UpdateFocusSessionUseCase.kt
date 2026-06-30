package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository

class UpdateFocusSessionUseCase(
    private val statisticRepository: StatisticRepository
) {
    suspend operator fun invoke(focusSession: FocusSession) {
        statisticRepository.updateFocusSession(focusSession)
    }
}
