package com.android.data.remote

import retrofit2.http.GET

interface VehicleApi {
    //Get API to fetch Vehicles Listing
    @GET("assignment.json")
    suspend fun getVehicles(): VehiclesDto
}