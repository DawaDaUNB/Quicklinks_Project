package com.app.quicklinks.ui

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.camera.core.ImageProxy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.quicklinks.QuicklinksApp
import com.app.quicklinks.R
import com.app.quicklinks.viewmodel.ScanViewModel
import com.app.quicklinks.viewmodel.ScanViewModelFactory
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavController) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val client = remember { OkHttpClient() }
    val app = LocalContext.current.applicationContext as QuicklinksApp
    val viewModel: ScanViewModel = viewModel(
        factory = ScanViewModelFactory(app.repository)
    )

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current

    var scannedCode by remember { mutableStateOf<String?>(null) }
    var lastScanTime by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.qr_scan), fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            popUpTo("scanner") { inclusive = true }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        if (cameraPermissionState.status.isGranted) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val barcodeScanner = BarcodeScanning.getClient()
                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy: ImageProxy ->
                            processImageProxy(barcodeScanner, imageProxy) { result ->
                                result?.let { value ->
                                    val now = System.currentTimeMillis()
                                    if (value == scannedCode && (now - (lastScanTime
                                            ?: 0L)) < 2000L
                                    ) {
                                        return@let
                                    }
                                    scannedCode = value
                                    lastScanTime = now
                                    try {
                                        val encoded = URLEncoder.encode(value, "UTF-8")
                                        val request = Request.Builder()
                                            .url("https://is.gd/create.php?format=simple&url=$encoded")
                                            .build()
                                        val response = client.newCall(request).execute()
                                        if (response.isSuccessful) {
                                            val shortcode = response.body?.string()?.trim() ?: "Error: Empty response"
                                            viewModel.saveScan(value,value, shortcode)

                                        } else {
                                            Toast.makeText(ctx,"Error: ${response.code}",Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(ctx,"Error: ${e.message}",Toast.LENGTH_SHORT).show()
                                    }
                                    viewModel.saveScan(value, value, value)
                                    Toast.makeText(ctx, "Code Scanned", Toast.LENGTH_SHORT).show()
                                    Log.d("QRScanner", "Detected: $value")
                                }
                            }
                        }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                analysis
                            )
                        } catch (exc: Exception) {
                            Log.e("QRScanner", "Camera binding failed", exc)
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )

            scannedCode?.let {
                Text(
                    text = "Scanned Code: $it",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LaunchedEffect(Unit) { cameraPermissionState.launchPermissionRequest() }
            Text(
                text = "Camera permission required to scan QR codes",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)

            )
        }
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    imageProxy: ImageProxy,
    onResult: (String?) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val value = barcode.rawValue
                    if (value != null) {
                        onResult(value)
                        break
                    }
                }
            }
            .addOnFailureListener {
                Log.e("QRScanner", "Scan failed", it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}


suspend fun shorterUrl(client: OkHttpClient, longUrl: String): String {
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