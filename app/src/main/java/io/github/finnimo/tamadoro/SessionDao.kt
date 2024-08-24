package io.github.finnimo.tamadoro

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.finnimo.tamadoro.Session

//the dao is an interface that lets me manipulate data in my database
@Dao
public interface SessionDao {
    @Insert
    fun addSession(sessions : Session)//parameter is of Session data type

    @Query("DELETE FROM Session")
    fun deleteAllSessions()

    @Query("SELECT * FROM Session")
    fun getAllSessions() : List<Session>

    @Query("SELECT * FROM Session WHERE tag = :tag")
    fun getSessionsByTag(tag : String): List<Session>
}