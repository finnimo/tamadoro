package io.github.finnimo.tamadoro.sessiondatabase

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId


class SessionManager(context: Context) {

    private var database = Room.databaseBuilder(
    context.applicationContext,
    SessionsDatabase::class.java,
        SessionsDatabase.NAME
    ).build()

    private val sessionDao = database.getSessionDao()

    private fun timestamp(): String {
        return System.currentTimeMillis().toString()
    }

    suspend fun getAllSessions(): List<Session> {
        return withContext(Dispatchers.IO) {
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

    fun getTotalDuration(): Int {
        return sessionDao.getTotalDuration()
    }

    fun getTotalDurationThisWeek(start: Long): Int {
        //val currentTime = System.currentTimeMillis()
        return sessionDao.getTotalDurationInPeriod(start,System.currentTimeMillis())
    }




}