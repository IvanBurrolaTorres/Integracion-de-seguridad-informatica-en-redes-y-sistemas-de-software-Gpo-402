package mx.tec.racetracker

import androidx.compose.runtime.*
import kotlinx.coroutines.delay

class RaceParticipant(val name: String, private val increment: Int = 1) {
    var currentProgress by mutableIntStateOf(0)
        private set
    suspend fun run() {
        while (currentProgress < 100) {
            delay(100)
            currentProgress = (currentProgress + increment).coerceAtMost(100)
        }
    }
    fun reset() { currentProgress = 0 }
}
