/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.repository.FakeRestApiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetTariffRatesUseCaseTest {

    private lateinit var getTariffRatesUseCase: GetTariffRatesUseCase
    private lateinit var fakeRestApiRepository: FakeRestApiRepository

    @BeforeTest
    fun setupUseCase() {
        fakeRestApiRepository = FakeRestApiRepository()
        getTariffRatesUseCase = GetTariffRatesUseCase(
            restApiRepository = fakeRestApiRepository,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `invoke should return tariff successfully when valid data is provided`() = runTest {
        val productCode = "product_123"
        val tariffCode = "E-1R-AGILE-FLEX-22-11-25-A"
        val expectedTariffSummary = TariffSummary(
            productCode = "AGILE-FLEX-22-11-25",
            tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
            fullName = "Octopus 12M Fixed April 2024 v1",
            description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
            displayName = "Octopus 12M Fixed",
            vatInclusiveUnitRate = 99.257,
            vatInclusiveStandingCharge = 94.682,
            availability = Clock.System.now()..Instant.DISTANT_FUTURE,
            isVariable = true,
        )

        fakeRestApiRepository.setSimpleProductTariffResponse = Result.success(expectedTariffSummary)

        val result = getTariffRatesUseCase(productCode, tariffCode)

        assertTrue(result.isSuccess)
        assertEquals(expectedTariffSummary, result.getOrNull())
    }

    @Test
    fun `invoke should return failure result when repository call fails`() = runTest {
        val productCode = "sample_product_code"
        val tariffCode = "sample_tariff_code"
        val errorMessage = "API Error"

        fakeRestApiRepository.setSimpleProductTariffResponse = Result.failure(RuntimeException(errorMessage))

        val result = getTariffRatesUseCase(productCode, tariffCode)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
}
