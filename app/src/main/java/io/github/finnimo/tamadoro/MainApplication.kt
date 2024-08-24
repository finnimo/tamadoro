package io.github.finnimo.tamadoro

import android.app.Application
import androidx.room.Room
/*
class MainApplication : Application() {
    /*a companion object is an object associated
    with the class rather than an instance of the class*/
    companion object {
        lateinit var database: SessionsDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the Room database
        database = Room.databaseBuilder(
            applicationContext,
            SessionsDatabase::class.java,
            SessionsDatabase.NAME
        ).build()
    }
}
*/
