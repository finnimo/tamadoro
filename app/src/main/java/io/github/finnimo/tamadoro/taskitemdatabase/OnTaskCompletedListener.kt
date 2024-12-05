package io.github.finnimo.tamadoro.taskitemdatabase

import io.github.finnimo.tamadoro.taskitemdatabase.TaskItem

interface onTaskCompletedListener {
    fun onTaskCompletedChanged(taskItem: TaskItem)

}