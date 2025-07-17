package com.pixeldev.compose.data.local


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QrCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQrCode(qrCode: QrCodeEntity)

    @Delete
    suspend fun deleteQrCode(qrCode: QrCodeEntity)

    @Update
    suspend fun updateQrCode(qrCode: QrCodeEntity)

    @Query("SELECT * FROM qr_codes ORDER BY createdAt DESC")
    fun getAllQrCodes(): Flow<List<QrCodeEntity>>

    @Query("SELECT * FROM qr_codes WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteQrCodes(): Flow<List<QrCodeEntity>>
}
