package com.lovaltech.lovalplan.feature.reminder.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.lovaltech.lovalplan.MainActivity
import com.lovaltech.lovalplan.R
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.progress.domain.model.CompletionResult
import com.lovaltech.lovalplan.feature.progress.domain.model.XpRules
import com.lovaltech.lovalplan.feature.reminder.presentation.AlarmActivity
import com.lovaltech.lovalplan.feature.reminder.receiver.ReminderActionReceiver
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Crea los canales y publica las notificaciones de LovalPlan. */
@Singleton
class ReminderNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val brandColor = 0xFF005EDC.toInt()

    /** Se llama una vez al abrir la app (LovalPlanApplication). Crear un canal existente no hace nada. */
    fun createChannels() {
        val manager = context.getSystemService(NotificationManager::class.java)
        val alarmAudio = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        val alarms = NotificationChannel(
            ReminderContract.CHANNEL_ALARMS, "Alarmas de actividades", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Suena como despertador cuando una actividad va a empezar"
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), alarmAudio)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 600, 400, 600, 400, 600)
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
        }
        val checks = NotificationChannel(
            ReminderContract.CHANNEL_CHECKS, "Actividades cumplidas", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Te pregunta si cumpliste una actividad y celebra tus logros"
        }
        manager.createNotificationChannels(listOf(alarms, checks))
    }

    /** Alarma de inicio: pantalla completa (AlarmActivity) + sonido de alarma. */
    fun showStartAlarm(event: ScheduleEvent) {
        val fullScreen = AlarmActivity.pendingIntent(context, event)
        val whenText = if (event.reminderMinutes == 0) "¡Empieza ahora!"
        else "Empieza en ${event.reminderMinutes} min · ${TimeText.hhmm(event.startMinute)}"
        val notification = NotificationCompat.Builder(context, ReminderContract.CHANNEL_ALARMS)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(brandColor)
            .setContentTitle("⏰ ${event.title}")
            .setContentText(whenText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreen, true)
            .setContentIntent(fullScreen)
            .setAutoCancel(true)
            .addAction(0, "Posponer ${ReminderContract.SNOOZE_MINUTES} min",
                ReminderActionReceiver.pendingIntent(context, ReminderContract.ACTION_SNOOZE, event.id))
            .addAction(0, "Descartar",
                ReminderActionReceiver.pendingIntent(context, ReminderContract.ACTION_DISMISS, event.id))
            .build()
        notify(ReminderContract.startNotificationId(event.id), notification)
    }

    /** Al terminar la actividad: "¿Lo cumpliste?" con el boton ¡Lo cumplí! */
    fun showEndCheck(event: ScheduleEvent) {
        val xp = XpRules.xpFor(event.durationMinutes)
        val notification = NotificationCompat.Builder(context, ReminderContract.CHANNEL_CHECKS)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(brandColor)
            .setContentTitle("¿Cumpliste «${event.title}»?")
            .setContentText("Márcala y gana +$xp XP para tu racha 🔥")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openApp())
            .setAutoCancel(true)
            .addAction(0, "¡Lo cumplí!",
                ReminderActionReceiver.pendingIntent(context, ReminderContract.ACTION_COMPLETE, event.id))
            .build()
        notify(ReminderContract.endNotificationId(event.id), notification)
    }

    /** Celebracion despues de cumplir desde la notificacion. */
    fun showReward(result: CompletionResult) {
        val extra = buildString {
            if (result.leveledUp) append(" · ¡Subiste a nivel ${result.level}!")
            result.newAchievements.firstOrNull()?.let { append(" · Logro: ${it.emoji} ${it.title}") }
        }
        val notification = NotificationCompat.Builder(context, ReminderContract.CHANNEL_CHECKS)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(brandColor)
            .setContentTitle("¡Bien hecho! +${result.xpGained} XP ⚡")
            .setContentText("Racha: ${result.currentStreak} 🔥$extra")
            .setContentIntent(openApp())
            .setAutoCancel(true)
            .build()
        notify(ReminderContract.REWARD_NOTIFICATION_ID, notification)
    }

    fun showMessage(title: String, text: String) {
        val notification = NotificationCompat.Builder(context, ReminderContract.CHANNEL_CHECKS)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(brandColor)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(openApp())
            .setAutoCancel(true)
            .build()
        notify(ReminderContract.REWARD_NOTIFICATION_ID, notification)
    }

    fun cancelStart(eventId: String) =
        NotificationManagerCompat.from(context).cancel(ReminderContract.startNotificationId(eventId))

    fun cancelEnd(eventId: String) =
        NotificationManagerCompat.from(context).cancel(ReminderContract.endNotificationId(eventId))

    private fun openApp(): PendingIntent = PendingIntent.getActivity(
        context, 1, Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    @SuppressLint("MissingPermission")
    private fun notify(id: Int, notification: android.app.Notification) {
        // En Android 13+ sin permiso de notificaciones no se puede publicar nada.
        val allowed = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        if (allowed) NotificationManagerCompat.from(context).notify(id, notification)
    }
}
