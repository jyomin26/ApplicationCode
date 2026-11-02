
package com.android.applicationcode.presentation.vehiclelist

import com.android.domain.model.Vehicle

sealed interface VehicleListUiState {

    //Initial data is loading and no cached data is available yet
    data object Loading : VehicleListUiState

    //Vehicles are available to display
    data class Success(val vehicles: List<Vehicle>,
                       val isLoading: Boolean = true,
                       val refreshError: String? = null) : VehicleListUiState

    //Loading completed successfully, but there are no vehicles
    data object Empty : VehicleListUiState

    //Initial loading failed and no cached vehicles are available
    data class Error(val message: String) : VehicleListUiState
}
