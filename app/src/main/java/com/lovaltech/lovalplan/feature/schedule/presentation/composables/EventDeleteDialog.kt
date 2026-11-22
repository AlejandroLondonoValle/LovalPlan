package com.lovaltech.lovalplan.feature.schedule.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** Igual que ProductDeleteDialog: solo pide confirmacion; la Screen decide que hacer. */
@Composable
fun EventDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar actividad") },
        text = { Text("Se quitará de tu horario y se cancelarán sus alarmas. Tu XP ya ganada se conserva.") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Eliminar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
