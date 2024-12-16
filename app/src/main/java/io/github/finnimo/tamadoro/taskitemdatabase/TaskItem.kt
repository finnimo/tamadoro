package io.github.finnimo.tamadoro.taskitemdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
class TaskItem(
    var name: String,
    var tag: String,
    var  dueDateString: String?,
    var completed: Boolean = false,
    @PrimaryKey(autoGenerate = true) var taskID: Int = 0
    )
{

fun dueDate(): LocalDate? = if (dueDateString == null) null
    else LocalDate.parse(dueDateString, dateFormatter)

    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
    }

}