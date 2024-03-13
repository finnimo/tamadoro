package com.example.tamadoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var timer:CountDownTimer
    private lateinit var totalDurationTextView:TextView
    private lateinit var showTotalDurationButton:Button
    private lateinit var statisticsViewBtn:Button

    private var timerRunning = false
    //private val initialTime = 25 * 60 * 1000L 25 mins
    private val initialTime = 2*1000L//for test purposes
    private var timeRemaining:Long = 0
    private val statistics = Statistics()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timerTextView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        timerTextView.text = formatTime(initialTime)
        totalDurationTextView = findViewById(R.id.totalDurationTextView)
        showTotalDurationButton = findViewById(R.id.showTotalDurationButton)

        statisticsViewBtn = findViewById(R.id.statisticsViewBtn)
        statisticsViewBtn.setOnClickListener {
            val openStatisticsActivity = Intent(this,StatisticsActivity::class.java)
            startActivity(openStatisticsActivity)
        }

        startButton.setOnClickListener {//when start button clicked
            if (timerRunning){
                pauseTimer()
            } else{
                startTimer(timeRemaining)
            }
        }

        showTotalDurationButton.setOnClickListener {
            val totalDuration = statistics.getTotalDuration()
            val totalSessions = statistics.getTotalSessions()
            totalDurationTextView.text = "Total Duration: $totalDuration \nTotal sessions: $totalSessions"
        }

        stopButton.setOnClickListener {
            stopTimer()
        }
        timeRemaining = initialTime
        timerTextView.text = formatTime(initialTime)
    }

    private fun startTimer(initialTime: Long){
        timer = object : CountDownTimer(initialTime,1000){
            override fun onTick(millisUntilFinished: Long){
                timeRemaining = millisUntilFinished
                timerTextView.text = formatTime(millisUntilFinished)
            }

            override fun onFinish(){ //WHEN TIMER ENDS
                stopTimer()
                timerTextView.text = "Finished!"
                timeRemaining = initialTime

                //val minutes = (initialTime/1000/60).toInt()
                val minutes = ((initialTime/1000)%60).toInt()
                statistics.addSession(minutes)


                //ill add smth here to send notif when done
            }
        }.start()

        startButton.text = "Pause"
        stopButton.visibility = Button.INVISIBLE
        timerRunning = true
    }

    private fun pauseTimer(){
        timer.cancel()
        startButton.text = "Resume"
        stopButton.visibility = Button.VISIBLE
        timerRunning = false
    }

    private fun stopTimer(){
        timer.cancel()
        timerTextView.text = formatTime(initialTime)
        startButton.text = "Start"
        stopButton.visibility = Button.INVISIBLE
        timerRunning = false
    }

    private fun formatTime(millis:Long): String{
        val minutes = millis/1000/60
        val seconds = (millis/1000) % 60
        return "%02d:%02d".format(minutes,seconds)
    }

}