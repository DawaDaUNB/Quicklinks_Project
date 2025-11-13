package com.app.quicklinks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanDao {
    @Insert
    suspend fun insertScan(scan: Scan)

    @Query("SELECT * FROM scans ORDER BY id DESC")
    suspend fun getAllScans(): List<Scan>

    @Delete
    suspend fun deleteScan(scan: Scan)
}