package com.jghazarian.lastincident

import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.jghazarian.lastincident.navigation.IncidentNavHost
import com.jghazarian.lastincident.theme.IncidentTheme

fun MainViewController() = ComposeUIViewController {
    IncidentTheme {
        IncidentNavHost(navController = rememberNavController())
    }
}