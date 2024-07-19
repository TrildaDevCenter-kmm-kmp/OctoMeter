/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFilteredProductsUseCase(
    private val octopusApiRepository: OctopusApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<List<ProductSummary>> {
        return withContext(dispatcher) {
            val getProductsResult = octopusApiRepository.getProducts(postcode = "WC1X 0ND")
            getProductsResult.fold(
                onSuccess = {
                    getProductsResult.mapCatching { products ->
                        products.filter {
                            it.direction == ProductDirection.IMPORT &&
                                it.brand == "OCTOPUS_ENERGY" &&
                                !it.features.contains(ProductFeature.BUSINESS) &&
                                !it.features.contains(ProductFeature.RESTRICTED)
                        }
                    }
                },
                onFailure = {
                    getProductsResult
                },
            )
        }
    }
}
