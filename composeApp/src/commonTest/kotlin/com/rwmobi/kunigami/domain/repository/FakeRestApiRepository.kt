/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Consumption
import com.rwmobi.kunigami.domain.model.Product
import com.rwmobi.kunigami.domain.model.Rate
import com.rwmobi.kunigami.domain.model.Tariff
import kotlinx.datetime.Instant

class FakeRestApiRepository : RestApiRepository {

    var setSimpleProductTariffResponse: Result<Tariff>? = null
    override suspend fun getSimpleProductTariff(
        productCode: String,
        tariffCode: String,
    ): Result<Tariff> {
        return setSimpleProductTariffResponse ?: throw RuntimeException("Fake result not defined")
    }

    var setProductsResponse: Result<List<Product>>? = null
    override suspend fun getProducts(): Result<List<Product>> {
        return setProductsResponse ?: throw RuntimeException("Fake result not defined")
    }

    var setStandardUnitRatesResponse: Result<List<Rate>>? = null
    override suspend fun getStandardUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant?,
        periodTo: Instant?,
    ): Result<List<Rate>> {
        return setStandardUnitRatesResponse ?: throw RuntimeException("Fake result not defined")
    }

    var setStandingChargesResponse: Result<List<Rate>>? = null
    override suspend fun getStandingCharges(
        productCode: String,
        tariffCode: String,
    ): Result<List<Rate>> {
        return setStandingChargesResponse ?: throw RuntimeException("Fake result not defined")
    }

    var setDayUnitRatesResponse: Result<List<Rate>>? = null
    override suspend fun getDayUnitRates(
        productCode: String,
        tariffCode: String,
    ): Result<List<Rate>> {
        return setDayUnitRatesResponse ?: throw RuntimeException("Fake result not defined")
    }

    var setNightUnitRatesResponse: Result<List<Rate>>? = null
    override suspend fun getNightUnitRates(
        productCode: String,
        tariffCode: String,
    ): Result<List<Rate>> {
        return setNightUnitRatesResponse ?: throw RuntimeException("Fake result not defined")
    }

    var setConsumptionResponse: Result<List<Consumption>>? = null
    override suspend fun getConsumption(
        apiKey: String,
        mpan: String,
        meterSerialNumber: String,
        periodFrom: Instant?,
        periodTo: Instant?,
    ): Result<List<Consumption>> {
        return setConsumptionResponse ?: throw RuntimeException("Fake result not defined")
    }

    var setAccountResponse: Result<List<Account>>? = null
    override suspend fun getAccount(
        apiKey: String,
        accountNumber: String,
    ): Result<List<Account>> {
        return setAccountResponse ?: throw RuntimeException("Fake result not defined")
    }
}