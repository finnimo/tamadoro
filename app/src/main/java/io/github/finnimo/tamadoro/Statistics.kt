package io.github.finnimo.tamadoro

class Statistics {
    data class Session(val seconds: Int)

    private val totalSessions: MutableList<Session> = mutableListOf()

    fun addSession(seconds: Int) {
        val session = Session(seconds)
        totalSessions.add(session)
    }

    fun getSession(index: Int): Session? {
        return if (index in totalSessions.indices) {
            totalSessions[index]
        } else {
            null
        }
    }

    fun getTotalSessions(): Int {
        return totalSessions.size
    }

    fun getTotalDuration(): Long {
        return totalSessions.sumOf { it.seconds.toLong() }
    }

    fun getAverageDuration(): Long {
        return if (totalSessions.isNotEmpty()) {
            getTotalDuration() / totalSessions.size
        } else {
            0
        }
    }

}
