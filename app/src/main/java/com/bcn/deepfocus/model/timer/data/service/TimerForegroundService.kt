package com.bcn.deepfocus.model.timer.data.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bcn.deepfocus.MainActivity
import com.bcn.deepfocus.R
import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import com.bcn.deepfocus.model.timer.domain.entity.Timer
import com.bcn.deepfocus.model.timer.domain.entity.TimerPhase
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import com.bcn.deepfocus.presentation.home.model.EFocusPreset
import com.bcn.deepfocus.utils.Key
import com.bcn.deepfocus.utils.formatDuration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class TimerForegroundService: Service() {
    @Inject
    lateinit var timerRepo: TimerRepository
    @Inject
    lateinit var statisticRepo: StatisticRepository
    private var timerJob: Job? = null
    private var runningSession: FocusSession? = null
    private var isForegroundStarted = false
    private var alertPlayer: MediaPlayer? = null
    private var sessionState = Timer(
        focusSeconds = 25 * 60,
        breakSeconds = 5 * 60,
        totalSeconds = 25 * 60,
        remainingSeconds = 25 * 60,
        isRunning = false,
        timerPhase = TimerPhase.FOCUS,
        completedCycles = 0
    )
    private var currentSession = FocusSet(
        id = EFocusPreset.DEFAULT.name,
        focusMinute = EFocusPreset.DEFAULT.focusMinutes,
        breakMinute = EFocusPreset.DEFAULT.breakMinutes
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
                val id = intent.getStringExtra(Key.ID) ?: ""
                val focusSeconds = intent.getIntExtra(Key.TIMER_FOCUS_DURATION, 25 * 60)
                val breakSeconds = intent.getIntExtra(Key.TIMER_BREAK_DURATION, 5 * 60)
                startTimer(id, focusSeconds, breakSeconds)
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
        serviceScope.cancel()
        isForegroundStarted = false
        releaseAlertPlayer()
        super.onDestroy()
    }

    private fun createRunningSession(): FocusSession {
        return FocusSession(
            startAtMillis = System.currentTimeMillis(),
            localDate = LocalDate.now().toString()
        )
    }

    private fun createNotification(timer: Timer): Notification {
        val phaseLabel = when (timer.timerPhase) {
            TimerPhase.FOCUS -> getString(R.string.focus_time)
            TimerPhase.BREAK -> getString(R.string.break_time)
        }
        val contentText = "$phaseLabel • ${formatDuration(timer.remainingSeconds)}"

        //Intent to open the app from a notification
        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, Key.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setSilent(true)
            .setSound(null)
            .setProgress(timer.totalSeconds, timer.totalSeconds - timer.remainingSeconds, false)
            .addAction(0, getString(R.string.open_app), openPendingIntent)
            .build()
    }

    private fun startTimer(id: String, focusSeconds: Int, breakSeconds: Int) {
        timerJob?.cancel()
        val initialSession = createRunningSession()
        runningSession = initialSession

        sessionState = Timer(
            focusSeconds = focusSeconds,
            breakSeconds = breakSeconds,
            totalSeconds = focusSeconds,
            remainingSeconds = focusSeconds,
            isRunning = true,
            timerPhase = TimerPhase.FOCUS,
            completedCycles = 0
        )

        currentSession = FocusSet(
            id = id,
            focusMinute = focusSeconds / 60,
            breakMinute = breakSeconds / 60
        )

        updateNotification(sessionState)
        showPhaseAlert()

        timerJob = serviceScope.launch {
            val sessionId = statisticRepo.saveFocusSession(initialSession)
            runningSession = initialSession.copy(id = sessionId)

            timerRepo.updateTimer(sessionState)
            timerRepo.updateActiveSession(currentSession)

            while (isActive) {
                delay(1000)

                val previousPhase = sessionState.timerPhase
                val remaining = sessionState.remainingSeconds - 1

                sessionState = if (remaining > 0) {
                    sessionState.copy(
                        remainingSeconds = remaining,
                        isRunning = true
                    )
                } else {
                    runningSession = runningSession
                        ?.addCompletedPhase(sessionState)
                        ?.also { statisticRepo.updateFocusSession(it) }
                    moveToNextPhase()
                }

                timerRepo.updateTimer(sessionState)
                val phaseChanged = previousPhase != sessionState.timerPhase
                updateNotification(sessionState)
                if (phaseChanged) {
                    showPhaseAlert()
                }
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
        timerRepo.updateActiveSession(currentSession)
        updateNotification(sessionState)
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
        val finishedSession = runningSession
            ?.addElapsedCurrentPhase(sessionState)
            ?.copy(endAtMillis = System.currentTimeMillis())

        sessionState = sessionState.copy(
            remainingSeconds = sessionState.totalSeconds,
            isRunning = false,
            timerPhase = TimerPhase.FOCUS
        )
        timerRepo.updateTimer(sessionState)
        timerRepo.updateActiveSession(currentSession)

        serviceScope.launch {
            finishedSession?.let { session ->
                if (session.id == 0L) {
                    statisticRepo.saveFocusSession(session)
                } else {
                    statisticRepo.updateFocusSession(session)
                }
            }
            isForegroundStarted = false
            val manager = getSystemService(NotificationManager::class.java)
            manager.cancel(Key.NOTIFICATION_TIMER_ID)
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun updateNotification(timer: Timer) {
        val foregroundNotification = createNotification(timer)
        val manager = getSystemService(NotificationManager::class.java)

        if (!isForegroundStarted) {
            startForeground(Key.NOTIFICATION_TIMER_ID, foregroundNotification)
            isForegroundStarted = true
        } else {
            manager.notify(Key.NOTIFICATION_TIMER_ID, foregroundNotification)
        }
    }

    private fun showPhaseAlert() {
        releaseAlertPlayer()
        val player = MediaPlayer.create(this, R.raw.alarm) ?: return

        alertPlayer = player
        player.setOnCompletionListener {
            releaseAlertPlayer()
        }
        player.setOnErrorListener { _, _, _ ->
            releaseAlertPlayer()
            true
        }
        player.start()
    }

    private fun releaseAlertPlayer() {
        alertPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        alertPlayer = null
    }

    private fun FocusSession.addCompletedPhase(timer: Timer): FocusSession {
        return when (timer.timerPhase) {
            TimerPhase.FOCUS -> copy(
                totalFocusSeconds = totalFocusSeconds + timer.totalSeconds
            )

            TimerPhase.BREAK -> copy(
                totalBreakSeconds = totalBreakSeconds + timer.totalSeconds,
                completedCycles = completedCycles + 1
            )
        }
    }

    private fun FocusSession.addElapsedCurrentPhase(timer: Timer): FocusSession {
        val elapsedSeconds = (timer.totalSeconds - timer.remainingSeconds).coerceAtLeast(0)
        if (elapsedSeconds == 0) return this

        return when (timer.timerPhase) {
            TimerPhase.FOCUS -> copy(
                totalFocusSeconds = totalFocusSeconds + elapsedSeconds
            )

            TimerPhase.BREAK -> copy(
                totalBreakSeconds = totalBreakSeconds + elapsedSeconds
            )
        }
    }
}
