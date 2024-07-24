package io.github.finnimo.tamadoro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.util.Log //for debuggin purposes
//imports for working with xml based ui
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat

//imports for notifications



import java.time.LocalDate
import java.time.LocalDateTime
class MainActivity : AppCompatActivity() {

    private lateinit var statistics: Statistics
    private lateinit var timerTextView: TextView
    private lateinit var startBtn: Button
    private lateinit var skipButton: Button
    private lateinit var timer: CountDownTimer
    private lateinit var showTotalDurationButton: Button
    private lateinit var statisticsViewBtn: Button
    private lateinit var breakModeBtn: Button
    private lateinit var focusModeBtn: Button

    //buttons for debug
    private lateinit var deleteStats: Button
    private lateinit var totalDurationTextView: TextView


    private var timerRunning = false

    //private val initialTime = 25 * 60 * 1000L 25 mins
    private var initialTime = 7 * 1000L //for test purposes
    private var timeRemaining: Long = 0
    private var isPomodoro: Boolean =
        true //boolean value to indicate whether a timer is in break or pomodoro mode
    private val breakLength = 5 * 1000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: add notifications





        statistics = Statistics(this)
        //ui inits
        timerTextView = findViewById(R.id.timerTextView)
        timerTextView.text = formatTime(initialTime)
        totalDurationTextView = findViewById(R.id.totalDurationTextView)
        // buttons
        startBtn = findViewById(R.id.startButton)
        skipButton = findViewById(R.id.skipButton)
        showTotalDurationButton = findViewById(R.id.showTotalDurationButton)
        statisticsViewBtn = findViewById(R.id.statisticsViewBtn)
        breakModeBtn = findViewById(R.id.breakTimer)
        focusModeBtn = findViewById(R.id.focusTimer)

        deleteStats = findViewById(R.id.deleteStats)



        statisticsViewBtn.setOnClickListener {
            val openStatisticsActivity = Intent(this, StatisticsActivity::class.java)
            startActivity(openStatisticsActivity)
        }

        deleteStats.setOnClickListener {
            statistics.deleteStats()
        }
        breakModeBtn.setOnClickListener {
            isPomodoro = false
            timerTextView.text = formatTime(breakLength)
            timeRemaining = breakLength
        }

        focusModeBtn.setOnClickListener {
            isPomodoro = true
            timerTextView.text = formatTime(initialTime)
            timeRemaining = initialTime
        }

        startBtn.setOnClickListener {//when start button clicked
            if (timerRunning) {
                pauseTimer()
            } else {
                startTimer(timeRemaining)
            }
        }

        showTotalDurationButton.setOnClickListener {
            val totalDuration = statistics.getTotalDuration()
            val totalSessions = statistics.getTotalSessions()
            totalDurationTextView.text =
                "Total Duration: $totalDuration \nTotal sessions: $totalSessions"
            statistics.getPeriod()
        }

        skipButton.setOnClickListener {
            skipTimer()
            if (isPomodoro == true){
                timeRemaining = initialTime
            } else{
                timeRemaining = breakLength
            }

        }
        timeRemaining = initialTime
        timerTextView.text = formatTime(initialTime)
    }

    private fun startTimer(time: Long) {
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                timerTextView.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() { //WHEN TIMER ENDS
                val minutes = (initialTime / 1000).toInt()
                if (isPomodoro == true) {
                    statistics.addSession(minutes)
                    statistics.setLastSessionDate(LocalDateTime.now())
                    statistics.getLastSessionDate()
                    stopTimer(initialTime)
                    statistics.getPeriod()


                } else {
                    stopTimer(breakLength)
                }

                timerTextView.text = "Finished!"

                // TODO: ill add smth here to send notif when done
            }
        }.start()

        startBtn.text = "Pause"
        skipButton.visibility = Button.INVISIBLE
        timerRunning = true
    }

    private fun pauseTimer() {
        timer.cancel()
        startBtn.text = "Resume"
        skipButton.visibility = Button.VISIBLE
        timerRunning = false
    }

    private fun stopTimer(time: Long) {
        timer.cancel()
        timerTextView.text = formatTime(time)
        startBtn.text = "Start"
        timerRunning = false
        timeRemaining = time

        //val minutes = (initialTime/1000/60).toInt() this is to make it to minutes but i want to debug using seconds
    }

    private fun skipTimer() {
        //to log time user has already focused for
        //if its in focus mode then the skip will skip to break mode, vice versa
        if (isPomodoro == true) {
            val timeFocusedAlready = ((initialTime - timeRemaining + 1000) / 1000).toInt()
            statistics.addSession(timeFocusedAlready)
            statistics.setLastSessionDate(LocalDateTime.now())
            timer.cancel()//cancels timer
            timerTextView.text = formatTime(breakLength)//resets text view
            isPomodoro = false

        } else {
            timer.cancel()
            timerTextView.text = formatTime(initialTime)
            isPomodoro = true
        }
        startBtn.text = "Start"
        skipButton.visibility = Button.INVISIBLE
        //resetting timer
        timerRunning = false


        //val minutes = (initialTime/1000/60).toInt()
    }

    private fun formatTime(millis: Long): String {
        val minutes = millis / 1000 / 60
        val seconds = (millis / 1000) % 60
        return "%02d:%02d".format(minutes, seconds)
    }

}
