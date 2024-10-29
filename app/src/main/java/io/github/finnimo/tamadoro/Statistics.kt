package io.github.finnimo.tamadoro

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

public class Statistics {
    companion object {

        private fun getLastSessionDateSP(context: Context): SharedPreferences {
            return context.getSharedPreferences("TAMADORO_LASTDATE", Context.MODE_PRIVATE)
        }
        private fun getCurrentStreakSP(context: Context): SharedPreferences {
            return context.getSharedPreferences("TAMADORO_CURRENTSTREAK", Context.MODE_PRIVATE)
        }
        private fun getHighestStreakSP(context: Context): SharedPreferences {
            return context.getSharedPreferences("TAMADORO_HIGHESTSTREAK", Context.MODE_PRIVATE)
        }

        fun updateLastSessionDate(context:  Context){
            val sharedPref = getLastSessionDateSP(context)
            val newDate = System.currentTimeMillis()

            // for debugging:
            val x = sharedPref.getLong("TAMADORO_LASTDATE",0)
            Log.d("first session date..","current session date:"+x.toString())

            sharedPref.edit().putLong("TAMADORO_LASTDATE", newDate).apply()
            Log.d("session date..","newdate:"+newDate.toString())
        }

        fun updateStreaks(context: Context) {
            val lastSessionSP = getLastSessionDateSP(context)
            val currentStreakSP = getCurrentStreakSP(context)
            val highestStreakSP = getHighestStreakSP(context)
            var currentStreak = currentStreakSP.getInt("TAMADORO_CURRENTSTREAK",0)
            val highestStreak = highestStreakSP.getInt("TAMADORO_HIGHESTSTREAK",0)

            val lastSessionLong = lastSessionSP.getLong("TAMADORO_LASTDATE",0)
            val lastSessionLocalDate = Instant.ofEpochMilli(lastSessionLong)
            val daysApart = ChronoUnit.DAYS.between(lastSessionLocalDate, LocalDate.now())

            if (highestStreak < currentStreak) {
                highestStreakSP.edit().putInt("TAMADORO_HIGHESTSTREAK", currentStreak).apply()
            }

            if (daysApart > 1) {
                // Reset if its been more than 1 day
                currentStreakSP.edit().putInt("TAMADORO_CURRENTSTREAK",0).apply()

            } else {
                // Add 1 to streak
                currentStreak = currentStreakSP.getInt("TAMADORO_CURRENTSTREAK",0)
                currentStreakSP.edit().putInt("TAMADORO_CURRENTSTREAK", currentStreak + 1).apply()
            }
            val x = currentStreakSP.getInt("TAMADORO_CURRENTSTREAK",0)

            Log.d("current streak",x.toString())
        }

    }
}