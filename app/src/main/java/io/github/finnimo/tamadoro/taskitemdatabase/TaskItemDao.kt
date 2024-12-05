package io.github.finnimo.tamadoro.taskitemdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.finnimo.tamadoro.taskitemdatabase.TaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskItemDao {

    @Query("SELECT * FROM TaskItem")
    fun getAllTaskItems(): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem): Unit

    @Update
    suspend fun updateTaskItem(taskItem: TaskItem)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)

    @Query("DELETE FROM TaskItem WHERE completed = 1")
    suspend fun deleteCompletedTasks()


}