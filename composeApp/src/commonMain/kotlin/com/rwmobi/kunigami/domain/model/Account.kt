/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class Account(
    val id: Int,
    val accountNumber: String,
    val fullAddress: String?,
    val movedInAt: Instant?,
    val movedOutAt: Instant?,
    val electricityMeterPoints: List<ElectricityMeterPoint>,
)
