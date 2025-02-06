package com.jghazarian.lastincident.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jghazarian.lastincident.screen.MainScreen
import com.jghazarian.lastincident.screen.IncidentDetailScreen
import com.jghazarian.lastincident.screen.IncidentEntryCommonScreen

@Composable
fun IncidentNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            MainScreen(
                navigateToIncidentDetail = { navController.navigate("${DetailDestination.route}/${it}") },
                navigateToIncidentEntry = { navController.navigate(EntryDestination.route) }
            )
        }

        composable(route = EntryDestination.route) {
            IncidentEntryCommonScreen(
                navigateBack = { navController.popBackStack()}
            )
        }

        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailDestination.incidentIdArg) {
                type = NavType.LongType
            })
        ) { navBackStackEntry ->
            val incidentId: Long? = navBackStackEntry.arguments?.getLong(DetailDestination.incidentIdArg)
            incidentId?.let {
                IncidentDetailScreen(
                    navigateBack = { navController.popBackStack() },
                    incidentId = it,
                )
            }
        }
    }
}