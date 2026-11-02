package com.android.domain

import com.android.domain.model.Vehicle
import com.android.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeVehicleRepository : VehicleRepository {
    val vehicles = listOf(
        Vehicle(
            vin = "TEST123",
            photo = "https://example.com/carfax.jpg",
            year = 2022,
            make = "Toyota",
            model = "Camry",
            trim = "SE",
            price = 25000.0,
            mileage = 30000,
            city = "Toronto",
            state = "ON",
            phone = "4165551234",
            exteriorColor = "White",
            interiorColor = "Black",
            driveType = "FWD",
            transmission = "Automatic",
            engine = "2.5L 4-Cylinder",
            bodyType = "Sedan",
            fuel = "Gasoline"
        )
    )
    var refreshCalled = false
    override fun getVehicles(): Flow<List<Vehicle>> {
        return flowOf(vehicles)
    }

    override fun getVehicleById(id: String): Flow<Vehicle?> {
        return flowOf(vehicles.find { it.vin == id })
    }

    override suspend fun refreshVehicles() {
        refreshCalled = true
    }
}