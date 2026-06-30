package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import kotlinx.coroutines.flow.Flow

class WatchTotalFocusSecondsByDateUseCase(
    private val statisticRepository: StatisticRepository
) {
    operator fun invoke(localDate: String): Flow<Int> {
        return statisticRepository.observeTotalFocusSecondsByDate(localDate)
    }
}
