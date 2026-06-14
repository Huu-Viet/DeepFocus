package com.bcn.deepfocus.navigation

sealed class Route {
    data class Splash(val route: String = "splash_screen"): Route()
    data class Home(val route: String = "home_screen"): Route()
    data class History(val route: String = "history_screen"): Route()
    data class Setting(val route: String = "setting_screen"): Route()
    data class TimeCustom(val route: String = "time_custom_screen"): Route()
}