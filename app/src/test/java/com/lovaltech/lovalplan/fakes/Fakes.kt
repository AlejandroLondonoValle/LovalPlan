package com.lovaltech.lovalplan.fakes

import com.lovaltech.lovalplan.core.sync.SyncReport
import com.lovaltech.lovalplan.feature.progress.domain.model.Completion
import com.lovaltech.lovalplan.feature.schedule.domain.model.EventCategory
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import com.lovaltech.lovalplan.feature.schedule.domain.reminder.ReminderScheduler
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Dobles de prueba. Gracias a que el dominio depende de INTERFACES, podemos probar
 * las reglas sin Room, sin Firebase y sin AlarmManager.
 */

/** Miercoles 25 de noviembre de 2026, 15:00 (UTC). */
val TEST_CLOCK: Clock = Clock.fixed(Instant.parse("2026-11-25T15:00:00Z"), ZoneOffset.UTC)
val TODAY: LocalDate = LocalDate.now(TEST_CLOCK)

fun event(
    id: String = "e1",
    title: String = "Gimnasio",
    recurrence: RecurrenceType = RecurrenceType.WEEKLY,
    days: Set<Int> = setOf(1, 3, 5),
    date: LocalDate? = null,
    start: Int = 6 * 60,
    end: Int = 7 * 60,
    reminder: Int = 10
) = ScheduleEvent(
    id = id, title = title, description = "", category = EventCategory.EXERCISE,
    recurrence = recurrence, daysOfWeek = days, date = date,
    startMinute = start, endMinute = end, reminderMinutes = reminder
)

fun completion(day: LocalDate, eventId: String = "e1", xp: Int = 15, startMinute: Int = 8 * 60) = Completion(
    id = ScheduleEvent.occurrenceKey(eventId, day),
    eventId = eventId,
    dayEpoch = day.toEpochDay(),
    completedAt = 0L,
    xp = xp,
    startMinute = startMinute
)

class FakeScheduleRepository : ScheduleRepository {
    val events = MutableStateFlow<List<ScheduleEvent>>(emptyList())
    override fun observeEvents(): Flow<List<ScheduleEvent>> = events
    override suspend fun getAll() = events.value
    override suspend fun findById(id: String) = events.value.firstOrNull { it.id == id }
    override suspend fun save(event: ScheduleEvent) {
        events.value = events.value.filterNot { it.id == event.id } + event
    }
    override suspend fun delete(id: String) {
        events.value = events.value.filterNot { it.id == id }
    }
    override suspend fun syncNow() = SyncReport(0, 0)
    override suspend fun clearLocal() { events.value = emptyList() }
}

class FakeReminderScheduler : ReminderScheduler {
    val scheduled = mutableListOf<String>()
    val cancelled = mutableListOf<String>()
    override fun schedule(event: ScheduleEvent) { scheduled += event.id }
    override fun cancel(eventId: String) { cancelled += eventId }
    override fun rescheduleAll(events: List<ScheduleEvent>) { events.forEach(::schedule) }
}
