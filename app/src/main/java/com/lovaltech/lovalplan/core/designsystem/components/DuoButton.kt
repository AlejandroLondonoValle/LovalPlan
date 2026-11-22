package com.lovaltech.lovalplan.core.designsystem.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueDark
import com.lovaltech.lovalplan.core.designsystem.theme.NightOutline
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary

private val FaceHeight = 52.dp
private val Depth = 4.dp
private val Shape = RoundedCornerShape(16.dp)

/**
 * Boton "3D" estilo Duolingo: una cara de color sobre una sombra mas oscura.
 * Al presionarlo la cara baja [Depth] y parece hundirse.
 * Es solo visual: recibe texto y evento, no conoce ViewModels.
 */
@Composable
fun DuoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = LovalBlue,
    shadowColor: Color = LovalBlueDark,
    contentColor: Color = Color.White,
    outlined: Boolean = false
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val offset by animateDpAsState(if (pressed && enabled) Depth else 0.dp, label = "duo-press")

    val face = when {
        !enabled -> NightOutline
        outlined -> NightSurface
        else -> color
    }
    val shadow = when {
        !enabled -> Color(0xFF1A2540)
        outlined -> NightOutline
        else -> shadowColor
    }
    val textColor = when {
        !enabled -> TextSecondary
        outlined -> color
        else -> contentColor
    }

    Box(modifier.height(FaceHeight + Depth)) {
        // Sombra inferior (no se mueve)
        Box(
            Modifier
                .fillMaxWidth()
                .height(FaceHeight)
                .offset(y = Depth)
                .background(shadow, Shape)
        )
        // Cara del boton (baja al presionar)
        Box(
            Modifier
                .fillMaxWidth()
                .height(FaceHeight)
                .offset(y = offset)
                .background(face, Shape)
                .then(if (outlined) Modifier.border(2.dp, NightOutline, Shape) else Modifier)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                color = textColor,
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 1.sp
            )
        }
    }
}
