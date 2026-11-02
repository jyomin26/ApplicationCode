package com.android.domain

import com.android.domain.usecase.GetVehicleByIdUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetVehicleByIdUseCaseTest {

    @Test
    fun `returns vehicle by id from repository`() = runTest {
        val repository = FakeVehicleRepository()
        val useCase = GetVehicleByIdUseCase(repository)
        val result = useCase("TEST123").first()

        assertEquals(
            repository.vehicles.find {
                it.vin == "TEST123"
            },
            result
        )
    }
}