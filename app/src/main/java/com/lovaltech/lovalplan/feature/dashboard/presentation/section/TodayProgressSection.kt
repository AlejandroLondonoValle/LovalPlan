package com.lovaltech.lovalplan.feature.dashboard.presentation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.components.ProgressRing
import com.lovaltech.lovalplan.core.designsystem.components.XpBar
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.StreakOrange
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats

/** Anillo con el progreso de hoy + meta diaria + barra de nivel. */
@Composable
fun TodayProgressSection(
    completed: Int,
    total: Int,
    progress: Float,
    stats: ProgressStats
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(NightSurface, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ProgressRing(progress = progress, diameter = 104.dp) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$completed/$total", style = MaterialTheme.typography.headlineSmall)
                Text("hoy", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val goalMet = stats.completedToday >= stats.dailyGoal
            Text(
                if (goalMet) "🔥 ¡Meta diaria cumplida!" else "Meta diaria: ${stats.completedToday}/${stats.dailyGoal}",
                style = MaterialTheme.typography.titleMedium,
                color = if (goalMet) StreakOrange else MaterialTheme.colorScheme.onSurface
            )
            Text(
                if (goalMet) "Tu racha sigue viva." else "Cumple ${stats.dailyGoal - stats.completedToday} más para mantener tu racha.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text("Nivel ${stats.level}", style = MaterialTheme.typography.labelLarge, color = LovalBlueLight)
            XpBar(progress = stats.xpIntoLevel.toFloat() / stats.xpPerLevel, modifier = Modifier.fillMaxWidth())
        }
    }
}
