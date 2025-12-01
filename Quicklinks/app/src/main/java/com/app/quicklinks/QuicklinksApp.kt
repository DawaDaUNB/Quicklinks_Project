package com.app.quicklinks

import android.app.Application
import androidx.room.Room
import com.app.quicklinks.data.AppDatabase
import com.app.quicklinks.data.ScanRepository
import com.app.quicklinks.data.user.UserDatabase
import com.app.quicklinks.data.user.UserRepository


class QuicklinksApp : Application() {

    lateinit var database: AppDatabase
    lateinit var userDatabase: UserDatabase
    lateinit var repository: ScanRepository
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()

        userDatabase = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "quicklinks-user-db"
        ).fallbackToDestructiveMigration()
            .build()
        userRepository = UserRepository(userDatabase.userDao())

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "quicklinks-db"
        ).fallbackToDestructiveMigration()
            .build()

        repository = ScanRepository(database.scanDao())

    }
}