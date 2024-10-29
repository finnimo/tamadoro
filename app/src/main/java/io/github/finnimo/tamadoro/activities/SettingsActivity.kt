package io.github.finnimo.tamadoro.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.github.finnimo.tamadoro.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lightButton:Button
    private lateinit var darkButton:Button
    private lateinit var editor:SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_main) // This should point to the settings layout

        lightButton = findViewById(R.id.lightTheme)
        darkButton = findViewById(R.id.darkTheme)
        sharedPreferences = getSharedPreferences("TAMADORO_THEME", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        lightButton.setOnClickListener {
            applyTheme("light")
        }

        darkButton.setOnClickListener {
            applyTheme("dark")
        }
    }

    private fun themeRefresh() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun applyTheme(themeName:String) {
        editor.putString("TAMADORO_THEME", themeName).apply()
        themeRefresh()
    }

}
