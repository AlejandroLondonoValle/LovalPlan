package com.lovaltech.lovalplan.feature.profile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla de una sola fila (id = 1): el perfil del usuario de este celular. */
@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1,
    val displayName: String,
    val dailyGoal: Int,
    val onboarded: Boolean
)
