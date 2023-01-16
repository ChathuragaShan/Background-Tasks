package com.chathurangashan.backgroundtasks.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun WorkManagerTypeScreen (navController: NavController = rememberNavController()){
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
                text = stringResource(R.string.work_manger_title),
                style = Typography.h1,
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickOneTimeImmediateWork(navController) },
            ) {
                Text( stringResource(R.string.one_time_immediate_work_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickPeriodWork(navController) },
            ) {
                Text( stringResource(R.string.periodic_work_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickLongRunningWork(navController) },
            ) {
                Text( stringResource(R.string.long_running_work_button_text) )
            }
        }
    }
}

fun onClickOneTimeImmediateWork(navController: NavController){
    navController.navigate(Screen.OneTimeImmediateWorkManagerExampleScreen.route)
}

fun onClickPeriodWork(navController: NavController){
    navController.navigate(Screen.PeriodicWorkManagerExampleScreen.route)
}

fun onClickLongRunningWork(navController: NavController){
    navController.navigate(Screen.LongRunningWorkManagerExampleScreen.route)
}
