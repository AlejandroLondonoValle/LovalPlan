package com.lovaltech.lovalplan.feature.reminder.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lovaltech.lovalplan.feature.reminder.data.ReminderContract
import com.lovaltech.lovalplan.feature.reminder.data.ReminderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Botones de las notificaciones: ¡Lo cumplí!, Posponer y Descartar. */
class ReminderActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getStringExtra(ReminderContract.EXTRA_EVENT_ID) ?: return
        val deps = EntryPointAccessors.fromApplication(context, ReminderEntryPoint::class.java)
        val notifier = deps.notifier()

        when (intent.action) {
            ReminderContract.ACTION_SNOOZE -> {
                notifier.cancelStart(eventId)
                deps.alarmScheduler().snooze(eventId, ReminderContract.SNOOZE_MINUTES)
            }
            ReminderContract.ACTION_DISMISS -> notifier.cancelStart(eventId)
            ReminderContract.ACTION_COMPLETE -> {
                notifier.cancelEnd(eventId)
                val pending = goAsync()
                CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                    try {
                        // Mismo caso de uso que el Dashboard: mismas reglas, misma XP.
                        val completeEvent = deps.completeEvent()
                        val result = completeEvent(eventId)
                        notifier.showReward(result)
                    } catch (e: Exception) {
                        notifier.showMessage("No se pudo marcar", e.message ?: "Intenta desde la app")
                    } finally {
                        pending.finish()
                    }
                }
            }
        }
    }

    companion object {
        fun pendingIntent(context: Context, action: String, eventId: String): PendingIntent {
            val intent = Intent(context, ReminderActionReceiver::class.java)
                .setAction(action)
                .putExtra(ReminderContract.EXTRA_EVENT_ID, eventId)
            return PendingIntent.getBroadcast(
                context, (eventId + action).hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
