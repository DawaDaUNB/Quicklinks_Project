package com.app.quicklinks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.quicklinks.data.Scan
import com.app.quicklinks.data.ScanDao

@Database(
    entities = [Scan::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(QRCodeConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}