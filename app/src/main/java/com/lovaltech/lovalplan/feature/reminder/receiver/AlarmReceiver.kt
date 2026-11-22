package com.lovaltech.lovalplan.feature.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lovaltech.lovalplan.feature.reminder.data.AlarmKind
import com.lovaltech.lovalplan.feature.reminder.data.ReminderContract
import com.lovaltech.lovalplan.feature.reminder.data.ReminderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * Lo despierta el AlarmManager. Busca la actividad en Room, muestra la notificacion
 * correspondiente y programa la siguiente ocurrencia.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getStringExtra(ReminderContract.EXTRA_EVENT_ID) ?: return
        val kind = AlarmKind.fromAction(intent.action) ?: return
        val deps = EntryPointAccessors.fromApplication(context, ReminderEntryPoint::class.java)

        // goAsync: permite trabajar en segundo plano (consultar Room) unos segundos.
        val pending = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                // Si la actividad se borro, no se avisa nada.
                val event = deps.scheduleRepository().findById(eventId) ?: return@launch
                when (kind) {
                    AlarmKind.START, AlarmKind.SNOOZE -> deps.notifier().showStartAlarm(event)
                    AlarmKind.END -> deps.notifier().showEndCheck(event)
                }
                if (kind != AlarmKind.SNOOZE) {
                    deps.alarmScheduler().scheduleFrom(event, LocalDateTime.now().plusMinutes(1))
                }
            } finally {
                pending.finish()
            }
        }
    }
}
