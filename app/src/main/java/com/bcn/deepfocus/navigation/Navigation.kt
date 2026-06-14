package com.bcn.deepfocus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bcn.deepfocus.presentation.custom.ui.TimeCustomScreen
import com.bcn.deepfocus.presentation.history.ui.HistoryScreen
import com.bcn.deepfocus.presentation.home.ui.HomeScreen
import com.bcn.deepfocus.presentation.setting.ui.SettingScreen

@Composable
fun Navigation(
    modifier: Modifier,
    navHostController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Route.Home().route
    ) {
        composable (
            route = Route.Home().route
        ) {
            HomeScreen()
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
            route = Route.TimeCustom().route
        ) {
            TimeCustomScreen()
        }

    }
}