package com.lovaltech.lovalplan.feature.profile.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary

/** Fila de un permiso: titulo, explicacion y "✓ Activado" o boton "Activar". */
@Composable
fun PermissionRow(title: String, description: String, granted: Boolean, onEnable: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        if (granted) {
            Text("✓ Activado", color = LovalBlueLight, style = MaterialTheme.typography.labelLarge)
        } else {
            TextButton(onClick = onEnable) { Text("Activar") }
        }
    }
}
