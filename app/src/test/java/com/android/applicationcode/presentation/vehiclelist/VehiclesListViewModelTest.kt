package com.android.applicationcode.presentation.vehiclelist

import com.android.domain.model.Vehicle
import com.android.domain.repository.VehicleRepository
import com.android.domain.usecase.GetVehiclesUseCase
import com.android.domain.usecase.RefreshVehiclesUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehiclesListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial empty database shows loading until refresh completes`() =
        runTest(dispatcher) {
            val refreshGate = CompletableDeferred<Unit>()
            val repository = FakeVehicleRepository(
                refreshGate = refreshGate
            )
            val viewModel = createViewModel(repository)

            runCurrent()

            assertEquals(
                VehicleListUiState.Loading,
                viewModel.uiState.value
            )

            refreshGate.complete(Unit)
            advanceUntilIdle()

            assertEquals(
                VehicleListUiState.Empty,
                viewModel.uiState.value
            )
        }

    @Test
    fun `successful refresh displays vehicles`() =
        runTest(dispatcher) {
            val repository = FakeVehicleRepository(
                refreshedVehicles = listOf(sampleVehicle)
            )
            val viewModel = createViewModel(repository)

            advanceUntilIdle()

            assertEquals(
                VehicleListUiState.Success(listOf(sampleVehicle)),
                viewModel.uiState.value
            )
        }

    @Test
    fun `failed refresh with no cached vehicles shows error`() =
        runTest(dispatcher) {
            val repository = FakeVehicleRepository(
                refreshFailure = RuntimeException("Network error")
            )
            val viewModel = createViewModel(repository)

            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value is VehicleListUiState.Error
            )
        }

    @Test
    fun `failed refresh preserves cached vehicles`() =
        runTest(dispatcher) {
            val repository = FakeVehicleRepository(
                cachedVehicles = listOf(sampleVehicle),
                refreshFailure = RuntimeException("Network error")
            )
            val viewModel = createViewModel(repository)

            advanceUntilIdle()

            assertEquals(
                VehicleListUiState.Success(listOf(sampleVehicle)),
                viewModel.uiState.value
            )
        }

    private fun createViewModel(
        repository: FakeVehicleRepository
    ) = VehiclesListViewModel(
        getVehicles = GetVehiclesUseCase(repository),
        refreshVehicles = RefreshVehiclesUseCase(repository)
    )

    private class FakeVehicleRepository(
        cachedVehicles: List<Vehicle> = emptyList(),
        private val refreshedVehicles: List<Vehicle> = emptyList(),
        private val refreshFailure: Exception? = null,
        private val refreshGate: CompletableDeferred<Unit>? = null
    ) : VehicleRepository {

        private val vehicles = MutableStateFlow(cachedVehicles)

        override fun getVehicles(): Flow<List<Vehicle>> = vehicles

        override fun getVehicleById(id: String): Flow<Vehicle?> =
             flowOf(vehicles.value.find { it.vin == id })

        override suspend fun refreshVehicles() {
            refreshGate?.await()

            refreshFailure?.let { throw it }

            vehicles.value = refreshedVehicles
        }
    }

    companion object {
        private val sampleVehicle = Vehicle(
            vin = "1HGCM82633A123456",
            photo = "",
            year = 2020,
            make = "Honda",
            model = "Civic",
            trim = "EX",
            price = 21999.0,
            mileage = 18750,
            city = "Austin",
            state = "TX",
            phone = "5125551234",
            exteriorColor = "Blue",
            interiorColor = "Black",
            driveType = "FWD",
            transmission = "CVT",
            engine = "4 Cyl",
            bodyType = "Sedan",
            fuel = "Gasoline"
        )
    }
}