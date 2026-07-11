package com.bcn.deepfocus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bcn.deepfocus.core.ui.theme.LanguageMode
import com.bcn.deepfocus.core.ui.theme.ThemeMode
import com.bcn.deepfocus.presentation.custom.ui.TimeCustomScreen
import com.bcn.deepfocus.presentation.statistic.ui.StatisticScreen
import com.bcn.deepfocus.presentation.home.ui.HomeScreen
import com.bcn.deepfocus.presentation.setting.ui.SettingScreen
import com.bcn.deepfocus.utils.Key

@Composable
fun Navigation(
    modifier: Modifier,
    navHostController: NavHostController,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    languageMode: LanguageMode,
    onLanguageModeChange: (LanguageMode) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Route.Home().route
    ) {
        composable (
            route = Route.Home().route
        ) {
            HomeScreen(
                modifier = modifier,
                navController = navHostController)
        }

        composable (
            route = Route.Statistic().route
        ) {
            StatisticScreen(
                modifier = modifier
            )
        }

        composable (
            route = Route.Setting().route
        ) {
            SettingScreen(
                modifier = modifier,
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                languageMode = languageMode,
                onLanguageModeChange = onLanguageModeChange
            )
        }

        composable (
            route = Route.TimeCustom().routeWithArgs,
            arguments = listOf(
                navArgument(Key.FOCUS) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(Key.BREAK) {
                    type = NavType.StringType
                    nullable = true
                },
            )
        ) { navBackStackEntry ->
            val focus = navBackStackEntry.arguments?.getString(Key.FOCUS)
            val breakTime = navBackStackEntry.arguments?.getString(Key.BREAK)
            val id = navBackStackEntry.arguments?.getString(Key.ID)
            TimeCustomScreen(
                focusMinute = focus?.toInt(),
                breakMinute = breakTime?.toInt(),
                id = id,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }

    }
}
