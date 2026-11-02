package com.android.domain.usecase

import com.android.domain.repository.VehicleRepository
import javax.inject.Inject

class GetVehicleByIdUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
     operator fun invoke(id: String) =
        repository.getVehicleById(id)
}