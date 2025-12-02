package com.app.quicklinks.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.quicklinks.R
import com.app.quicklinks.viewmodel.UserViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.quicklinks.QuicklinksApp
import com.app.quicklinks.viewmodel.ScanViewModel
import com.app.quicklinks.viewmodel.ScanViewModelFactory
import com.app.quicklinks.viewmodel.LoginAuth
@Composable
fun LoginScreen(
    navController: NavController,
    loginAuth: LoginAuth,
    userViewModel: UserViewModel
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val app = LocalContext.current.applicationContext as QuicklinksApp
    val viewModel: ScanViewModel = viewModel(
        factory = ScanViewModelFactory(app.repository)
    )

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var loginError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        Text(
//            text = "QuickLinks",
//            color = MaterialTheme.colorScheme.onPrimary,
//            fontSize = 32.sp,
//            fontWeight = FontWeight.Bold
//        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(250.dp)
            )
        }

//        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = screenHeight)
                .background(
                    color = MaterialTheme.colorScheme.background,
                   // shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // EMAIL FIELD
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(text = stringResource(R.string.email), color = MaterialTheme.colorScheme.onBackground) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = stringResource(R.string.password), color = MaterialTheme.colorScheme.onBackground) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground

                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation =
                        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) R.drawable.ic_visibility_off
                                    else R.drawable.ic_visibility
                                ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (loginError != null) {
                    Text(
                        text = loginError!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        navController.navigate("forgotPassword")
                    }
                ) {
                    Text("Forgot Password?", color = Color(0xFF4487E2))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        userViewModel.loginUser(email, password) { success ->
                            if (success) {
                                loginError = null
                                userViewModel.getUserId(email, password) { userId ->
                                    if (userId != null) {
                                        loginAuth.login(userId)
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        loginError = "Error Logging In"
                                    }
                                }
//                                navController.navigate("home") {
//                                    popUpTo("login") { inclusive = true }
//                                }
                            } else {
                                loginError = "Invalid email or password"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4487E2)
                    )
                ) {
                    Text("Sign In", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = {

                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp,
                        brush = SolidColor(MaterialTheme.colorScheme.primary)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text("Continue as Guest", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.padding(20.dp))

                Row(
                    modifier = Modifier.padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don’t have an account? ", color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        "Sign Up",
                        color = Color(0xFF4487E2),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("signup")
                        }
                    )
                }
            }
        }
    }
}
