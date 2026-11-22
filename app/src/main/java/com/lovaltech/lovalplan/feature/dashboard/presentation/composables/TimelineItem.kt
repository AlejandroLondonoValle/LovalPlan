package com.lovaltech.lovalplan.feature.dashboard.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.core.designsystem.theme.XpGold
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.progress.domain.model.XpRules
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.StatusBadge
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.style

/** Fila de la linea de tiempo de hoy: hora, actividad, XP y boton de cumplir. */
@Composable
fun TimelineItem(item: AgendaItem, onComplete: () -> Unit, onOpen: () -> Unit) {
    val event = item.event
    val style = event.category.style()
    val done = item.status == AgendaStatus.COMPLETED
    Row(
        Modifier
            .fillMaxWidth()
            .background(NightSurface, RoundedCornerShape(18.dp))
            .clickable(onClick = onOpen)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(Modifier.width(52.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(TimeText.hhmm(event.startMinute), style = MaterialTheme.typography.titleSmall)
            Text(TimeText.hhmm(event.endMinute), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Column(Modifier.weight(1f)) {
            Text(
                "${style.emoji} ${event.title}",
                style = MaterialTheme.typography.titleMedium,
                textDecoration = if (done) TextDecoration.LineThrough else null,
                color = if (done) TextSecondary else MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                StatusBadge(item.status)
                Text(
                    "+${XpRules.xpFor(event.durationMinutes)} XP",
                    style = MaterialTheme.typography.labelMedium,
                    color = XpGold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        CompleteButton(item.status, onComplete)
    }
}
