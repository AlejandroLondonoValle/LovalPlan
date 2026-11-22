package com.lovaltech.lovalplan.feature.schedule.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary

/** Dia de la tira semanal: inicial, numero y un punto si tiene actividades. */
@Composable
fun DayChip(
    initial: String,
    dayNumber: Int,
    selected: Boolean,
    isToday: Boolean,
    hasEvents: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier
            .background(if (selected) LovalBlue else NightSurface, shape)
            .then(if (isToday && !selected) Modifier.border(2.dp, LovalBlue, shape) else Modifier)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(initial, style = MaterialTheme.typography.labelMedium, color = if (selected) Color.White else TextSecondary)
        Text("$dayNumber", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Box(
            Modifier
                .size(6.dp)
                .background(
                    when {
                        !hasEvents -> Color.Transparent
                        selected -> Color.White
                        else -> LovalBlueLight
                    },
                    CircleShape
                )
        )
    }
}
