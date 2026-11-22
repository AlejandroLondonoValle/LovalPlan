package com.lovaltech.lovalplan.feature.progress.presentation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.NightOutline
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.StreakOrange
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.progress.domain.model.ProgressStats

/** Racha: numero grande + los ultimos 7 dias (llama si se cumplio la meta). */
@Composable
fun StreakWeekSection(stats: ProgressStats) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(NightSurface, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("🔥", style = MaterialTheme.typography.headlineLarge)
            Column {
                Text("${stats.currentStreak} días de racha", style = MaterialTheme.typography.titleLarge, color = StreakOrange)
                Text(
                    "Mejor racha: ${stats.bestStreak} · Meta: ${stats.dailyGoal} al día",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            stats.lastSevenDays.forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        Modifier
                            .size(36.dp)
                            .background(if (day.goalMet) StreakOrange else NightOutline, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (day.goalMet) "🔥" else "${day.completed}", style = MaterialTheme.typography.labelLarge)
                    }
                    Text(TimeText.dayInitial(day.date.dayOfWeek.value), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
        }
    }
}
