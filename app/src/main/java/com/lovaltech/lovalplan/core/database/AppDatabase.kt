package com.lovaltech.lovalplan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lovaltech.lovalplan.feature.profile.data.local.ProfileDao
import com.lovaltech.lovalplan.feature.profile.data.local.ProfileEntity
import com.lovaltech.lovalplan.feature.progress.data.local.CompletionDao
import com.lovaltech.lovalplan.feature.progress.data.local.CompletionEntity
import com.lovaltech.lovalplan.feature.schedule.data.local.EventDao
import com.lovaltech.lovalplan.feature.schedule.data.local.EventEntity

/** Base de datos local "lovalplan.db" con 3 tablas: events, completions y profile. */
@Database(
    entities = [EventEntity::class, CompletionEntity::class, ProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun completionDao(): CompletionDao
    abstract fun profileDao(): ProfileDao
}
