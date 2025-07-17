package com.pixeldev.compose.presentation.scan

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.barcode.BarcodeScanning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize


import androidx.compose.foundation.background
import androidx.compose.material3.*

import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.AndroidView

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas

import androidx.core.content.ContextCompat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.pixeldev.compose.presentation.QrViewModel
import com.pixeldev.compose.presentation.home.BottomNavItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun ScanQrScreen(navController: NavHostController, viewModel: QrViewModel) {
    val context = LocalContext.current
    var barcode by rememberSaveable { mutableStateOf("") }
    var showRationaleDialog by remember { mutableStateOf(false) }

    var scannedDialogVisible by remember { mutableStateOf(false) }
    var isSending by remember { mutableStateOf(false) }
    val bottomNavController = rememberNavController() // You can pass it instead if needed

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) {
            showRationaleDialog = true
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            hasPermission -> {
                // Show QR scanner
                ScanCode(
                    onQrCodeDetected = { scannedCode ->
                        if (barcode != scannedCode) {
                            barcode = scannedCode
                            scannedDialogVisible = true
                        }
                    },
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.Center)
                )
            }

            showRationaleDialog -> {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Camera Permission Required") },
                    text = { Text("To scan QR codes, camera permission is required.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showRationaleDialog = false
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }) {
                            Text("Grant")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showRationaleDialog = false
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            else -> {
                Box(Modifier.align(Alignment.Center)) {
                    Text("Requesting camera permission...", color = Color.White)
                }
            }
        }

       /* if (barcode.isNotBlank()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Scanned: $barcode",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { barcode = "" }) {
                    Text("Scan Again")
                }
            }
        }*/
    }
    if (scannedDialogVisible) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("QR Code Detected") },
            text = { Text("Data: $barcode") },
            confirmButton = {
                Row {
                    TextButton(
                        enabled = !isSending,
                        onClick = {
                            // Save scanned QR code to DB
                            viewModel.insertScannedQrCode(barcode)

                            scannedDialogVisible = false
                            barcode = "" // Reset to allow scanning again

                            Toast.makeText(context, "QR code saved", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("Save")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(
                        enabled = !isSending,
                        onClick = {
                            // Just send & navigate home, if you want to keep that
                            isSending = true

                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    sendQrCodeToApi(barcode)

                                    withContext(Dispatchers.Main) {
                                        isSending = false
                                        scannedDialogVisible = false
                                        Toast.makeText(context, "Sent successfully", Toast.LENGTH_SHORT).show()
                                        navController.navigate(BottomNavItem.History.route) {
                                            popUpTo(BottomNavItem.Scan.route) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error sending data", Toast.LENGTH_SHORT).show()
                                        isSending = false
                                    }
                                }
                            }
                        }
                    ) {
                        Text(if (isSending) "Sending..." else "Send & Go Home")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isSending,
                    onClick = {
                        scannedDialogVisible = false
                        barcode = "" // allow scanning again
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


}


suspend fun sendQrCodeToApi(data: String) {
    delay(1500) // Simulate network call
    // e.g., use Retrofit or Ktor here to send `data`
}

@Composable
fun ScanCode(
    onQrCodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var barcode by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var qrCodeDetected by remember { mutableStateOf(false) }
    var boundingRect by remember { mutableStateOf<Rect?>(null) }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS or CameraController.IMAGE_CAPTURE)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(16.dp)),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    val options = BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                            Barcode.FORMAT_QR_CODE,
                            Barcode.FORMAT_CODABAR,
                            Barcode.FORMAT_CODE_93,
                            Barcode.FORMAT_CODE_39,
                            Barcode.FORMAT_CODE_128,
                            Barcode.FORMAT_EAN_8,
                            Barcode.FORMAT_EAN_13,
                            Barcode.FORMAT_AZTEC
                        ).build()

                    val barcodeScanner = BarcodeScanning.getClient(options)

                    cameraController.setImageAnalysisAnalyzer(
                        ContextCompat.getMainExecutor(ctx),
                        MlKitAnalyzer(
                            listOf(barcodeScanner),
                            ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                            ContextCompat.getMainExecutor(ctx)
                        ) { result ->
                            val barcodeResults = result?.getValue(barcodeScanner)
                            if (!barcodeResults.isNullOrEmpty()) {
                                val newCode = barcodeResults.first().rawValue
                                val newRect = barcodeResults.first().boundingBox

                                // Call onQrCodeDetected directly
                                onQrCodeDetected(newCode ?: "")
                                boundingRect = newRect
                        }}
                    )

                    cameraController.bindToLifecycle(lifecycleOwner)
                    this.controller = cameraController
                }
            }
        )

        // Draw red rectangle overlay after detection (optional)
        /*if (qrCodeDetected) {
            LaunchedEffect(Unit) {
                delay(100)
                onQrCodeDetected(barcode ?: "")
            }

            DrawRectangle(rect = boundingRect)
        }*/
    }
}

@Composable
fun DrawRectangle(rect: Rect?) {
    val composeRect = rect?.toComposeRect()

    composeRect?.let {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Red,
                topLeft = Offset(it.left, it.top),
                size = Size(it.width, it.height),
                style = Stroke(width = 5f)
            )
        }
    }
}
