/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
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

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TagWithIcon(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
) {
    Row(
        modifier = modifier
            .background(
                color = AppTheme.colorScheme.secondaryContainer,
                shape = AppTheme.shapes.medium,
            )
            .padding(
                horizontal = AppTheme.dimens.grid_1,
                vertical = AppTheme.dimens.grid_0_5,
            ),
    ) {
        Icon(
            modifier = Modifier.size(size = 16.dp),
            contentDescription = null,
            tint = AppTheme.colorScheme.onSecondaryContainer,
            painter = icon,
        )
        Text(
            modifier = Modifier.padding(start = AppTheme.dimens.grid_1),
            color = AppTheme.colorScheme.onSecondaryContainer,
            style = AppTheme.typography.labelMedium,
            text = text.uppercase(),
        )
    }
}

@Preview
@Composable
private fun TagWithIconPreview() {
    CommonPreviewSetup {
        TagWithIcon(
            icon = painterResource(resource = ProductFeature.GREEN.iconResource),
            text = stringResource(resource = ProductFeature.GREEN.stringResource),
        )
    }
}
