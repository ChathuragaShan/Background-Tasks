package com.chathurangashan.backgroundtasks.ui.screen

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chathurangashan.backgroundtasks.R
import com.chathurangashan.backgroundtasks.services.MusicPlayBindService
import com.chathurangashan.backgroundtasks.ui.theme.Typography

@Composable
@Preview
fun BindServiceExampleScreen (navController: NavController = rememberNavController()){

    val context = LocalContext.current
    var bindService: MusicPlayBindService? by remember { mutableStateOf(null) }
    var isBound by remember { mutableStateOf (false) }

    val bindServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {

            val binder = service as MusicPlayBindService.MusicPlayBinder
            bindService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    LaunchedEffect(key1 = Unit){
        val intent = Intent(context, MusicPlayBindService::class.java)
        context.bindService(intent, bindServiceConnection, Context.BIND_AUTO_CREATE)
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                text = stringResource(R.string.bind_service_music_title),
                style = Typography.h1,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    onClick = { onClickStart(isBound,bindService) },
                ) {
                    Text( stringResource(R.string.music_start_button_text) )
                }
                Button(
                    onClick = { onClickStop(isBound, bindService) },
                ) {
                    Text( stringResource(R.string.music_stop_button_text) )
                }
            }
        }
    }
}

fun onClickStart(isBound: Boolean, service: MusicPlayBindService?){
    if(isBound){
        service?.startMediaPlayer()
    }
}

fun onClickStop(isBound: Boolean, service: MusicPlayBindService?){
    if(isBound){
        service?.stopMediaPlayer()
    }
}