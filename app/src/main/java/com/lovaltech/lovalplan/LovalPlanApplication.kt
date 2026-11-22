package com.lovaltech.lovalplan

import android.app.Application
import com.lovaltech.lovalplan.feature.reminder.data.ReminderNotifier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/** Punto de entrada del grafo de Hilt. Al iniciar, crea los canales de notificacion. */
@HiltAndroidApp
class LovalPlanApplication : Application() {

    @Inject lateinit var notifier: ReminderNotifier

    override fun onCreate() {
        super.onCreate()
        notifier.createChannels()
    }
}
