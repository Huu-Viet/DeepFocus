package com.bcn.deepfocus.model.custom.domain.usecase

import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.custom.domain.repo.CustomTimerRepo
import com.bcn.deepfocus.utils.Response
import kotlinx.coroutines.flow.Flow

class GetFocusSetUseCase(
    private val customTimerRepo: CustomTimerRepo
) {
    operator fun invoke(id: String): Flow<Response<FocusSet>> {
        return customTimerRepo.getFocusSet(id)
    }
}
