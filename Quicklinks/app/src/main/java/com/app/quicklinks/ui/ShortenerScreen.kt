package com.app.quicklinks.ui

import android.widget.Toast
import androidx.core.net.toUri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.quicklinks.QuicklinksApp
import com.app.quicklinks.viewmodel.ScanViewModel
import com.app.quicklinks.viewmodel.ScanViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortenerScreen(navController: NavController) {
    // Use the new Clipboard API
    val clipboardManager = LocalClipboardManager.current
    var urlText by remember{ mutableStateOf("") }
    var shortenedUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val client = remember { OkHttpClient() }
    val scope = rememberCoroutineScope()

    val app = LocalContext.current.applicationContext as QuicklinksApp
    val viewModel: ScanViewModel = viewModel(
        factory = ScanViewModelFactory(app.repository)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("URL Shortener") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = urlText,
                onValueChange = { urlText = it },
                label = { Text("Enter URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    urlText = clipboardManager.getText().toString()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.ContentPaste,
                    contentDescription = "Edit URL",
                    modifier = Modifier.size(18.dp)
                )
            }


            Button(
                onClick = {
                    if (urlText.isNotBlank()) {
                        isLoading = true
                        shortenedUrl = null
                        scope.launch {
                            val result = shortenUrl(client, urlText)
                            shortenedUrl = result
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Shortening..." else "Shorten URL")
            }

            shortenedUrl?.let { short ->
                Text("Shortened URL:", style = MaterialTheme.typography.titleMedium)
                Text(short, color = MaterialTheme.colorScheme.primary)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Copy to clipboard with new API
                    Button(onClick = {
                        clipboardManager.setText(AnnotatedString(short))
                    }) {
                        Text("Copy")
                    }
                    // TODO: Save link to history
                    Button(onClick = {
                        shortenedUrl?.let { viewModel.saveScan(urlText,urlText,it) }
                    }) {
                        Text("Save")
                    }
                    //Open link to check accuracy
                    Button(onClick = {
                        navController.context.startActivity(
                            android.content.Intent(android.content.Intent.ACTION_VIEW, short.toUri())
                        )
                    }) {
                        Text("Open")
                    }
                }
            }
        }
    }
}

suspend fun shortenUrl(client: OkHttpClient, longUrl: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val encoded = URLEncoder.encode(longUrl, "UTF-8")
            val request = Request.Builder()
                .url("https://is.gd/create.php?format=simple&url=$encoded")
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?.trim() ?: "Error: Empty response"
            } else {
                "Error: ${response.code}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}