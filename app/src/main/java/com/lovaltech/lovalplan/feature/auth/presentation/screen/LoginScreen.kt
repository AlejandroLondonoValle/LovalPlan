package com.lovaltech.lovalplan.feature.auth.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueContainer
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.feature.auth.presentation.composables.BrandHeader
import com.lovaltech.lovalplan.feature.auth.presentation.google.GoogleIdTokenRequester
import com.lovaltech.lovalplan.feature.auth.presentation.google.GoogleTokenResult
import com.lovaltech.lovalplan.feature.auth.presentation.section.LoginActionsSection
import com.lovaltech.lovalplan.feature.auth.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

/** Pantalla de bienvenida: Google o invitado. */
@Composable
fun LoginScreen(
    vm: LoginViewModel,
    onSignedIn: () -> Unit
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(ui.done) { if (ui.done) onSignedIn() }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LovalBlueContainer, Night, Night)))
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        BrandHeader(Modifier.align(Alignment.Center).padding(bottom = 120.dp))
        LoginActionsSection(
            ui = ui,
            onGoogle = {
                vm.onGoogleStarted()
                scope.launch {
                    when (val result = GoogleIdTokenRequester.request(context)) {
                        is GoogleTokenResult.Success -> vm.onGoogleToken(result.idToken)
                        is GoogleTokenResult.Failure -> vm.onGoogleError(result.message)
                    }
                }
            },
            onGuest = { vm.continueAsGuest() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
