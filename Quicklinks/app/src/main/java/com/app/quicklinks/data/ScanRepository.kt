package com.app.quicklinks.data

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScanRepository(private val dao: ScanDao) {

    suspend fun saveScan(value: String, oURL: String, sURL: String) {
        dao.insertScan(
            Scan(
                text = value,
                originalUrl = oURL,
                shortcode = sURL,
                qrCode = null,
                favorite = false,
                timestamp = System.currentTimeMillis()
            )
        )
    }
    suspend fun deleteScan(scan: Scan) {
        dao.deleteScan(scan)
    }
    fun getHistory(): Flow<List<Scan>> = flow {
        emit(dao.getAllScans())
    }

    fun getHistoryAlphabetical(): Flow<List<Scan>> = flow {
        emit(dao.getAllScansAlphabetical())
    }

    suspend fun updateScanName(scanId: Long, newName: String) {
        dao.updateScanName(scanId,newName)
    }

    suspend fun updateScanFavorite(scanId: Long, isFavorite: Boolean) {
        dao.updateScanFavorite(scanId,isFavorite)
    }

    suspend fun updateScanQR(scanId: Long, newQR: ByteArray?) {
        dao.updateScanQR(scanId,newQR)
    }

}