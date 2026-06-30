package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository

class DeleteFocusSessionUseCase(
    private val statisticRepository: StatisticRepository
) {
    suspend operator fun invoke(id: Long) {
        statisticRepository.deleteFocusSession(id)
    }
}
