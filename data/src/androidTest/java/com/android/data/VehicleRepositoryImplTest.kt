package com.android.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.data.local.AppDatabase
import com.android.data.remote.Dealer
import com.android.data.remote.Images
import com.android.data.remote.VehicleDto
import com.android.data.remote.VehiclesDto
import com.android.data.repository.VehicleRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VehicleRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var api: FakeVehicleApi
    private lateinit var repository: VehicleRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        api = FakeVehicleApi()

        repository = VehicleRepositoryImpl(
            api = api,
            dao = database.vehicleDao(),
            database = database
        )
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun refreshVehiclesSavesApiVehiclesToDatabase() = runTest {
        api.response = VehiclesDto(listings = listOf(sampleVehicleDto))

        repository.refreshVehicles()

        val result = repository.getVehicles().first()

        assertEquals(1, result.size)
        assertEquals("VIN123", result.first().vin)
        assertEquals("Honda", result.first().make)
    }

    @Test
    fun failedRefreshPreservesCachedVehicles() = runTest {
        // Insert the initial cached vehicle.
        api.response = VehiclesDto(listings = listOf(sampleVehicleDto))
        repository.refreshVehicles()

        // Make the next API request fail.
        api.shouldFail = true

        assertThrows(RuntimeException::class.java) {
            kotlinx.coroutines.runBlocking {
                repository.refreshVehicles()
            }
        }

        val result = repository.getVehicles().first()

        assertEquals(1, result.size)
        assertEquals("VIN123", result.first().vin)
    }

    @Test
    fun getVehicleByVinReturnsMatchingVehicle() = runTest {
        api.response = VehiclesDto(listings = listOf(sampleVehicleDto))
        repository.refreshVehicles()

        val result = repository.getVehicleById("VIN123").first()

        assertEquals("VIN123", result?.vin)
        assertEquals("Honda", result?.make)
    }

    private val sampleVehicleDto = VehicleDto(
        vin = "VIN123",
        year = 2020,
        make = "Honda",
        model = "Civic",
        trim = "EX",
        listPrice = 21999.0,
        mileage = 18750,
        exteriorColor = "Blue",
        interiorColor = "Black",
        engine = "4 Cyl",
        drivetype = "FWD",
        transmission = "CVT",
        bodytype = "Sedan",
        dealer = Dealer(
            city = "Austin",
            state = "TX",
            phone = "5125551234"
        ),
        images = Images(large = listOf("photo-url")),
        fuel = "Gasoline"
    )
}