package com.android.domain.usecase

import com.android.domain.repository.VehicleRepository
import javax.inject.Inject

class GetVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
     operator fun invoke() =
        repository.getVehicles()
}