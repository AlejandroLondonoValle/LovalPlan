package com.lovaltech.lovalplan.feature.schedule.presentation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.EventCard

/** Lista de actividades del dia elegido (equivale a ProductListSection). */
@Composable
fun DayAgendaSection(
    label: String,
    agenda: List<AgendaItem>,
    onEdit: (String) -> Unit,
    onAskDelete: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(label, style = MaterialTheme.typography.titleLarge)
        if (agenda.isEmpty()) {
            Column(
                Modifier.fillMaxWidth().padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🐉", style = MaterialTheme.typography.headlineLarge)
                Text(
                    "Día libre. Pulsa + para agregar una actividad.",
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            agenda.forEach { item ->
                EventCard(
                    item = item,
                    onEdit = { onEdit(item.event.id) },
                    onAskDelete = { onAskDelete(item.event.id) }
                )
            }
        }
    }
}
