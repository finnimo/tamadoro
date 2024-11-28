package io.github.finnimo.tamadoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TasksFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskName: TextView
    private lateinit var taskDesc: TextView
    private lateinit var newTaskBtn: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskName = view.findViewById(R.id.taskNameTemp)
        taskDesc = view.findViewById(R.id.taskDescTemp)
        newTaskBtn = view.findViewById(R.id.newTaskBtn)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        newTaskBtn.setOnClickListener {
            NewTaskSheet().show(childFragmentManager, "newTask")
        }

        taskViewModel.name.observe(viewLifecycleOwner) {
            taskName.text = String.format("Task Name: %s", it)
        }

        taskViewModel.desc.observe(viewLifecycleOwner) {
            taskDesc.text = String.format("Task Desc: %s", it)
        }

    }


}