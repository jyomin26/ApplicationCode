package com.android.domain

import com.android.domain.usecase.GetVehiclesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetVehiclesUseCaseTest {

    @Test
    fun `returns vehicles from repository`() = runTest {
        val repository = FakeVehicleRepository()
        val useCase = GetVehiclesUseCase(repository)

        val result = useCase().first()

        assertEquals(repository.vehicles, result)
    }
}