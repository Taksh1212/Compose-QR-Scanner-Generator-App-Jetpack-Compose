package com.pixeldev.compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QrCodeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun qrCodeDao(): QrCodeDao
}
