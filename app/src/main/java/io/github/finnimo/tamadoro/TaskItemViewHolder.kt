package io.github.finnimo.tamadoro

import android.graphics.Paint
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val view: View,
): RecyclerView.ViewHolder(view) {

    private val taskName: TextView = view.findViewById(R.id.taskName)
    private val taskDueDate: TextView = view.findViewById(R.id.taskDueDate)
    private val checkBoxBtn: ImageButton = view.findViewById(R.id.checkBoxBtn)
    private val cardView: CardView = view.findViewById(R.id.taskItemContainer)

    fun bindTaskItem(taskItem: TaskItem) {
        taskName.text = taskItem.name

        if (taskItem.dueDate != null) {
            taskDueDate.text = taskItem.dueDate?.format(DateTimeFormatter.ofPattern("dd/MM"))
        } else {
            taskDueDate.text = ""
        }

        checkBoxBtn.setOnClickListener {

            if (taskItem.completed) {
                taskName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                checkBoxBtn.setImageResource(R.drawable.ic_checkbox_checked)

            } else {
                taskName.setPaintFlags(0)
                checkBoxBtn.setImageResource(R.drawable.ic_checkbox_unchecked)
            }

            taskItem.completed = !taskItem.completed

        }

        cardView.setOnClickListener {

            NewTaskSheet(taskItem).show((itemView.context as FragmentActivity).supportFragmentManager, "newTask")

        }


    }

}