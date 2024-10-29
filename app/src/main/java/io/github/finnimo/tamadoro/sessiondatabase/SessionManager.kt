package io.github.finnimo.tamadoro.sessiondatabase

import android.content.Context
import android.util.Log
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
            Log.e("SessionManager.addSession", "Session added on ${timestamp()}")
        }
    }

    fun deleteAllSessions() {
        GlobalScope.launch(Dispatchers.IO) {
            sessionDao.deleteAllSessions()
            Log.d("SessionManager.deleteAllSessions()", "All sessions deleted on ${timestamp()} ")
        }
    }

    private fun timestamp(): String {
        return System.currentTimeMillis().toString()
    }


}