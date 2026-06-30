package com.bcn.deepfocus.model.statistic.data.mapper_impl

import com.bcn.deepfocus.core.common.ILocalMapper
import com.bcn.deepfocus.model.statistic.data.local.entities.FocusSessionEntity
import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession

class LocalFocusSessionMapper : ILocalMapper<FocusSession, FocusSessionEntity> {
    override fun mapToDomain(e: FocusSessionEntity): FocusSession {
        return FocusSession(
            id = e.id,
            focusSetId = e.focusSetId,
            startAtMillis = e.startAtMillis,
            endAtMillis = e.endAtMillis,
            totalFocusSeconds = e.totalFocusSeconds,
            totalBreakSeconds = e.totalBreakSeconds,
            completedCycles = e.completedCycles,
            localDate = e.localDate
        )
    }

    override fun mapToEntity(d: FocusSession): FocusSessionEntity {
        return FocusSessionEntity(
            id = d.id,
            focusSetId = d.focusSetId,
            startAtMillis = d.startAtMillis,
            endAtMillis = d.endAtMillis,
            totalFocusSeconds = d.totalFocusSeconds,
            totalBreakSeconds = d.totalBreakSeconds,
            completedCycles = d.completedCycles,
            localDate = d.localDate
        )
    }
}
