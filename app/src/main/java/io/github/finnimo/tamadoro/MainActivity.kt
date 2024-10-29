package io.github.finnimo.tamadoro
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.util.Log //for debugging purposes
import android.util.TypedValue
//imports for working with xml based ui
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import io.github.finnimo.tamadoro.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//imports for notifications


import java.time.LocalDateTime
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //ROOM RELATED STUFF

    private lateinit var database: SessionsDatabase
    private lateinit var sessionDao: SessionDao
    private lateinit var addButton:Button
    private lateinit var manager: SessionManager
    // OBJECT DECLARATIONS

    private lateinit var statistics: Statistics
    private lateinit var timer: CountDownTimer
//TODO: maybe try to declare variables straight away in OnCreate
    // TEXTVIEW DECLARATIONS

    private lateinit var timerTextView: TextView

    // BUTTON DECLARATIONS
    private lateinit var startBtn: Button
    private lateinit var skipButton: Button
    private lateinit var statisticsViewBtn: Button
    private lateinit var breakModeBtn: Button
    private lateinit var focusModeBtn: Button
    private lateinit var newTag: EditText
    private lateinit var deleteStats: Button
    private lateinit var settingsBtn: Button

    private lateinit var tag: String

    // VARIABLE DEC & ASSIGNMENTS

    private var timerRunning = false
    //private val initialTime = 25 * 60 * 1000L , this is used to change time to 25 mins, but currently im working with seconds for debugging purposes
    private var initialTime = 7 * 1000L //for test purposes as stated above
    private var timeRemaining: Long = 0
    private var isPomodoro: Boolean = true //boolean value to indicate whether a timer is in break or pomodoro mode
    private var breakLength = 5 * 1000L


    override fun onCreate(savedInstanceState: Bundle?) {

        //HANDLING THEME BEFORE EVERYTHING IS RENDERED
        val sharedPrefs = getSharedPreferences("Theme", MODE_PRIVATE)
        val selectedTheme = sharedPrefs.getString("theme", "light") ?: "light"

        when (selectedTheme) {
            "dark" -> setTheme(R.style.darkTheme)
            //"x" -> setTheme(R.style.x)  // Add additional themes as needed
            else -> setTheme(R.style.lightTheme) // Default to light
        }
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(TimerFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.timerNav -> {
                    loadFragment(TimerFragment())
                    true
                }
                R.id.tasksNav -> {
                    loadFragment(TasksFragment())
                    true
                }
                R.id.petNav -> {
                    loadFragment(PetFragment())
                    true
                }

                R.id.statsNav -> {
                    loadFragment(StatsFragment())
                    true
                }
                else -> false
            }
        }


        addButton = findViewById(R.id.addButton)

        // THEMES RELATED THINGS

        //ROOMS STUFF

        manager = SessionManager(this)
        manager.getAllSessions()

        // Handle the error appropriately


        addButton.setOnClickListener {
            manager.addSession(1800,"study")
        }


        //TODO: add notifications

        //statistics = Statistics(this)
        //^ that was the old way i implemented stats



        // TEXT VIEWS + EDIT TEXT
        timerTextView = findViewById(R.id.timerTextView)
        newTag = findViewById(R.id.newTag)

        // BUTTONS
        startBtn = findViewById(R.id.startButton)
        skipButton = findViewById(R.id.skipButton)
        statisticsViewBtn = findViewById(R.id.statisticsViewBtn)
        settingsBtn = findViewById(R.id.settingsBtn)

        breakModeBtn = findViewById(R.id.breakTimer)
        focusModeBtn = findViewById(R.id.focusTimer)

        deleteStats = findViewById(R.id.deleteStats)


        //TAG
        tag = newTag.getText().toString()

        //updating timer text view. by default, the timer will show 25:00 on launch.
        timerTextView.text = formatTime(initialTime)

        newTag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This is called as the text is changing
                tag = (s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Optional: Do something after the text has changed
            }
        })

        statisticsViewBtn.setOnClickListener {
            val openStatisticsActivity = Intent(this, StatisticsActivity::class.java)
            startActivity(openStatisticsActivity)
        }

        settingsBtn.setOnClickListener {
            val openSettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(openSettingsActivity)
        }

        deleteStats.setOnClickListener {
            manager.deleteAllSessions()
        }
        breakModeBtn.setOnClickListener {
            isPomodoro = false
            timerTextView.text = formatTime(breakLength)
            timeRemaining = breakLength
            updateButtonAppearance()
        }

        focusModeBtn.setOnClickListener {
            isPomodoro = true
            timerTextView.text = formatTime(initialTime)
            timeRemaining = initialTime
            updateButtonAppearance()
        }

        startBtn.setOnClickListener {//when start button clicked
            if (timerRunning) {
                pauseTimer()
            } else {
                startTimer(timeRemaining)
            }
        }


        skipButton.setOnClickListener {
            skipTimer()
            timeRemaining = if (isPomodoro){
                initialTime
            } else{
                breakLength
            }

        }
        timeRemaining = initialTime
        timerTextView.text = formatTime(initialTime)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment) // Replace with your fragment container's ID
        transaction.commit()

    }

    private fun getColour(attr: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }


    private fun updateButtonAppearance() {

        val primary = getColour(androidx.constraintlayout.widget.R.attr.colorPrimary)
        val secondary = getColour(com.google.android.material.R.attr.colorSecondary)


        if (isPomodoro) {
            focusModeBtn.backgroundTintList = ColorStateList.valueOf(secondary)
            breakModeBtn.backgroundTintList = ColorStateList.valueOf(primary)
        } else {
            breakModeBtn.backgroundTintList = ColorStateList.valueOf(secondary)
            focusModeBtn.backgroundTintList = ColorStateList.valueOf(primary)
        }



    }


    private fun startTimer(time: Long) {
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                timerTextView.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() { //WHEN TIMER ENDS
                val minutes = (initialTime / 1000).toInt()
                if (isPomodoro) {
                    manager.addSession(minutes,tag)
                    statistics.setLastSessionDate(LocalDateTime.now())
                    statistics.getLastSessionDate()
                    stopTimer(initialTime)
                    statistics.getPeriod()

                } else {
                    stopTimer(breakLength)
                }

                timerTextView.text = "Finished!"

                // TODO: ill add smth here to send a notif when done
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
        if (isPomodoro) {
            val timeFocusedAlready = ((initialTime - timeRemaining + 1000) / 1000).toInt()
            manager.addSession(timeFocusedAlready,tag)
            statistics.setLastSessionDate(LocalDateTime.now())
            timer.cancel()//cancels timer
            timerTextView.text = formatTime(breakLength)//resets text view
            isPomodoro = false

        } else {
            timer.cancel()
            timerTextView.text = formatTime(initialTime)
            isPomodoro = true
        }
        updateButtonAppearance()
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
