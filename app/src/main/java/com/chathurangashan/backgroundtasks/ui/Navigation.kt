package com.chathurangashan.backgroundtasks.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chathurangashan.backgroundtasks.ui.screen.*

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
        composable(route = Screen.ServiceTypesScreen.route){
            ServiceTypesScreen(navigationController)
        }
        composable(route = Screen.BackgroundServiceExampleScreen.route){
            BackgroundServiceExampleScreen(navigationController)
        }
        composable(route = Screen.ForegroundServiceExampleScreen.route){
            ForegroundServiceExampleScreen(navigationController)
        }
        composable(route = Screen.BindServiceExampleScreen.route){
            BindServiceExampleScreen(navigationController)
        }
        composable(route = Screen.WorkManagerTypeScreen.route){
            WorkManagerTypeScreen(navigationController)
        }
        composable(route = Screen.OneTimeImmediateWorkManagerExampleScreen.route){
            OneTimeWorkManagerExampleScreen(navigationController)
        }
    }
}