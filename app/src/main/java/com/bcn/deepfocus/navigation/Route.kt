package com.bcn.deepfocus.navigation

import com.bcn.deepfocus.utils.Key

sealed class Route {
    data class Splash(val route: String = "splash_screen"): Route()
    data class Home(val route: String = "home_screen"): Route()
    data class Statistic(val route: String = "statistic_screen"): Route()
    data class Setting(val route: String = "setting_screen"): Route()
    data class TimeCustom(
        val route: String = "time_custom_screen",
        val routeWithArgs: String = "$route?focus={${Key.FOCUS}}&break={${Key.BREAK}}&id={${Key.ID}}"
    ): Route() {
        fun putArgs(focus: Int, breakTime: Int, id: String): String{
            return "$route?focus=$focus&break=$breakTime&id=$id"
        }
    }
}