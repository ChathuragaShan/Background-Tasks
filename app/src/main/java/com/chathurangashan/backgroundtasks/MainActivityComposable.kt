package com.chathurangashan.backgroundtasks

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.chathurangashan.backgroundtasks.services.MusicPlayForegroundService
import com.chathurangashan.backgroundtasks.ui.Navigation

class MainActivityComposable : AppCompatActivity() {

    val sharedPreferences by lazy { (application as ThisApplication).sharedPreferences }
    val networkService by lazy { (application as ThisApplication).networkService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }

    override fun onDestroy() {
        val serviceIntent = Intent(this, MusicPlayForegroundService::class.java)
        stopService(serviceIntent)
        super.onDestroy()
    }
}