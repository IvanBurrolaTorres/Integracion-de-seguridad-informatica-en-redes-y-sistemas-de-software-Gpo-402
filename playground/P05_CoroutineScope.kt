import kotlinx.coroutines.*

suspend fun tiempoJugadorUno(): String {
    delay(1000)
    return "1: 12.4 s"
}

suspend fun tiempoJugadorDos(): String {
    delay(1000)
    return "2: 11.8 s"
}

suspend fun resultados(): String = coroutineScope {
    val uno = async { tiempoJugadorUno() }
    val dos = async { tiempoJugadorDos() }
    "${uno.await()} — ${dos.await()}"
}

fun main() = runBlocking {
    println("Arranca la carrera")
    println(resultados())
    println("Se acabó")
}
