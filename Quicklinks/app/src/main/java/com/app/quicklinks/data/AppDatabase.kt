package com.app.quicklinks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.quicklinks.data.Scan
import com.app.quicklinks.data.ScanDao

@Database(
    entities = [Scan::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}