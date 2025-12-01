package com.app.quicklinks.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.quicklinks.R
import com.app.quicklinks.utils.generateQrBitmap
import com.app.quicklinks.utils.saveQrToGallery
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrGeneratorScreen(navController: NavController) {
    val context = LocalContext.current

    var text by rememberSaveable { mutableStateOf("") }
    var foregroundColor by rememberSaveable { mutableStateOf(Color.BLACK) }
    var backgroundColor by rememberSaveable { mutableStateOf(Color.WHITE) }
    var borderSize by rememberSaveable { mutableStateOf(0f) }
    var borderColor by rememberSaveable { mutableStateOf(Color.BLACK) }
    var qrBitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }

    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(text, foregroundColor, backgroundColor, borderSize, borderColor) {
        if (text.isNotBlank()) {
            qrBitmap = generateQrBitmap(
                text = text,
                size = 512,
                foreground = foregroundColor,
                background = backgroundColor,
                errorCorrection = ErrorCorrectionLevel.M,
                borderSize = borderSize.toInt(),
                borderColor = borderColor
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "QR Generator",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Spacer(Modifier.height(12.dp))

                        qrBitmap?.let { bm ->
                            Image(
                                bitmap = bm.asImageBitmap(),
                                contentDescription = "QR Preview",
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.Center)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)                     // same size as QR
                                .align(Alignment.CenterEnd)      // symmetrical padding
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            popUpTo("generator") { inclusive = true }
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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter URL") },
                )

                IconButton(onClick = {
                    text = clipboardManager.getText().toString()
                }) {
                    Icon(Icons.Filled.ContentPaste, contentDescription = "Copy paste URL")
                }

            }


            Spacer(Modifier.height(30.dp))
            Text("Foreground Color (RGB)")

            var fgR by rememberSaveable { mutableIntStateOf(foregroundColor shr 16 and 0xFF) }
            var fgG by rememberSaveable { mutableIntStateOf(foregroundColor shr 8 and 0xFF) }
            var fgB by rememberSaveable { mutableIntStateOf(foregroundColor and 0xFF) }

            ColorSliders("Red", fgR) { fgR = it; foregroundColor = Color.rgb(fgR, fgG, fgB) }
            ColorSliders("Green", fgG) { fgG = it; foregroundColor = Color.rgb(fgR, fgG, fgB) }
            ColorSliders("Blue", fgB) { fgB = it; foregroundColor = Color.rgb(fgR, fgG, fgB) }

            Spacer(Modifier.height(20.dp))
            Text("Background Color (RGB)")

            var bgR by rememberSaveable { mutableIntStateOf(backgroundColor shr 16 and 0xFF) }
            var bgG by rememberSaveable { mutableIntStateOf(backgroundColor shr 8 and 0xFF) }
            var bgB by rememberSaveable { mutableIntStateOf(backgroundColor and 0xFF) }

            ColorSliders("Red", bgR) { bgR = it; backgroundColor = Color.rgb(bgR, bgG, bgB) }
            ColorSliders("Green", bgG) { bgG = it; backgroundColor = Color.rgb(bgR, bgG, bgB) }
            ColorSliders("Blue", bgB) { bgB = it; backgroundColor = Color.rgb(bgR, bgG, bgB) }

            Spacer(Modifier.height(30.dp))
            Text("Border Size: ${borderSize.toInt()}")
            Slider(
                value = borderSize,
                onValueChange = { borderSize = it },
                valueRange = 0f..20f
            )

            Spacer(Modifier.height(20.dp))
            Text("Border Color (RGB)")

            var brR by rememberSaveable { mutableIntStateOf(borderColor shr 16 and 0xFF) }
            var brG by rememberSaveable { mutableIntStateOf(borderColor shr 8 and 0xFF) }
            var brB by rememberSaveable { mutableIntStateOf(borderColor and 0xFF) }

            ColorSliders("Red", brR) { brR = it; borderColor = Color.rgb(brR, brG, brB) }
            ColorSliders("Green", brG) { brG = it; borderColor = Color.rgb(brR, brG, brB) }
            ColorSliders("Blue", brB) { brB = it; borderColor = Color.rgb(brR, brG, brB) }


            Spacer(Modifier.height(20.dp))

            qrBitmap?.let { bm ->
                Image(
                    bitmap = bm.asImageBitmap(),
                    contentDescription = "Generated QR",
                    modifier = Modifier.size(250.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    qrBitmap?.let {
                        saveQrToGallery(context, it)
                        Toast.makeText(context, "Code saved in gallery", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Save to Gallery")
            }
        }
    }
}

@Composable
fun ColorSliders(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Text("$label: $value")
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        valueRange = 0f..255f
    )
}
