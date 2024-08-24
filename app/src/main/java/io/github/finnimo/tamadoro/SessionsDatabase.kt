package io.github.finnimo.tamadoro

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.finnimo.tamadoro.Session
import io.github.finnimo.tamadoro.SessionDao


/*This is the Room Database that manages and provides acces to the database and handles
* all connections between the database and my apps logic through the DAO i created
* a class needs to be created to extend the room database so i get access
* to built in methods that make it easier to work with SQLite with android

Defines my entity, which is the table storing my session. i defined the data type
* thats stored in there already in the Session.kt file*/
@Database(entities = [Session::class], version = 1)

//a layer of abstraction simplifying database management. it allows me to use my entities and Daos
//essentially handles boilerplate code that comes with using SQLite directly
abstract class SessionsDatabase : RoomDatabase() {
    abstract fun getSessionDao() : SessionDao

    companion object {
        const val NAME = "Sessions_DB"
    }
}