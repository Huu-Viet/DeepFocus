package com.bcn.deepfocus.model.timer.data.service

import android.content.Context
import android.content.Intent
import com.bcn.deepfocus.utils.Key

class TimerServiceManager(
    private val context: Context
) {
    fun startTimer(id: String, focusSeconds: Int, breakSeconds: Int) {
        val intent = Intent(context, TimerForegroundService::class.java).apply {
            action = Key.TIMER_START
            putExtra(Key.ID, id)
            putExtra(Key.TIMER_FOCUS_DURATION, focusSeconds)
            putExtra(Key.TIMER_BREAK_DURATION, breakSeconds)
        }
        context.startForegroundService(intent)
    }

    fun stopTimer() {
        val intent = Intent(context, TimerForegroundService::class.java).apply {
            action = Key.TIMER_STOP
        }
        context.startForegroundService(intent)
    }

    fun restart() {
        val intent = Intent(context, TimerForegroundService::class.java).apply {
            action = Key.TIMER_RESTART
        }
        context.startForegroundService(intent)
    }
}
