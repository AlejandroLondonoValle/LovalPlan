package com.lovaltech.lovalplan.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock

/**
 * La "hora actual" tambien es una dependencia. En la app real es el reloj del sistema;
 * en las pruebas usamos Clock.fixed(...) para simular cualquier dia y hora.
 */
@Module
@InstallIn(SingletonComponent::class)
object TimeModule {
    @Provides
    fun clock(): Clock = Clock.systemDefaultZone()
}
