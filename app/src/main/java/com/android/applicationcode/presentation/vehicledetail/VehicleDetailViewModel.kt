package com.android.applicationcode.presentation.vehicledetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.applicationcode.presentation.navigation.NavRoutes
import com.android.domain.usecase.GetVehicleByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VehicleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getVehicleById: GetVehicleByIdUseCase
) : ViewModel() {

    private val vehicleId: String =
        checkNotNull(savedStateHandle[NavRoutes.VIN_ARG])

    val uiState = getVehicleById(vehicleId)
        .map { vehicle ->
            if (vehicle != null) {
                VehicleDetailUiState.Success(vehicle)
            } else {
                VehicleDetailUiState.NotFound
            }
        }
        .catch { exception ->
            emit(
                VehicleDetailUiState.Error(
                    message = exception.message ?: "Failed to load vehicle detail"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VehicleDetailUiState.Loading
        )
}