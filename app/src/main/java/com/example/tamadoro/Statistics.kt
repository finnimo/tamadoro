package com.example.tamadoro

class Statistics {
    data class Session(val minutes: Int)

    private val totalSessions: MutableList<Session> = mutableListOf()

    fun addSession(minutes: Int){
        val session = Session(minutes)
        totalSessions.add(session)
    }

    fun getSession(index:Int):Session?{
        return if (index in totalSessions.indices){
            totalSessions[index]
        } else{
            null
        }
    }

    fun getTotalSessions():Int{
        return totalSessions.size
    }

    fun getTotalDuration(): Long{
        return totalSessions.sumOf {it.minutes.toLong()}
    }

    fun getAverageDuration():Long{
        return if (totalSessions.isNotEmpty()){
            getTotalDuration()/totalSessions.size
        } else{
            0
        }
    }

}