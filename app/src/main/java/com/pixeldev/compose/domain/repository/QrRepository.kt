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
package com.pixeldev.compose.domain.repository

import com.pixeldev.compose.data.local.QrCodeDao
import com.pixeldev.compose.data.local.QrCodeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrRepository @Inject constructor(private val dao: QrCodeDao) {
    val allQrCodes = dao.getAllQrCodes()
    val favoriteQrCodes = dao.getFavoriteQrCodes()

    suspend fun insert(qr: QrCodeEntity) = dao.insertQrCode(qr)
    suspend fun delete(qr: QrCodeEntity) = dao.deleteQrCode(qr)
    suspend fun update(qr: QrCodeEntity) = dao.updateQrCode(qr)
}
