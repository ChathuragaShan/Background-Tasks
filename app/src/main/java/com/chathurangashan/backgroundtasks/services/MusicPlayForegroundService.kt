package com.chathurangashan.backgroundtasks.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.chathurangashan.backgroundtasks.R

private const val MUSIC_PLAY_NOTIFICATION_ID = 2
private const val CHANNEL_ID = "music_notification"
class MusicPlayForegroundService : Service() {

    private val mediaPlayer by lazy { MediaPlayer.create(this, R.raw.sample_track) }
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private lateinit var notification: Notification

    override fun onCreate() {
        super.onCreate()

        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.music_notification_title))
            .setContentText(getText(R.string.music_notification_description))
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID,
                "Music Play Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "This is music play notification"
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(MUSIC_PLAY_NOTIFICATION_ID, notification)
        }

        startForeground(MUSIC_PLAY_NOTIFICATION_ID, notification)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        mediaPlayer.apply {
            isLooping  = true
            start()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }

}