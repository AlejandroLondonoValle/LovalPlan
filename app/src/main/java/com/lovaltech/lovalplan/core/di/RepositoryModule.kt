package com.lovaltech.lovalplan.core.di

import com.lovaltech.lovalplan.feature.auth.data.repository.AuthRepositoryImpl
import com.lovaltech.lovalplan.feature.auth.domain.repository.AuthRepository
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
}
