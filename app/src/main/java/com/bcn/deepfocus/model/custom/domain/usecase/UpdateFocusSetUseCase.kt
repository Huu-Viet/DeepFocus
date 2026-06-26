package com.bcn.deepfocus.model.custom.domain.usecase

import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.custom.domain.repo.CustomTimerRepo

class UpdateFocusSetUseCase(
    private val customTimerRepo: CustomTimerRepo
) {
    suspend operator fun invoke(focusSet: FocusSet) {
        customTimerRepo.updateFocusSet(focusSet)
    }
}
