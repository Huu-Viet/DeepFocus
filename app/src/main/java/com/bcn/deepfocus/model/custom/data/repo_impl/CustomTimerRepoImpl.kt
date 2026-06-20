package com.bcn.deepfocus.model.custom.data.repo_impl

import android.util.Log
import com.bcn.deepfocus.core.common.ILocalMapper
import com.bcn.deepfocus.model.custom.data.local.dao.FocusSetDao
import com.bcn.deepfocus.model.custom.data.local.entities.FocusSetEntity
import com.bcn.deepfocus.model.custom.data.mapper_impl.LocalFocusSetMapper
import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.custom.domain.repo.CustomTimerRepo
import com.bcn.deepfocus.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

private const val TAG = "CustomTimerRepoImpl"

class CustomTimerRepoImpl(
    private val focusSetDao: FocusSetDao,
    private val localMapper: ILocalMapper<FocusSet, FocusSetEntity>
): CustomTimerRepo
{
    override suspend fun saveFocusSet(focusSet: FocusSet) {
        try {
            val entity = localMapper.mapToEntity(focusSet)
            focusSetDao.insertFocusSet(entity)
        } catch (e: Exception) {
            Log.e(TAG, "saveFocusSet: ", e)
        }
    }

    override fun getFocusSet(id: String): Flow<Response<FocusSet>> =
        focusSetDao.getFocusSet(id)
            .map { entity ->
                entity?.let {
                    Response.Success(localMapper.mapToDomain(it))
                } ?: Response.Error("FocusSet not found")
            }
            .onStart {
                emit(Response.Loading())
            }
            .catch { e -> emit(Response.Error(e.message ?: "Get Focus Set Error")) }


    override suspend fun updateFocusSet(fixedFocusSet: FocusSet) {
        try {
            val entity = localMapper.mapToEntity(fixedFocusSet)
            focusSetDao.updateFocusSet(entity.id, entity.focusMinute, entity.breakMinute)
        } catch (e: Exception) {
            Log.e(TAG, "updateFocusSet: ", e)
        }
    }

    override suspend fun deleteFocusSet(id: String) {
        try {
            focusSetDao.deleteFocusSet(id)
        } catch (e: Exception) {
            Log.e(TAG, "deleteFocusSet: ", e)
        }
    }
}