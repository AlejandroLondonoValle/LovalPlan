package com.lovaltech.lovalplan.feature.reminder.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.lovaltech.lovalplan.MainActivity
import com.lovaltech.lovalplan.feature.reminder.domain.NextOccurrence
import com.lovaltech.lovalplan.feature.reminder.receiver.AlarmReceiver
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import com.lovaltech.lovalplan.feature.schedule.domain.reminder.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementacion real de ReminderScheduler con AlarmManager.
 * - START usa setAlarmClock: es la API de "despertador" (suena aunque el celular duerma
 *   y muestra el icono de alarma en la barra de estado).
 * - END usa setExactAndAllowWhileIdle.
 * Si el sistema no permite alarmas exactas, se usa una alarma aproximada.
 */
@Singleton
class AlarmReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : ReminderScheduler {

    private val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(event: ScheduleEvent) = scheduleFrom(event, LocalDateTime.now())

    /**
     * Programa la proxima ocurrencia a partir de [from]. El receptor usa "ahora + 1 min"
     * para no reprogramar la misma alarma que acaba de sonar.
     */
    fun scheduleFrom(event: ScheduleEvent, from: LocalDateTime) {
        cancel(event.id)
        NextOccurrence.startTrigger(event, from)?.let { set(it, event.id, AlarmKind.START) }
        NextOccurrence.endTrigger(event, from)?.let { set(it, event.id, AlarmKind.END) }
    }

    fun snooze(eventId: String, minutes: Int) {
        set(LocalDateTime.now().plusMinutes(minutes.toLong()), eventId, AlarmKind.SNOOZE)
    }

    override fun cancel(eventId: String) {
        AlarmKind.entries.forEach { kind -> alarmManager.cancel(pendingIntent(eventId, kind)) }
    }

    override fun rescheduleAll(events: List<ScheduleEvent>) = events.forEach(::schedule)

    // Se usa la zona horaria vigente en cada llamada: si el usuario cambia de zona,
    // BootReceiver (TIMEZONE_CHANGED) reprograma todo con la nueva.
    @SuppressLint("ScheduleExactAlarm", "MissingPermission")
    private fun set(at: LocalDateTime, eventId: String, kind: AlarmKind) {
        val triggerMillis = at.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val operation = pendingIntent(eventId, kind)
        if (canScheduleExact()) {
            if (kind == AlarmKind.END) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, operation)
            } else {
                val info = AlarmManager.AlarmClockInfo(triggerMillis, openAppIntent())
                alarmManager.setAlarmClock(info, operation)
            }
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, operation)
        }
    }

    private fun canScheduleExact(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    private fun pendingIntent(eventId: String, kind: AlarmKind): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
            .setAction(kind.action)
            .putExtra(ReminderContract.EXTRA_EVENT_ID, eventId)
        // Un requestCode distinto por actividad y tipo: asi cada alarma es independiente.
        val requestCode = eventId.hashCode() * 3 + kind.ordinal
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun openAppIntent(): PendingIntent = PendingIntent.getActivity(
        context, 0, Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
