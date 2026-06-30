package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import kotlinx.coroutines.flow.Flow

class WatchAbandonedSessionsByDateUseCase(
    private val statisticRepository: StatisticRepository
) {
    operator fun invoke(localDate: String): Flow<List<FocusSession>> {
        return statisticRepository.observeAbandonedSessionsByDate(localDate)
    }
}
