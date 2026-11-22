package com.lovaltech.lovalplan.feature.reminder.data

import com.lovaltech.lovalplan.feature.progress.domain.usecase.CompleteEventUseCase
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Los BroadcastReceiver los crea el sistema, no Hilt. Con un EntryPoint le pedimos
 * a Hilt las dependencias "a mano": EntryPointAccessors.fromApplication(...).
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReminderEntryPoint {
    fun scheduleRepository(): ScheduleRepository
    fun alarmScheduler(): AlarmReminderScheduler
    fun notifier(): ReminderNotifier
    fun completeEvent(): CompleteEventUseCase
}
