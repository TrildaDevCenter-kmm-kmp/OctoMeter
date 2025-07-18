/*
 * Copyright (c) 2024-2025. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.cyanish
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.p_kwh
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ReferenceTariffTile(
    modifier: Modifier = Modifier,
    tariff: Tariff,
    indicatorColor: Color,
) {
    val dimensGrid1 = AppTheme.dimens.grid_1
    Column(
        modifier = modifier
            .clip(shape = AppTheme.shapes.large)
            .background(AppTheme.colorScheme.surfaceContainer)
            .drawBehind {
                val stripeWidth = dimensGrid1.toPx()
                drawRect(
                    color = indicatorColor,
                    topLeft = Offset(x = size.width - stripeWidth, y = 0f),
                    size = Size(width = stripeWidth, height = size.height),
                )
            }
            .padding(AppTheme.dimens.grid_2),
        verticalArrangement = Arrangement.Top,
    ) {
        tariff.vatInclusiveStandardUnitRate?.let { unitRate ->
            Text(
                text = unitRate.roundToTwoDecimalPlaces().toString(),
                style = AppTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = stringResource(resource = Res.string.p_kwh),
                style = AppTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = tariff.displayName,
            style = AppTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = tariff.tariffCode,
            style = AppTheme.typography.labelSmall,
            color = AppTheme.colorScheme.onSurface.copy(
                alpha = 0.5f,
            ),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        ReferenceTariffTile(
            modifier = Modifier.fillMaxWidth(),
            tariff = TariffSamples.agileFlex221125,
            indicatorColor = cyanish,
        )
    }
}
