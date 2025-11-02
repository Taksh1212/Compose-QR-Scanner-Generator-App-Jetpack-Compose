/*
 * Copyright (C) 2025 Dinesh2510
 * All rights reserved.
 *
 * Author: Dinesh2510
 * Date: 2025-11-02
 *
 * This file is part of the QR Scan project.
 *
 * GitHub: https://github.com/Dinesh2510
 * YouTube Channel: https://www.youtube.com/@pixeldesigndeveloper
 *
 */
package com.pixeldev.compose.presentation.create

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.pixeldev.compose.presentation.QrViewModel
import com.pixeldev.compose.core.common.Utlis.bitmapToByteArray
import com.pixeldev.compose.data.local.QrCodeEntity
import com.pixeldev.compose.ui.theme.DarkBackground
import com.pixeldev.compose.ui.theme.DarkSurface
import com.pixeldev.compose.ui.theme.PrimaryAccent
import com.pixeldev.compose.ui.theme.PrimaryBlack
import com.pixeldev.compose.ui.theme.PrimaryText
import com.pixeldev.compose.ui.theme.SuccessColor

@Composable
fun QrGeneratorScreen(viewModel: QrViewModel) {
    var text by rememberSaveable { mutableStateOf("") }
    var generatedText by rememberSaveable { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground) // 🌑 Screen background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text to generate QR", color = PrimaryText) },
            modifier = Modifier
                .fillMaxWidth()
               /* .background(DarkSurface, shape = RoundedCornerShape(8.dp))*/, // 🟦 Field background
            colors = TextFieldDefaults.colors(
                focusedTextColor = PrimaryText,
                unfocusedTextColor = PrimaryText.copy(alpha = 0.8f),
                disabledTextColor = PrimaryText.copy(alpha = 0.3f),

                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,

                cursorColor = PrimaryAccent,

                focusedIndicatorColor = PrimaryAccent,
                unfocusedIndicatorColor = PrimaryText.copy(alpha = 0.5f),

                focusedLabelColor = PrimaryAccent,
                unfocusedLabelColor = PrimaryText.copy(alpha = 0.7f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    generatedText = text
                }
            },
            enabled = text.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryAccent,
                contentColor =PrimaryBlack
            )
        ) {
            Text("Generate QR")
        }

        Spacer(modifier = Modifier.height(24.dp))

        generatedText?.let { data ->
            val bitmap = generateQrCode(data)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .border(2.dp, PrimaryAccent, shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val imageBytes = bitmapToByteArray(it)
                        viewModel.insert(data, isScanned = false, image = imageBytes)
                        Toast.makeText(context, "QR saved", Toast.LENGTH_SHORT).show()

                        // Reset after saving
                        text = ""
                        generatedText = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessColor,
                        contentColor = PrimaryBlack
                    )
                ) {
                    Text("Save QR")
                }
            }
        }
    }
}


fun generateQrCode(text: String): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val bmp = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}
/*To add multiple QR types (like Text, URL, Email, Phone, SMS, WiFi, Geo, etc.) to your QR code generator screen, we can introduce a dropdown (or segmented control) that allows the user to choose the QR content type. Based on the selected type, you can generate the appropriate string format.*/