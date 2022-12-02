package com.chathurangashan.backgroundtasks.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chathurangashan.backgroundtasks.ui.screen.MainScreen
import com.chathurangashan.backgroundtasks.ui.screen.ThreadExampleScreen

@Composable
fun Navigation(){
    val navigationController = rememberNavController()
    NavHost(navController = navigationController, startDestination = Screen.MainScreen.route){
        composable(route = Screen.MainScreen.route){
            MainScreen(navigationController)
        }
        composable(route = Screen.ThreadExampleScreen.route){
            ThreadExampleScreen(navigationController)
        }
    }
}