package io.github.finnimo.tamadoro.taskitemdatabase

import android.graphics.Paint
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import io.github.finnimo.tamadoro.R
import io.github.finnimo.tamadoro.fragments.NewTaskSheet
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val view: View,
    private val listener: onTaskCompletedListener
): RecyclerView.ViewHolder(view) {

    private val taskName: TextView = view.findViewById(R.id.taskName)
    private val taskDueDate: TextView = view.findViewById(R.id.taskDueDate)
    private val checkBoxBtn: ImageButton = view.findViewById(R.id.checkBoxBtn)
    private val cardView: CardView = view.findViewById(R.id.taskItemContainer)

    fun bindTaskItem(taskItem: TaskItem) {
        taskName.text = taskItem.name

        if (taskItem.dueDate() != null) {
            taskDueDate.text = taskItem.dueDate()?.format(DateTimeFormatter.ofPattern("dd/MM"))
        } else {
            taskDueDate.text = ""
        }

        if (taskItem.completed) {
            taskName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            checkBoxBtn.setImageResource(R.drawable.ic_checkbox_checked)

        } else {
            taskName.setPaintFlags(0)
            checkBoxBtn.setImageResource(R.drawable.ic_checkbox_unchecked)
        }

        checkBoxBtn.setOnClickListener {

            taskItem.completed = !taskItem.completed
            listener.onTaskCompletedChanged(taskItem)

        }

        cardView.setOnClickListener {

            NewTaskSheet(taskItem).show((itemView.context as FragmentActivity).supportFragmentManager, "newTask")

        }

    }

}