package io.github.finnimo.tamadoro.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//imports for working with xml based ui
import androidx.fragment.app.Fragment
import io.github.finnimo.tamadoro.fragments.PetFragment
import io.github.finnimo.tamadoro.R
import io.github.finnimo.tamadoro.fragments.StatsFragment
import io.github.finnimo.tamadoro.fragments.TasksFragment
import io.github.finnimo.tamadoro.databinding.ActivityMainBinding
import io.github.finnimo.tamadoro.fragments.TimerFragment
import io.github.finnimo.tamadoro.sessiondatabase.SessionsDatabase

//imports for notifications

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionsDatabase: SessionsDatabase
    override fun onCreate(savedInstanceState: Bundle?) {

        //HANDLING THEME BEFORE EVERYTHING IS RENDERED
        val sharedPrefs = getSharedPreferences("TAMADORO_THEME", MODE_PRIVATE)
        val selectedTheme = sharedPrefs.getString("TAMADORO_THEME", "light") ?: "light"


        when (selectedTheme) {
            "dark" -> setTheme(R.style.darkTheme)

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
    }

    private fun loadFragment(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment) // Replace with your fragment container's ID
        transaction.commit()

    }
}
