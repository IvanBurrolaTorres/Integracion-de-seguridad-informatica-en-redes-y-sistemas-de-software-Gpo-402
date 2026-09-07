# Race Tracker · Práctica 3 TC2007B

Proyecto Android completo en Kotlin y Jetpack Compose, paquete `mx.tec.racetracker`.

## Abrir y ejecutar
1. En Android Studio selecciona **Open** y abre esta carpeta `RaceTracker` (no solo `app`).
2. Espera a que termine la sincronización de Gradle.
3. Selecciona un emulador o teléfono Android 7.0 o superior y pulsa **Run**.

Configuración: AGP 9.3.1, Kotlin/Compose 2.4.10, Gradle 9.7.1, compileSdk/targetSdk 37, minSdk 24. AGP 9 integra Kotlin: no agregues `org.jetbrains.kotlin.android`. Gradle usa JDK 17 o superior; se recomienda el JDK incluido en Android Studio. El SDK local está configurado para este equipo. En otra computadora Android Studio debe ajustar `local.properties`.

La app funciona sin red, base de datos ni permisos especiales. La primera sincronización en otra computadora puede necesitar internet para descargar herramientas.

## Uso
- Elige uno de los tres jugadores y toca **Arrancar**.
- En cada nueva carrera se sortean los incrementos 1, 2 y 3. Nadie tiene una ventaja fija. Las carreras duran unos 18 segundos; el más rápido llega en unos 6 segundos.
- **Pausa** conserva progreso y velocidades. **Arrancar** reanuda.
- La apuesta queda bloqueada también durante la pausa.
- El botón vuelve a **Arrancar** cuando llegan los tres. Se muestra el ganador, el podio y si acertaste.
- Puedes elegir otro candidato y arrancar otra carrera desde el resultado.
- **Reiniciar** pone la carrera en cero y conserva el marcador. **Borrar marcador** lo pone en cero por separado.
- Girar conserva la carrera y el marcador mediante un ViewModel. Cerrar definitivamente la sesión no guarda resultados en disco.

## Archivos
- `RaceParticipant.kt`: progreso observable, delay y propagación de cancelación.
- `RaceViewModel.kt`: sorteo, apuesta, llegadas, marcador y protección contra ejecuciones antiguas.
- `ui/RaceTrackerApp.kt`: efecto condicionado y pantalla sin lógica asíncrona en los controles.
- `playground/`: cinco ejemplos independientes de la Parte 0.
- `docs/bitacora.md`: respuestas y decisiones técnicas.
- `docs/verificacion.md`: comprobaciones realmente realizadas.
- `entregables/`: APK instalable, dos videos de 40 segundos, capturas y resultado de compilación.

El repositorio incluye commits `bloque-a`, `bloque-b`, `bloque-c`, `paso-d` y `quiniela`. El historial muestra construcción incremental; no se dejaron los bugs didácticos en la versión final. La Parte 0 y los experimentos manuales de clase son actividades personales: la bitácora explica las respuestas sin afirmar que el alumno los haya realizado.
