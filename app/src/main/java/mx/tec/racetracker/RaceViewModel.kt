package mx.tec.racetracker

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

enum class RacePhase { READY, RUNNING, PAUSED, FINISHED }

class RaceViewModel : ViewModel() {
    val participants = List(3) { RaceParticipant(it, "Jugador ${it + 1}") }
    var phase by mutableStateOf(RacePhase.READY)
        private set
    var selectedId by mutableStateOf<Int?>(null)
        private set
    var roundBetId by mutableStateOf<Int?>(null)
        private set
    var hits by mutableIntStateOf(0)
        private set
    var completedRaces by mutableIntStateOf(0)
        private set
    var arrivalOrder by mutableStateOf<List<Int>>(emptyList())
        private set
    var executionId by mutableIntStateOf(0)
        private set

    private val raceMutex = Mutex()
    val isRunning get() = phase == RacePhase.RUNNING
    val canBet get() = phase == RacePhase.READY || phase == RacePhase.FINISHED
    val winnerId get() = arrivalOrder.firstOrNull()
    val guessedCorrectly get() = winnerId != null && winnerId == roundBetId

    fun selectParticipant(id: Int) {
        if (canBet && participants.any { it.id == id }) selectedId = id
    }

    fun toggleRunning() {
        if (isRunning) {
            phase = RacePhase.PAUSED
            executionId++
            return
        }
        if (selectedId == null) return
        if (phase != RacePhase.PAUSED) {
            // Permutación uniforme: todos tienen la misma probabilidad de recibir cada velocidad.
            // Se sortea únicamente al empezar una carrera nueva, después de apostar.
            val speeds = listOf(1, 2, 3).shuffled()
            participants.forEachIndexed { index, player -> player.prepare(speeds[index]) }
            arrivalOrder = emptyList()
            roundBetId = selectedId
        }
        executionId++
        phase = RacePhase.RUNNING
    }

    fun resetRace() {
        // Invalida primero cualquier continuación pendiente del delay.
        executionId++
        phase = RacePhase.READY
        participants.forEach { it.reset() }
        arrivalOrder = emptyList()
        selectedId = null
        roundBetId = null
    }

    fun resetScore() {
        hits = 0
        completedRaces = 0
    }

    /** El efecto de Compose aporta la corrutina; el ViewModel conserva el estado al girar. */
    suspend fun runRace(token: Int) = raceMutex.withLock {
        fun isCurrent() = token == executionId && isRunning
        if (!isCurrent()) return@withLock
        coroutineScope {
            participants.forEach { player ->
                launch {
                    player.run(
                        canAdvance = { isCurrent() },
                        onFinish = {
                            // Todas las actualizaciones ocurren en Main, sin suspender entre leer y escribir.
                            if (isCurrent() && player.id !in arrivalOrder) {
                                arrivalOrder = arrivalOrder + player.id
                            }
                        }
                    )
                }
            }
        }
        // Solo una carrera completa cuenta; pausar o reiniciar nunca puntúa.
        if (isCurrent() && participants.all { it.currentProgress == it.maxProgress }) {
            completedRaces++
            if (guessedCorrectly) hits++
            phase = RacePhase.FINISHED
        }
    }
}
