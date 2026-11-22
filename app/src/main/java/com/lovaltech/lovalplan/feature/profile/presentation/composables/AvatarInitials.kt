package com.lovaltech.lovalplan.feature.profile.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlueLight

/** Avatar circular con la inicial del nombre. */
@Composable
fun AvatarInitials(name: String, size: Dp = 96.dp) {
    Box(
        Modifier
            .size(size)
            .background(LovalBlue, CircleShape)
            .border(4.dp, LovalBlueLight, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            name.trim().firstOrNull()?.uppercase() ?: "🐉",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
    }
}
