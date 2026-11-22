package com.lovaltech.lovalplan.feature.auth.presentation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.components.DuoButton
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary
import com.lovaltech.lovalplan.feature.auth.presentation.state.LoginUiState

/** Botones de acceso. No sabe nada de Firebase: solo emite eventos. */
@Composable
fun LoginActionsSection(
    ui: LoginUiState,
    onGoogle: () -> Unit,
    onGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (ui.loading) CircularProgressIndicator(color = LovalBlue)
        ui.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }
        DuoButton(
            text = "Continuar con Google",
            onClick = onGoogle,
            enabled = !ui.loading,
            color = Color.White,
            shadowColor = Color(0xFFB8C4DA),
            contentColor = LovalBlue,
            modifier = Modifier.fillMaxWidth()
        )
        DuoButton(
            text = "Continuar como invitado",
            onClick = onGuest,
            enabled = !ui.loading,
            outlined = true,
            color = TextSecondary,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Como invitado tus datos se guardan en este celular y se respaldan de forma anónima.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
