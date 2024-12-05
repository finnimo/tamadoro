package io.github.finnimo.tamadoro.taskitemdatabase

import android.app.Application
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItemRepository
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItemsDataBase

class TodoApplication: Application() {
    private val database by lazy { TaskItemsDataBase.getDatabase(this) }
    val repository by lazy { TaskItemRepository(database.taskItemDao()) }
}