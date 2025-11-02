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
package com.pixeldev.compose.core.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
object Utlis {
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun byteArrayToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * A common and reusable AlertDialog composable.
     *
     * @param showDialog State to control the visibility of the dialog.
     * @param onDismissRequest Lambda to be called when the user tries to dismiss the dialog
     * (e.g., by tapping outside or pressing the back button).
     * @param title The title of the dialog.
     * @param subtitle The subtitle of the dialog (optional, can be null).
     * @param message The main message content of the dialog.
     * @param onConfirm Lambda to be called when the "OK" button is clicked.
     * @param onCancel Lambda to be called when the "Cancel" button is clicked.
     * @param confirmButtonText The text for the "OK" button. Defaults to "OK".
     * @param cancelButtonText The text for the "Cancel" button. Defaults to "Cancel".
     */
    @Composable
    fun CommonAlertDialog(
        showDialog: Boolean,
        onDismissRequest: () -> Unit,
        title: String,
        subtitle: String? = null, // Optional subtitle
        message: String,
        onConfirm: () -> Unit,
        onCancel: () -> Unit,
        confirmButtonText: String = "OK",
        cancelButtonText: String = "Cancel"
    ) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = {
                    Column {
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        subtitle?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                },
                text = {
                    Text(text = message)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onConfirm()
                            onDismissRequest() // Dismiss dialog after action
                        }
                    ) {
                        Text(confirmButtonText)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onCancel()
                            onDismissRequest() // Dismiss dialog after action
                        }
                    ) {
                        Text(cancelButtonText)
                    }
                }
            )
        }
    }
    @Composable
    fun PreviewCommonAlertDialog() {
        val showSimpleDialog = remember { mutableStateOf(false) }
        val showComplexDialog = remember { mutableStateOf(false) }

        Column {
            Button(onClick = { showSimpleDialog.value = true }) {
                Text("Show Simple Dialog")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showComplexDialog.value = true }) {
                Text("Show Complex Dialog")
            }
        }


        // Simple Dialog Example
        CommonAlertDialog(
            showDialog = showSimpleDialog.value,
            onDismissRequest = { showSimpleDialog.value = false },
            title = "Simple Alert",
            message = "This is a simple alert dialog message.",
            onConfirm = { /* Handle OK action for simple dialog */ println("Simple OK clicked!") },
            onCancel = { /* Handle Cancel action for simple dialog */ println("Simple Cancel clicked!") }
        )

        // Complex Dialog Example
        CommonAlertDialog(
            showDialog = showComplexDialog.value,
            onDismissRequest = { showComplexDialog.value = false },
            title = "Confirm Action",
            subtitle = "Are you sure?",
            message = "Do you really want to proceed with this operation? This action cannot be undone.",
            onConfirm = { /* Handle OK action for complex dialog */ println("Complex OK clicked!") },
            onCancel = { /* Handle Cancel action for complex dialog */ println("Complex Cancel clicked!") },
            confirmButtonText = "Proceed",
            cancelButtonText = "Dismiss"
        )
    }
}