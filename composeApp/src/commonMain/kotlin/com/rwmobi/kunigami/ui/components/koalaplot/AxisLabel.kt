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

package com.rwmobi.kunigami.ui.components.koalaplot

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.rwmobi.kunigami.ui.theme.AppTheme

@Composable
fun AxisLabel(
    modifier: Modifier = Modifier,
    label: String,
) {
    Text(
        modifier = modifier,
        color = AppTheme.colorScheme.onBackground,
        style = AppTheme.typography.labelMedium,
        overflow = TextOverflow.Visible,
        maxLines = 1,
        text = label,
    )
}
