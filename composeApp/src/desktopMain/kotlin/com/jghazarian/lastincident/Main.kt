package com.jghazarian.lastincident

import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import com.jghazarian.lastincident.di.initKoin
import com.jghazarian.lastincident.navigation.IncidentNavHost

fun main() = application {
//    val koin = initKoin().koin
    val koin = initKoin {
        modules(appModule)
    }
    val windowState = rememberWindowState()
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "LastIncident",
    ) {
//        App()
//        IncidentApp()
        IncidentNavHost(navController = rememberNavController())
//        CommonScreen(
//            navigateToIncidentEntry = {},
//            modifier = Modifier,
//            timerViewModel = TimerViewModel()
//        )
    }
}