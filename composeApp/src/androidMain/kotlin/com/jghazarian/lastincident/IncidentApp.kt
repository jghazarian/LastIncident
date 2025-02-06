package com.jghazarian.lastincident

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jghazarian.lastincident.navigation.IncidentNavHost

@Composable
fun IncidentApp(navController: NavHostController = rememberNavController()) {
    IncidentNavHost(navController = navController)
}