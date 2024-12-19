package io.github.finnimo.tamadoro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.finnimo.tamadoro.R
import io.github.finnimo.tamadoro.sessiondatabase.SessionManager
import io.github.finnimo.tamadoro.sessiondatabase.Statistics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

class StatsFragment : Fragment() {

    private lateinit var manager: SessionManager
    private lateinit var highestStreakTV: TextView
    private lateinit var currentStreakTV: TextView
    private lateinit var totalDurationTV: TextView
    private lateinit var totalDurationTodayTV: TextView
    private lateinit var totalDurationThisWeekTV: TextView
    private lateinit var averageSessionDurationTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manager = SessionManager(requireContext())
        GlobalScope.launch(Dispatchers.Main) {
            val sessions = manager.getAllSessions()
            //val sessionDuration = manager.getTotalDuration()
           // averageSessionDurationTV.text = (sessionDuration/size).toString()

        }

        //initializing text views

        highestStreakTV = view.findViewById(R.id.highestStreakTextView)
        currentStreakTV = view.findViewById(R.id.currentStreakTextView)
        totalDurationTV = view.findViewById(R.id.totalDurationTextView)
        totalDurationTodayTV = view.findViewById(R.id.totalDurationTodayTextView)
        totalDurationThisWeekTV = view.findViewById(R.id.totalDurationThisWeekTextView)
        averageSessionDurationTV = view.findViewById(R.id.averageSessionDuration)

        viewLifecycleOwner.lifecycleScope.launch {
            val totalDuration = withContext(Dispatchers.IO) {
                manager.getTotalDuration()
            }

            val totalDurationToday = withContext(Dispatchers.IO) {
                val startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                manager.getTotalDurationThisWeek(startOfDay)
            }

            val totalDurationThisWeek = withContext(Dispatchers.IO) {
                val startOfWeek = (LocalDate.now().with(DayOfWeek.MONDAY)).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                manager.getTotalDurationThisWeek(startOfWeek)
            }

            totalDurationTV.text = "${((totalDuration.toDouble()/3600).toBigDecimal().setScale(2, RoundingMode.UP).toDouble())} Hrs"
            totalDurationTodayTV.text = "${((totalDurationToday.toDouble()/3600).toBigDecimal().setScale(2, RoundingMode.UP).toDouble())} Hrs"
            totalDurationThisWeekTV.text = "${(totalDurationThisWeek.toDouble()/3600).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()} Hrs"
        }

        val highestStreak = Statistics.getHighestStreak(requireContext())
        highestStreakTV.text = highestStreak.toString()

        Statistics.streakCheckOnViewCreated(requireContext())
        val currentStreak =  Statistics.getCurrentStreak(requireContext()).toString()
        Log.d("current Streak", currentStreak)
        currentStreakTV.text = currentStreak






    }





}