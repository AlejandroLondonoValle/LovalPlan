package com.lovaltech.lovalplan.feature.schedule.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.NightOutline
import com.lovaltech.lovalplan.core.designsystem.theme.NightSurface
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary

/** Pastilla seleccionable reutilizable: categorias, dias, recordatorio, tipo de repeticion. */
@Composable
fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = LovalBlue
) {
    val shape = RoundedCornerShape(14.dp)
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
        color = if (selected) Color.White else TextSecondary,
        modifier = modifier
            .background(if (selected) selectedColor else NightSurface, shape)
            .border(2.dp, if (selected) selectedColor else NightOutline, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    )
}
