package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import kotlinx.coroutines.flow.Flow

class WatchCompletedSessionsBetweenDatesUseCase(
    private val statisticRepository: StatisticRepository
) {
    operator fun invoke(startDate: String, endDate: String): Flow<List<FocusSession>> {
        return statisticRepository.observeCompletedSessionsBetweenDates(startDate, endDate)
    }
}
