package com.bcn.deepfocus

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.net.toUri
import com.bcn.deepfocus.utils.Key
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DeepFocusApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        createTimerChannel()
    }

    private fun createTimerChannel() {
        val channel = NotificationChannel(
            Key.NOTIFICATION_CHANNEL_ID,
            Key.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(channel)
    }
}