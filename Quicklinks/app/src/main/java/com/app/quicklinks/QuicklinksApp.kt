package com.app.quicklinks

import android.app.Application
import androidx.room.Room
import com.app.quicklinks.data.AppDatabase
import com.app.quicklinks.data.ScanRepository

class QuicklinksApp : Application() {

    lateinit var database: AppDatabase
    lateinit var repository: ScanRepository

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "quicklinks-db"
        )   .fallbackToDestructiveMigration()
            .build()


        repository = ScanRepository(database.scanDao())
    }
}