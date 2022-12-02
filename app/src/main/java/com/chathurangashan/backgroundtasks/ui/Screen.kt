package com.chathurangashan.backgroundtasks.ui

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object ThreadExampleScreen: Screen("thread_example")
}