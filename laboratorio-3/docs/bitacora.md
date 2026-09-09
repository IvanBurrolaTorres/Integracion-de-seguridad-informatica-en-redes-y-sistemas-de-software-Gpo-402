# Bitácora técnica · Práctica 3

Documento de explicación del código. No afirma que el alumno haya ejecutado los experimentos de clase.

## Ejercicio 0
1. Dos launch de 1 s: aproximadamente 1 s total. El código posterior a launch puede imprimirse antes de las metas. Entre metas no se debe depender del orden.
2. async { a() }.await() y luego async { b() }.await(): aproximadamente 2 s. Se lanza b después de esperar a a.
3. Dos async dentro de coroutineScope, esperados al final: aproximadamente 1 s. El scope espera a sus hijos.

## B2: diagnóstico
- suspend permite suspender; no inicia otra corrutina. Dos llamadas consecutivas siguen siendo secuenciales.
- Es la misma estructura de P0.2.
- Falta launch para iniciar cada corredor en su propia corrutina.

## C1: el botón miente
Los dos launch devuelven el control enseguida y se ejecuta raceInProgress = false. Al sacar LaunchedEffect de la composición también se cancela su trabajo; las barras incluso pueden detenerse casi en cero. coroutineScope debe envolver los launch para esperar a todos antes de bajar la bandera.

## Paso D: ámbitos e hilos
LaunchedEffect pertenece a la composición. Al salir se cancelan sus hijos. rememberCoroutineScope permite lanzar desde eventos de UI; viewModelScope pertenece al ViewModel; lifecycleScope pertenece al ciclo de vida de Activity/Fragment.

La carrera utiliza el hilo principal y lo libera en cada delay. Dispatchers.IO se reserva para operaciones bloqueantes de entrada/salida; Default para cálculo intensivo. suspend por sí mismo no cambia de hilo. No hay razón para usar IO en este simulador.

CancellationException se vuelve a lanzar. Sin un catch también se propagaría correctamente. Un catch amplio puede ocultar la cancelación; en el ejemplo específico con catch fuera del while, el ciclo sale, no sigue iterando mágicamente.

Fuentes: https://developer.android.com/develop/ui/compose/side-effects y https://kotlinlang.org/docs/coroutines-basics.html

## Quiniela: lista, ganador y estado
Se eligió una List<RaceParticipant>: los tres tienen el mismo comportamiento y la lista permite lanzar, dibujar, reiniciar y comprobar metas con una sola regla. Evita olvidar al tercero al modificar una operación.

El ViewModel conserva corredores, apuesta, velocidades, llegadas y marcador al girar. LaunchedEffect sigue siendo dueño de la ejecución: la actividad anterior cancela su efecto y la nueva lo reanuda desde el progreso conservado. Un Mutex evita solapamientos entre efectos al recrear la pantalla. Un identificador invalida continuaciones después de pausa o reinicio, incluso ante toques rápidos. No hay almacenamiento en disco; el requisito es conservar la sesión y la rotación.

Cada nueva carrera mezcla [1, 2, 3] y asigna un incremento distinto a cada jugador. Cualquiera puede recibir el 3; la apuesta se cierra antes del sorteo. Reanudar conserva el sorteo original. Que se repita un ganador por azar es válido: no se fuerza una alternancia que haría predecible la apuesta.

Cada corredor llama onFinish en el mismo paso en que llega a 100. Se agrega su identificador al orden de llegada; el primero es el ganador. El orden de await o de la lista no identifica quién llegó primero. Las actualizaciones están en Main y no se suspenden entre comprobar y agregar. coroutineScope espera a los tres y entonces registra una sola carrera y, si corresponde, un acierto.

Reiniciar cancela la carrera sin contarla, borra apuesta y progreso, y conserva el marcador. Borrar marcador tiene su propio botón. La apuesta permanece cerrada durante una pausa para impedir cambiarla después de observar quién va ganando. El resultado usa la apuesta original, aunque se elija otro jugador para la siguiente carrera.
