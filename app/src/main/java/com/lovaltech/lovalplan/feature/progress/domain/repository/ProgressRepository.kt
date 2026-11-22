package com.lovaltech.lovalplan.feature.progress.domain.repository

import com.lovaltech.lovalplan.core.sync.SyncReport
import com.lovaltech.lovalplan.feature.progress.domain.model.Completion
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun observeCompletions(): Flow<List<Completion>>
    suspend fun getCompletions(): List<Completion>
    suspend fun findCompletion(id: String): Completion?
    suspend fun saveCompletion(completion: Completion)
    suspend fun syncNow(): SyncReport
    suspend fun clearLocal()
}
