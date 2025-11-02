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
package com.pixeldev.compose.presentation.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import com.pixeldev.compose.ui.theme.DarkBackground
import com.pixeldev.compose.ui.theme.DarkSurface
import com.pixeldev.compose.ui.theme.PrimaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Us") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padd ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground) // 🌑 Screen background
                .padding(padd), contentAlignment = Alignment.Center
        ) {
            Text("About Us Full Screen Content", color = PrimaryText)
        }
    }
}
