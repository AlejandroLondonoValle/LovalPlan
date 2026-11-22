package com.lovaltech.lovalplan.feature.schedule.presentation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.DayChip
import com.lovaltech.lovalplan.feature.schedule.presentation.state.WeekDayUi
import java.time.LocalDate

/** Flechas de semana + 7 dias seleccionables. */
@Composable
fun WeekStripSection(
    week: List<WeekDayUi>,
    weekLabel: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSelect: (LocalDate) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPrevious) {
                Icon(painterResource(R.drawable.ic_chevron_left), contentDescription = "Semana anterior")
            }
            Text(
                weekLabel,
                style = MaterialTheme.typography.titleSmall,
                color = TextSecondary,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(onClick = onNext) {
                Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = "Semana siguiente")
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            week.forEach { day ->
                DayChip(
                    initial = day.initial,
                    dayNumber = day.dayNumber,
                    selected = day.isSelected,
                    isToday = day.isToday,
                    hasEvents = day.hasEvents,
                    onClick = { onSelect(day.date) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
