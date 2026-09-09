# Verificación · 7 de septiembre de 2026

## Compilación
Ejecutado en este equipo con el JDK de Android Studio:

```sh
./gradlew :app:assembleDebug :app:lintDebug --offline --console=plain
```

Resultado: BUILD SUCCESSFUL. Lint: No issues found (sin errores ni advertencias). APK en entregables/RaceTracker-debug.apk. Se incluyen la salida de compilación y el informe de lint. No se añadieron pruebas automatizadas.

## Comprobación interactiva en emulador
- Inicio: tres barras en cero, Arrancar deshabilitado hasta elegir una apuesta.
- Pausa: se observaron avances 30 %, 15 % y 45 %; otra lectura posterior mantuvo exactamente los mismos valores. Arrancar reanudó la carrera.
- Fin: las tres barras llegaron a 100 %, se mostró el orden de llegada y el botón volvió a Arrancar.
- Quiniela: una carrera ganada por Jugador 3 con apuesta por Jugador 1 dejó el marcador en 0 de 2; la siguiente, ganada por Jugador 2 y apostada por Jugador 2, produjo 1 de 3. El video 02 registra ambas consecutivamente.
- Rotación después del resultado: el marcador conservó 1 de 3.
- Rotación durante una carrera nueva: después de girar y volver, se pudo pausar con avances 36 %, 18 % y 54 %, conservando el marcador 1 de 3.
- Reiniciar esa carrera incompleta devolvió las tres barras a cero y conservó 1 de 3.
- Borrar marcador y reiniciar dejaron nuevamente 0 de 0 y las tres barras en cero.
- La llegada se registra en onFinish, y el marcador se modifica una sola vez después de esperar a todos los hijos. Revisión de código de estos puntos y de la invalidación de ejecuciones anteriores.

Las comprobaciones describen lo que ejecutó el asistente en el emulador. No sustituyen los experimentos conceptuales ni la defensa oral del alumno.

## Videos
- 01-pausa-reanudar.mp4: arranque, pausa, reanudación y fin, mostrando el botón.
- 02-quiniela-rotacion.mp4: dos carreras consecutivas, una apuesta fallida y otra acertada, seguidas de giro de pantalla. Se aceleró uniformemente a aproximadamente 1.28× para ajustarlo a 40 segundos; los resultados y el orden de los eventos son los originales.
- Ambos duran 40 segundos; se prolonga el último fotograma cuando hace falta completar la duración.

## Alcance
Los ganadores pueden repetirse por azar; no se fuerza alternancia. La igualdad de oportunidades procede de mezclar uniformemente las tres velocidades con la misma distribución para todos, no de una garantía sobre una muestra pequeña.

El estado se conserva durante la sesión y las rotaciones. No hay base de datos ni persistencia tras cerrar definitivamente la sesión. Las dependencias se descargan solo para compilar si no están disponibles localmente; la app no utiliza red.
