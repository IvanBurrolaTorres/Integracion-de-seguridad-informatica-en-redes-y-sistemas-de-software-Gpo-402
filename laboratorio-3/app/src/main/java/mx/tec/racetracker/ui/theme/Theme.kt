package mx.tec.racetracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colors = lightColorScheme(
    primary = Color(0xFF315DDA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4EAFF),
    background = Color(0xFFF5F6FA),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF18233E),
    surfaceVariant = Color(0xFFE9EDF5)
)

@Composable
fun RaceTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, content = content)
}
