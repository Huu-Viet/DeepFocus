package com.bcn.deepfocus.model.timer.data.service

import android.content.Context
import android.content.Intent
import com.bcn.deepfocus.utils.Key

class TimerServiceManager(
    private val context: Context
) {
    fun startTimer(durationMinutes: Int) {
        val intent = Intent(context, TimerForegroundService::class.java).apply {
            action = Key.TIMER_START
            putExtra(Key.TIMER_DURATION, durationMinutes)
        }
        context.startForegroundService(intent)
    }

    fun stopTimer() {
        val intent = Intent(context, TimerForegroundService::class.java).apply {
            action = Key.TIMER_STOP
        }
        context.startForegroundService(intent)
    }

    fun restart(durationMinutes: Int) {
        val intent = Intent(context, TimerForegroundService::class.java).apply {
            action = Key.TIMER_RESTART
            putExtra(Key.TIMER_DURATION, durationMinutes)
        }
        context.startForegroundService(intent)
    }
}