package com.bcn.deepfocus.model.statistic.domain.repo

import com.bcn.deepfocus.model.statistic.domain.entities.DailyFocusSummary
import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import kotlinx.coroutines.flow.Flow

interface StatisticRepository {
    suspend fun saveFocusSession(focusSession: FocusSession): Long
    suspend fun updateFocusSession(focusSession: FocusSession)
    suspend fun getFocusSession(id: Long): FocusSession?
    fun observeFocusSession(id: Long): Flow<FocusSession?>
    fun observeCompletedSessionsByDate(localDate: String): Flow<List<FocusSession>>
    fun observeCompletedSessionsBetweenDates(startDate: String, endDate: String): Flow<List<FocusSession>>
    fun observeAbandonedSessionsByDate(localDate: String): Flow<List<FocusSession>>
    fun observeAbandonedSessionsBetweenDates(startDate: String, endDate: String): Flow<List<FocusSession>>
    fun observeTotalFocusSecondsByDate(localDate: String): Flow<Int>
    fun observeDailyFocusSummaries(startDate: String, endDate: String): Flow<List<DailyFocusSummary>>
    suspend fun deleteFocusSession(id: Long)
}
