package io.github.finnimo.tamadoro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class StatisticsActivity : AppCompatActivity() {

    private lateinit var mainViewBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_main)
        mainViewBtn= findViewById(R.id.mainViewBtn)
        mainViewBtn.setOnClickListener {
            val openMainActivity = Intent(this, MainActivity::class.java)
            startActivity(openMainActivity)
        }

    }
}
