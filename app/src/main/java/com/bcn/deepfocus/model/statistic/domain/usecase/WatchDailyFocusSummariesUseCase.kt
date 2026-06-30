package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.entities.DailyFocusSummary
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import kotlinx.coroutines.flow.Flow

class WatchDailyFocusSummariesUseCase(
    private val statisticRepository: StatisticRepository
) {
    operator fun invoke(startDate: String, endDate: String): Flow<List<DailyFocusSummary>> {
        return statisticRepository.observeDailyFocusSummaries(startDate, endDate)
    }
}
