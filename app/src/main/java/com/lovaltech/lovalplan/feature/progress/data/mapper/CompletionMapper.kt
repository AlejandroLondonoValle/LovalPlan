package com.lovaltech.lovalplan.feature.progress.data.mapper

import com.lovaltech.lovalplan.feature.progress.data.local.CompletionEntity
import com.lovaltech.lovalplan.feature.progress.data.remote.CompletionDto
import com.lovaltech.lovalplan.feature.progress.domain.model.Completion

// Entity <-> Dominio
fun CompletionEntity.toDomain() = Completion(id, eventId, dayEpoch, completedAt, xp, startMinute)

fun Completion.toEntity() = CompletionEntity(
    id = id, eventId = eventId, dayEpoch = dayEpoch,
    completedAt = completedAt, xp = xp, startMinute = startMinute, pending = true
)

// Entity <-> Documento remoto
fun CompletionEntity.toDto() = CompletionDto(id, eventId, dayEpoch, completedAt, xp, startMinute)

fun CompletionDto.toEntity() = CompletionEntity(
    id = id, eventId = eventId, dayEpoch = dayEpoch,
    completedAt = completedAt, xp = xp, startMinute = startMinute, pending = false
)
