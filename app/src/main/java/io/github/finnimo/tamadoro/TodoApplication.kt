package io.github.finnimo.tamadoro

import android.app.Application

class TodoApplication: Application() {
    private val database by lazy { TaskItemsDataBase.getDatabase(this) }
    val repository by lazy { TaskItemRepository(database.taskItemDao()) }
}