package com.chathurangashan.backgroundtasks.ui

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object ThreadExampleScreen: Screen("thread_example")
    object ServiceTypesScreen: Screen("service_example")
    object ForegroundServiceExampleScreen: Screen("foreground_service_example")
    object BackgroundServiceExampleScreen: Screen("background_service_example")
}