package mx.tec.racetracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.tec.racetracker.RaceParticipant
import mx.tec.racetracker.RacePhase
import mx.tec.racetracker.RaceViewModel

private val RunnerColors = listOf(Color(0xFF315DDA), Color(0xFF008575), Color(0xFFAE4E16))

@Composable
fun RaceTrackerApp(modifier: Modifier = Modifier, model: RaceViewModel = viewModel()) {
    if (model.isRunning) {
        val token = model.executionId
        LaunchedEffect(model, token) { model.runRace(token) }
    }
    RaceTrackerScreen(
        players = model.participants,
        phase = model.phase,
        selectedId = model.selectedId,
        roundBetId = model.roundBetId,
        arrivalOrder = model.arrivalOrder,
        hits = model.hits,
        total = model.completedRaces,
        onSelect = model::selectParticipant,
        onToggle = model::toggleRunning,
        onReset = model::resetRace,
        onResetScore = model::resetScore,
        modifier = modifier
    )
}

@Composable
fun RaceTrackerScreen(
    players: List<RaceParticipant>,
    phase: RacePhase,
    selectedId: Int?,
    roundBetId: Int?,
    arrivalOrder: List<Int>,
    hits: Int,
    total: Int,
    onSelect: (Int) -> Unit,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onResetScore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val running = phase == RacePhase.RUNNING
    val canBet = phase == RacePhase.READY || phase == RacePhase.FINISHED
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PRÁCTICA 3  /  LA QUINIELA", style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text("Race Tracker", style = MaterialTheme.typography.headlineMedium)
            Text(when (phase) {
                RacePhase.READY -> "Lista"
                RacePhase.RUNNING -> "En curso"
                RacePhase.PAUSED -> "En pausa"
                RacePhase.FINISHED -> "Finalizada"
            }, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Row(Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("TUS ACIERTOS", style = MaterialTheme.typography.labelMedium)
                    Text("$hits de $total", style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.semantics { contentDescription = "Marcador: $hits de $total" })
                }
                TextButton(onClick = onResetScore, enabled = !running && phase != RacePhase.PAUSED) {
                    Text("Borrar marcador")
                }
            }
        }
        Column {
            Text("Elige a tu favorito", style = MaterialTheme.typography.titleMedium)
            Text(if (canBet) "Las velocidades se sortean al arrancar." else "Apuesta cerrada hasta terminar o reiniciar.",
                style = MaterialTheme.typography.bodySmall)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                players.forEach { player ->
                    FilterChip(
                        selected = selectedId == player.id,
                        enabled = canBet,
                        onClick = { onSelect(player.id) },
                        label = { Text(player.name, style = MaterialTheme.typography.labelMedium) }
                    )
                }
            }
        }
        players.forEach { player ->
            StatusIndicator(player, arrivalOrder.indexOf(player.id).takeIf { it >= 0 }?.plus(1))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onToggle, enabled = selectedId != null, modifier = Modifier.weight(1f)) {
                Text(if (running) "Pausa" else "Arrancar")
            }
            OutlinedButton(onClick = onReset, modifier = Modifier.weight(1f)) { Text("Reiniciar") }
        }
        when (phase) {
            RacePhase.READY -> Text("Apuesta para habilitar Arrancar. Los tres tienen la misma oportunidad.",
                style = MaterialTheme.typography.bodySmall)
            RacePhase.PAUSED -> Text("Avance guardado. Arrancar continúa esta misma carrera.",
                style = MaterialTheme.typography.bodySmall)
            RacePhase.RUNNING -> Text("La carrera termina cuando los tres llegan a la meta.",
                style = MaterialTheme.typography.bodySmall)
            RacePhase.FINISHED -> {
                val winner = players.firstOrNull { it.id == arrivalOrder.firstOrNull() }
                val correct = winner != null && winner.id == roundBetId
                Card(colors = CardDefaults.cardColors(containerColor = if (correct) Color(0xFFDDF3E9) else Color(0xFFE9EDF5))) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Ganó ${winner?.name.orEmpty()}", style = MaterialTheme.typography.titleLarge)
                        Text(if (correct) "¡Acertaste! +1 punto" else "Esta vez no acertaste. ¡Inténtalo otra vez!")
                        Text("Podio: " + arrivalOrder.joinToString(" · ") { "J${it + 1}" },
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusIndicator(player: RaceParticipant, place: Int?) {
    val color = RunnerColors[player.id]
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.size(28.dp).background(color, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Text("${player.id + 1}", color = Color.White, style = MaterialTheme.typography.labelLarge)
                    }
                    Text(player.name, style = MaterialTheme.typography.titleMedium)
                }
                Text(if (place != null) "$place.º · 100 %" else "${player.currentProgress} %",
                    style = MaterialTheme.typography.labelLarge)
            }
            LinearProgressIndicator(
                progress = { player.currentProgress / player.maxProgress.toFloat() },
                modifier = Modifier.fillMaxWidth().height(8.dp)
                    .semantics { contentDescription = "${player.name}: ${player.currentProgress} por ciento" },
                color = color,
                trackColor = color.copy(alpha = 0.12f)
            )
        }
    }
}
