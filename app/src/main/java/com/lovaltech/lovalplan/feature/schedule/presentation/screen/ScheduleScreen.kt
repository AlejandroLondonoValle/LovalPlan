package com.lovaltech.lovalplan.feature.schedule.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.EventDeleteDialog
import com.lovaltech.lovalplan.feature.schedule.presentation.section.DayAgendaSection
import com.lovaltech.lovalplan.feature.schedule.presentation.section.WeekStripSection
import com.lovaltech.lovalplan.feature.schedule.presentation.viewmodel.ScheduleViewModel

/** Pantalla "Horario": conecta el ViewModel con las secciones y la navegacion. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    vm: ScheduleViewModel,
    onAddEvent: (dayEpoch: Long) -> Unit,
    onEditEvent: (String) -> Unit
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Night,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi horario") },
                actions = { TextButton(onClick = vm::goToToday) { Text("Hoy") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Night)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddEvent(ui.selectedDay.toEpochDay()) },
                containerColor = LovalBlue,
                contentColor = Color.White
            ) { Icon(painterResource(R.drawable.ic_add), contentDescription = "Agregar actividad") }
        }
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WeekStripSection(
                    week = ui.week,
                    weekLabel = ui.weekLabel,
                    onPrevious = vm::previousWeek,
                    onNext = vm::nextWeek,
                    onSelect = vm::selectDay
                )
            }
            item {
                DayAgendaSection(
                    label = ui.selectedLabel,
                    agenda = ui.agenda,
                    onEdit = onEditEvent,
                    onAskDelete = { pendingDeleteId = it }
                )
            }
        }
    }

    if (pendingDeleteId != null) EventDeleteDialog(
        onConfirm = {
            pendingDeleteId?.let(vm::delete)
            pendingDeleteId = null
        },
        onDismiss = { pendingDeleteId = null }
    )
}
