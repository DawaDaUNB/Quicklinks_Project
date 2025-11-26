package com.app.quicklinks.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.app.quicklinks.viewmodel.ScanViewModel
import com.app.quicklinks.QuicklinksApp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.quicklinks.viewmodel.ScanViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material.icons.filled.CopyAll

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
        topBar = { TopAppBar(title = { Text("History") }) }
    ) { padding ->

        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (history.isEmpty()) {
                item {
                    Text("No scans yet.")
                }
            } else {
                items(history) { scan ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Filled.CheckBoxOutlineBlank, contentDescription = "Copy scan")
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

                        IconButton(onClick = { viewModel.deleteScan(scan) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete scan")
                        }
                    }
                }
            }
        }
    }
}