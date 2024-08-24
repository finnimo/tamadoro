package io.github.finnimo.tamadoro

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/*an entity is a table in my sqlite database
each instance of the session class represents a row in the session table
each variable below is a field in the table
*/
@Entity //Session will also be the table name
data class Session (
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //used to identify each row
    var seconds:Int,
    var tag:String = "None",
    var dateTime:Long = System.currentTimeMillis(),
    )