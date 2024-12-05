package io.github.finnimo.tamadoro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.finnimo.tamadoro.R
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItemModelFactory
import io.github.finnimo.tamadoro.taskitemdatabase.TaskViewModel
import io.github.finnimo.tamadoro.taskitemdatabase.TodoApplication
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItem
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItemAdapter
import io.github.finnimo.tamadoro.taskitemdatabase.onTaskCompletedListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TasksFragment : Fragment(), onTaskCompletedListener {

    private val taskViewModel: TaskViewModel by viewModels {
        TaskItemModelFactory((requireActivity().application as TodoApplication).repository)
    }

    private lateinit var adapter: TaskItemAdapter
    private lateinit var newTaskBtn: Button
    private lateinit var clearBtn: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        recyclerView = view.findViewById(R.id.todoListRecyclerView)
        newTaskBtn = view.findViewById(R.id.newTaskBtn)
        clearBtn = view.findViewById(R.id.clearCompletedBtn)

        adapter = TaskItemAdapter(this,emptyList())
        recyclerView.adapter = adapter

        taskViewModel.taskItems.observe(viewLifecycleOwner) { tasks ->
            adapter.setTasks(tasks)
        }

        newTaskBtn.setOnClickListener {
            NewTaskSheet(null).show(childFragmentManager, "newTask")
            setRecyclerView()
        }

        clearBtn.setOnClickListener {
            taskViewModel.deleteCompletedtasks()
        }

    }

    override fun onTaskCompletedChanged(task: TaskItem) {

        taskViewModel.updateTaskItem(task)
    }

    private fun setRecyclerView() {
        taskViewModel.taskItems.observe(viewLifecycleOwner) { taskItems ->
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = TaskItemAdapter(this@TasksFragment,taskItems)
            }
        }
    }


}