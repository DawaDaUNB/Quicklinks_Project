package com.app.quicklinks.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.quicklinks.QuicklinksApp
import com.app.quicklinks.R
import com.app.quicklinks.viewmodel.ScanViewModel
import com.app.quicklinks.viewmodel.ScanViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {

    val app = LocalContext.current.applicationContext as QuicklinksApp
    val viewModel: ScanViewModel = viewModel(
        factory = ScanViewModelFactory(app.repository)
    )

    val history by viewModel.history.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            popUpTo("history") { inclusive = true }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF4487E2),
                    titleContentColor = androidx.compose.ui.graphics.Color.White
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
//            Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = {
//                        viewModel.loadHistoryAlphabetical()
//                    }) {
//                        Icon(Icons.Filled.SortByAlpha, contentDescription = "Search by Id")
//                    }
//
//                    IconButton(onClick = {
//                        viewModel.loadHistory()
//                    }) {
//                        Icon(Icons.Filled.DateRange, contentDescription = "Search by Date")
//                    }
//                }
            if (history.isEmpty()) {
                item {
                    Text("No scans yet.")
                }
            } else {

                items(history) { scan ->
                    (
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = {
                                    viewModel.updateFavorite(scan, (!scan.favorite))
                                }) {
                                    if (scan.favorite) {
                                        Icon(
                                            Icons.Filled.CheckBox,
                                            contentDescription = "Unfavorite"
                                        )
                                    } else {
                                        Icon(
                                            Icons.Filled.CheckBoxOutlineBlank,
                                            contentDescription = "Favorite"
                                        )
                                    }
                                }

                                Text(
                                    scan.text,
                                    modifier = Modifier.weight(1f)
                                )

                                val clipboard = LocalClipboardManager.current

                                IconButton(onClick = {
                                    clipboard.setText(AnnotatedString(scan.text))
                                }) {
                                    Icon(Icons.Filled.CopyAll, contentDescription = "Copy scan")
                                }

                                var showDeleteDialog by remember { mutableStateOf(false) }

                                IconButton(onClick = { showDeleteDialog = true }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete scan")
                                }

                                if (showDeleteDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDeleteDialog = false },
                                        title = { Text("Delete scan?") },
                                        text = { Text("Are you sure you want to delete this scan? This action cannot be undone.") },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                viewModel.deleteScan(scan)
                                                showDeleteDialog = false
                                            }) {
                                                Text("Delete")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDeleteDialog = false }) {
                                                Text("Cancel")
                                            }
                                        }
                                    )
                                }
                            })
                    if (viewModel.fold){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),

                            ) {
                            Text(
                                scan.originalUrl,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                scan.shortcode,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
