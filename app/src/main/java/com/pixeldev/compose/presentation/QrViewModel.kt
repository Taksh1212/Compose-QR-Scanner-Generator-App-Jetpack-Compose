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
package com.pixeldev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pixeldev.compose.data.local.QrCodeEntity
import com.pixeldev.compose.domain.repository.QrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrViewModel @Inject constructor(private val repository: QrRepository) : ViewModel() {
    val allQrCodes = repository.allQrCodes.asLiveData()
    val favorites = repository.favoriteQrCodes.asLiveData()

    fun insert(content: String, isScanned: Boolean, image: ByteArray?) {
        viewModelScope.launch {
            val qrCode = QrCodeEntity(
                content = content,
                isScanned = isScanned,
                image = image
            )
            repository.insert(qrCode)
        }
    }
    fun insertScannedQrCode(content: String) {
        viewModelScope.launch {
            val entity = QrCodeEntity(
                content = content,
                isScanned = true,
                createdAt = System.currentTimeMillis()
            )
            repository.insert(entity)
        }
    }

    fun delete(qr: QrCodeEntity) = viewModelScope.launch {
        repository.delete(qr)
    }

    fun toggleFavorite(qr: QrCodeEntity) = viewModelScope.launch {
        repository.update(qr.copy(isFavorite = !qr.isFavorite))
    }
}