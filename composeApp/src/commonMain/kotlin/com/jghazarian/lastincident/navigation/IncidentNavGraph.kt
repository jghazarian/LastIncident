package com.jghazarian.lastincident.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import co.touchlab.kermit.Logger
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
                navigateToIncidentDetail = {
//                    navController.currentBackStackEntry?.savedStateHandle?.set("incidentId", it)
                    navController.navigate("${DetailDestination.route}/${it}")
//                    navController.navigate(DetailDestination.route)
                                           },
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
            //TODO: learn more about how previousbackstackentry works and if we need it for savedstatehandle to work properly
//            val incidentId2 = navController.previousBackStackEntry?.savedStateHandle?.get<Long>("incidentId")
            Logger.d("bad route")
//            val incidentId2: Long? = navBackStackEntry.savedStateHandle.get<Long>("incidentId")
//            Logger.d("incidentId2: $incidentId2 and testid: $testid")
//            val incidentId: Long? = navBackStackEntry.arguments?.getLong(DetailDestination.incidentIdArg)
//            incidentId2?.let {
//                Logger.d("incidentid: $id")
            val tempid = navBackStackEntry.savedStateHandle.get<Long>("incidentId")
            Logger.d("tempid: $tempid")
                IncidentDetailScreen(
                    navigateBack = {
//                        navController.popBackStack()
                        navController.navigateUp()
                                   },
                    navigateToIncidentDetail = { navController.navigate("${DetailDestination.route}/${it}") },
//                    incidentId = 0,
                )
//            }
        }
        composable(
            route = DetailDestination.route,
        ) {
            Logger.d("correct route")
            val incidentId = navController.previousBackStackEntry?.savedStateHandle?.get<Long>("incidentId")
//            val incidentId = navBackStackEn
            incidentId?.let {
                Logger.d("incidentid: $it")
                IncidentDetailScreen(
                    navigateBack = {
//                        navController.popBackStack()
                        navController.navigateUp()
                                   },
                    navigateToIncidentDetail = { navController.navigate("${DetailDestination.route}/${it}") },
//                    incidentId = it,
                )
            }
        }
    }
}