/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.MessageActionScreen
import com.rwmobi.kunigami.ui.destinations.account.components.AppInfoFooter
import com.rwmobi.kunigami.ui.destinations.account.components.ClearCredentialSectionAdaptive
import com.rwmobi.kunigami.ui.destinations.account.components.ElectricityMeterPointCard
import com.rwmobi.kunigami.ui.destinations.account.components.SimpleTitleButtonCard
import com.rwmobi.kunigami.ui.destinations.account.components.UpdateApiKeyDialog
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_cache
import kunigami.composeapp.generated.resources.account_clear_credential_button_cta
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.account_error_account_empty
import kunigami.composeapp.generated.resources.account_moved_in
import kunigami.composeapp.generated.resources.account_moved_out
import kunigami.composeapp.generated.resources.account_unknown_installation_address
import kunigami.composeapp.generated.resources.account_update_api_key
import kunigami.composeapp.generated.resources.database_remove_outline
import kunigami.composeapp.generated.resources.key
import kunigami.composeapp.generated.resources.retry
import kunigami.composeapp.generated.resources.unlink
import kunigami.composeapp.generated.resources.update
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountInformationScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUIState,
    uiEvent: AccountUIEvent,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_3),
    ) {
        if (uiState.userProfile?.account == null) {
            MessageActionScreen(
                modifier = modifier.fillMaxSize(),
                text = stringResource(resource = Res.string.account_error_account_empty),
                icon = painterResource(resource = Res.drawable.unlink),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = uiEvent.onRefresh,
                secondaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onSecondaryButtonClicked = uiEvent.onClearCredentialButtonClicked,
            )
        } else {
            Spacer(modifier = Modifier.height(height = dimension.grid_2))

            uiState.userProfile.account.fullAddress?.let {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = it,
                )
            } ?: run {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = stringResource(resource = Res.string.account_unknown_installation_address),
                )
            }

            uiState.userProfile.account.movedInAt?.let {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(resource = Res.string.account_moved_in, it.getLocalDateString()),
                )
            }

            uiState.userProfile.account.movedOutAt?.let {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(resource = Res.string.account_moved_out, it.getLocalDateString()),
                )
            }

            uiState.userProfile.account.electricityMeterPoints.forEach { meterPoint ->
                ElectricityMeterPointCard(
                    modifier = Modifier.fillMaxWidth(),
                    selectedMpan = uiState.userProfile.selectedMpan,
                    selectedMeterSerialNumber = uiState.userProfile.selectedMeterSerialNumber,
                    meterPoint = meterPoint,
                    requestedLayout = uiState.requestedLayout,
                    onMeterSerialNumberSelected = uiEvent.onMeterSerialNumberSelected,
                    onReloadTariff = uiEvent.onRefresh,
                )
            }
        }

        var isUpdateAPIKeyDialogOpened by rememberSaveable { mutableStateOf(false) }
        SimpleTitleButtonCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(resource = Res.string.account_update_api_key),
            buttonLabel = stringResource(resource = Res.string.update),
            buttonPainter = painterResource(resource = Res.drawable.key),
            onButtonClicked = { isUpdateAPIKeyDialogOpened = true },
        )

        if (isUpdateAPIKeyDialogOpened && uiState.userProfile?.account?.accountNumber != null) {
            UpdateApiKeyDialog(
                initialValue = "",
                onDismiss = { isUpdateAPIKeyDialogOpened = false },
                onUpdateAPIKey = { newKey ->
                    uiEvent.onSubmitCredentials(newKey, uiState.userProfile.account.accountNumber)
                    isUpdateAPIKeyDialogOpened = false
                },
            )
        }

        SimpleTitleButtonCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(resource = Res.string.account_clear_cache),
            buttonLabel = stringResource(resource = Res.string.account_clear_credential_button_cta),
            buttonPainter = painterResource(resource = Res.drawable.database_remove_outline),
            onButtonClicked = uiEvent.onClearCache,
        )

        ClearCredentialSectionAdaptive(
            modifier = Modifier.fillMaxWidth(),
            onClearCredentialButtonClicked = uiEvent.onClearCredentialButtonClicked,
            useWideLayout = uiState.requestedLayout != AccountScreenLayoutStyle.Compact,
        )

        AppInfoFooter(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            AccountInformationScreen(
                modifier = Modifier.padding(all = 32.dp),
                uiState = AccountUIState(
                    isLoading = false,
                    requestedScreenType = AccountScreenType.Account,
                    requestedLayout = AccountScreenLayoutStyle.WideWrapped,
                    userProfile = UserProfile(
                        selectedMpan = "1200000345678",
                        selectedMeterSerialNumber = "11A1234567",
                        account = AccountSamples.accountTwoElectricityMeterPoint,
                    ),
                    errorMessages = listOf(),
                ),
                uiEvent = AccountUIEvent(
                    onClearCredentialButtonClicked = {},
                    onSubmitCredentials = { _, _ -> },
                    onRefresh = {},
                    onMeterSerialNumberSelected = { _, _ -> },
                    onErrorShown = {},
                    onClearCache = {},
                    onScrolledToTop = {},
                    onShowSnackbar = {},
                    onSpecialErrorScreenShown = {},
                ),
            )
        }
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    CommonPreviewSetup {
        AccountInformationScreen(
            modifier = Modifier.padding(all = 32.dp),
            uiState = AccountUIState(
                isLoading = false,
                requestedScreenType = AccountScreenType.Error(specialErrorScreen = SpecialErrorScreen.NetworkError),
                requestedLayout = AccountScreenLayoutStyle.WideWrapped,
                userProfile = UserProfile(
                    selectedMpan = "1200000345678",
                    selectedMeterSerialNumber = "11A1234567",
                    account = AccountSamples.account928,
                ),
                errorMessages = listOf(),
            ),
            uiEvent = AccountUIEvent(
                onClearCredentialButtonClicked = {},
                onSubmitCredentials = { _, _ -> },
                onRefresh = {},
                onMeterSerialNumberSelected = { _, _ -> },
                onErrorShown = {},
                onClearCache = {},
                onScrolledToTop = {},
                onShowSnackbar = {},
                onSpecialErrorScreenShown = {},
            ),
        )
    }
}
