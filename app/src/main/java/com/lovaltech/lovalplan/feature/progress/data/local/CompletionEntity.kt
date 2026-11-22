package com.lovaltech.lovalplan.feature.progress.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "completions". Solo necesita "pending": los cumplimientos no se editan
 * ni se borran (historial), por eso no lleva isDeleted.
 */
@Entity(tableName = "completions")
data class CompletionEntity(
    @PrimaryKey val id: String,
    val eventId: String,
    val dayEpoch: Long,
    val completedAt: Long,
    val xp: Int,
    val startMinute: Int,
    val pending: Boolean = true
)
