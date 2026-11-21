package com.lovaltech.lovalplan.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.NightOutline
import com.lovaltech.lovalplan.core.designsystem.theme.XpGold

/** Barra de progreso gruesa y redondeada (experiencia, metas). */
@Composable
fun XpBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = XpGold
) {
    val animated by animateFloatAsState(progress.coerceIn(0f, 1f), label = "xp-bar")
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier
            .height(14.dp)
            .clip(shape)
            .background(NightOutline)
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(animated)
                .clip(shape)
                .background(color)
        )
    }
}
