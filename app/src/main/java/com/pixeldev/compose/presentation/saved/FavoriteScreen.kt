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
package com.pixeldev.compose.presentation.saved

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.pixeldev.compose.presentation.QrViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.pixeldev.compose.data.local.QrCodeEntity
import com.pixeldev.compose.presentation.history.QrListItem
import com.pixeldev.compose.ui.theme.DarkBackground
import com.pixeldev.compose.ui.theme.PrimaryText

@Composable
fun FavoriteScreen(viewModel: QrViewModel) {
    val favorites by viewModel.favorites.observeAsState(emptyList())

    if (favorites.isEmpty()) {
        // ⛔ No items message
        Box(
            modifier = Modifier
                .fillMaxSize()  .background(DarkBackground)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No favorite QR codes found.",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryText
            )
        }
    } else {
        // ✅ Show the list
        LazyColumn( modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
        ) {
            items(favorites.size) { index ->
                val qr = favorites[index]
                QrListItem(
                    qr = qr,
                    onDelete = { viewModel.delete(qr) },
                    onFavoriteToggle = { viewModel.toggleFavorite(qr) }
                )
            }
        }
    }
}
