package com.lovaltech.lovalplan.feature.schedule.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Unica clase que conoce la ruta users/{uid}/events en Firestore. */
class FirestoreEventDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    private fun events(uid: String) = db.collection("users").document(uid).collection("events")

    suspend fun push(uid: String, dto: EventDto) {
        val data = mapOf(
            "id" to dto.id,
            "title" to dto.title,
            "description" to dto.description,
            "category" to dto.category,
            "recurrence" to dto.recurrence,
            "daysOfWeek" to dto.daysOfWeek,
            "dateEpochDay" to dto.dateEpochDay,
            "startMinute" to dto.startMinute,
            "endMinute" to dto.endMinute,
            "reminderMinutes" to dto.reminderMinutes,
            "updatedAt" to dto.updatedAt,
            "isDeleted" to dto.isDeleted
        )
        events(uid).document(dto.id).set(data).await()
    }

    suspend fun fetchFromServer(uid: String): List<EventDto> =
        events(uid).get(Source.SERVER).await().documents.mapNotNull { doc ->
            val id = doc.getString("id") ?: return@mapNotNull null
            EventDto(
                id = id,
                title = doc.getString("title").orEmpty(),
                description = doc.getString("description").orEmpty(),
                category = doc.getString("category") ?: "OTHER",
                recurrence = doc.getString("recurrence") ?: "WEEKLY",
                daysOfWeek = (doc.get("daysOfWeek") as? List<*>)
                    ?.mapNotNull { (it as? Number)?.toInt() } ?: emptyList(),
                dateEpochDay = doc.getLong("dateEpochDay"),
                startMinute = (doc.getLong("startMinute") ?: 0L).toInt(),
                endMinute = (doc.getLong("endMinute") ?: 0L).toInt(),
                reminderMinutes = (doc.getLong("reminderMinutes") ?: 0L).toInt(),
                updatedAt = doc.getLong("updatedAt") ?: 0L,
                isDeleted = doc.getBoolean("isDeleted") ?: false
            )
        }
}
