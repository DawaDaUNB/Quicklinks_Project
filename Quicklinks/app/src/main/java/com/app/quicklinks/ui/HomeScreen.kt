package com.app.quicklinks.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.quicklinks.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("QuickLinks") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { navController.navigate(NavRoutes.Scanner.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Scan QR Code")
            }

            Button(
                onClick = { navController.navigate(NavRoutes.Shortener.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Type in URL")
            }

            Button(
                onClick = { navController.navigate(NavRoutes.Generator.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate QR Code")
            }

            Button(
                onClick = { navController.navigate(NavRoutes.History.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View History")
            }
        }
    }
}