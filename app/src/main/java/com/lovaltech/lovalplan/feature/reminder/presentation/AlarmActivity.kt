package com.lovaltech.lovalplan.feature.reminder.presentation

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.lovaltech.lovalplan.core.designsystem.theme.LovalPlanTheme
import com.lovaltech.lovalplan.core.util.TimeText
import com.lovaltech.lovalplan.feature.reminder.data.AlarmReminderScheduler
import com.lovaltech.lovalplan.feature.reminder.data.ReminderContract
import com.lovaltech.lovalplan.feature.reminder.data.ReminderNotifier
import com.lovaltech.lovalplan.feature.reminder.presentation.screen.AlarmScreen
import com.lovaltech.lovalplan.feature.schedule.domain.model.ScheduleEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Pantalla de "despertador". Se abre desde la notificacion de pantalla completa,
 * aparece sobre la pantalla de bloqueo y hace sonar el tono de alarma en bucle.
 */
@AndroidEntryPoint
class AlarmActivity : ComponentActivity() {

    @Inject lateinit var scheduler: AlarmReminderScheduler
    @Inject lateinit var notifier: ReminderNotifier

    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOverLockScreen()

        val eventId = intent.getStringExtra(ReminderContract.EXTRA_EVENT_ID).orEmpty()
        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val timeRange = intent.getStringExtra(EXTRA_TIME).orEmpty()

        startRinging()
        // Por seguridad el tono se detiene solo despues de 1 minuto.
        lifecycleScope.launch {
            delay(60_000)
            ringtone?.stop()
        }

        setContent {
            LovalPlanTheme {
                AlarmScreen(
                    title = title,
                    timeRange = timeRange,
                    snoozeMinutes = ReminderContract.SNOOZE_MINUTES,
                    onStart = { close(eventId) },
                    onSnooze = {
                        scheduler.snooze(eventId, ReminderContract.SNOOZE_MINUTES)
                        close(eventId)
                    }
                )
            }
        }
    }

    private fun showOverLockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun startRinging() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ringtone = RingtoneManager.getRingtone(this, uri)?.apply {
            audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) isLooping = true
            play()
        }
    }

    private fun close(eventId: String) {
        ringtone?.stop()
        notifier.cancelStart(eventId)
        finish()
    }

    override fun onDestroy() {
        ringtone?.stop()
        super.onDestroy()
    }

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_TIME = "extra_time"

        fun pendingIntent(context: Context, event: ScheduleEvent): PendingIntent {
            val intent = Intent(context, AlarmActivity::class.java)
                .putExtra(ReminderContract.EXTRA_EVENT_ID, event.id)
                .putExtra(EXTRA_TITLE, event.title)
                .putExtra(EXTRA_TIME, TimeText.range(event.startMinute, event.endMinute))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            return PendingIntent.getActivity(
                context, event.id.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
