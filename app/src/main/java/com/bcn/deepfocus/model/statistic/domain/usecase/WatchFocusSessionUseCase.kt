package com.bcn.deepfocus.model.statistic.domain.usecase

import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import kotlinx.coroutines.flow.Flow

class WatchFocusSessionUseCase(
    private val statisticRepository: StatisticRepository
) {
    operator fun invoke(id: Long): Flow<FocusSession?> {
        return statisticRepository.observeFocusSession(id)
    }
}
