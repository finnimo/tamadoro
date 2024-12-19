package io.github.finnimo.tamadoro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.finnimo.tamadoro.fragments.pethandling.Pet
import io.github.finnimo.tamadoro.R
import io.github.finnimo.tamadoro.sessiondatabase.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode



class PetFragment : Fragment() {

    private lateinit var coinsTV: TextView
    private lateinit var meter: ImageView
    private lateinit var weeklyGoalTV: TextView
    private lateinit var sessionsTodayTV: TextView


    private lateinit var pet: Pet
    private lateinit var manager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pet = Pet(requireContext())
        manager = SessionManager(requireContext())
        GlobalScope.launch(Dispatchers.Main) {
            manager.getAllSessions()
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

            val happiness = pet.happinessChecker(pet.onTrack(currentWeekDuration),focusedToday)
            if (happiness == 1) {
                meter.setImageResource(R.drawable.happiness_1)
            } else if (happiness == 2) {
                meter.setImageResource(R.drawable.happiness_2)
            } else {
                meter.setImageResource(R.drawable.happiness_3)
            }


        }

        coinsTV = view.findViewById(R.id.coinsTV)
        meter = view.findViewById(R.id.happinessMeter)
        weeklyGoalTV = view.findViewById(R.id.weeklyGoalTV)
        sessionsTodayTV = view.findViewById(R.id.sessionsTodayTV)

        coinsTV.text = pet.getTotalCoins().toString()
        var goalInHrs = (pet.getWeeklyGoal().toDouble()/60).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

        weeklyGoalTV.text = goalInHrs.toString()

        val pickerContainer = view.findViewById<LinearLayout>(R.id.pickerContainer)
        val hoursPicker = view.findViewById<NumberPicker>(R.id.hoursPicker)
        val minutesPicker = view.findViewById<NumberPicker>(R.id.minutesPicker)
        val saveDurationBtn = view.findViewById<Button>(R.id.durationSaverBtn)

        weeklyGoalTV.setOnClickListener {
            pickerContainer.visibility = LinearLayout.VISIBLE

            hoursPicker.minValue = 0
            hoursPicker.maxValue = 120
            minutesPicker.minValue = 0
            minutesPicker.maxValue = 59

            val initHours: Int = (pet.getWeeklyGoal() / 60)
            val initMins: Int = pet.getWeeklyGoal() % 60

            hoursPicker.value = initHours
            minutesPicker.value = initMins

            saveDurationBtn.setOnClickListener {

                val hours = hoursPicker.value
                val mins = minutesPicker.value

                var durationInMins: Int = (hours * 60) + mins

                pet.changeWeeklyGoal(durationInMins)
                goalInHrs = (pet.getWeeklyGoal().toDouble()/60).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

                weeklyGoalTV.text = goalInHrs.toString()
                pickerContainer.visibility = LinearLayout.INVISIBLE

            }


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pet, container, false)


        return inflater.inflate(R.layout.fragment_pet, container, false)
    }

}