package com.app.quicklinks.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class Scan(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val originalUrl: String,
    val shortcode: String,
    val qrCode: ByteArray?,
    val favorite: Boolean,
    val timestamp: Long
)