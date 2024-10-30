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

/**
 * A simple [Fragment] subclass.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatsFragment : Fragment() {

    private lateinit var manager: SessionManager
    private lateinit var highestStreakTV: TextView
    private lateinit var currentStreakTV: TextView
    private lateinit var totalDurationTV: TextView
    private lateinit var totalDurationThisWeekTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        manager = SessionManager(requireContext())
        manager.getAllSessions()

        //initializing text views

        highestStreakTV = view.findViewById(R.id.highestStreakTextView)
        currentStreakTV = view.findViewById(R.id.currentStreakTextView)
        totalDurationTV = view.findViewById(R.id.totalDurationTextView)
        totalDurationThisWeekTV = view.findViewById(R.id.totalDurationThisWeekTextView)

        viewLifecycleOwner.lifecycleScope.launch {
            val totalDuration = withContext(Dispatchers.IO) {
                manager.getTotalDuration()
            }

            val totalDurationThisWeek = withContext(Dispatchers.IO) {
                manager.getTotalDurationThisWeek()
            }
            totalDurationTV.text = totalDuration.toString()
            totalDurationThisWeekTV.text = totalDurationThisWeek.toString()
        }

        val highestStreak = Statistics.getHighestStreak(requireContext())
        highestStreakTV.text = highestStreak.toString()

        val currentStreak = Statistics.getCurrentStreak(requireContext())
        currentStreakTV.text = currentStreak.toString()



    }



}