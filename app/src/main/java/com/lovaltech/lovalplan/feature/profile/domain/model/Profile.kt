package com.lovaltech.lovalplan.feature.profile.domain.model

/**
 * Perfil local del usuario.
 * @param dailyGoal cuantas actividades hay que cumplir en un dia para que cuente en la racha.
 * @param onboarded true cuando ya paso por la pantalla de inicio de sesion.
 */
data class Profile(
    val displayName: String = "",
    val dailyGoal: Int = 1,
    val onboarded: Boolean = false
)
