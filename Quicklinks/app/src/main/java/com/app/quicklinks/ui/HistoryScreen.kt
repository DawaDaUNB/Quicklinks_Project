package com.app.quicklinks.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.quicklinks.QuicklinksApp
import com.app.quicklinks.R
import com.app.quicklinks.viewmodel.LoginAuth
import com.app.quicklinks.viewmodel.ScanViewModel
import com.app.quicklinks.viewmodel.ScanViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, loginAuth: LoginAuth) {
    //val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val app = LocalContext.current.applicationContext as QuicklinksApp
    val viewModel: ScanViewModel = viewModel(
        factory = ScanViewModelFactory(app.repository)
    )
    val currentUser = loginAuth.userId?: -1L
    val history by viewModel.history.collectAsState()
  //  var searchText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadHistory(currentUser)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = {
                    navController.navigate("home") {
                        popUpTo("history") { inclusive = true }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }},
                title = { Text(stringResource(R.string.history), fontSize = 32.sp) },
                actions = {



                    IconButton(onClick = { viewModel.loadHistoryAlphabetical(currentUser) }) {
                        Icon(
                            Icons.Filled.SortByAlpha,
                            contentDescription = "Search by Id",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                    }
                    IconButton(onClick = { viewModel.loadHistory(currentUser) }) {
                        Icon(
                            Icons.Filled.DateRange,
                            contentDescription = "Search by Date",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
//                    OutlinedTextField(
//                        value = searchText,
//                        onValueChange = { searchText = it },
//                        label = { Text("Type to Search") },
//                    )
//                    IconButton(onClick = { viewModel.searchHistory(searchText) }) {
//                        Icon(
//                            Icons.Filled.Search,
//                            contentDescription = "Search by Name",
//                            tint = MaterialTheme.colorScheme.onPrimary
//                        )
//                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                //.padding(padding)
                //.padding(16.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.secondary,MaterialTheme.colorScheme.background), // Your gradient colors
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                ),
        ) {
            if (history.isEmpty()) {
                item {
                    Text("No scans yet.")
                }
            } else {

                items(history) { scan ->

                    var retract by remember { mutableStateOf(true) }
                    var editName by remember { mutableStateOf(false) }

                    (
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    IconButton(onClick = {
                                        viewModel.updateFavorite(scan, (!scan.favorite), currentUser)
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
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                retract = !retract
                                            }
                                    )

                                    val clipboard = LocalClipboardManager.current

                                    IconButton(onClick = {
                                        clipboard.setText(AnnotatedString(scan.shortcode))
                                    }) {
                                        Icon(Icons.Filled.CopyAll, contentDescription = "Copy scan")
                                    }

                                    var showDeleteDialog by remember { mutableStateOf(false) }

                                    IconButton(onClick = { showDeleteDialog = true }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete scan"
                                        )
                                    }

                                    if (showDeleteDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showDeleteDialog = false },
                                            title = { Text(stringResource(R.string.delete)) },
                                            text = { Text("Are you sure you want to delete this scan? This action cannot be undone.") },
                                            confirmButton = {
                                                TextButton(onClick = {
                                                    viewModel.deleteScan(scan, currentUser)
                                                    showDeleteDialog = false
                                                }) {
                                                    Text(stringResource(R.string.delete))
                                                }
                                            },
                                            dismissButton = {
                                                TextButton(onClick = { showDeleteDialog = false }) {
                                                    Text(stringResource(R.string.cancel))
                                                }
                                            }
                                        )
                                    }
                                }
                                AnimatedVisibility(visible = !retract) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp),

                                        ) {
                                        Text("Original: " + scan.originalUrl)
                                        Spacer(Modifier.height(20.dp))
                                        Text("Shortcode: " + scan.shortcode)

                                        Spacer(Modifier.height(20.dp))
                                        if (editName) {
                                            var newName by remember { mutableStateOf("") }
                                            OutlinedTextField(
                                                value = newName,
                                                onValueChange = { newName = it },
                                                label = { Text("Enter new name") },
                                            )
                                            Spacer(Modifier.height(20.dp))
                                            Button(onClick = {
                                                viewModel.updateName(
                                                    scan,
                                                    newName,
                                                    currentUser
                                                ); editName = !editName
                                            }) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Rename Scan"
                                                )
                                                Text("Rename Scan")
                                            }
                                        } else {
                                            Button(onClick = { editName = !editName }) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Rename Scan"
                                                )
                                                Text("Rename Scan")
                                            }
                                        }

                                    }
                                }

                            })
                }
            }
        }
    }
}
