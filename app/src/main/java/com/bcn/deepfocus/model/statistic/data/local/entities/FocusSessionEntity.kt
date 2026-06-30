package com.bcn.deepfocus.model.statistic.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "focus_session",
    indices = [
        Index(value = ["localDate"]),
        Index(value = ["startAtMillis"])
    ]
)
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val focusSetId: String? = null,
    val startAtMillis: Long,
    val endAtMillis: Long? = null,
    val totalFocusSeconds: Int = 0,
    val totalBreakSeconds: Int = 0,
    val completedCycles: Int = 0,
    val localDate: String,
)
