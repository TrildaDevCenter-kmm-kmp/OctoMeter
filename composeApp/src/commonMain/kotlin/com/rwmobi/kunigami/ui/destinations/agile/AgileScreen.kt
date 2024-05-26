/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.extensions.getNextHalfHourCountdownMillis
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.ui.components.DemoModeCtaAdaptive
import com.rwmobi.kunigami.ui.components.IndicatorTextValueGridItem
import com.rwmobi.kunigami.ui.components.LargeTitleWithIcon
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.destinations.agile.components.CountDownWidget
import com.rwmobi.kunigami.ui.destinations.agile.components.TariffDetailsAdaptive
import com.rwmobi.kunigami.ui.destinations.agile.components.TariffSummaryCardAdaptive
import com.rwmobi.kunigami.ui.extensions.getPercentageColorIndex
import com.rwmobi.kunigami.ui.extensions.partitionList
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.HorizontalLineAnnotation
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_about_tariff
import kunigami.composeapp.generated.resources.agile_demo_introduction
import kunigami.composeapp.generated.resources.agile_different_tariff
import kunigami.composeapp.generated.resources.agile_unit_rate_details
import kunigami.composeapp.generated.resources.agile_vat_unit_rate
import kunigami.composeapp.generated.resources.provide_api_key
import kunigami.composeapp.generated.resources.revenue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AgileScreen(
    modifier: Modifier = Modifier,
    uiState: AgileUIState,
    uiEvent: AgileUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val dimension = LocalDensity.current.getDimension()
    val lazyListState = rememberLazyListState()
    val colorPalette = remember {
        generateGYRHueColorPalette(
            saturation = 0.6f,
            lightness = 0.6f,
        )
    }

    Box(modifier = modifier) {
        if (uiState.rateGroupedCells.isNotEmpty()) {
            ScrollbarMultiplatform(
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.rateGroupedCells.isNotEmpty(),
                lazyListState = lazyListState,
            ) { contentModifier ->
                LazyColumn(
                    modifier = contentModifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = dimension.grid_4),
                    state = lazyListState,
                ) {
                    if (uiState.isDemoMode == true) {
                        item {
                            DemoModeCtaAdaptive(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimension.grid_2),
                                description = stringResource(resource = Res.string.agile_demo_introduction),
                                ctaButtonLabel = stringResource(resource = Res.string.provide_api_key),
                                onCtaButtonClicked = uiEvent.onNavigateToAccountTab,
                                useWideLayout = uiState.requestedAdaptiveLayout != WindowWidthSizeClass.Compact,
                            )
                        }
                    }

                    uiState.barChartData?.let { barChartData ->
                        item {
                            BoxWithConstraints(
                                modifier = Modifier.padding(top = dimension.grid_1),
                            ) {
                                val constraintModifier = when (uiState.requestedChartLayout) {
                                    is RequestedChartLayout.Portrait -> {
                                        Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(4 / 3f)
                                    }

                                    is RequestedChartLayout.LandScape -> {
                                        Modifier
                                            .fillMaxWidth()
                                            .height(uiState.requestedChartLayout.requestedMaxHeight)
                                    }
                                }

                                VerticalBarChart(
                                    modifier = constraintModifier.padding(all = dimension.grid_2),
                                    entries = barChartData.verticalBarPlotEntries,
                                    yAxisRange = uiState.rateRange,
                                    yAxisTitle = stringResource(resource = Res.string.agile_vat_unit_rate),
                                    labelGenerator = { index ->
                                        barChartData.labels[index]
                                    },
                                    tooltipGenerator = { index ->
                                        barChartData.tooltips[index]
                                    },
                                    colorPalette = colorPalette,
                                    backgroundPlot = { graphScope ->
                                        if (uiState.isCurrentlyOnDifferentTariff() &&
                                            uiState.userProfile?.tariff != null
                                        ) {
                                            graphScope.HorizontalLineAnnotation(
                                                location = uiState.userProfile.tariff.vatInclusiveUnitRate,
                                                lineStyle = LineStyle(
                                                    brush = SolidColor(MaterialTheme.colorScheme.error),
                                                    strokeWidth = dimension.grid_0_5,
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f),
                                                    alpha = 0.5f,
                                                    colorFilter = null, // No color filter
                                                    blendMode = DrawScope.DefaultBlendMode,
                                                ),
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }

                    item(key = "tariffDetails") {
                        TariffDetailsAdaptive(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimension.grid_2),
                            windowWidthSizeClass = uiState.requestedAdaptiveLayout,
                            agileTariffBlock = if (uiState.agileTariff != null) {
                                { modifier ->
                                    TariffSummaryCardAdaptive(
                                        modifier = modifier.padding(vertical = dimension.grid_0_5),
                                        heading = stringResource(resource = Res.string.agile_about_tariff).uppercase(),
                                        tariff = uiState.agileTariff,
                                        layoutType = uiState.requestedAdaptiveLayout,
                                    )
                                }
                            } else {
                                null
                            },
                            countDownWidget = if (uiState.rateGroupedCells.isNotEmpty()) {
                                { modifier ->
                                    Row(
                                        modifier = modifier,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        CountDownWidget(
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .heightIn(max = 216.dp)
                                                .aspectRatio(1f),
                                            colorPalette = colorPalette,
                                            rateRange = uiState.rateRange,
                                            rateGroupedCells = uiState.rateGroupedCells,
                                        )
                                    }
                                }
                            } else {
                                null
                            },
                            currentTariffBlock = null,
                        )
                    }

                    if (uiState.isCurrentlyOnDifferentTariff() && uiState.userProfile?.tariff != null) {
                        item(key = "currentDifferentTariff") {
                            TariffSummaryCardAdaptive(
                                modifier = modifier.padding(
                                    horizontal = dimension.grid_3,
                                    vertical = dimension.grid_0_5,
                                ),
                                heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                                tariff = uiState.userProfile.tariff,
                                layoutType = uiState.requestedAdaptiveLayout,
                            )
                        }
                    }

                    if (uiState.rateGroupedCells.isNotEmpty()) {
                        item(key = "headingUnitRateDetails") {
                            LargeTitleWithIcon(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimension.grid_2),
                                icon = painterResource(resource = Res.drawable.revenue),
                                label = stringResource(resource = Res.string.agile_unit_rate_details),
                            )
                        }
                    }

                    uiState.rateGroupedCells.forEach { rateGroup ->
                        item(key = "${rateGroup.title}Title") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = dimension.grid_2,
                                        horizontal = dimension.grid_4,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    text = rateGroup.title,
                                )

                                Text(
                                    modifier = Modifier.wrapContentSize(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    text = stringResource(resource = Res.string.agile_vat_unit_rate),
                                )
                            }
                        }

                        // We can do fancier grouping, but for now evenly-distributed is ok
                        val partitionedItems = rateGroup.rates.partitionList(columns = uiState.requestedRateColumns)
                        val maxRows = partitionedItems.maxOf { it.size }

                        items(maxRows) { rowIndex ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = dimension.grid_4,
                                        vertical = dimension.grid_0_25,
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_3),
                            ) {
                                for (columnIndex in partitionedItems.indices) {
                                    val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
                                    if (item != null) {
                                        IndicatorTextValueGridItem(
                                            modifier = Modifier.weight(1f),
                                            indicatorColor = colorPalette[
                                                item.vatInclusivePrice.getPercentageColorIndex(
                                                    maxValue = uiState.rateRange.endInclusive,
                                                ),
                                            ],
                                            label = item.validFrom.toLocalHourMinuteString(),
                                            value = item.vatInclusivePrice.roundToTwoDecimalPlaces().toString(precision = 2),
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (uiState.isLoading) {
            LoadingScreen(
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            // no data
            Text("Placeholder for no data")
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()

        while (isActive) {
            val delayMillis = Clock.System.now().getNextHalfHourCountdownMillis()
            delay(timeMillis = delayMillis)
            uiEvent.onRefresh()
        }
    }

    LaunchedEffect(uiState.requestScrollToTop) {
        if (uiState.requestScrollToTop) {
            lazyListState.scrollToItem(index = 0)
            uiEvent.onScrolledToTop()
        }
    }
}
