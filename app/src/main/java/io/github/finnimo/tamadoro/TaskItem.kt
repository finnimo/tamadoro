package io.github.finnimo.tamadoro

import java.time.LocalDate
import java.util.UUID

class TaskItem(
    var name: String,
    var dueDate: LocalDate?,
    var completed:Boolean = false,
    var taskID: UUID = UUID.randomUUID()
    )
{



}