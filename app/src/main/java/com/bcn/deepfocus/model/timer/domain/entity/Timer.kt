package com.bcn.deepfocus.model.timer.domain.entity

data class Timer(
    val totalSeconds: Int,
    val remainingSeconds: Int,
    val isRunning: Boolean
)
