package com.android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val vin: String,
    val photo : String,
    val year: Int,
    val make: String,
    val model: String,
    val trim: String,
    val price: Double,
    val mileage: Int,
    val city: String,
    val state: String,
    val phone: String,
    val exteriorColor: String,
    val interiorColor: String,
    val driveType: String,
    val transmission: String,
    val engine: String,
    val bodyType: String,
    val fuel: String
)
