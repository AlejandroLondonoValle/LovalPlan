package com.lovaltech.lovalplan.core.di

import android.content.Context
import androidx.room.Room
import com.lovaltech.lovalplan.core.database.AppDatabase
import com.lovaltech.lovalplan.feature.profile.data.local.ProfileDao
import com.lovaltech.lovalplan.feature.schedule.data.local.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun database(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "lovalplan.db").build()

    @Provides
    fun eventDao(database: AppDatabase): EventDao = database.eventDao()

    @Provides
    fun profileDao(database: AppDatabase): ProfileDao = database.profileDao()
}
