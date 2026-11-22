package com.lovaltech.lovalplan.feature.progress.data.repository

import com.lovaltech.lovalplan.core.sync.SyncReport
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import com.lovaltech.lovalplan.feature.progress.data.local.CompletionDao
import com.lovaltech.lovalplan.feature.progress.data.mapper.toDomain
import com.lovaltech.lovalplan.feature.progress.data.mapper.toDto
import com.lovaltech.lovalplan.feature.progress.data.mapper.toEntity
import com.lovaltech.lovalplan.feature.progress.data.remote.FirestoreCompletionDataSource
import com.lovaltech.lovalplan.feature.progress.domain.model.Completion
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
    private val dao: CompletionDao,
    private val remote: FirestoreCompletionDataSource,
    private val auth: AuthRepository
) : ProgressRepository {
    private val syncMutex = Mutex()

    override fun observeCompletions(): Flow<List<Completion>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override suspend fun getCompletions(): List<Completion> = dao.getAll().map { it.toDomain() }

    override suspend fun findCompletion(id: String): Completion? = dao.find(id)?.toDomain()

    override suspend fun saveCompletion(completion: Completion) = dao.upsert(completion.toEntity())

    override suspend fun syncNow(): SyncReport = syncMutex.withLock {
        val uid = auth.requireUid()

        var uploaded = 0
        for (local in dao.getPending()) {
            remote.push(uid, local.toDto())
            dao.markSynced(local.id)
            uploaded++
        }

        // Historial inmutable: solo se agregan los que no existen en este celular.
        var downloaded = 0
        for (dto in remote.fetchFromServer(uid)) {
            if (dao.find(dto.id) == null) {
                dao.upsert(dto.toEntity())
                downloaded++
            }
        }
        SyncReport(uploaded, downloaded)
    }

    override suspend fun clearLocal() = dao.clear()
}
