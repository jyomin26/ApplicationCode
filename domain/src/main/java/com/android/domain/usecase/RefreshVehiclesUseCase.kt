package com.android.domain.usecase

import com.android.domain.repository.VehicleRepository
import javax.inject.Inject

class RefreshVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke() {
        repository.refreshVehicles()
    }
}