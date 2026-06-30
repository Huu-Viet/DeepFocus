package com.bcn.deepfocus.model.timer.data.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bcn.deepfocus.R
import com.bcn.deepfocus.model.timer.domain.entity.Timer
import com.bcn.deepfocus.model.timer.domain.entity.TimerPhase
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import com.bcn.deepfocus.utils.Key
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService: Service() {
    @Inject
    lateinit var timerRepo: TimerRepository
    private var timerJob: Job? = null
    private var sessionState = Timer(
        focusSeconds = 25 * 60,
        breakSeconds = 5 * 60,
        totalSeconds = 25 * 60,
        remainingSeconds = 25 * 60,
        isRunning = false,
        timerPhase = TimerPhase.FOCUS,
        completedCycles = 0
    )
    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when(action) {
            Key.TIMER_START -> {
                val focusSeconds = intent.getIntExtra(Key.TIMER_FOCUS_DURATION, 25 * 60)
                val breakSeconds = intent.getIntExtra(Key.TIMER_BREAK_DURATION, 5 * 60)
                startForeground(
                    Key.NOTIFICATION_TIMER_ID,
                    createNotification(focusSeconds)
                )
                startTimer(focusSeconds, breakSeconds)
            }

            Key.TIMER_STOP -> {
                stopTimer()
            }

            Key.TIMER_RESTART -> {
                restartCurrentPhase()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timerJob?.cancel()
        super.onDestroy()
    }

    private fun createNotification(
        time: Int
    ): Notification {

        return NotificationCompat.Builder(
            this,
            Key.NOTIFICATION_CHANNEL_ID,
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Deep Focus")
            .setContentText(time.toString())
            .setOngoing(true)
            .build()
    }

    private fun startTimer(focusSeconds: Int, breakSeconds: Int) {
        timerJob?.cancel()
        sessionState = Timer(
            focusSeconds = focusSeconds,
            breakSeconds = breakSeconds,
            totalSeconds = focusSeconds,
            remainingSeconds = focusSeconds,
            isRunning = true,
            timerPhase = TimerPhase.FOCUS,
            completedCycles = 0
        )

        timerJob = serviceScope.launch {
            timerRepo.updateTimer(sessionState)

            while (isActive) {
                delay(1000)

                val remaining = sessionState.remainingSeconds - 1

                sessionState = if (remaining > 0) {
                    sessionState.copy(
                        remainingSeconds = remaining,
                        isRunning = true
                    )
                } else {
                    moveToNextPhase()
                }

                timerRepo.updateTimer(sessionState)

//                updateNotification(remaining)
            }
        }
    }

    private fun restartCurrentPhase() {
        val phaseDuration = when (sessionState.timerPhase) {
            TimerPhase.FOCUS -> sessionState.focusSeconds
            TimerPhase.BREAK -> sessionState.breakSeconds
        }
        sessionState = sessionState.copy(
            totalSeconds = phaseDuration,
            remainingSeconds = phaseDuration,
            isRunning = true
        )
        timerRepo.updateTimer(sessionState)
    }

    private fun moveToNextPhase(): Timer {
        return when (sessionState.timerPhase) {
            TimerPhase.FOCUS -> sessionState.copy(
                timerPhase = TimerPhase.BREAK,
                totalSeconds = sessionState.breakSeconds,
                remainingSeconds = sessionState.breakSeconds,
                isRunning = true
            )

            TimerPhase.BREAK -> sessionState.copy(
                timerPhase = TimerPhase.FOCUS,
                totalSeconds = sessionState.focusSeconds,
                remainingSeconds = sessionState.focusSeconds,
                isRunning = true,
                completedCycles = sessionState.completedCycles + 1
            )
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        sessionState = sessionState.copy(
            remainingSeconds = sessionState.totalSeconds,
            isRunning = false
        )
        timerRepo.updateTimer(sessionState)

        stopForeground(STOP_FOREGROUND_REMOVE)

        stopSelf()
    }

    private fun updateNotification(remaining: Int) {
        TODO("Not yet implemented")
    }
}
