package io.github.finnimo.tamadoro

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskItem::class], version = 1, exportSchema = false)
abstract class TaskItemsDataBase: RoomDatabase() {
    abstract fun taskItemDao(): TaskItemDao

    companion object {
        @Volatile
        private var INSTANCE: TaskItemsDataBase? = null

        fun getDatabase(context: Context): TaskItemsDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskItemsDataBase::class.java,
                    "TASKITEMS_DB"

                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}