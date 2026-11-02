package com.android.data.repository

import androidx.room.withTransaction
import com.android.data.local.AppDatabase
import com.android.data.local.VehicleDao
import com.android.data.mapper.toDomain
import com.android.data.mapper.toEntity
import com.android.data.remote.VehicleApi
import com.android.domain.model.Vehicle
import com.android.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val api: VehicleApi,
    private val dao: VehicleDao,
    private val database: AppDatabase
) : VehicleRepository {

    override fun getVehicles(): Flow<List<Vehicle>> =
        dao.getVehicles().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getVehicleById(id: String): Flow<Vehicle?> =
        dao.getVehicleById(id).map { entity ->
            entity?.toDomain()
        }


    override suspend fun refreshVehicles() {
        try {
            val response = api.getVehicles()
            val vehicles = response.listings
            database.withTransaction {
                dao.deleteAllVehicles()
                vehicles.map { it.toEntity() }.let { dao.insertVehicles(it) }
            }
        } catch (exception: Exception) {
            throw exception
        }
    }
}