package com.bcn.deepfocus.model.statistic.domain.entities

data class FocusSession(
    val id: Long = 0,
    val focusSetId: String? = null,
    val startAtMillis: Long,
    val endAtMillis: Long? = null,
    val totalFocusSeconds: Int = 0,
    val totalBreakSeconds: Int = 0,
    val completedCycles: Int = 0,
    val localDate: String,
)
