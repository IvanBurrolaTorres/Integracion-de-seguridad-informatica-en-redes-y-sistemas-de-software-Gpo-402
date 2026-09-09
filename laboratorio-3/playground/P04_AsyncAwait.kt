import kotlinx.coroutines.*

suspend fun tiempoJugadorUno(): String {
    delay(1000)
    return "1: 12.4 s"
}

suspend fun tiempoJugadorDos(): String {
    delay(1000)
    return "2: 11.8 s"
}

fun main() = runBlocking {
    println("Arranca la carrera")
    val uno = async { tiempoJugadorUno() }
    val dos = async { tiempoJugadorDos() }
    println("${uno.await()} — ${dos.await()}")
    println("Se acabó")
}
