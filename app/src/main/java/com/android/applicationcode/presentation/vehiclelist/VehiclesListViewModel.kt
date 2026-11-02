package com.android.applicationcode.presentation.vehiclelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.model.Vehicle
import com.android.domain.usecase.GetVehiclesUseCase
import com.android.domain.usecase.RefreshVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class VehiclesListViewModel @Inject constructor(
    private val getVehicles: GetVehiclesUseCase,
    private val refreshVehicles: RefreshVehiclesUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<VehicleListUiState>(VehicleListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val vehicles = MutableStateFlow<List<Vehicle>?>(null)
    private val initialRefreshFinished = MutableStateFlow(false)
    private val refreshError = MutableStateFlow<String?>(null)

    init {
        observeVehicles()
        refresh()
        observeUiState()
    }

    private fun observeVehicles() {
        viewModelScope.launch {
            getVehicles()
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    refreshError.value = "Unable to load vehicles."
                    initialRefreshFinished.value = true
                }
                .collect{ result ->
                    vehicles.value = result
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                refreshVehicles()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                refreshError.value =
                    "Unable to fetch vehicles. Please try again."
            } finally {
                initialRefreshFinished.value = true
            }
        }
    }

    private fun observeUiState() {
        viewModelScope.launch {
            combine(
                vehicles,
                initialRefreshFinished,
                refreshError
            ) { result, refreshFinished, error ->
                when {
                    result == null ->
                        VehicleListUiState.Loading

                    result.isNotEmpty() ->
                        VehicleListUiState.Success(result)

                    !refreshFinished ->
                        VehicleListUiState.Loading

                    error != null ->
                        VehicleListUiState.Error(error)

                    else ->
                        VehicleListUiState.Empty
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}