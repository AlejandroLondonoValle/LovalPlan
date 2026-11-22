package com.lovaltech.lovalplan.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Destinos de Navigation 3 (igual que ProductListRoute / ProductFormRoute en la fase 1).

@Serializable data object LoginRoute : NavKey

// Pestanas principales (barra inferior)
@Serializable data object DashboardRoute : NavKey
@Serializable data object ScheduleRoute : NavKey
@Serializable data object AchievementsRoute : NavKey
@Serializable data object ProfileRoute : NavKey

/**
 * Formulario para crear (eventId = null) o editar una actividad.
 * dayEpoch: dia preseleccionado al crear desde el horario.
 */
@Serializable data class EventFormRoute(
    val eventId: String? = null,
    val dayEpoch: Long? = null
) : NavKey
