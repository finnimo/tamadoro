package io.github.finnimo.tamadoro

import android.content.Context
import android.util.Log
import android.widget.Button
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionManager(context: Context) {

    private var database = Room.databaseBuilder(
    context.applicationContext,
    SessionsDatabase::class.java,
    SessionsDatabase.NAME
    ).build()

    private val sessionDao = database.getSessionDao()

    fun getAllSessions(){
        GlobalScope.launch(Dispatchers.IO) {
            sessionDao.getAllSessions()
        }
    }

    fun addSession(seconds: Int, tag: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val newSession = Session(
                seconds = seconds,
                dateTime = System.currentTimeMillis(),
                tag = tag
            )
            sessionDao.addSession(newSession)
            Log.e("SESSION ADD?", "YEZZ")
        }
    }

}