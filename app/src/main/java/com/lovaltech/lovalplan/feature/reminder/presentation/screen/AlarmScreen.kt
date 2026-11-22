package com.lovaltech.lovalplan.feature.reminder.presentation.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.components.DuoButton
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueContainer
import com.lovaltech.lovalplan.core.designsystem.theme.Night
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary

/** Pantalla de alarma: dragon "latiendo", titulo, horario y dos botones grandes. */
@Composable
fun AlarmScreen(
    title: String,
    timeRange: String,
    snoozeMinutes: Int,
    onStart: () -> Unit,
    onSnooze: () -> Unit
) {
    val pulse = rememberInfiniteTransition(label = "pulse")
    val scale by pulse.animateFloat(
        initialValue = 0.92f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "scale"
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LovalBlueContainer, Night)))
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .size(180.dp)
                    .scale(scale)
                    .background(LovalBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.ic_dragon), contentDescription = null, Modifier.size(100.dp))
            }
            Text("¡Es hora!", style = MaterialTheme.typography.headlineLarge)
            Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Text(timeRange, style = MaterialTheme.typography.titleMedium, color = TextSecondary)
        }
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DuoButton("¡Vamos!", onClick = onStart, modifier = Modifier.fillMaxWidth())
            DuoButton("Posponer $snoozeMinutes min", onClick = onSnooze, modifier = Modifier.fillMaxWidth(), outlined = true)
            Spacer(Modifier.height(8.dp))
        }
    }
}
