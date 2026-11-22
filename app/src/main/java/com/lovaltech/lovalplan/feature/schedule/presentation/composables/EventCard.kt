package com.lovaltech.lovalplan.feature.schedule.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent

/** Tarjeta de una actividad en el horario: color de categoria, horas, repeticion y estado. */
@Composable
fun EventCard(
    item: AgendaItem,
    onEdit: () -> Unit,
    onAskDelete: () -> Unit
) {
    val event = item.event
    val style = event.category.style()
    Row(
        Modifier
            .fillMaxWidth()
            .background(NightSurface, RoundedCornerShape(18.dp))
            .clickable(onClick = onEdit)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            Modifier
                .width(6.dp)
                .height(56.dp)
                .background(style.color, RoundedCornerShape(3.dp))
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("${style.emoji}  ${event.title}", style = MaterialTheme.typography.titleMedium)
            Text(
                "${TimeText.range(event.startMinute, event.endMinute)} · ${TimeText.duration(event.durationMinutes)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "${repeatText(event)} · 🔔 ${reminderText(event.reminderMinutes)}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            StatusBadge(item.status)
        }
        IconButton(onClick = onAskDelete) {
            Icon(painterResource(R.drawable.ic_delete), contentDescription = "Eliminar", tint = TextSecondary, modifier = Modifier.size(22.dp))
        }
    }
}

fun repeatText(event: ScheduleEvent): String = when (event.recurrence) {
    RecurrenceType.WEEKLY ->
        if (event.daysOfWeek.size == 7) "Todos los días"
        else event.daysOfWeek.sorted().joinToString(" · ") { TimeText.dayShort(it) }
    RecurrenceType.ONCE -> event.date?.let { "Solo el ${TimeText.shortDate(it)}" } ?: "Sin fecha"
}

fun reminderText(minutes: Int): String = if (minutes == 0) "a la hora" else "$minutes min antes"
