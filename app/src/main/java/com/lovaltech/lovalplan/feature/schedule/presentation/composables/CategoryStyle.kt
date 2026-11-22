package com.lovaltech.lovalplan.feature.schedule.presentation.composables

import androidx.compose.ui.graphics.Color
import com.lovaltech.lovalplan.feature.schedule.domain.model.EventCategory

/** Como se VE cada categoria. Vive en presentacion porque son colores y textos de UI. */
data class CategoryStyle(val label: String, val emoji: String, val color: Color)

fun EventCategory.style(): CategoryStyle = when (this) {
    EventCategory.STUDY -> CategoryStyle("Estudio", "📚", Color(0xFF4D8EFF))
    EventCategory.WORK -> CategoryStyle("Trabajo", "💼", Color(0xFF9B7BFF))
    EventCategory.EXERCISE -> CategoryStyle("Ejercicio", "🏃", Color(0xFF2EC4D6))
    EventCategory.PERSONAL -> CategoryStyle("Personal", "🧘", Color(0xFFFF7EB6))
    EventCategory.HOME -> CategoryStyle("Hogar", "🏠", Color(0xFFFFB020))
    EventCategory.OTHER -> CategoryStyle("Otro", "✨", Color(0xFF8AA4CF))
}
