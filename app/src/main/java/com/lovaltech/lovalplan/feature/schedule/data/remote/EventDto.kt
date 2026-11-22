package com.lovaltech.lovalplan.feature.schedule.data.remote

/** Documento de Firestore en users/{uid}/events/{id}. Sin anotaciones de Room. */
data class EventDto(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val recurrence: String,
    val daysOfWeek: List<Int>,
    val dateEpochDay: Long?,
    val startMinute: Int,
    val endMinute: Int,
    val reminderMinutes: Int,
    val updatedAt: Long,
    val isDeleted: Boolean
)
