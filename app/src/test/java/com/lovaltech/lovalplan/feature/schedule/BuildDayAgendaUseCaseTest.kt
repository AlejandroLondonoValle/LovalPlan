package com.lovaltech.lovalplan.feature.schedule

import com.lovaltech.lovalplan.fakes.TODAY
import com.lovaltech.lovalplan.fakes.event
import com.lovaltech.lovalplan.feature.schedule.domain.model.AgendaStatus
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import com.lovaltech.lovalplan.feature.schedule.domain.usecase.BuildDayAgendaUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class BuildDayAgendaUseCaseTest {

    private val build = BuildDayAgendaUseCase()
    private val now = TODAY.atTime(15, 0) // miercoles 15:00

    private val morning = event(id = "m", days = setOf(3), start = 8 * 60, end = 9 * 60)
    private val afternoon = event(id = "a", days = setOf(3), start = 14 * 60 + 30, end = 16 * 60)
    private val night = event(id = "n", days = setOf(3), start = 20 * 60, end = 21 * 60)
    private val monday = event(id = "lun", days = setOf(1))

    @Test
    fun `solo incluye las actividades del dia y las ordena por hora`() {
        val agenda = build(TODAY, now, listOf(night, monday, morning, afternoon), emptySet())
        assertEquals(listOf("m", "a", "n"), agenda.map { it.event.id })
    }

    @Test
    fun `calcula el estado segun la hora actual y los cumplimientos`() {
        val done = setOf(ScheduleEvent.occurrenceKey("m", TODAY))
        val status = build(TODAY, now, listOf(morning, afternoon, night), done).associate { it.event.id to it.status }
        assertEquals(AgendaStatus.COMPLETED, status["m"])
        assertEquals(AgendaStatus.IN_PROGRESS, status["a"])
        assertEquals(AgendaStatus.UPCOMING, status["n"])
    }

    @Test
    fun `una actividad que ya termino sin cumplirse queda pendiente`() {
        val agenda = build(TODAY, now, listOf(morning), emptySet())
        assertEquals(AgendaStatus.MISSED, agenda.single().status)
    }
}
