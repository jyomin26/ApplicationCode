package com.android.domain

import com.android.domain.usecase.RefreshVehiclesUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class RefreshVehiclesUseCaseTest {
    @Test
    fun `returns vehicles from repository`() = runTest {
        val repository = FakeVehicleRepository()
         RefreshVehiclesUseCase(repository)()
        assertTrue(repository.refreshCalled)
    }
}

