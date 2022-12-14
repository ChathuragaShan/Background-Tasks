package com.chathurangashan.backgroundtasks.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.chathurangashan.backgroundtasks.R

class MusicPlayBackgroundService : Service() {

    private val mediaPlayer by lazy { MediaPlayer.create(this, R.raw.sample_track) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        mediaPlayer.apply {
            isLooping  = true
            start()
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }
}