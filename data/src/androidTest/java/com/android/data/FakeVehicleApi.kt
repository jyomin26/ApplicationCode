package com.android.data

import com.android.data.remote.VehicleApi
import com.android.data.remote.VehiclesDto

class FakeVehicleApi : VehicleApi {

    var response: VehiclesDto? = null
    var shouldFail = false

    override suspend fun getVehicles(): VehiclesDto {
        if (shouldFail) {
            throw RuntimeException("Network error")
        }

        return requireNotNull(response) {
            "Fake API response was not configured"
        }
    }
}