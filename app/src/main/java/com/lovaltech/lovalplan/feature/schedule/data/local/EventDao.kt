package com.lovaltech.lovalplan.feature.schedule.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    // Flow: Room emite de nuevo cada vez que cambia la tabla.
    @Query("SELECT * FROM events WHERE isDeleted = 0 ORDER BY startMinute ASC")
    fun observeVisible(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isDeleted = 0 ORDER BY startMinute ASC")
    suspend fun getVisible(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun find(id: String): EventEntity?

    @Query("SELECT * FROM events WHERE pending = 1")
    suspend fun getPending(): List<EventEntity>

    @Upsert
    suspend fun upsert(event: EventEntity)

    // Solo marca sincronizada la version que se subio (si cambio despues, sigue pendiente).
    @Query("UPDATE events SET pending = 0 WHERE id = :id AND updatedAt = :version")
    suspend fun markSynced(id: String, version: Long)

    @Query("DELETE FROM events")
    suspend fun clear()
}
