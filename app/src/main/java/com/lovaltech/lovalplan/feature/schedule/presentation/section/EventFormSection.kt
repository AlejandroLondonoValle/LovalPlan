package com.lovaltech.lovalplan.feature.schedule.presentation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.components.DuoButton
import com.lovaltech.lovalplan.core.designsystem.components.LovalTextField
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.core.designsystem.theme.XpGold
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.progress.domain.model.XpRules
import com.lovaltech.lovalplan.feature.schedule.domain.model.EventCategory
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.DatePickerField
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.SelectableChip
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.TimePickerField
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.style
import com.lovaltech.lovalplan.feature.schedule.presentation.state.EventFormUiState
import java.time.LocalDate

private val ReminderOptions = listOf(0, 5, 10, 15, 30, 60)

/** Formulario completo. Solo recibe estado y callbacks (como ProductFormSection). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventFormSection(
    ui: EventFormUiState,
    onTitle: (String) -> Unit,
    onDescription: (String) -> Unit,
    onCategory: (EventCategory) -> Unit,
    onRecurrence: (RecurrenceType) -> Unit,
    onToggleDay: (Int) -> Unit,
    onDate: (LocalDate) -> Unit,
    onStart: (Int) -> Unit,
    onEnd: (Int) -> Unit,
    onReminder: (Int) -> Unit,
    onSave: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LovalTextField("¿Qué vas a hacer?", ui.title, onTitle)
        LovalTextField("Notas (opcional)", ui.description, onDescription, singleLine = false, minLines = 2)

        Label("Categoría")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            EventCategory.entries.forEach { category ->
                val style = category.style()
                SelectableChip(
                    text = "${style.emoji} ${style.label}",
                    selected = ui.category == category,
                    onClick = { onCategory(category) },
                    selectedColor = style.color
                )
            }
        }

        Label("¿Cuándo se repite?")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SelectableChip("🔁 Cada semana", ui.recurrence == RecurrenceType.WEEKLY,
                { onRecurrence(RecurrenceType.WEEKLY) }, Modifier.weight(1f))
            SelectableChip("📅 Una sola vez", ui.recurrence == RecurrenceType.ONCE,
                { onRecurrence(RecurrenceType.ONCE) }, Modifier.weight(1f))
        }
        if (ui.recurrence == RecurrenceType.WEEKLY) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..7).forEach { day ->
                    SelectableChip(
                        text = TimeText.dayInitial(day),
                        selected = day in ui.daysOfWeek,
                        onClick = { onToggleDay(day) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            DatePickerField("Fecha", ui.date, onDate, Modifier.fillMaxWidth())
        }

        Label("Horario")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimePickerField("Inicio", ui.startMinute, onStart, Modifier.weight(1f))
            TimePickerField("Fin", ui.endMinute, onEnd, Modifier.weight(1f))
        }

        Label("Alarma")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ReminderOptions.forEach { minutes ->
                SelectableChip(
                    text = if (minutes == 0) "A la hora" else if (minutes == 60) "1 h antes" else "$minutes min",
                    selected = ui.reminderMinutes == minutes,
                    onClick = { onReminder(minutes) }
                )
            }
        }

        val duration = ui.endMinute - ui.startMinute
        if (duration > 0) {
            Text(
                "⚡ Ganarás +${XpRules.xpFor(duration)} XP al cumplirla (${TimeText.duration(duration)})",
                color = XpGold,
                style = MaterialTheme.typography.titleSmall
            )
        }
        ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        DuoButton(
            text = if (ui.saving) "Guardando..." else "Guardar",
            onClick = onSave,
            enabled = !ui.saving,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Label(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall, color = TextSecondary)
}
