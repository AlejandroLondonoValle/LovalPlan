package com.lovaltech.lovalplan.feature.progress.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Unica clase que conoce la ruta users/{uid}/completions. */
class FirestoreCompletionDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    private fun completions(uid: String) =
        db.collection("users").document(uid).collection("completions")

    suspend fun push(uid: String, dto: CompletionDto) {
        val data = mapOf(
            "id" to dto.id,
            "eventId" to dto.eventId,
            "dayEpoch" to dto.dayEpoch,
            "completedAt" to dto.completedAt,
            "xp" to dto.xp,
            "startMinute" to dto.startMinute
        )
        completions(uid).document(dto.id).set(data).await()
    }

    suspend fun fetchFromServer(uid: String): List<CompletionDto> =
        completions(uid).get(Source.SERVER).await().documents.mapNotNull { doc ->
            val id = doc.getString("id") ?: return@mapNotNull null
            CompletionDto(
                id = id,
                eventId = doc.getString("eventId").orEmpty(),
                dayEpoch = doc.getLong("dayEpoch") ?: 0L,
                completedAt = doc.getLong("completedAt") ?: 0L,
                xp = (doc.getLong("xp") ?: 0L).toInt(),
                startMinute = (doc.getLong("startMinute") ?: 0L).toInt()
            )
        }
}
