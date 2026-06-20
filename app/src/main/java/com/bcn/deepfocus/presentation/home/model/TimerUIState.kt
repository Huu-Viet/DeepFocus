package com.bcn.deepfocus.presentation.home.model

data class TimerUIState(
    val totalSeconds: Int,
    val remainingSeconds: Int,
    val isRunning: Boolean,
    val progress: Float,
    val timerPhase: TimerPhase
)
