package com.lovaltech.lovalplan.feature.dashboard.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.NightOutline
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus

/**
 * Circulo grande para marcar "¡Lo cumplí!".
 * - Proxima: candado (aun no se puede).
 * - En curso / Pendiente: circulo vacio que se puede tocar.
 * - Cumplida: circulo azul con check.
 */
@Composable
fun CompleteButton(status: AgendaStatus, onClick: () -> Unit) {
    val size = 48.dp
    when (status) {
        AgendaStatus.COMPLETED -> Box(
            Modifier.size(size).background(LovalBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(painterResource(R.drawable.ic_check), contentDescription = "Cumplida", tint = Color.White) }

        AgendaStatus.UPCOMING -> Box(
            Modifier.size(size).border(3.dp, NightOutline, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(painterResource(R.drawable.ic_lock), contentDescription = "Aún no empieza", tint = TextSecondary, modifier = Modifier.size(20.dp)) }

        AgendaStatus.IN_PROGRESS, AgendaStatus.MISSED -> Box(
            Modifier
                .size(size)
                .border(3.dp, LovalBlueLight, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) { Icon(painterResource(R.drawable.ic_check), contentDescription = "Marcar como cumplida", tint = LovalBlueLight) }
    }
}
