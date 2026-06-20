package com.bcn.deepfocus.model.custom.domain.repo

import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.utils.Response
import kotlinx.coroutines.flow.Flow

interface CustomTimerRepo {
    suspend fun saveFocusSet(focusSet: FocusSet)
    fun getFocusSet(id: String): Flow<Response<FocusSet>>
    suspend fun updateFocusSet(fixedFocusSet: FocusSet)
    suspend fun deleteFocusSet(id: String)
}