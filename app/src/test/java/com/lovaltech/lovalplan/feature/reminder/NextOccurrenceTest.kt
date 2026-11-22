package com.lovaltech.lovalplan.feature.reminder

import com.lovaltech.lovalplan.fakes.TODAY
import com.lovaltech.lovalplan.fakes.event
import com.lovaltech.lovalplan.feature.reminder.domain.NextOccurrence
import com.lovaltech.lovalplan.feature.schedule.domain.model.RecurrenceType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NextOccurrenceTest {

    private val now = TODAY.atTime(15, 0) // miercoles 15:00

    @Test
    fun `la alarma suena los minutos de anticipacion antes del inicio`() {
        val gym = event(days = setOf(3), start = 18 * 60, end = 19 * 60, reminder = 10)
        assertEquals(TODAY.atTime(17, 50), NextOccurrence.startTrigger(gym, now))
        assertEquals(TODAY.atTime(19, 0), NextOccurrence.endTrigger(gym, now))
    }

    @Test
    fun `si la de hoy ya paso, programa la del siguiente dia que toque`() {
        // Lunes, miercoles y viernes a las 6:00 -> hoy (mie) ya paso -> viernes 5:50
        val gym = event(days = setOf(1, 3, 5), start = 6 * 60, end = 7 * 60, reminder = 10)
        assertEquals(TODAY.plusDays(2).atTime(5, 50), NextOccurrence.startTrigger(gym, now))
    }

    @Test
    fun `una actividad de fecha unica en el pasado no programa nada`() {
        val exam = event(recurrence = RecurrenceType.ONCE, days = emptySet(), date = TODAY.minusDays(3))
        assertNull(NextOccurrence.startTrigger(exam, now))
    }

    @Test
    fun `una actividad de fecha unica lejana tambien se programa`() {
        val trip = event(recurrence = RecurrenceType.ONCE, days = emptySet(), date = TODAY.plusDays(40), start = 9 * 60, end = 10 * 60, reminder = 0)
        assertEquals(TODAY.plusDays(40).atTime(9, 0), NextOccurrence.startTrigger(trip, now))
    }
}
