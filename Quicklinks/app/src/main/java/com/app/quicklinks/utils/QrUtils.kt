package com.app.quicklinks.utils

import android.graphics.Color

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.os.Build
import android.provider.MediaStore
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

//qr code properties
fun generateQrBitmap(
    text: String,
    size: Int,
    foreground: Int,
    background: Int,
    errorCorrection: ErrorCorrectionLevel,
    addLogo: Boolean = false,
    logo: Bitmap? = null,
    borderSize: Int = 0,
    borderColor: Int = Color.BLACK
): Bitmap {

    val hints = mapOf(
        EncodeHintType.ERROR_CORRECTION to errorCorrection
    )

    val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints)
    val bmp = createBitmap(size, size)

    for (x in 0 until size) {
        for (y in 0 until size) {
            val isBlack = bitMatrix[x, y]
            bmp[x, y] = if (isBlack) foreground else background
        }
    }

    var finalBitmap = bmp

    if (borderSize > 0) {
        finalBitmap = addBorder(finalBitmap, borderSize, borderColor)
    }

    if (addLogo && logo != null) {
        finalBitmap = overlayLogo(finalBitmap, logo)
    }

    return finalBitmap
}

//boarder color
fun addBorder(bitmap: Bitmap, borderSize: Int, borderColor: Int): Bitmap {
    val newSize = bitmap.width + borderSize * 2
    val output = createBitmap(newSize, newSize)
    val canvas = Canvas(output)
    canvas.drawColor(borderColor)
    canvas.drawBitmap(bitmap, borderSize.toFloat(), borderSize.toFloat(), null)
    return output
}

//Can't make it work
fun overlayLogo(qr: Bitmap, logo: Bitmap): Bitmap {
    val overlay = createBitmap(qr.width, qr.height, qr.config ?: Bitmap.Config.ARGB_8888)
    val canvas = Canvas(overlay)

    val scaleFactor = qr.width / 5f
    val resizedLogo = logo.scale(scaleFactor.toInt(), scaleFactor.toInt())

    canvas.drawBitmap(qr, 0f, 0f, null)
    val centerX = (qr.width - resizedLogo.width) / 2f
    val centerY = (qr.height - resizedLogo.height) / 2f
    canvas.drawBitmap(resizedLogo, centerX, centerY, null)

    return overlay
}

/**
 * to gallery
 */
fun saveQrToGallery(context: Context, bitmap: Bitmap) {
    val resolver = context.contentResolver
    val filename = "QR_${System.currentTimeMillis()}.png"

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Quicklinks")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        resolver.openOutputStream(it)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }
    }
}
