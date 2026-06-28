package com.bcn.deepfocus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bcn.deepfocus.presentation.custom.ui.TimeCustomScreen
import com.bcn.deepfocus.presentation.history.ui.HistoryScreen
import com.bcn.deepfocus.presentation.home.ui.HomeScreen
import com.bcn.deepfocus.presentation.setting.ui.SettingScreen
import com.bcn.deepfocus.utils.Key

@Composable
fun Navigation(
    modifier: Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Route.Home().route
    ) {
        composable (
            route = Route.Home().route
        ) {
            HomeScreen(navController = navHostController)
        }

        composable (
            route = Route.History().route
        ) {
            HistoryScreen()
        }

        composable (
            route = Route.Setting().route
        ) {
            SettingScreen()
        }

        composable (
            route = Route.TimeCustom().routeWithArgs,
            arguments = listOf(
                navArgument(Key.FOCUS) {type = NavType.IntType},
                navArgument(Key.BREAK) {type = NavType.IntType},
            )
        ) { navBackStackEntry ->
            val focus = navBackStackEntry.arguments?.getInt(Key.FOCUS)
            val breakTime = navBackStackEntry.arguments?.getInt(Key.BREAK)
            TimeCustomScreen(
                focusMinute = focus,
                breakMinute = breakTime,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

    }
}