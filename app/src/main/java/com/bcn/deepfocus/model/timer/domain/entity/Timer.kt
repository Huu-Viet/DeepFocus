package com.bcn.deepfocus.model.timer.domain.entity

data class Timer(
    val focusSeconds: Int,
    val breakSeconds: Int,
    val totalSeconds: Int,
    val remainingSeconds: Int,
    val isRunning: Boolean,
    val timerPhase: TimerPhase,
    val completedCycles: Int
)
