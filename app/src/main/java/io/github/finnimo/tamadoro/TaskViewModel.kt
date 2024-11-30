package io.github.finnimo.tamadoro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.UUID

class TaskViewModel: ViewModel() {

    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    init {
        taskItems.value = mutableListOf()
    }

    fun addTaskItem(newTask: TaskItem) {
        val list = taskItems.value
        list!!.add(newTask)
        taskItems.postValue(list)
    }

    fun updateTaskItem(id: UUID, name: String, dueDate: LocalDate?) {
        val list = taskItems.value
        val task = list!!.find { it.taskID == id }!!
        task.name = name
        task.dueDate = dueDate
        taskItems.postValue(list)
    }

   /* fun toggleCompleted(taskItem: TaskItem) {
        val list = taskItems.value
        val task = list!!.find { it.taskID == taskItem.taskID }!!
        task.completed = !task.completed
    }*/


}