package com.chathurangashan.backgroundtasks.ui.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
import com.chathurangashan.backgroundtasks.services.MusicPlayBackgroundService
import com.chathurangashan.backgroundtasks.ui.theme.Typography

@Composable
@Preview
fun BackgroundServiceExampleScreen(navController: NavController = rememberNavController()){

    val context = LocalContext.current
    val serviceIntent = Intent(context,MusicPlayBackgroundService::class.java)

    LaunchedEffect(key1 = Unit){
        context.startService(serviceIntent)
    }

    DisposableEffect(key1 = Unit){
        onDispose {
            context.stopService(serviceIntent)
        }
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
                text = stringResource(R.string.background_music_title),
                style = Typography.h1,
                textAlign = TextAlign.Center
            )
        }
    }
}