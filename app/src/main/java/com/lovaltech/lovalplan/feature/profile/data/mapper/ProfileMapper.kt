package com.lovaltech.lovalplan.feature.profile.data.mapper

import com.lovaltech.lovalplan.feature.profile.data.local.ProfileEntity
import com.lovaltech.lovalplan.feature.profile.domain.model.Profile

fun ProfileEntity.toDomain() = Profile(displayName, dailyGoal, onboarded)

fun Profile.toEntity() = ProfileEntity(
    id = 1, displayName = displayName, dailyGoal = dailyGoal, onboarded = onboarded
)
