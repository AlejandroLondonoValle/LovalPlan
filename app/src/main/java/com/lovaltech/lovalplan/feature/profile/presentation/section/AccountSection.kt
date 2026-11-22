package com.lovaltech.lovalplan.feature.profile.presentation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.components.DuoButton
import com.lovaltech.lovalplan.core.designsystem.theme.ErrorRed
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue

/** Sincronizar, vincular Google (si es invitado) y cerrar sesion. */
@Composable
fun AccountSection(
    isGuest: Boolean,
    isSyncing: Boolean,
    onSync: () -> Unit,
    onLinkGoogle: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DuoButton(
            text = if (isSyncing) "Sincronizando..." else "Sincronizar ahora",
            onClick = onSync,
            enabled = !isSyncing,
            modifier = Modifier.fillMaxWidth()
        )
        if (isGuest) {
            DuoButton(
                text = "Vincular cuenta de Google",
                onClick = onLinkGoogle,
                enabled = !isSyncing,
                color = Color.White,
                shadowColor = Color(0xFFB8C4DA),
                contentColor = LovalBlue,
                modifier = Modifier.fillMaxWidth()
            )
        }
        DuoButton(
            text = "Cerrar sesión",
            onClick = onSignOut,
            outlined = true,
            color = ErrorRed,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
