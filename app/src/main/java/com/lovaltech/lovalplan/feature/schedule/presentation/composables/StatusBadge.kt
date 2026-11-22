package com.lovaltech.lovalplan.feature.schedule.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.ErrorRed
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.NightOutline
import com.lovaltech.lovalplan.core.designsystem.theme.StreakOrange
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus

/** Etiqueta pequena con el estado de la actividad en ese dia. */
@Composable
fun StatusBadge(status: AgendaStatus, modifier: Modifier = Modifier) {
    val (text, color) = when (status) {
        AgendaStatus.COMPLETED -> "✓ Cumplida" to LovalBlue
        AgendaStatus.IN_PROGRESS -> "● En curso" to StreakOrange
        AgendaStatus.MISSED -> "Pendiente" to ErrorRed
        AgendaStatus.UPCOMING -> "Próxima" to NightOutline
    }
    Text(
        text,
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .padding(top = 4.dp)
            .background(color, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
