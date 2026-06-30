package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository

class SaveFocusSessionUseCase(
    private val statisticRepository: StatisticRepository
) {
    suspend operator fun invoke(focusSession: FocusSession): Long {
        return statisticRepository.saveFocusSession(focusSession)
    }
}
