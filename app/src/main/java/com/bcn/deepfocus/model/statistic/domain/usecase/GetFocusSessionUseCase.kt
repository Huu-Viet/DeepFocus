package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository

class GetFocusSessionUseCase(
    private val statisticRepository: StatisticRepository
) {
    suspend operator fun invoke(id: Long): FocusSession? {
        return statisticRepository.getFocusSession(id)
    }
}
