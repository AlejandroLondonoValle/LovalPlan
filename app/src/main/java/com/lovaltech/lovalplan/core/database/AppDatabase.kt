package com.lovaltech.lovalplan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lovaltech.lovalplan.feature.profile.data.local.ProfileDao
import com.lovaltech.lovalplan.feature.profile.data.local.ProfileEntity
import com.lovaltech.lovalplan.feature.schedule.data.local.EventDao
import com.lovaltech.lovalplan.feature.schedule.data.local.EventEntity

/** Base de datos local "lovalplan.db". */
@Database(
    entities = [EventEntity::class, ProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun profileDao(): ProfileDao
}
