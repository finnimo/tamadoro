package io.github.finnimo.tamadoro

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext

public class Pet() {

    companion object {

        fun getSharedPrefEditor(context: Context): SharedPreferences{
            return context.getSharedPreferences("TAMADORO_COINS", Context.MODE_PRIVATE)
        }

        fun addCoins(context: Context, durationFocused: Int) {
            val sp = getSharedPrefEditor(context)
            var coins = sp.getInt("TAMADORO_COINS", 0)
            val noOfCoins = 10 * durationFocused
            coins += noOfCoins
            sp.edit().putInt("TAMADORO_COINS",coins).apply()

            //TODO: REMEMBER TO CHANGE COIN MULTIPLIER FOR ACTUAL APP
        }

        fun getTotalCoins(context: Context):Int {
            val sp = getSharedPrefEditor(context)
            return sp.getInt("TAMADORO_COINS", 0)
        }
    }
}