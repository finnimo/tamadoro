package io.github.finnimo.tamadoro.taskitemdatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.finnimo.tamadoro.R

class TaskItemAdapter(

    private val listener: onTaskCompletedListener,
    private var taskItems: List<TaskItem>,

    ): RecyclerView.Adapter<TaskItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.singular_task_item,parent,false)
        return TaskItemViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bindTaskItem(taskItems[position])
    }

    override fun getItemCount(): Int {
        return taskItems.size
    }

    fun setTasks(newTasks: List<TaskItem>) {
        taskItems = newTasks
        notifyDataSetChanged()
    }

}