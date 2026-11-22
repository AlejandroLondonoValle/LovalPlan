package com.lovaltech.lovalplan.feature.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lovaltech.lovalplan.feature.reminder.data.ReminderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Android borra todas las alarmas al reiniciar el celular. Este receptor las vuelve a
 * programar (tambien si cambia la hora/zona o se actualiza la app).
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val deps = EntryPointAccessors.fromApplication(context, ReminderEntryPoint::class.java)
        val pending = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                deps.alarmScheduler().rescheduleAll(deps.scheduleRepository().getAll())
            } finally {
                pending.finish()
            }
        }
    }
}
