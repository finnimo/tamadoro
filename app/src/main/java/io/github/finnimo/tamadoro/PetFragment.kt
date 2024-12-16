package io.github.finnimo.tamadoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.util.query
import io.github.finnimo.tamadoro.sessiondatabase.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PetFragment : Fragment() {

    private lateinit var coinsTV: TextView
    private lateinit var meter: TextView
    private lateinit var weeklyGoalTV: TextView
    private lateinit var sessionsTodayTV: TextView


    private lateinit var pet: Pet
    private lateinit var manager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pet = Pet(requireContext())
        manager = SessionManager(requireContext())
        GlobalScope.launch(Dispatchers.Main) {
            val sessions = manager.getAllSessions()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val sessionsToday = withContext(Dispatchers.IO) {
                manager.getTotalSessionToday()
            }
            val currentWeekDuration = withContext(Dispatchers.IO) {
                manager.getTotalDurationThisWeek()
            }

            val focusedToday = (sessionsToday > 0)
            sessionsTodayTV.text = sessionsToday.toString()
            meter.text = pet.happinessChecker(pet.onTrack(currentWeekDuration),focusedToday).toString()
        }

        coinsTV = view.findViewById(R.id.coinsTV)
        meter = view.findViewById(R.id.happinessMeter)
        weeklyGoalTV = view.findViewById(R.id.weeklyGoalTV)
        sessionsTodayTV = view.findViewById(R.id.sessionsTodayTV)

        coinsTV.text = pet.getTotalCoins().toString()
        weeklyGoalTV.text = pet.getWeeklyGoal().toString()




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pet, container, false)
    }

}