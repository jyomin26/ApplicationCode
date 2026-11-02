package com.android.data.di

import com.android.data.repository.VehicleRepositoryImpl
import com.android.domain.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindVehicleRepository(
        implementation: VehicleRepositoryImpl
    ): VehicleRepository
}