package com.chathurangashan.backgroundtasks.ui

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object ThreadExampleScreen: Screen("thread_example")
    object ServiceTypesScreen: Screen("service_example")
    object ForegroundServiceExampleScreen: Screen("foreground_service_example")
    object BackgroundServiceExampleScreen: Screen("background_service_example")
    object BindServiceExampleScreen: Screen("bind_service_example")
    object WorkManagerTypeScreen: Screen("work_manager_example")
    object OneTimeImmediateWorkManagerExampleScreen: Screen("one_time_immediate_example")
}