package io.github.finnimo.tamadoro

import android.content.Context

public class Pet(val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("TAMADORO_COINS", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private var coins = sharedPreferences.getInt("TAMADORO_COINS", 0) ?: 0

    private fun addCoins(durationFocused: Int) {

    }

    private fun subCoins(noOfCoins: Int) {
        coins -= noOfCoins
        editor.putInt("TAMADORO_COINS",coins).apply()
    }

    private fun showCoins():Int {
        return coins
    }

    companion object {
        fun addCoins(durationFocused: Int) {
            val noOfCoins = 10 * durationFocused
            coins += noOfCoins
            editor.putInt("TAMADORO_COINS",coins).apply()
        }
    }
}