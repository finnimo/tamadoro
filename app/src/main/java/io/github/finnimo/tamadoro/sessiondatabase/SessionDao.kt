package io.github.finnimo.tamadoro.sessiondatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//the dao is an interface that lets me manipulate data in my database
@Dao
public interface SessionDao {
    //add 1 session
    @Insert
    fun addSession(sessions : Session)//parameter is of Session data type
    //delete all sessions
    @Query("DELETE FROM Session")
    fun deleteAllSessions()
    //get all sessions
    @Query("SELECT * FROM Session")
    fun getAllSessions() : List<Session>
    //get all sessions of a specific tag
    @Query("SELECT * FROM Session WHERE tag = :tag")
    fun getSessionsByTag(tag : String): List<Session>
    //get total duration
    @Query("SELECT SUM(seconds) FROM Session")
    fun getTotalDuration(): Int
    //get duration from today
    @Query("SELECT SUM(seconds) FROM Session WHERE ((dateTime >= :startDate) and (dateTime <= :endDate))")
    fun getTotalDurationInPeriod(startDate: Long, endDate: Long): Int

    //currently working in seconds, make sure it wokrs in minutes

}