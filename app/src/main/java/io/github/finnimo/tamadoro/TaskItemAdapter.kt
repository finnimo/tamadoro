package io.github.finnimo.tamadoro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TaskItemAdapter(

    private val taskItems: List<TaskItem>,

): RecyclerView.Adapter<TaskItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.singular_task_item,parent,false)
        return TaskItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bindTaskItem(taskItems[position])
    }

    override fun getItemCount(): Int {
        return taskItems.size
    }

}