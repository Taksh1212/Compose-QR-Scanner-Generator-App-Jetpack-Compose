package com.pixeldev.compose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_codes")
data class QrCodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val isFavorite: Boolean = false,
    val isScanned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val image: ByteArray? = null // ← NEW
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QrCodeEntity

        if (id != other.id) return false
        if (isFavorite != other.isFavorite) return false
        if (isScanned != other.isScanned) return false
        if (createdAt != other.createdAt) return false
        if (content != other.content) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + isFavorite.hashCode()
        result = 31 * result + isScanned.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
