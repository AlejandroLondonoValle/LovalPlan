package com.lovaltech.lovalplan.feature.dashboard.presentation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.feature.dashboard.presentation.composables.TimelineItem
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem

/** Linea de tiempo de hoy con el boton de cumplir en cada actividad. */
@Composable
fun TodayTimelineSection(
    items: List<AgendaItem>,
    onComplete: (String) -> Unit,
    onOpen: (String) -> Unit
) {
    if (items.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Tu día", style = MaterialTheme.typography.titleLarge)
        items.forEach { item ->
            TimelineItem(
                item = item,
                onComplete = { onComplete(item.event.id) },
                onOpen = { onOpen(item.event.id) }
            )
        }
    }
}
