package com.lovaltech.lovalplan.feature.schedule.data.mapper

import com.lovaltech.lovalplan.feature.schedule.data.local.EventEntity
import com.lovaltech.lovalplan.feature.schedule.data.remote.EventDto

// Entity <-> documento remoto. "pending" nunca viaja a Firestore.

fun EventEntity.toDto() = EventDto(
    id = id,
    title = title,
    description = description,
    category = category,
    recurrence = recurrence,
    daysOfWeek = daysOfWeek.split(",").mapNotNull { it.trim().toIntOrNull() },
    dateEpochDay = dateEpochDay,
    startMinute = startMinute,
    endMinute = endMinute,
    reminderMinutes = reminderMinutes,
    updatedAt = updatedAt,
    isDeleted = isDeleted
)

fun EventDto.toEntity() = EventEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    recurrence = recurrence,
    daysOfWeek = daysOfWeek.sorted().joinToString(","),
    dateEpochDay = dateEpochDay,
    startMinute = startMinute,
    endMinute = endMinute,
    reminderMinutes = reminderMinutes,
    updatedAt = updatedAt,
    pending = false,
    isDeleted = isDeleted
)
