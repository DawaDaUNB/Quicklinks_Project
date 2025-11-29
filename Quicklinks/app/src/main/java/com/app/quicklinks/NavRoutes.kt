package com.app.quicklinks

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Login : NavRoutes("login")
    object Home : NavRoutes("home")
    object Scanner : NavRoutes("scanner")
    object Shortener : NavRoutes("shortener")
    object History : NavRoutes("history")
    object Generator : NavRoutes("generator")
}