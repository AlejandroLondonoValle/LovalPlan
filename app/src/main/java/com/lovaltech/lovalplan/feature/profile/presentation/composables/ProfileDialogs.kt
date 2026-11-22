package com.lovaltech.lovalplan.feature.profile.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lovaltech.lovalplan.core.designsystem.components.LovalTextField

/** Dialogo para cambiar el nombre visible. */
@Composable
fun EditNameDialog(current: String, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tu nombre") },
        text = { LovalTextField("Nombre", name, { name = it.take(40) }) },
        confirmButton = { TextButton(onClick = { onConfirm(name) }, enabled = name.isNotBlank()) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

/** Confirmacion de cierre de sesion; al invitado le advierte que puede perder datos. */
@Composable
fun SignOutDialog(isGuest: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = {
            Text(
                if (isGuest) "Eres invitado: si cierras sesión se borrarán los datos de este celular. " +
                    "Vincula tu cuenta de Google o sincroniza antes si quieres conservarlos."
                else "Se borrarán los datos locales y las alarmas de este celular. " +
                    "Lo sincronizado seguirá en tu cuenta de Google."
            )
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Cerrar sesión") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
