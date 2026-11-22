package com.lovaltech.lovalplan.feature.profile.presentation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.feature.profile.presentation.composables.PermissionRow
import com.lovaltech.lovalplan.feature.profile.presentation.state.PermissionStatus

/** Los 3 permisos de las alarmas, con acceso directo a Ajustes si falta alguno. */
@Composable
fun PermissionsSection(
    status: PermissionStatus,
    onEnableNotifications: () -> Unit,
    onEnableExactAlarms: () -> Unit,
    onEnableFullScreen: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(NightSurface, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("⏰ Alarmas y avisos", style = MaterialTheme.typography.titleMedium)
        PermissionRow("Notificaciones", "Para avisarte y preguntarte si cumpliste", status.notifications, onEnableNotifications)
        PermissionRow("Alarmas exactas", "Para que suene justo a la hora", status.exactAlarms, onEnableExactAlarms)
        PermissionRow("Pantalla completa", "Para mostrar la alarma aunque el celular esté bloqueado", status.fullScreen, onEnableFullScreen)
    }
}
