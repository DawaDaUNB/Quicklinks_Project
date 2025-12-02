package com.app.quicklinks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.quicklinks.data.user.UserDatabase
import com.app.quicklinks.data.user.UserRepository
import com.app.quicklinks.ui.*
import com.app.quicklinks.viewmodel.UserViewModel
import com.app.quicklinks.viewmodel.UserViewModelFactory
import com.app.quicklinks.ui.theme.MyApplicationTheme
import com.app.quicklinks.viewmodel.LoginAuth
class MainActivity : ComponentActivity() {
    //val loginAuth: LoginAuth by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            MyApplicationTheme {

                val navController = rememberNavController()
                val context = LocalContext.current
                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(
                        UserRepository(
                            UserDatabase.getDatabase(context).userDao()
                        )
                    )
                )

                AppNavigation(navController, userViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onFinished = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }
        composable("signup") {
            SignupScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }
        composable("forgotPassword") {
            ForgotPasswordScreen(navController, userViewModel)
        }
        composable(NavRoutes.Home.route) { HomeScreen(navController) }
        composable(NavRoutes.Scanner.route) { ScannerScreen(navController) }
        composable(NavRoutes.Shortener.route) { ShortenerScreen(navController) }
        composable(NavRoutes.History.route) { HistoryScreen(navController) }
        composable(NavRoutes.Generator.route) { QrGeneratorScreen(navController) }
    }
}
