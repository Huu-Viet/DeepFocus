package com.bcn.deepfocus.presentation.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bcn.deepfocus.R
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import com.bcn.deepfocus.utils.Key
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService: Service() {
    @Inject
    lateinit var timerRepo: TimerRepository
    private var timerJob: Job? = null
    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val time = intent?.getIntExtra(Key.TIMER_DURATION, 0)
        val action = intent?.action
        when(action) {
            Key.TIMER_START -> {
                startForeground(
                    Key.NOTIFICATION_TIMER_ID,
                    createNotification(time ?: 25 )
                )
                startTimer(time ?: (25 * 60))
            }

            Key.TIMER_STOP -> {
                stopTimer()
            }

            Key.TIMER_RESTART -> {
                startTimer(time ?: (25 * 60))
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

    private fun startTimer(duration: Int) {
        timerJob?.cancel()

        timerJob = serviceScope.launch {
            var remaining = duration

            timerRepo.updateTime(remaining)

            while (isActive && remaining > 0) {
                delay(1000)

                remaining--

                timerRepo.updateTime(remaining)

//                updateNotification(remaining)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()

        stopForeground(STOP_FOREGROUND_REMOVE)

        stopSelf()
    }

    private fun updateNotification(remaining: Int) {
        TODO("Not yet implemented")
    }
}