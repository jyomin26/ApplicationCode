package com.android.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicles")
    fun getVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE vin = :vin")
    fun getVehicleById(vin: String): Flow<VehicleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    @Query("DELETE FROM vehicles")
    suspend fun deleteAllVehicles()
}