package com.jghazarian.lastincident

import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.jghazarian.lastincident.navigation.IncidentNavHost

fun MainViewController() = ComposeUIViewController {
    IncidentNavHost(navController = rememberNavController())
}