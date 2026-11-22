package com.lovaltech.lovalplan.feature.progress.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletionDao {
    @Query("SELECT * FROM completions ORDER BY completedAt DESC")
    fun observeAll(): Flow<List<CompletionEntity>>

    @Query("SELECT * FROM completions")
    suspend fun getAll(): List<CompletionEntity>

    @Query("SELECT * FROM completions WHERE id = :id LIMIT 1")
    suspend fun find(id: String): CompletionEntity?

    @Query("SELECT * FROM completions WHERE pending = 1")
    suspend fun getPending(): List<CompletionEntity>

    @Upsert
    suspend fun upsert(completion: CompletionEntity)

    @Query("UPDATE completions SET pending = 0 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("DELETE FROM completions")
    suspend fun clear()
}
