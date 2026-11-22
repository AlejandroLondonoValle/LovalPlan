package com.lovaltech.lovalplan.feature.auth.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.designsystem.theme.LovalBlue
import com.lovaltech.lovalplan.core.designsystem.theme.TextSecondary

/** Logo del dragon sobre el azul de marca + nombre y lema. */
@Composable
fun BrandHeader(modifier: Modifier = Modifier) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            Modifier
                .size(140.dp)
                .background(LovalBlue, RoundedCornerShape(36.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(R.drawable.ic_dragon), contentDescription = "LovalTech", Modifier.size(86.dp))
        }
        Text("LovalPlan", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Organiza tu tiempo, cumple tus actividades\ny sube de nivel cada día.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
