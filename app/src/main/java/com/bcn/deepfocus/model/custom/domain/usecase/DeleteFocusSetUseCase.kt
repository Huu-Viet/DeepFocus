package com.bcn.deepfocus.model.custom.domain.usecase

import com.bcn.deepfocus.model.custom.domain.repo.CustomTimerRepo

class DeleteFocusSetUseCase(
    private val customTimerRepo: CustomTimerRepo
) {
    suspend operator fun invoke(id: String) {
        customTimerRepo.deleteFocusSet(id)
    }
}
