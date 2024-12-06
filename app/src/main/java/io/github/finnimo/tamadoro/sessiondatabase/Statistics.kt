package io.github.finnimo.tamadoro.sessiondatabase

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class Statistics {
    companion object {

        private fun getLastSessionDateSP(context: Context): SharedPreferences {
            return context.getSharedPreferences("TAMADORO_LASTDATE", Context.MODE_PRIVATE)
        }

        private fun getCurrentStreakSP(context: Context): SharedPreferences {
            return context.getSharedPreferences("TAMADORO_CURRENTSTREAK", Context.MODE_PRIVATE)
        }

        private fun getCurrentStreak(context: Context): Int {
            val sharedPref = getCurrentStreakSP(context)
            return sharedPref.getInt("TAMADORO_CURRENTSTREAK",0)
        }

        private fun editCurrentStreak(context: Context, type: Boolean) {
            val currentStreakSP = getCurrentStreakSP(context)
            var currentStreak = getCurrentStreak(context)
            if (type) {
                currentStreak++
                currentStreakSP.edit().putInt("TAMADORO_CURRENTSTREAK", currentStreak).apply()
            }
            else {
                currentStreakSP.edit().putInt("TAMADORO_CURRENTSTREAK", 0).apply()
            }
        }

        private fun getHighestStreakSP(context: Context): SharedPreferences {
            return context.getSharedPreferences("TAMADORO_HIGHESTSTREAK", Context.MODE_PRIVATE)
        }

        fun getHighestStreak(context: Context): Int {
            val sharedPref = getHighestStreakSP(context)
            return sharedPref.getInt("TAMADORO_HIGHESTSTREAK",0)
        }

        fun updateLastSessionDate(context:  Context){
            val sharedPref = getLastSessionDateSP(context)
            val newDate = System.currentTimeMillis()

            // for debugs
            /*val x = sharedPref.getLong("TAMADORO_LASTDATE",0)
            Log.d("first session date..","current session date:"+x.toString())*/

            sharedPref.edit().putLong("TAMADORO_LASTDATE", newDate).apply()
            // for debugs
            /*sharedPref.edit().putLong("TAMADORO_LASTDATE", 1727790817000).apply()
            Log.d("session date..","new date:"+ sharedPref.getLong("TAMADORO_LASTDATE",0).toString())*/
        }

        fun updateStreaks(context: Context) {
            val lastSessionSP = getLastSessionDateSP(context)
            val highestStreakSP = getHighestStreakSP(context)
            val currentStreak = getCurrentStreak(context)
            val highestStreak = highestStreakSP.getInt("TAMADORO_HIGHESTSTREAK",0)

            val lastSessionLong = lastSessionSP.getLong("TAMADORO_LASTDATE",0)
            val lastSessionLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastSessionLong), ZoneId.systemDefault())
            val daysApart = (ChronoUnit.DAYS.between(lastSessionLocalDate, LocalDateTime.now())).toInt()
            //val daysApart = (ChronoUnit.DAYS.between(lastSessionLocalDate, LocalDateTime.of(2024, 10, 3, 9, 0))).toInt()
            //val daysApart = (ChronoUnit.DAYS.between(lastSessionLocalDate, LocalDateTime.of(2024, 12, 2, 7, 0))).toInt()
            //Log.d("Streak before calc", currentStreak.toString())

            if (daysApart > 1) {
                // Reset if its been more than 1 day
                editCurrentStreak(context, true)

            } else if ((daysApart == 1) or (currentStreak == 0)) {
                //if its been 1 day since last session:
                editCurrentStreak(context, true)
            }

            if (highestStreak < currentStreak) {
                highestStreakSP.edit().putInt("TAMADORO_HIGHESTSTREAK", currentStreak).apply()
            }
            //for debug
            /*val x = currentStreakSP.getInt("TAMADORO_CURRENTSTREAK",0)
            Log.d("Streak after calc",x.toString())*/
            Log.d("Last session logged at: ",lastSessionSP.getLong("TAMADORO_LASTDATE", 0).toString())
            Log.d("Current time:", System.currentTimeMillis().toString())
        }

        fun streakCheckOnViewCreated(context: Context): Int{
            val lastSessionSP = getLastSessionDateSP(context)
            val lastSessionLong = lastSessionSP.getLong("TAMADORO_LASTDATE",0)
            val lastSessionLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastSessionLong), ZoneId.systemDefault())
            val daysApart = (ChronoUnit.DAYS.between(lastSessionLocalDate, LocalDateTime.now())).toInt()
            if (daysApart > 1) {
                editCurrentStreak(context, false)
            }
            return getCurrentStreak(context)
        }


    }
}