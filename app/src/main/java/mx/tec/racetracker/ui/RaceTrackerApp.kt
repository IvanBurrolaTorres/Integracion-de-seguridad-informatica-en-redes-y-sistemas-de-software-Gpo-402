package mx.tec.racetracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.racetracker.RaceParticipant

@Composable
fun RaceTrackerApp(modifier: Modifier = Modifier) {
    val players = remember { listOf(RaceParticipant("Jugador 1"), RaceParticipant("Jugador 2", 2)) }
    Column(modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Carrera", style = MaterialTheme.typography.headlineMedium)
        players.forEach {
            Text(it.name)
            LinearProgressIndicator(progress = { it.currentProgress / 100f })
        }
    }
}
