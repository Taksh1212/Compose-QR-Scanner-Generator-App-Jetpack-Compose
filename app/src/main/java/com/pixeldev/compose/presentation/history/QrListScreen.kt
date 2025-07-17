package com.pixeldev.compose.presentation.history

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pixeldev.compose.core.common.Utlis.byteArrayToBitmap
import com.pixeldev.compose.presentation.QrViewModel
import com.pixeldev.compose.data.local.QrCodeEntity

@Composable
fun QrListScreen(viewModel: QrViewModel) {
    val qrList by viewModel.allQrCodes.observeAsState(emptyList())

    if (qrList.isEmpty()) {
        // Empty state UI
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No QR codes found.")
        }
    } else {
        // List UI
        LazyColumn {
            items(qrList.size) { qrCode ->
                QrListItem(
                    qrList[qrCode],
                    onDelete = { viewModel.delete(qrList[qrCode]) },
                    onFavoriteToggle = { viewModel.toggleFavorite(qrList[qrCode]) }
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
            .clickable { showDialog = true }, // 💡 Show dialog on click
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
                overflow = TextOverflow.Ellipsis
            )

            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (qr.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite"
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text("QR Details")
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
                        contentDescription = "Toggle Favorite"
                    )
                }
            },
            dismissButton = {
                IconButton(onClick = {
                    onDelete()
                    showDialog = false
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        )
    }
}
