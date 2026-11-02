package com.android.data.mapper

import com.android.data.local.VehicleEntity
import com.android.data.remote.VehicleDto
import com.android.domain.model.Vehicle

fun VehicleDto.toEntity() = VehicleEntity(
    vin = vin,
    photo = images.large?.get(0) ?: "",
    year = year,
    make = make,
    model = model,
    trim = trim,
    price = listPrice,
    mileage = mileage,
    city = dealer.city,
    state = dealer.state,
    phone = dealer.phone,
    exteriorColor = exteriorColor,
    interiorColor = interiorColor,
    driveType = drivetype,
    transmission = transmission,
    engine = engine,
    bodyType = bodytype,
    fuel = fuel
)

fun VehicleEntity.toDomain() = Vehicle(
    vin = vin,
    photo = photo,
    year = year,
    make = make,
    model = model,
    trim = trim,
    price = price,
    mileage = mileage,
    city = city,
    state = state,
    phone = phone,
    exteriorColor = exteriorColor,
    interiorColor = interiorColor,
    driveType = driveType,
    transmission = transmission,
    engine = engine,
    bodyType = bodyType,
    fuel = fuel
)