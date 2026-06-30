package com.bcn.deepfocus.model.statistic.data.repo_impl

import com.bcn.deepfocus.core.common.ILocalMapper
import com.bcn.deepfocus.model.statistic.data.local.dao.FocusSessionDao
import com.bcn.deepfocus.model.statistic.data.local.entities.FocusSessionEntity
import com.bcn.deepfocus.model.statistic.domain.entities.DailyFocusSummary
import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StatisticRepositoryImpl(
    private val focusSessionDao: FocusSessionDao,
    private val localMapper: ILocalMapper<FocusSession, FocusSessionEntity>
) : StatisticRepository {
    override suspend fun saveFocusSession(focusSession: FocusSession): Long {
        return focusSessionDao.insertSession(localMapper.mapToEntity(focusSession))
    }

    override suspend fun updateFocusSession(focusSession: FocusSession) {
        focusSessionDao.updateSession(localMapper.mapToEntity(focusSession))
    }

    override suspend fun getFocusSession(id: Long): FocusSession? {
        return focusSessionDao.getSession(id)?.let(localMapper::mapToDomain)
    }

    override fun observeFocusSession(id: Long): Flow<FocusSession?> {
        return focusSessionDao.observeSession(id)
            .map { entity -> entity?.let(localMapper::mapToDomain) }
    }

    override fun observeCompletedSessionsByDate(localDate: String): Flow<List<FocusSession>> {
        return focusSessionDao.observeCompletedSessionsByDate(localDate)
            .mapToDomainList()
    }

    override fun observeCompletedSessionsBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<List<FocusSession>> {
        return focusSessionDao.observeCompletedSessionsBetweenDates(startDate, endDate)
            .mapToDomainList()
    }

    override fun observeAbandonedSessionsByDate(localDate: String): Flow<List<FocusSession>> {
        return focusSessionDao.observeAbandonedSessionsByDate(localDate)
            .mapToDomainList()
    }

    override fun observeAbandonedSessionsBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<List<FocusSession>> {
        return focusSessionDao.observeAbandonedSessionsBetweenDates(startDate, endDate)
            .mapToDomainList()
    }

    override fun observeTotalFocusSecondsByDate(localDate: String): Flow<Int> {
        return focusSessionDao.observeTotalFocusSecondsByDate(localDate)
    }

    override fun observeDailyFocusSummaries(
        startDate: String,
        endDate: String
    ): Flow<List<DailyFocusSummary>> {
        return observeCompletedSessionsBetweenDates(startDate, endDate)
            .map { sessions ->
                sessions
                    .groupBy { it.localDate }
                    .map { (localDate, items) ->
                        DailyFocusSummary(
                            localDate = localDate,
                            totalFocusSeconds = items.sumOf { it.totalFocusSeconds },
                            totalBreakSeconds = items.sumOf { it.totalBreakSeconds },
                            completedCycles = items.sumOf { it.completedCycles },
                            sessionCount = items.size
                        )
                    }
                    .sortedBy { it.localDate }
            }
    }

    override suspend fun deleteFocusSession(id: Long) {
        focusSessionDao.deleteSession(id)
    }

    private fun Flow<List<FocusSessionEntity>>.mapToDomainList(): Flow<List<FocusSession>> {
        return map { entities ->
            entities.map(localMapper::mapToDomain)
        }
    }
}
