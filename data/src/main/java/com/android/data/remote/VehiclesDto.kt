package com.android.data.remote

data class VehiclesDto(val listings: List<VehicleDto>)

data class VehicleDto(
    val vin: String,
    val year: Int,
    val make: String,
    val model: String,
    val trim: String,
    val listPrice: Double,
    val mileage: Int,
    val exteriorColor: String,
    val interiorColor: String,
    val engine: String,
    val drivetype: String,
    val transmission: String,
    val bodytype: String,
    val dealer: Dealer,
    val images: Images,
    val fuel: String,
)

data class Dealer(
    val city: String, val state: String, val phone: String
)

data class Images(
    val large: List<String>?
)
