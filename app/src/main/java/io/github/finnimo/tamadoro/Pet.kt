package io.github.finnimo.tamadoro

import android.content.Context

public class Pet(val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("TAMADORO_COINS", Context.MODE_PRIVATE)
}