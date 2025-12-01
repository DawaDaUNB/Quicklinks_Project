package com.app.quicklinks.ui

import androidx.core.net.toUri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.quicklinks.QuicklinksApp
import com.app.quicklinks.R
import com.app.quicklinks.viewmodel.ScanViewModel
import com.app.quicklinks.viewmodel.ScanViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

@Composable
fun ShortenerScreen(navController: NavController) {

    val clipboardManager = LocalClipboardManager.current
    var urlText by rememberSaveable { mutableStateOf("") }
    var saveUrl by rememberSaveable { mutableStateOf("") }
    var shortenedUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val client = remember { OkHttpClient() }
    val scope = rememberCoroutineScope()

    val app = LocalContext.current.applicationContext as QuicklinksApp
    val viewModel: ScanViewModel = viewModel(
        factory = ScanViewModelFactory(app.repository)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5487E2))
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Shorten URL",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            value = urlText,
                            onValueChange = { urlText = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Enter URL") }
                        )

                        IconButton(onClick = {
                            urlText = clipboardManager.getText().toString()
                        }, enabled = !isLoading) {
                            Icon(
                                Icons.Filled.ContentPaste,
                                contentDescription = "Copy paste URL",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                    }

                    Button(
                        onClick = {
                            if (urlText.isNotBlank()) {
                                isLoading = true
                                shortenedUrl = null
                                scope.launch {
                                    saveUrl = urlText
                                    val result = shortenUrl(client, urlText)
                                    shortenedUrl = result
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && urlText != "",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4487E2),
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (isLoading) "Shortening..." else "Shorten URL")
                    }

                    shortenedUrl?.let { short ->

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Shortened URL:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = short,
                            color = Color(0xFF4487E2),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (!short.contains("Error")) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Button(onClick = {
                                    clipboardManager.setText(AnnotatedString(short))
                                }) {
                                    Text("Copy")
                                }

                                Button(onClick = {
                                    shortenedUrl?.let {
                                        viewModel.saveScan(
                                            saveUrl,
                                            saveUrl,
                                            short
                                        )
                                    }
                                }) {
                                    Text("Save")
                                }
                                Button(onClick = {
                                    navController.context.startActivity(
                                        android.content.Intent(
                                            android.content.Intent.ACTION_VIEW,
                                            short.toUri()
                                        )
                                    )
                                }) {
                                    Text("Open")
                                }
                            }
                        }
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
