package com.lovaltech.lovalplan.feature.schedule.data.mapper

import com.lovaltech.lovalplan.feature.schedule.data.local.EventEntity
import com.lovaltech.lovalplan.feature.schedule.domain.model.EventCategory
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import java.time.LocalDate

// Entity <-> Dominio. El dominio nunca ve "1,3,5" ni epochDay: ve Set<Int> y LocalDate.

fun EventEntity.toDomain() = ScheduleEvent(
    id = id,
    title = title,
    description = description,
    category = runCatching { EventCategory.valueOf(category) }.getOrDefault(EventCategory.OTHER),
    recurrence = runCatching { RecurrenceType.valueOf(recurrence) }.getOrDefault(RecurrenceType.WEEKLY),
    daysOfWeek = daysOfWeek.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet(),
    date = dateEpochDay?.let(LocalDate::ofEpochDay),
    startMinute = startMinute,
    endMinute = endMinute,
    reminderMinutes = reminderMinutes,
    updatedAt = updatedAt
)

fun ScheduleEvent.toEntity() = EventEntity(
    id = id,
    title = title,
    description = description,
    category = category.name,
    recurrence = recurrence.name,
    daysOfWeek = daysOfWeek.sorted().joinToString(","),
    dateEpochDay = date?.toEpochDay(),
    startMinute = startMinute,
    endMinute = endMinute,
    reminderMinutes = reminderMinutes,
    updatedAt = updatedAt,
    pending = true,
    isDeleted = false
)
