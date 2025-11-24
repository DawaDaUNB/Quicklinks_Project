package com.app.quicklinks.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.app.quicklinks.utils.generateQrBitmap
import com.app.quicklinks.utils.saveQrToGallery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrGeneratorScreen() {
    val context = LocalContext.current

    var text by rememberSaveable { mutableStateOf("") }
    var foregroundColor by rememberSaveable { mutableStateOf(Color.BLACK) }
    var backgroundColor by rememberSaveable { mutableStateOf(Color.WHITE) }
    var borderSize by rememberSaveable { mutableStateOf(0f) }
    var borderColor by rememberSaveable { mutableStateOf(Color.BLACK) }
    var qrBitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }

    // auto-update preview
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
                }
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

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter text or URL") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(2.dp))
            Text("Foreground Color (RGB)")
            Spacer(Modifier.height(8.dp))

            var fgR by remember { mutableStateOf(foregroundColor shr 16 and 0xFF) }
            var fgG by remember { mutableStateOf(foregroundColor shr 8 and 0xFF) }
            var fgB by remember { mutableStateOf(foregroundColor and 0xFF) }

            Text("Red: $fgR")
            Slider(
                value = fgR.toFloat(),
                onValueChange = {
                    fgR = it.toInt()
                    foregroundColor = Color.rgb(fgR, fgG, fgB)
                },
                valueRange = 0f..255f
            )

            Text("Green: $fgG")
            Slider(
                value = fgG.toFloat(),
                onValueChange = {
                    fgG = it.toInt()
                    foregroundColor = Color.rgb(fgR, fgG, fgB)
                },
                valueRange = 0f..255f
            )

            Text("Blue: $fgB")
            Slider(
                value = fgB.toFloat(),
                onValueChange = {
                    fgB = it.toInt()
                    foregroundColor = Color.rgb(fgR, fgG, fgB)
                },
                valueRange = 0f..255f
            )

            Spacer(Modifier.height(20.dp))

            Text("Background Color (RGB)")
            Spacer(Modifier.height(8.dp))

            var bgR by remember { mutableStateOf(backgroundColor shr 16 and 0xFF) }
            var bgG by remember { mutableStateOf(backgroundColor shr 8 and 0xFF) }
            var bgB by remember { mutableStateOf(backgroundColor and 0xFF) }

            Text("Red: $bgR")
            Slider(
                value = bgR.toFloat(),
                onValueChange = {
                    bgR = it.toInt()
                    backgroundColor = Color.rgb(bgR, bgG, bgB)
                },
                valueRange = 0f..255f
            )

            Text("Green: $bgG")
            Slider(
                value = bgG.toFloat(),
                onValueChange = {
                    bgG = it.toInt()
                    backgroundColor = Color.rgb(bgR, bgG, bgB)
                },
                valueRange = 0f..255f
            )

            Text("Blue: $bgB")
            Slider(
                value = bgB.toFloat(),
                onValueChange = {
                    bgB = it.toInt()
                    backgroundColor = Color.rgb(bgR, bgG, bgB)
                },
                valueRange = 0f..255f
            )

            Spacer(Modifier.height(36.dp))

            // Border slider
            Text("Border Size: ${borderSize.toInt()}")
            Slider(
                value = borderSize,
                onValueChange = { borderSize = it },
                valueRange = 0f..20f
            )

            Text("Border Color (RGB)")
            Spacer(Modifier.height(8.dp))

            var brR by remember { mutableStateOf(borderColor shr 16 and 0xFF) }
            var brG by remember { mutableStateOf(borderColor shr 8 and 0xFF) }
            var brB by remember { mutableStateOf(borderColor and 0xFF) }

            Text("Red: $brR")
            Slider(
                value = brR.toFloat(),
                onValueChange = {
                    brR = it.toInt()
                    borderColor = Color.rgb(brR, brG, brB)
                },
                valueRange = 0f..255f
            )

            Text("Green: $brG")
            Slider(
                value = brG.toFloat(),
                onValueChange = {
                    brG = it.toInt()
                    borderColor = Color.rgb(brR, brG, brB)
                },
                valueRange = 0f..255f
            )

            Text("Blue: $brB")
            Slider(
                value = brB.toFloat(),
                onValueChange = {
                    brB = it.toInt()
                    borderColor = Color.rgb(brR, brG, brB)
                },
                valueRange = 0f..255f
            )

            Spacer(Modifier.height(20.dp))


            Spacer(Modifier.height(20.dp))

            // QR Preview
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