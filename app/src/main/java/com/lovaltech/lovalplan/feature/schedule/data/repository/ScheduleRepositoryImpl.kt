package com.lovaltech.lovalplan.feature.schedule.data.repository

import com.lovaltech.lovalplan.core.sync.SyncReport
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import com.lovaltech.lovalplan.feature.schedule.data.local.EventDao
import com.lovaltech.lovalplan.feature.schedule.data.mapper.toDomain
import com.lovaltech.lovalplan.feature.schedule.data.mapper.toDto
import com.lovaltech.lovalplan.feature.schedule.data.mapper.toEntity
import com.lovaltech.lovalplan.feature.schedule.data.remote.FirestoreEventDataSource
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Misma estrategia que ProductRepositoryImpl (fase 1):
 * Room es la fuente de verdad; Firestore se sincroniza al pulsar Sincronizar.
 */
class ScheduleRepositoryImpl @Inject constructor(
    private val dao: EventDao,
    private val remote: FirestoreEventDataSource,
    private val auth: AuthRepository
) : ScheduleRepository {
    private val syncMutex = Mutex()

    override fun observeEvents(): Flow<List<ScheduleEvent>> =
        dao.observeVisible().map { rows -> rows.map { it.toDomain() } }

    override suspend fun getAll(): List<ScheduleEvent> = dao.getVisible().map { it.toDomain() }

    override suspend fun findById(id: String): ScheduleEvent? =
        dao.find(id)?.takeUnless { it.isDeleted }?.toDomain()

    override suspend fun save(event: ScheduleEvent) {
        // Primero Room: funciona sin Internet.
        dao.upsert(event.copy(updatedAt = System.currentTimeMillis()).toEntity())
    }

    override suspend fun delete(id: String) {
        val current = dao.find(id) ?: return
        // Tombstone: se conserva marcado hasta sincronizar.
        dao.upsert(current.copy(isDeleted = true, pending = true, updatedAt = System.currentTimeMillis()))
    }

    override suspend fun syncNow(): SyncReport = syncMutex.withLock {
        val uid = auth.requireUid()

        var uploaded = 0
        for (local in dao.getPending()) {
            remote.push(uid, local.toDto())
            dao.markSynced(local.id, local.updatedAt)
            uploaded++
        }

        val cloudItems = remote.fetchFromServer(uid)
        var downloaded = 0
        for (dto in cloudItems) {
            val cloud = dto.toEntity()
            val local = dao.find(cloud.id)
            if (local?.pending == true) continue            // nunca pisar un cambio local pendiente
            if (local == null || cloud.updatedAt > local.updatedAt) {
                dao.upsert(cloud)
                downloaded++
            }
        }
        SyncReport(uploaded, downloaded)
    }

    override suspend fun clearLocal() = dao.clear()
}
