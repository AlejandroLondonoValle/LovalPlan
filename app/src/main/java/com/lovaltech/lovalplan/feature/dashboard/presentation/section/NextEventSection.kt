package com.lovaltech.lovalplan.feature.dashboard.presentation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.components.DuoButton
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueDark
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaItem
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus

/** Tarjeta azul destacada: lo que viene (o lo que esta en curso). */
@Composable
fun NextEventSection(
    next: AgendaItem?,
    minutesToNext: Long?,
    hasEventsToday: Boolean,
    onAddEvent: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(LovalBlue, LovalBlueDark)), RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (next == null) {
            Text(
                if (hasEventsToday) "No quedan actividades por empezar hoy 🎉" else "Hoy no tienes actividades",
                style = MaterialTheme.typography.titleLarge, color = Color.White
            )
            Text("Planea algo nuevo y sigue sumando XP.", color = Color.White.copy(alpha = 0.85f))
            DuoButton(
                "Agregar actividad", onClick = onAddEvent,
                color = Color.White, shadowColor = Color(0xFFB8C4DA), contentColor = LovalBlue,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            val label = if (next.status == AgendaStatus.IN_PROGRESS) "EN CURSO" else
                "SIGUIENTE · en ${minutesToNext?.let(::untilText) ?: "breve"}"
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(painterResource(R.drawable.ic_alarm), contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text(label, style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
            Text(next.event.title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text(
                TimeText.range(next.event.startMinute, next.event.endMinute),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

private fun untilText(minutes: Long): String = when {
    minutes < 1 -> "menos de 1 min"
    minutes < 60 -> "$minutes min"
    else -> "${minutes / 60} h ${minutes % 60} min"
}
