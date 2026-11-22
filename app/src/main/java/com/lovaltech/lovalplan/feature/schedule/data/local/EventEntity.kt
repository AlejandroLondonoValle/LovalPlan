package com.lovaltech.lovalplan.feature.schedule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Fila de la tabla "events" en SQLite. Igual que ProductEntity:
 * pending = cambio sin subir; isDeleted = tombstone.
 * Los tipos complejos se guardan "aplanados": dias "1,3,5" y fecha como epochDay.
 */
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val recurrence: String,
    val daysOfWeek: String,
    val dateEpochDay: Long?,
    val startMinute: Int,
    val endMinute: Int,
    val reminderMinutes: Int,
    val updatedAt: Long,
    val pending: Boolean = true,
    val isDeleted: Boolean = false
)
