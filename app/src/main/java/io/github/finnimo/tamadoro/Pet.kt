package io.github.finnimo.tamadoro

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

public class Pet(val context: Context) {


    private val totalCoins: Int = getSharedPrefs(context,"TAMADORO_COINS").getInt("TAMADORO_COINS", 0)
    private val weeklyGoal: Int = getSharedPrefs(context,"TAMADORO_MINS_GOAL").getInt("TAMADORO_MINS_GOAL", 0)

    fun getSharedPrefs(context: Context, name: String): SharedPreferences{
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getTotalCoins(): Int {
        return totalCoins
    }

    fun getWeeklyGoal(): Int {
        return weeklyGoal
    }

    fun addCoins(durationFocused: Int) {
        var coins = totalCoins
        val noOfCoins = 10 * durationFocused
        coins += noOfCoins
        getSharedPrefs(context,"TAMADORO_COINS").edit().putInt("TAMADORO_COINS",coins).apply()

        //TODO: REMEMBER TO CHANGE COIN MULTIPLIER FOR ACTUAL APP
    }

    fun getCurrentFocusQuota(): Int {
        val calendar = Calendar.getInstance()
        var dayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK)

        dayOfWeekInt = if (dayOfWeekInt == 1) {
            7
        } else {
            dayOfWeekInt - 1
        }
        // above is so that monday is always day 1
        // this return the amount of hours that shouldve been studied already this week
        return dayOfWeekInt * weeklyGoal/7
    }

    fun onTrack(duration: Int): Boolean {
        val currentQuota = getCurrentFocusQuota()
        return duration >= currentQuota
    }

    fun happinessChecker(onTrack: Boolean, focusedToday: Boolean): Int {
        var tempHappiness = 3
        if (!focusedToday) {
            tempHappiness -+ 1
        }
        if (!onTrack) {
            tempHappiness -= 1
        }
        getSharedPrefs(context,"TAMADORO_HAPPINESS").edit().putInt("TAMADORO_HAPPINESS",tempHappiness).apply()
        return getSharedPrefs(context,"TAMADORO_HAPPINESS").getInt("TAMADORO_HAPPINESS", 0)
    }

}