package io.github.finnimo.tamadoro
import android.content.Context//context lets me do stuff to sharedprefs
import android.content.SharedPreferences
import android.util.Log
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.util.*
import java.time.temporal.ChronoUnit

//shared prefs prevent statistics data from being lost when switching views
public class Statistics(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("userStats", Context.MODE_PRIVATE)
    data class Session(val seconds: Int, val dateTime: Long)
    //ATTRIBUTES:
    private var allSessions: MutableList<Session> = mutableListOf()
    private var currentStreak: Int = 0
    lateinit private var lastSessionDate: LocalDateTime;

    init {
        // load saved stats from SharedPreferences when the class is initialized
        allSessions = loadSavedStats()
    }

    private fun loadSavedStats(): MutableList<Session> {
        val sessionsSet = sharedPreferences.getStringSet("userStats", mutableSetOf()) ?: mutableSetOf()
        return sessionsSet.map { entry ->
            val parts = entry.split(":")
            Session(parts[0].toInt(), parts[1].toLong())
        }.toMutableList()
    }// TODO: update this to add dates

    private fun saveStats() {
        val sessionsSet = allSessions.map { "${it.seconds}:${it.dateTime}" }.toMutableSet()
        sharedPreferences.edit().putStringSet("userStats", sessionsSet).apply()
    }

    fun addSession(seconds: Int) {
        val session = Session(seconds, System.currentTimeMillis())
        allSessions.add(session)
        saveStats() // Save the updated totalSessions list
    }



    fun getAverageDuration(): Long {
        return if (allSessions.isNotEmpty()) {
            getTotalDuration() / allSessions.size
        } else {
            0
        }
    }

    fun setLastSessionDate(newDate: LocalDateTime) {
        this.lastSessionDate = newDate
    }

    fun deleteStats(){
    sharedPreferences.edit().clear().apply()
        allSessions.clear()
    }

    fun updateStreaks(){
        var period = Duration.between(this.lastSessionDate,LocalDate.now());
    }
//methods for retrieving info
    fun getTotalSessions(): Int {
        return allSessions.size
    }

    fun getTotalDuration(): Long {
        return allSessions.sumOf { it.seconds.toLong() }
    }

    fun getLastSessionDate() {
        Log.e("GET LAST SESSION DATE",(this.lastSessionDate).toString())
    }

    fun getPeriod() {
        val period = Duration.between(this.lastSessionDate,LocalDateTime.now())
        Log.e("GET PERIOD",(period).toString())
            ?: run {
                Log.e("GET PERIOD", "No session date available")
            }
    }



}

