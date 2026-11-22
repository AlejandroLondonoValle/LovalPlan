package com.lovaltech.lovalplan.feature.profile.data.repository

import com.lovaltech.lovalplan.feature.profile.data.local.ProfileDao
import com.lovaltech.lovalplan.feature.profile.data.mapper.toDomain
import com.lovaltech.lovalplan.feature.profile.data.mapper.toEntity
import com.lovaltech.lovalplan.feature.profile.domain.model.Profile
import com.lovaltech.lovalplan.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** El perfil vive solo en Room (es configuracion de este celular). */
class ProfileRepositoryImpl @Inject constructor(
    private val dao: ProfileDao
) : ProfileRepository {

    override fun observeProfile(): Flow<Profile> =
        dao.observe().map { it?.toDomain() ?: Profile() }

    override suspend fun getProfile(): Profile = dao.get()?.toDomain() ?: Profile()

    override suspend fun completeOnboarding(displayName: String?) {
        val current = getProfile()
        dao.upsert(
            current.copy(
                displayName = displayName?.takeIf { it.isNotBlank() } ?: current.displayName,
                onboarded = true
            ).toEntity()
        )
    }

    override suspend fun updateName(name: String) {
        dao.upsert(getProfile().copy(displayName = name.trim().take(40)).toEntity())
    }

    override suspend fun updateDailyGoal(goal: Int) {
        dao.upsert(getProfile().copy(dailyGoal = goal.coerceIn(1, 10)).toEntity())
    }

    override suspend fun clear() = dao.clear()
}
