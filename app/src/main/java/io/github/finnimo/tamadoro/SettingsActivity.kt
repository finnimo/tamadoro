package io.github.finnimo.tamadoro

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lightButton:Button
    private lateinit var darkButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_main) // This should point to the settings layout

        lightButton = findViewById(R.id.lightTheme)
        darkButton = findViewById(R.id.darkTheme)
        sharedPreferences = getSharedPreferences("Theme", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        lightButton.setOnClickListener {
            editor.putString("theme", "light").apply()
            themeRefresh()
        }

        darkButton.setOnClickListener {
            editor.putString("theme", "dark").apply()
            themeRefresh()
        }
    }

    private fun themeRefresh() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
