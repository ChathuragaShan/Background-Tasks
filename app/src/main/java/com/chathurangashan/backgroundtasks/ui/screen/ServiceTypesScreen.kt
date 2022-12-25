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
fun ServiceTypesScreen (navController: NavController = rememberNavController()){
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
                text = stringResource(R.string.service_type_main_title),
                style = Typography.h1,
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickBackgroundService(navController)  },
            ) {
                Text( stringResource(R.string.background_service_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickForegroundService(navController) },
            ) {
                Text( stringResource(R.string.foreground_service_button_text) )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClickBindService(navController) },
            ) {
                Text( stringResource(R.string.bind_service_button_text) )
            }
        }
    }
}

fun onClickBackgroundService(navController: NavController){
    navController.navigate(Screen.BackgroundServiceExampleScreen.route)
}

fun onClickForegroundService(navController: NavController){
    navController.navigate(Screen.ForegroundServiceExampleScreen.route)
}

fun onClickBindService(navController: NavController){
    navController.navigate(Screen.ForegroundServiceExampleScreen.route)
}