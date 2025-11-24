package com.app.quicklinks.data

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScanRepository(private val dao: ScanDao) {

    suspend fun saveScan(value: String, oUrl: String, sUrl: String) {
        dao.insertScan(
            Scan(
                text = value,
                originalUrl = oUrl,
                shortcode = value,
                qrCode = null,
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
}