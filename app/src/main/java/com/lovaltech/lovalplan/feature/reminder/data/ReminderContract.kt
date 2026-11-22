package com.lovaltech.lovalplan.feature.reminder.data

/** Tipos de alarma que maneja el AlarmManager. */
enum class AlarmKind {
    START,   // "va a empezar": alarma a pantalla completa
    END,     // "termino": notificacion con el boton ¡Lo cumplí!
    SNOOZE;  // alarma pospuesta 5 minutos

    val action: String get() = "com.lovaltech.lovalplan.alarm.$name"

    companion object {
        fun fromAction(action: String?): AlarmKind? = entries.firstOrNull { it.action == action }
    }
}

/** Claves y acciones compartidas por receptores, notificaciones y AlarmActivity. */
object ReminderContract {
    const val EXTRA_EVENT_ID = "extra_event_id"

    const val ACTION_COMPLETE = "com.lovaltech.lovalplan.action.COMPLETE"
    const val ACTION_SNOOZE = "com.lovaltech.lovalplan.action.SNOOZE"
    const val ACTION_DISMISS = "com.lovaltech.lovalplan.action.DISMISS"

    const val CHANNEL_ALARMS = "alarmas_actividades"
    const val CHANNEL_CHECKS = "actividades_cumplidas"

    const val SNOOZE_MINUTES = 5

    fun startNotificationId(eventId: String) = eventId.hashCode()
    fun endNotificationId(eventId: String) = eventId.hashCode() + 1
    const val REWARD_NOTIFICATION_ID = 7_777
}
