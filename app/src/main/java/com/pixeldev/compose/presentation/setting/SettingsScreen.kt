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
package com.pixeldev.compose.presentation.setting

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.pixeldev.compose.R
import com.pixeldev.compose.core.common.Utlis.CommonAlertDialog
import com.pixeldev.compose.presentation.create.QrGeneratorScreen
import com.pixeldev.compose.presentation.history.QrListScreen
import com.pixeldev.compose.presentation.saved.FavoriteScreen
import com.pixeldev.compose.presentation.QrViewModel
import com.pixeldev.compose.presentation.drawer.AboutScreen
import com.pixeldev.compose.presentation.drawer.ContactUsScreen
import com.pixeldev.compose.presentation.drawer.DummyListScreen
import com.pixeldev.compose.presentation.drawer.FAQsScreen
import com.pixeldev.compose.presentation.drawer.PrivacyPolicyScreen
import com.pixeldev.compose.presentation.drawer.TermsConditionsScreen
import com.pixeldev.compose.presentation.navigation.AppRoutes
import com.pixeldev.compose.presentation.scan.ScanQrScreen
import com.pixeldev.compose.ui.theme.DarkBackground
import com.pixeldev.compose.ui.theme.DarkSurface
import com.pixeldev.compose.ui.theme.PrimaryAccent
import com.pixeldev.compose.ui.theme.PrimaryText
import com.pixeldev.compose.ui.theme.SecondaryAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
    ) {
        LazyColumn(
            modifier = Modifier
                .background(DarkBackground)
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item { Text("Settings-> this is Dummy UI",  color = Color.White,) }
            item { SectionTitle("Theme") }

            item { SectionTitle("Scan Settings") }
            item { ToggleItem("Auto-open URL", true) }
            item { ToggleItem("Vibrate on Scan", false) }
            item { ToggleItem("Beep on Scan", true) }

            item { SectionTitle("Generate Settings") }
            item { ToggleItem("Add Logo to QR", false) }
            item { DropdownItem("Save Format", listOf("PNG", "SVG"), selected = "PNG") }

            item { SectionTitle("History") }
            item { ToggleItem("Enable History", true) }
            item { ButtonItem("Clear History") }

            item { SectionTitle("Security") }
            item { ToggleItem("Confirm before opening links", true) }

            item { SectionTitle("App") }
            item { InfoItem("App Version", "1.0.0") }
            item { ButtonItem("Rate the App") }
            item { ButtonItem("Share the App") }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ToggleItem(label: String, checked: Boolean, onToggle: (Boolean) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label,  color = Color.White,)
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

@Composable
fun DropdownItem(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label: $selected",  color = Color.White, modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(vertical = 8.dp))
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    })
            }
        }
    }
}


@Composable
fun ButtonItem(text: String, onClick: () -> Unit = {}) {
    TextButton(onClick = onClick, modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text,color = Color.White,)
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label,  color = Color.White,)
        Text(value,  color = Color.White,)
    }
}
