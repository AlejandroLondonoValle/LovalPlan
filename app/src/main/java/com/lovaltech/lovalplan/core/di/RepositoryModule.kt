package com.lovaltech.lovalplan.core.di

import com.lovaltech.lovalplan.feature.auth.data.repository.AuthRepositoryImpl
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
import com.lovaltech.lovalplan.feature.profile.data.repository.ProfileRepositoryImpl
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import com.lovaltech.lovalplan.feature.progress.data.repository.ProgressRepositoryImpl
import com.lovaltech.lovalplan.feature.progress.domain.repository.ProgressRepository
import com.lovaltech.lovalplan.feature.reminder.data.AlarmReminderScheduler
import com.lovaltech.lovalplan.feature.schedule.data.repository.ScheduleRepositoryImpl
import com.lovaltech.lovalplan.feature.schedule.domain.reminder.ReminderScheduler
import com.lovaltech.lovalplan.feature.schedule.domain.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Enlaza cada contrato del dominio con su implementacion de la capa data. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindAuth(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindSchedule(impl: ScheduleRepositoryImpl): ScheduleRepository

    @Binds @Singleton
    abstract fun bindProgress(impl: ProgressRepositoryImpl): ProgressRepository

    @Binds @Singleton
    abstract fun bindProfile(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindReminderScheduler(impl: AlarmReminderScheduler): ReminderScheduler
}
