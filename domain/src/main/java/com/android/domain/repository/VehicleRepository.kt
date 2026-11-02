package com.android.domain.repository

import com.android.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    /**
     * Fetch vehicles from Db and return the List of Vehicles Flow object
     */
    fun getVehicles(): Flow<List<Vehicle>>

    /**
     * Fetch Vehicle detail from Db and return the Vehicle Flow object
     */
    fun getVehicleById(id: String): Flow<Vehicle?>

    /**
     * get the Vehicles list from backend
     */
    suspend fun refreshVehicles()
}