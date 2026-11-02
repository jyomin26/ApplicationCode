package com.android.domain.model

/**
 * Pure Kotlin Vehicle data class of the Application
 */
data class Vehicle( val vin: String,
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
                    val fuel: String)