package com.app.quicklinks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanDao {
    @Insert
    suspend fun insertScan(scan: Scan)

    @Query("SELECT * FROM scans WHERE userId = :uId ORDER BY favorite DESC, id ASC")
    suspend fun getAllScans(uId: Long): List<Scan>

    @Query("SELECT * FROM scans WHERE userId = :uId ORDER BY favorite DESC, text ASC")
    suspend fun getAllScansAlphabetical(uId: Long): List<Scan>

    @Query("SELECT * FROM scans WHERE userId = :uId ORDER BY favorite DESC, id DESC")
    suspend fun getAllScansReverse(uId: Long): List<Scan>

    @Query("SELECT * FROM scans WHERE userId = :uId ORDER BY favorite DESC, text DESC")
    suspend fun getAllScansAlphabeticalReverse(uId: Long): List<Scan>

    @Delete
    suspend fun deleteScan(scan: Scan)

    @Query("UPDATE scans SET text = :newName WHERE id = :scanId")
    suspend fun updateScanName(scanId: Long, newName: String)

    @Query("UPDATE scans SET favorite = :isFavorite WHERE id = :scanId")
    suspend fun updateScanFavorite(scanId: Long, isFavorite: Boolean)

    @Query("UPDATE scans SET qrCode = :newQRCode WHERE id = :scanId")
    suspend fun updateScanQR(scanId: Long, newQRCode: ByteArray?)

    @Query("SELECT * FROM scans WHERE text LIKE '%' || :searchQuery || '%' AND userId = :uId ORDER BY favorite DESC, text ASC")
    fun searchScan(searchQuery: String?, uId: Long): List<Scan>
}