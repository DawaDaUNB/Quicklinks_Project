package com.app.quicklinks.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScanRepository(private val dao: ScanDao) {

    suspend fun saveScan(value: String) {
        dao.insertScan(
            Scan(
                text = value,
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