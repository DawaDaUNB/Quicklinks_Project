package com.app.quicklinks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScanDao {
    @Insert
    suspend fun insertScan(scan: Scan)

    @Query("SELECT * FROM scans ORDER BY favorite DESC, id ASC")
    suspend fun getAllScans(): List<Scan>

    @Query("SELECT * FROM scans ORDER BY favorite DESC, text ASC")
    suspend fun getAllScansAlphabetical(): List<Scan>

    @Query("SELECT * FROM scans ORDER BY favorite DESC, id DESC")
    suspend fun getAllScansReverse(): List<Scan>

    @Query("SELECT * FROM scans ORDER BY favorite DESC, text DESC")
    suspend fun getAllScansAlphabeticalReverse(): List<Scan>

    @Delete
    suspend fun deleteScan(scan: Scan)

    @Query("UPDATE scans SET text = :newName WHERE id = :scanId")
    suspend fun updateScanName(scanId: Long, newName: String)

    @Query("UPDATE scans SET favorite = :isFavorite WHERE id = :scanId")
    suspend fun updateScanFavorite(scanId: Long, isFavorite: Boolean)

    @Query("UPDATE scans SET qrCode = :newQRCode WHERE id = :scanId")
    suspend fun updateScanQR(scanId: Long, newQRCode: ByteArray?)

    // @Query("SELECT * FROM scans WHERE text LIKE '%' || :searchQuery || '%'")
    //fun searchBySubstring(searchQuery: String)
}