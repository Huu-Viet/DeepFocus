package com.bcn.deepfocus.navigation

import com.bcn.deepfocus.utils.Key

sealed class Route {
    data class Splash(val route: String = "splash_screen"): Route()
    data class Home(val route: String = "home_screen"): Route()
    data class History(val route: String = "history_screen"): Route()
    data class Setting(val route: String = "setting_screen"): Route()
    data class TimeCustom(
        val route: String = "time_custom_screen",
        val routeWithArgs: String = "$route?focus={${Key.FOCUS}}?break={${Key.BREAK}}"
    ): Route() {
        fun putArgs(focus: Int, breakTime: Int): String{
            return "$route?focus=$focus?break=$breakTime"
        }
    }
}