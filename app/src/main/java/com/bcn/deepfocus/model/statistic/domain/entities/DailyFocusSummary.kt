package com.bcn.deepfocus.model.statistic.domain.entities

data class DailyFocusSummary(
    val localDate: String,
    val totalFocusSeconds: Int,
    val totalBreakSeconds: Int,
    val completedCycles: Int,
    val sessionCount: Int
)
