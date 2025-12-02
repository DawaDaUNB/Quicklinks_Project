package com.app.quicklinks.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.quicklinks.NavRoutes
import com.app.quicklinks.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.app.quicklinks.viewmodel.LoginAuth


@Composable
fun HomeScreen(navController: NavController, loginAuth: LoginAuth) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
        ) {
            //if (isLandscape) {}
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(min = screenHeight)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DashboardCard(
                    title = stringResource(R.string.qr_scan),
                    icon = Icons.Default.QrCodeScanner,
                    onClick = { navController.navigate(NavRoutes.Scanner.route) }
                )

                DashboardCard(
                    title = stringResource(R.string.shorten_url),
                    icon = Icons.Default.Link,
                    onClick = { navController.navigate(NavRoutes.Shortener.route) }
                )

                DashboardCard(
                    title = stringResource(R.string.qr_generator),
                    icon = Icons.Default.QrCode2,
                    onClick = { navController.navigate(NavRoutes.Generator.route) }
                )

                DashboardCard(
                    title = stringResource(R.string.history),
                    icon = Icons.Default.History,
                    onClick = { navController.navigate(NavRoutes.History.route) }
                )

                var showLogoutDialog by remember { mutableStateOf(false) }

                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = { Text(stringResource(R.string.logout)) },
                        text = { Text(stringResource(R.string.logout_confirm)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showLogoutDialog = false
                                loginAuth.logout()
                                navController.navigate(NavRoutes.Login.route) {
                                    popUpTo(NavRoutes.Home.route) { inclusive = true }
                                }
                            }) {
                                Text(stringResource(R.string.logout))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLogoutDialog = false }) {
                                Text(stringResource(R.string.cancel))
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Logout",
                        color = Color(0xFF4487E2),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            showLogoutDialog = true
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)

            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        //    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF4487E2),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 24.sp,
                color = Color(0xFF4487E2),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
