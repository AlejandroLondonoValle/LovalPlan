package com.lovaltech.lovalplan.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Formateo de horas y fechas en espanol. No depende de Compose ni de Android,
 * por eso lo usan tanto la UI como las notificaciones.
 */
object TimeText {
    private val spanish: Locale = Locale.forLanguageTag("es-CO")
    private val shortDate = DateTimeFormatter.ofPattern("EEE d 'de' MMM", spanish)
    private val longDate = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", spanish)

    /** 390 -> "06:30" */
    fun hhmm(minuteOfDay: Int): String = "%02d:%02d".format(minuteOfDay / 60, minuteOfDay % 60)

    fun range(start: Int, end: Int): String = "${hhmm(start)} – ${hhmm(end)}"

    /** ISO: 1 = lunes ... 7 = domingo */
    fun dayInitial(isoDay: Int): String = listOf("L", "M", "X", "J", "V", "S", "D")[isoDay - 1]

    fun dayShort(isoDay: Int): String = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")[isoDay - 1]

    fun shortDate(date: LocalDate): String = date.format(shortDate)

    fun longDate(date: LocalDate): String = date.format(longDate).replaceFirstChar { it.uppercase() }

    /** 75 -> "1 h 15 min" */
    fun duration(minutes: Int): String = when {
        minutes < 60 -> "$minutes min"
        minutes % 60 == 0 -> "${minutes / 60} h"
        else -> "${minutes / 60} h ${minutes % 60} min"
    }
}
