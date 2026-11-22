package com.lovaltech.lovalplan.feature.schedule.domain.repository

import com.lovaltech.lovalplan.core.sync.SyncReport
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import kotlinx.coroutines.flow.Flow

/** Contrato del horario. Igual que ProductRepository: dice QUE se hace, no COMO. */
interface ScheduleRepository {
    fun observeEvents(): Flow<List<ScheduleEvent>>
    suspend fun getAll(): List<ScheduleEvent>
    suspend fun findById(id: String): ScheduleEvent?
    suspend fun save(event: ScheduleEvent)
    suspend fun delete(id: String)
    suspend fun syncNow(): SyncReport
    suspend fun clearLocal()
}
