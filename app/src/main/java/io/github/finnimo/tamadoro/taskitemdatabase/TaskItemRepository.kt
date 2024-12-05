package io.github.finnimo.tamadoro.taskitemdatabase

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskItemRepository(private val taskItemDao: TaskItemDao) {

    val allTaskItems: Flow<List<TaskItem>> = taskItemDao.getAllTaskItems().map { tasks ->
        tasks.sortedBy { it.completed }
    }

    @WorkerThread
    suspend fun insertTaskItem(taskItem: TaskItem){
        taskItemDao.insertTaskItem(taskItem)
    }

    @WorkerThread
    suspend fun updateTaskItem(taskItem: TaskItem) {
        taskItemDao.updateTaskItem(taskItem)
    }

    @WorkerThread
    suspend fun deleteTaskItem(taskItem: TaskItem) {
        taskItemDao.deleteTaskItem(taskItem)
    }

    @WorkerThread
    suspend fun deleteCompletedTasks() {
        taskItemDao.deleteCompletedTasks()
    }

}