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
import java.util.Calendar


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

    fun addSession(seconds: Int, tag: String) {//change this to minutes when necessary
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


    fun getTotalDurationThisWeek(): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        val durationThisWeek: Int = sessionDao.getTotalDurationInPeriod(startOfDay,System.currentTimeMillis())
        return durationThisWeek
    }

    fun getTotalSessionToday(): Int {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        val sessionsToday = sessionDao.getSessionInPeriod(startOfDay)
        Log.d("no. of sessions",sessionsToday.size.toString())
        return sessionsToday.size

    }



}