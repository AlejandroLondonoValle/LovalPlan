package com.lovaltech.lovalplan.feature.profile.domain.repository

import com.lovaltech.lovalplan.feature.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile>
    suspend fun getProfile(): Profile
    suspend fun completeOnboarding(displayName: String?)
    suspend fun updateName(name: String)
    suspend fun updateDailyGoal(goal: Int)
    suspend fun clear()
}
