package com.pixeldev.compose.di

import android.content.Context
import androidx.room.Room
import com.pixeldev.compose.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "qr_db").build()

    @Provides
    fun provideDao(db: AppDatabase) = db.qrCodeDao()
}

