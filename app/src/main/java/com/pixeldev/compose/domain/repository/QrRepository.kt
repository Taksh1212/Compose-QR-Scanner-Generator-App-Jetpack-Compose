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
