package io.github.finnimo.tamadoro.fragments

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.finnimo.tamadoro.activities.MainActivity
import io.github.finnimo.tamadoro.fragments.pethandling.Pet
import io.github.finnimo.tamadoro.R
import io.github.finnimo.tamadoro.sessiondatabase.Statistics
import io.github.finnimo.tamadoro.sessiondatabase.SessionManager
import io.github.finnimo.tamadoro.activities.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TimerFragment : Fragment() {

    // VARIABLES TO ACCESS ROOMS/DATABASE & COINS CLASS

    private lateinit var manager: SessionManager
    private lateinit var pet: Pet
    private var timer: CountDownTimer? = null

    // BUTTONS

    private lateinit var startBtn: Button
    private lateinit var skipButton: ImageButton
    private lateinit var breakModeBtn: Button
    private lateinit var focusModeBtn: Button
    private lateinit var newTag: EditText
    private lateinit var deleteStats: Button
    private lateinit var settingsBtn: ImageButton

    private lateinit var pickerContainer: LinearLayout
    private lateinit var hoursPicker: NumberPicker
    private lateinit var minutesPicker: NumberPicker
    private lateinit var durationSave: Button

    private lateinit var button:Button
    private lateinit var navbar: BottomNavigationView

    // TEXTVIEW DECLARATIONS

    private lateinit var timerTextView: TextView

    //TAG FOR SESSION TYPE

    private var tag: String = "Tag"

    // OTHER VARIABLES FOR TIMER FUNCTIONS

    private var timerRunning = false
    private var initialTime: Long = 0
    //private var initialTime = 2 * 1000L for test purposes as stated above
    private var timeRemaining: Long = 0
    private var isPomodoro: Boolean = true //boolean value to indicate whether a timer is in break or pomodoro mode
    private var breakLength = 5 * 1000L


    //NOTIFICATIONS
    private val channelID = "ChannelID"
    private val channelName = "NotificationChannel"
    private val notifID = 1
    private val notifPermissionRequestCode = 100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initializing database and grabbing sessions first so that I don't accidentally access database before it's initialized, avoiding this crash

        manager = SessionManager(requireContext())
        pet = Pet(requireContext())

        initialTime = requireContext().getSharedPreferences("TAMADORO_FOCUSDURATION", Context.MODE_PRIVATE)
            .getLong("TAMADORO_FOCUSDURATION",25 * 60 * 1000L)
        //initialTime = 7 * 1000L
        breakLength = requireContext().getSharedPreferences("TAMADORO_BREAKDURATION", Context.MODE_PRIVATE)
            .getLong("TAMADORO_BREAKDURATION",5 * 60 * 1000L)

        GlobalScope.launch(Dispatchers.Main) {
            manager.getAllSessions()
        }

        //For notifications

        createNotifChannel()
        checkNotificationPermission()

        // TEXT VIEWS + EDIT TEXT

        timerTextView = view.findViewById(R.id.timerTextView)
        newTag = view.findViewById(R.id.newTag)

        // BUTTONS

        startBtn = view.findViewById(R.id.startButton)
        skipButton = view.findViewById(R.id.skipButton)
        settingsBtn = view.findViewById(R.id.settingsBtn)

        breakModeBtn = view.findViewById(R.id.breakTimer)
        focusModeBtn = view.findViewById(R.id.focusTimer)
        deleteStats = view.findViewById(R.id.deleteStats)

        hoursPicker = view.findViewById(R.id.hoursPicker)
        minutesPicker = view.findViewById(R.id.minutesPicker)
        pickerContainer = view.findViewById(R.id.pickerContainer)
        durationSave = view.findViewById(R.id.durationSaverBtn)
        //navbar = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        button = view.findViewById(R.id.button)

        //TAG

        tag = newTag.text.toString()

        //Formats the timer to display the right count down starting number
        timerTextView.text = formatTime(initialTime)

        // The following code actively listens for change in editText component containing the session tag, whenever it changes, that's the tag that'll be appended to the session on timer finish

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

        // UI COMPONENTS CLICKED ACTIONS

        timerTextView.setOnClickListener {
            if (!timerRunning) {

                pickerContainer.visibility = LinearLayout.VISIBLE

                hoursPicker.minValue = 0
                hoursPicker.maxValue = 12
                minutesPicker.minValue = 0
                minutesPicker.maxValue = 59

                val currentDuration: Long
                val durationType: String

                if (isPomodoro) {
                    currentDuration = initialTime
                    durationType = "FOCUSDURATION"
                } else {
                    currentDuration = breakLength
                    durationType = "BREAKDURATION"
                }

                val initHours: Int = (currentDuration / 3600000).toInt()
                val initMins: Int = (((currentDuration % 3600000) / 60000)).toInt()

                hoursPicker.value = initHours
                minutesPicker.value = initMins

                durationSave.setOnClickListener {

                    val hours = hoursPicker.value
                    val mins = minutesPicker.value

                    val durationInMillis: Long = ((hours * 3600) + (mins * 60)) * 1000L

                    if (isPomodoro) {
                        initialTime = durationInMillis
                    } else {
                        breakLength = durationInMillis
                    }
                    timeRemaining = durationInMillis
                    Log.d("initial time now", initialTime.toString())

                    requireContext().getSharedPreferences("TAMADORO_$durationType", Context.MODE_PRIVATE).edit().
                    putLong("TAMADORO_$durationType", durationInMillis).apply()

                    timerTextView.text = formatTime(timeRemaining)
                    pickerContainer.visibility = LinearLayout.INVISIBLE

                }


            }

        }

        deleteStats.setOnClickListener {
            manager.deleteAllSessions()
        }

        focusModeBtn.setOnClickListener {
            isPomodoro = true
            timerTextView.text = formatTime(initialTime)
            timeRemaining = initialTime
            updateButtonAppearance()
        }

        breakModeBtn.setOnClickListener {
            isPomodoro = false
            timerTextView.text = formatTime(breakLength)
            timeRemaining = breakLength
            updateButtonAppearance()
        }

        startBtn.setOnClickListener {//when start button clicked
            if (timerRunning) {
                stopPauseTimer(true, 0)
            } else {
                startTimer(timeRemaining)
            }
            Log.d("start button pressed", "PRESSED ")
        }


        skipButton.setOnClickListener {
            skipTimer()
            timeRemaining = if (isPomodoro){
                initialTime
            } else{
                breakLength
            }

        }

        settingsBtn.setOnClickListener {
            val openSettingsActivity = Intent(requireActivity(), SettingsActivity::class.java)
            startActivity(openSettingsActivity)
        }

        button.setOnClickListener {
            logSession(69)
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
                //val minutes = (initialTime / 60000, above is for debug
                if (isPomodoro) {
                    logSession(minutes)

                } else {
                    stopPauseTimer(false, breakLength)
                }
                showNotification()


            }
        }.start()

        startBtn.text = "Pause"
        skipButton.visibility = Button.INVISIBLE
        timerRunning = true
    }

   private fun logSession(minutes: Int) {
        manager.addSession(minutes, tag)
        pet.addCoins(minutes/60)
        stopPauseTimer(false, initialTime)
        timerTextView.text = "Finished!"

        Statistics.updateStreaks(requireContext())
        Statistics.updateLastSessionDate(requireContext())

        //DEBUG STUFF:
        val x = pet.getTotalCoins()
        Log.d("coin amount", x.toString())
    }

    private fun stopPauseTimer(pause: Boolean, time: Long) {
        timer?.cancel()
        timerRunning = false
        if (pause) {
            startBtn.text = "Resume"
            skipButton.visibility = Button.VISIBLE
        } else {
            timerTextView.text = formatTime(time)
            startBtn.text = "Start"
            timeRemaining = time
        }
    }

    private fun skipTimer() {
        //to log time user has already focused for
        //if its in focus mode then the skip will skip to break mode, vice versa
        if (isPomodoro) {
            val timeFocusedAlready = ((initialTime - timeRemaining + 1000) / 1000).toInt()
            manager.addSession(timeFocusedAlready,tag)
            //statistics.setLastSessionDate(LocalDateTime.now())
            timerTextView.text = formatTime(breakLength)//resets text view
            isPomodoro = false

        } else {
            timerTextView.text = formatTime(initialTime)
            isPomodoro = true
        }
        timer?.cancel()
        updateButtonAppearance()
        startBtn.text = "Start"
        skipButton.visibility = Button.INVISIBLE

        //resetting timer
        timerRunning = false

    }

    private fun showNotification() {

        val notificationManager = NotificationManagerCompat.from(requireContext())
        if (notificationManager.areNotificationsEnabled()) {
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationSound: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


            val builder = NotificationCompat.Builder(requireContext(), channelID)
                .setContentTitle("Title")
                .setContentText("This is a sample notif")
                .setSmallIcon(R.drawable.notification_temp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setFullScreenIntent(pendingIntent,true)

                .setContentIntent(pendingIntent)
                .setSound(notificationSound)
                .setVibrate(longArrayOf(1000))

            with(NotificationManagerCompat.from(requireContext())) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(notifID, builder.build())
            }
        } else {
            checkNotificationPermission()
        }
    }

    private fun createNotifChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)
            val notifmanager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notifmanager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), notifPermissionRequestCode)
            }
        }
    }

    private fun getColour(attr: Int): Int {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    private fun updateUI() {
        if (timerRunning) {
            settingsBtn.visibility = Button.INVISIBLE
            navbar.visibility = View.INVISIBLE
        } else {
            settingsBtn.visibility = Button.VISIBLE
            navbar.visibility = View.VISIBLE
        }
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

    private fun formatTime(millis: Long): String {
        val minutes = millis / 1000 / 60
        val seconds = (millis / 1000) % 60
        return "%02d:%02d".format(minutes, seconds)
    }

}
