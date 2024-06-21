/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto.prices

import kotlinx.serialization.Serializable

@Serializable
data class PricesApiResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<RateDto>,
) {
    fun getNextPageNumber(): Int? {
        val regex = Regex("page=(\\d+)")
        return next?.let {
            regex.find(it)?.groups?.get(1)?.value?.toIntOrNull()
        }
    }
}
