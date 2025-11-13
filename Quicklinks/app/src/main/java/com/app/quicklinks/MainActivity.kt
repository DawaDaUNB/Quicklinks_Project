package com.app.quicklinks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.quicklinks.ui.HomeScreen
import com.app.quicklinks.ui.HistoryScreen
import com.app.quicklinks.ui.ScannerScreen
import com.app.quicklinks.ui.ShortenerScreen
import com.app.quicklinks.ui.QrGeneratorScreen
import com.app.quicklinks.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route) { HomeScreen(navController) }
        composable(NavRoutes.Scanner.route) { ScannerScreen() }
        composable(NavRoutes.Shortener.route) { ShortenerScreen(navController) }
        composable(NavRoutes.History.route) { HistoryScreen(navController) }
        composable(NavRoutes.Generator.route) { QrGeneratorScreen() }
    }
}