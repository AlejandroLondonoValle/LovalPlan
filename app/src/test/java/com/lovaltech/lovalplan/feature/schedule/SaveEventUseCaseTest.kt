package com.lovaltech.lovalplan.feature.schedule

import com.lovaltech.lovalplan.fakes.FakeReminderScheduler
import com.lovaltech.lovalplan.fakes.FakeScheduleRepository
import com.lovaltech.lovalplan.fakes.TEST_CLOCK
import com.lovaltech.lovalplan.fakes.TODAY
import com.lovaltech.lovalplan.fakes.event
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import com.lovaltech.lovalplan.feature.schedule.domain.usecase.SaveEventUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveEventUseCaseTest {

    private val repository = FakeScheduleRepository()
    private val scheduler = FakeReminderScheduler()
    private val save = SaveEventUseCase(repository, scheduler, TEST_CLOCK)

    private fun errorOf(block: suspend () -> Unit): String? =
        runCatching { runBlocking { block() } }.exceptionOrNull()?.message

    @Test
    fun `una actividad valida se guarda y se programa su alarma`() = runBlocking {
        save(event(id = "gym"))
        assertEquals(1, repository.events.value.size)
        assertTrue("gym" in scheduler.scheduled)
    }

    @Test
    fun `el titulo es obligatorio`() {
        assertEquals("El título es obligatorio", errorOf { save(event(title = "  ")) })
        assertTrue(repository.events.value.isEmpty())
    }

    @Test
    fun `el fin debe ser despues del inicio`() {
        assertEquals(
            "La hora de fin debe ser posterior al inicio",
            errorOf { save(event(start = 10 * 60, end = 9 * 60)) }
        )
    }

    @Test
    fun `una actividad semanal necesita al menos un dia`() {
        assertEquals("Elige al menos un día de la semana", errorOf { save(event(days = emptySet())) })
    }

    @Test
    fun `una actividad de fecha unica no puede quedar en el pasado`() {
        val past = event(recurrence = RecurrenceType.ONCE, days = emptySet(), date = TODAY.minusDays(1))
        assertEquals("La fecha no puede estar en el pasado", errorOf { save(past) })
    }
}
