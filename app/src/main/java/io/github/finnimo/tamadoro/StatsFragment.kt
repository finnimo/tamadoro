package io.github.finnimo.tamadoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.finnimo.tamadoro.sessiondatabase.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            val size = sessions.size
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

            totalDurationTV.text = totalDuration.toString()
            totalDurationTodayTV.text = totalDurationToday.toString()
            totalDurationThisWeekTV.text = totalDurationThisWeek.toString()
        }

        val highestStreak = Statistics.getHighestStreak(requireContext())
        highestStreakTV.text = highestStreak.toString()

        val currentStreak = Statistics.streakCheckOnViewCreated(requireContext())
        currentStreakTV.text = currentStreak.toString()




    }





}