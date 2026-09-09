import kotlinx.coroutines.*

suspend fun correJugadorUno() {
    delay(1000)
    println("Jugador 1 en la meta")
}

suspend fun correJugadorDos() {
    delay(1000)
    println("Jugador 2 en la meta")
}

fun main() = runBlocking {
    println("Arranca la carrera")
    launch { correJugadorUno() }
    launch { correJugadorDos() }
    println("Se acabó")
}
