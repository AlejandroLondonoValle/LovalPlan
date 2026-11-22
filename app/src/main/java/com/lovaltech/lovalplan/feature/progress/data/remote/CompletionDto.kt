package com.lovaltech.lovalplan.feature.progress.data.remote

/** Documento users/{uid}/completions/{id}. */
data class CompletionDto(
    val id: String,
    val eventId: String,
    val dayEpoch: Long,
    val completedAt: Long,
    val xp: Int,
    val startMinute: Int
)
