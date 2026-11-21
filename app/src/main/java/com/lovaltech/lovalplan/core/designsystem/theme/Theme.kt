package com.lovaltech.lovalplan.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LovalColors = darkColorScheme(
    primary = LovalBlue,
    onPrimary = Color.White,
    primaryContainer = LovalBlueContainer,
    onPrimaryContainer = TextPrimary,
    secondary = XpGold,
    onSecondary = Night,
    tertiary = StreakOrange,
    onTertiary = Night,
    background = Night,
    onBackground = TextPrimary,
    surface = Night,
    onSurface = TextPrimary,
    surfaceVariant = NightSurfaceHigh,
    onSurfaceVariant = TextSecondary,
    surfaceContainerLowest = Night,
    surfaceContainerLow = NightSurface,
    surfaceContainer = NightSurface,
    surfaceContainerHigh = NightSurfaceHigh,
    surfaceContainerHighest = NightSurfaceHigh,
    outline = NightOutline,
    outlineVariant = NightOutline,
    error = ErrorRed,
    onError = Color.White
)

private val base = Typography()

// Tipografia "redonda y gruesa" al estilo de las apps de habitos.
private val LovalTypography = Typography(
    headlineLarge = base.headlineLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 30.sp),
    headlineMedium = base.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
    headlineSmall = base.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
    titleLarge = base.titleLarge.copy(fontWeight = FontWeight.Bold),
    titleMedium = base.titleMedium.copy(fontWeight = FontWeight.Bold),
    titleSmall = base.titleSmall.copy(fontWeight = FontWeight.Bold),
    bodyLarge = base.bodyLarge,
    bodyMedium = base.bodyMedium,
    bodySmall = base.bodySmall,
    labelLarge = base.labelLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 15.sp),
    labelMedium = base.labelMedium.copy(fontWeight = FontWeight.Bold),
    labelSmall = base.labelSmall.copy(fontWeight = FontWeight.Bold)
)

private val LovalShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

/** Tema unico de la app: siempre oscuro, como pidio el cliente. */
@Composable
fun LovalPlanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LovalColors,
        typography = LovalTypography,
        shapes = LovalShapes,
        content = content
    )
}
