package com.android.applicationcode.presentation.vehicledetail

import com.android.domain.model.Vehicle

sealed interface VehicleDetailUiState {
    //Room has not emitted a result yet.
    data object Loading : VehicleDetailUiState

    //the requested vehicle exists.
    data class Success(
        val vehicle: Vehicle
    ) : VehicleDetailUiState

    //the query completed but no vehicle matches the VIN.
    data object NotFound : VehicleDetailUiState

    //loading the vehicle failed
    data class Error(
        val message: String
    ) : VehicleDetailUiState
}