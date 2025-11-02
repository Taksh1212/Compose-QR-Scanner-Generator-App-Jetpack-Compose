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
package com.pixeldev.compose.presentation.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pixeldev.compose.core.common.Utlis.byteArrayToBitmap
import com.pixeldev.compose.presentation.QrViewModel
import com.pixeldev.compose.data.local.QrCodeEntity
import com.pixeldev.compose.ui.theme.DarkBackground
import com.pixeldev.compose.ui.theme.DarkSurface
import com.pixeldev.compose.ui.theme.PrimaryAccent
import com.pixeldev.compose.ui.theme.PrimaryText
import com.pixeldev.compose.ui.theme.SuccessColor

@Composable
fun QrListScreen(viewModel: QrViewModel) {
    val qrList by viewModel.allQrCodes.observeAsState(emptyList())

    if (qrList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No QR codes found.",
                color = PrimaryText
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            items(qrList.size) { index ->
                QrListItem(
                    qr = qrList[index],
                    onDelete = { viewModel.delete(qrList[index]) },
                    onFavoriteToggle = { viewModel.toggleFavorite(qrList[index]) }
                )
            }
        }
    }
}
@Composable
fun QrListItem(qr: QrCodeEntity, onDelete: () -> Unit, onFavoriteToggle: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface  // 🟦 Card background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            qr.image?.let { imageBytes ->
                val bitmap = byteArrayToBitmap(imageBytes)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 12.dp)
                )
            }

            Text(
                text = qr.content,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = PrimaryText  // 📝 Make text readable
            )

            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (qr.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (qr.isFavorite) SuccessColor else PrimaryAccent
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.8f)
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = DarkSurface,       // 🟦 Dialog background
            titleContentColor = PrimaryText,
            textContentColor = PrimaryText,
            title = {
                Text("QR Details", color = PrimaryText)
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    qr.image?.let {
                        val bitmap = byteArrayToBitmap(it)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(240.dp)
                                .padding(bottom = 8.dp)
                        )
                    }

                    Text(
                        text = qr.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = PrimaryText,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            },
            confirmButton = {
                IconButton(onClick = {
                    onFavoriteToggle()
                    showDialog = false
                }) {
                    Icon(
                        imageVector = if (qr.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if (qr.isFavorite) SuccessColor else PrimaryAccent
                    )
                }
            },
            dismissButton = {
                IconButton(onClick = {
                    onDelete()
                    showDialog = false
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.8f)
                    )
                }
            }
        )
    }
}
