package io.github.finnimo.tamadoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TasksFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var newTaskBtn: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.todoListRecyclerView)
        newTaskBtn = view.findViewById(R.id.newTaskBtn)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)


        newTaskBtn.setOnClickListener {
            NewTaskSheet(null).show(childFragmentManager, "newTask")
            setRecyclerView()
        }

    }

    private fun setRecyclerView() {
        taskViewModel.taskItems.observe(viewLifecycleOwner) {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = TaskItemAdapter(it)
            }
        }
    }

}