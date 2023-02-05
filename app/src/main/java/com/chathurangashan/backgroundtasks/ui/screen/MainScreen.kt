package com.chathurangashan.backgroundtasks.ui.screen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.chathurangashan.backgroundtasks.ui.Screen
import com.chathurangashan.backgroundtasks.ui.theme.Typography

@Composable
@Preview
fun MainScreen (navController: NavController = rememberNavController()) {

    val context = LocalContext.current
    val activity = context as Activity

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
                text = stringResource(R.string.background_tasks_main_title),
                style = Typography.h1,
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickThread(navController) },
            ) {
                Text( stringResource(R.string.thread_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickService(navController) },
            ) {
                Text( stringResource(R.string.service_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {  activity.finish() },
            ) {
                Text( stringResource(R.string.coroutine_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {  onClickWorkManager(navController) },
            ) {
                Text( stringResource(R.string.work_manager_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {  onClickAlarmManager(navController) },
            ) {
                Text( stringResource(R.string.alarm_button_text) )
            }
        }
    }
}

fun onClickThread(navController: NavController){
    navController.navigate(Screen.ThreadExampleScreen.route)
}

fun onClickService(navController: NavController){
    navController.navigate(Screen.ServiceTypesScreen.route)
}

fun onClickWorkManager(navController: NavController){
    navController.navigate(Screen.WorkManagerTypeScreen.route)
}

fun onClickAlarmManager(navController: NavController){
    navController.navigate(Screen.AlarmManagerExampleScreen.route)
}
