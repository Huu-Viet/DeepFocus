package com.bcn.deepfocus.model.custom.domain.usecase

import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.custom.domain.repo.CustomTimerRepo
import kotlinx.coroutines.flow.Flow

class WatchAllFocusSetsUseCase(
    private val customTimerRepo: CustomTimerRepo
) {
    operator fun invoke(): Flow<List<FocusSet>> {
        return customTimerRepo.watchAllFocusSets()
    }
}
