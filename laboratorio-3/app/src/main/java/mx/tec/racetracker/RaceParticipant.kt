package mx.tec.racetracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

class RaceParticipant(val id: Int, val name: String) {
    val maxProgress = 100
    var currentProgress by mutableIntStateOf(0)
        private set
    private var progressIncrement = 1

    fun prepare(increment: Int) {
        require(increment > 0)
        progressIncrement = increment
        reset()
    }

    /** La guarda evita avances de una ejecución anterior después de pausar/reiniciar. */
    suspend fun run(canAdvance: () -> Boolean, onFinish: () -> Unit) {
        if (currentProgress == maxProgress) return
        try {
            while (currentProgress < maxProgress) {
                delay(180L)
                if (!canAdvance()) return
                currentProgress = (currentProgress + progressIncrement).coerceAtMost(maxProgress)
                // Se registra la llegada aquí, no según el orden de await o de la lista.
                if (currentProgress == maxProgress) onFinish()
            }
        } catch (e: CancellationException) {
            throw e
        }
    }

    fun reset() { currentProgress = 0 }
}
