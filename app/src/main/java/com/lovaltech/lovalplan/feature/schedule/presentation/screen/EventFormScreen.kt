package com.lovaltech.lovalplan.feature.schedule.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.feature.schedule.presentation.composables.EventDeleteDialog
import com.lovaltech.lovalplan.feature.schedule.presentation.section.EventFormSection
import com.lovaltech.lovalplan.feature.schedule.presentation.viewmodel.EventFormViewModel

/** Crear / editar actividad. Vuelve atras cuando saved = true. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormScreen(
    vm: EventFormViewModel,
    eventId: String?,
    dayEpoch: Long?,
    onDone: () -> Unit
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    var askDelete by remember { mutableStateOf(false) }
    LaunchedEffect(eventId) { vm.load(eventId, dayEpoch) }
    LaunchedEffect(ui.saved) { if (ui.saved) onDone() }

    Scaffold(
        containerColor = Night,
        topBar = {
            TopAppBar(
                title = { Text(if (eventId == null) "Nueva actividad" else "Editar actividad") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(painterResource(R.drawable.ic_back), contentDescription = "Volver")
                    }
                },
                actions = {
                    if (eventId != null) IconButton(onClick = { askDelete = true }) {
                        Icon(painterResource(R.drawable.ic_delete), contentDescription = "Eliminar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Night)
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).imePadding()) {
            EventFormSection(
                ui = ui,
                onTitle = vm::updateTitle,
                onDescription = vm::updateDescription,
                onCategory = vm::updateCategory,
                onRecurrence = vm::updateRecurrence,
                onToggleDay = vm::toggleDay,
                onDate = vm::updateDate,
                onStart = vm::updateStart,
                onEnd = vm::updateEnd,
                onReminder = vm::updateReminder,
                onSave = vm::save
            )
        }
    }

    if (askDelete) EventDeleteDialog(
        onConfirm = {
            askDelete = false
            vm.delete()
        },
        onDismiss = { askDelete = false }
    )
}
