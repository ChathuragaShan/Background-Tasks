package com.chathurangashan.backgroundtasks.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.chathurangashan.backgroundtasks.R
import kotlinx.coroutines.NonCancellable.start

class MusicPlayBindService : Service() {

    private val binder = MusicPlayBinder()
    private val mediaPlayer by lazy { MediaPlayer.create(this, R.raw.sample_track) }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class MusicPlayBinder : Binder() {
        fun getService() : MusicPlayBindService {
            return this@MusicPlayBindService
        }
    }

    override fun onCreate() {
        mediaPlayer.apply {
            isLooping = true
        }

    }

    fun startMediaPlayer(){
        mediaPlayer.start()
    }

    fun stopMediaPlayer(){
        mediaPlayer.stop()
    }

}