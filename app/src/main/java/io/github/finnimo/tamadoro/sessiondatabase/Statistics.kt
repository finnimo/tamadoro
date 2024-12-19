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

        private fun getCurrentStreakSP(context: Context): SharedPreferences {
            return context.getSharedPreferences("TAMADORO_CURRENTSTREAK", Context.MODE_PRIVATE)
        }

        fun getCurrentStreak(context: Context): Int {
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
            val sharedPref = context.getSharedPreferences("TAMADORO_LASTDATE", Context.MODE_PRIVATE)
            val newDate = System.currentTimeMillis()

            // for debugs
            /*val x = sharedPref.getLong("TAMADORO_LASTDATE",0)
            Log.d("first session date..","current session date:"+x.toString())*/

            //sharedPref.edit().putLong("TAMADORO_LASTDATE", newDate).apply()
            // for debugs
            sharedPref.edit().putLong("TAMADORO_LASTDATE", newDate).apply()
            Log.d("Last Session Date recorded:","new last date:"+ sharedPref.getLong("TAMADORO_LASTDATE",0).toString())
        }

        fun updateStreaks(context: Context) {
            val lastSessionSP = context.getSharedPreferences("TAMADORO_LASTDATE", Context.MODE_PRIVATE)
            val currentStreak = getCurrentStreak(context)

            val lastSessionLong = lastSessionSP.getLong("TAMADORO_LASTDATE",0)

            val lastSessionLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastSessionLong), ZoneId.systemDefault()).toLocalDate()
            //val currentDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(1734722412000), ZoneId.systemDefault())

            val currentDate = LocalDateTime.now().toLocalDate()
            val daysApart = ChronoUnit.DAYS.between(lastSessionLocalDate, currentDate).toInt()



            //val daysApart = (ChronoUnit.DAYS.between(lastSessionLocalDate, currentDate)).toInt()

            Log.d("Days Apart last session date & current date", daysApart.toString())

            if (lastSessionLong == 0.toLong()) {

                editCurrentStreak(context, true)

            } else {
                if ((daysApart == 1) || (currentStreak == 0)) {
                    //if its been 1 day since last session:
                    editCurrentStreak(context, true)
                } else if (daysApart > 1) {
                    // Reset if its been more than 1 day
                    editCurrentStreak(context, false)
                }
            }



            updateHighestStreak(context)
            //for debug
            /*val x = currentStreakSP.getInt("TAMADORO_CURRENTSTREAK",0)
            Log.d("Streak after calc",x.toString())*/

            Log.d("Last session logged at: ",lastSessionSP.getLong("TAMADORO_LASTDATE", 0).toString())
            Log.d("Current time:", System.currentTimeMillis().toString())
            Log.d("current streak:", getCurrentStreak(context).toString())

        }

        private fun updateHighestStreak(context: Context) {
            val highestStreak = getHighestStreak(context)
            val currentStreak = getCurrentStreak(context)
            if (currentStreak >= highestStreak) {
                context.getSharedPreferences("TAMADORO_HIGHESTSTREAK", Context.MODE_PRIVATE).edit().putInt("TAMADORO_HIGHESTSTREAK",currentStreak).apply()
            }
        }

        fun streakCheckOnViewCreated(context: Context): Int{
            val lastSessionSP = context.getSharedPreferences("TAMADORO_LASTDATE", Context.MODE_PRIVATE)
            val lastSessionLong = lastSessionSP.getLong("TAMADORO_LASTDATE",0)
            val lastSessionLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastSessionLong), ZoneId.systemDefault())

            val daysApart = ChronoUnit.DAYS.between(lastSessionLocalDate, LocalDateTime.now()).toInt()
            if (daysApart > 1) {
                editCurrentStreak(context, false)
            }
            return getCurrentStreak(context)
        }


    }
}