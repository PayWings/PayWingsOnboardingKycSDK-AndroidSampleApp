package com.paywings.onboarding.kyc.android.sample_app.ui.screens.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import com.paywings.onboarding.kyc.android.sample_app.BuildConfig
import com.paywings.onboarding.kyc.android.sample_app.R
import com.paywings.onboarding.kyc.android.sample_app.dataStore
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.NavRoute
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.LaunchKycVerificationProcess
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.ProcessingButton
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.ScreenTitle
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.kyc.KycEmailAlreadyUsedDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.kyc.KycEmailNotMatchingReferenceNumberDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.kyc.KycPhoneNumberNotMatchingReferenceNumberDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.kyc.KycReferenceNumberAlreadyUsedDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.kyc.KycReferenceNumberInvalidDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.ErrorDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.NoInternetConnectionDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.SystemDialogUiState
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.settings.SettingsNav
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.scaffoldWindowInsets
import com.paywings.onboarding.kyc.android.sample_app.util.consume
import com.paywings.onboarding.kyc.android.sdk.util.PayWingsOnboardingKycResult
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


/**
 * Every screen has a route, so that we don't have to add the route setup of all screens in one file.
 *
 * Inherits NavRoute, to be able to navigate away from this screen. All navigation logic is in there.
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
object MainNav : NavRoute<MainViewModel> {

    override val route = "main_screen"

    @Composable
    override fun viewModel(): MainViewModel = hiltViewModel()

    @Composable
    override fun Content(
        viewModel: MainViewModel,
        arguments: Bundle?,
        onCloseApp: () -> Unit
    ) = MainScreen(viewModel = viewModel, onCloseApp = onCloseApp)
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun MainScreen(viewModel: MainViewModel, onCloseApp: () -> Unit) {

    val settings by LocalContext.current.dataStore.data.collectAsState(initial = null)

    val kycReferenceNumberInvalidDialogState = rememberMaterialDialogState(initialValue = false)
    val kycReferenceNumberAlreadyUsedDialogState = rememberMaterialDialogState(initialValue = false)
    val kycEmailAlreadyUsedDialogState = rememberMaterialDialogState(initialValue = false)
    val kycEmailNotMatchingReferenceNumberDialogState = rememberMaterialDialogState(initialValue = false)
    val kycPhoneNumberNotMatchingReferenceNumberDialogState = rememberMaterialDialogState(initialValue = false)
    val noInternetConnectionDialogState = rememberMaterialDialogState(initialValue = false)
    val errorDialogState = rememberMaterialDialogState(initialValue = false)
    var errorMessage: String by remember { mutableStateOf("") }

    val showKycReferenceNumberAlreadyUsedDialog: () -> Unit = remember { { kycReferenceNumberAlreadyUsedDialogState.show() } }
    val showKycReferenceNumberInvalidDialog: () -> Unit = remember { { kycReferenceNumberInvalidDialogState.show() } }
    val showKycEmailAlreadyUsedDialog: () -> Unit = remember { { kycEmailAlreadyUsedDialogState.show() } }
    val showKycEmailNotMatchingReferenceNumberDialog: () -> Unit = remember { { kycEmailNotMatchingReferenceNumberDialogState.show() } }
    val showKycPhoneNumberNotMatchingReferenceNumberDialog: () -> Unit = remember { { kycPhoneNumberNotMatchingReferenceNumberDialogState.show() } }

    val uiState = viewModel.uiState

    val onNavigateToScreen: (route: String) -> Unit = remember {
        return@remember viewModel::navigateToRoute
    }

    val onSignOut: () -> Unit = remember {
        return@remember viewModel::signOutUser
    }

    val onStartKyc: () -> Unit = remember {
        return@remember viewModel::startKyc
    }

    val onKycSuccessResult: (successResult: PayWingsOnboardingKycResult.Success) -> Unit = remember {
        return@remember viewModel::kycSuccessResult
    }

    val onKycResultFailure: (failureResult: PayWingsOnboardingKycResult.Failure) -> Unit = remember {
        return@remember viewModel::kycResultFailure
    }

    val onCancel: () -> Unit = remember {
        return@remember viewModel::kycCanceled
    }

    val onError: (kycErrorStatusCode: Int, kycErrorStatusDescription: String) -> Unit = remember {
        return@remember viewModel::kycError
    }


    LaunchKycVerificationProcess(
        userFirstName = uiState.firstName,
        userLastName = uiState.lastName,
        userEmail = uiState.email ,
        userMobileNumber = uiState.phoneNumber,
        userAccessToken = viewModel.userSession.accessToken,
        userRefreshTone = viewModel.userSession.refreshToken,
        kycApiUrl = settings?.get(stringPreferencesKey("sdkEndpointUrl"))?:"",
        kycApiUsername = settings?.get(stringPreferencesKey("sdkEndpointUsername"))?:"",
        kycApiPassword = settings?.get(stringPreferencesKey("sdkEndpointPassword"))?:"",
        referenceNumber = settings?.get(stringPreferencesKey("referenceNumber"))?:"",
        startKycProcess = uiState.startKyc?.getValue()?:false,
        onSuccess = onKycSuccessResult,
        onFailure = onKycResultFailure,
        onError = onError,
        onShowKycReferenceNumberAlreadyUsedDialog = showKycReferenceNumberAlreadyUsedDialog,
        onShowKycReferenceNumberInvalidDialog = showKycReferenceNumberInvalidDialog,
        onShowKycEmailAlreadyUsedDialog = showKycEmailAlreadyUsedDialog,
        onShowKycEmailNotMatchingReferenceNumberDialog = showKycEmailNotMatchingReferenceNumberDialog,
        onShowKycPhoneNumberNotMatchingReferenceNumberDialog = showKycPhoneNumberNotMatchingReferenceNumberDialog,
        onCancel = onCancel
    )

    uiState.systemDialogUiState?.consume {
        when (it) {
            is SystemDialogUiState.ShowNoInternetConnection -> noInternetConnectionDialogState.show()
            is SystemDialogUiState.ShowError -> {
                errorMessage = it.errorMessage
                errorDialogState.show()
            }
        }
    }

    uiState.systemDialogUiState?.consume {
        when (it) {
            is SystemDialogUiState.ShowNoInternetConnection -> noInternetConnectionDialogState.show()
            is SystemDialogUiState.ShowError -> {
                errorMessage = it.errorMessage
                errorDialogState.show()
            }
        }
    }

    MainContent(
        userId = uiState.userId,
        firstName = uiState.firstName,
        lastName = uiState.lastName,
        emailAddress = uiState.email,
        phoneNumber = uiState.phoneNumber,
        kycStatus = uiState.kycStatus,
        kycStatusColor = uiState.kycStatusColor,
        kycReferenceId = uiState.kycReferenceId,
        kycReferenceNumber = uiState.kycReferenceNumber,
        kycPersonId = uiState.kycPersonId,
        kycId = uiState.kycId,
        isStartKyc = uiState.isKycInProgress,
        onSignOut = onSignOut,
        onStartKyc = onStartKyc,
        onShowSettings = { onNavigateToScreen(SettingsNav.route) }
    )

    KycReferenceNumberInvalidDialog(
        dialogState = kycReferenceNumberInvalidDialogState
    )

    KycReferenceNumberAlreadyUsedDialog(
        dialogState = kycReferenceNumberAlreadyUsedDialogState
    )

    KycEmailAlreadyUsedDialog(
        dialogState = kycEmailAlreadyUsedDialogState
    )

    KycEmailNotMatchingReferenceNumberDialog(
        dialogState = kycEmailNotMatchingReferenceNumberDialogState
    )

    KycPhoneNumberNotMatchingReferenceNumberDialog(
        dialogState = kycPhoneNumberNotMatchingReferenceNumberDialogState
    )


    NoInternetConnectionDialog(
        dialogState = noInternetConnectionDialogState,
        cancelButtonNameResId = R.string.button_exit,
        onRecheckInternetConnection = {
            noInternetConnectionDialogState.takeIf { it.showing }?.hide()
        },
        onCancel = {
            noInternetConnectionDialogState.takeIf { it.showing }?.hide()
            onCloseApp()
        }
    )

    ErrorDialog(
        dialogState = errorDialogState,
        detailedMessage = errorMessage,
        onCancel = {
            errorDialogState.takeIf { it.showing }?.hide()
            onCloseApp()
        }
    )

    LaunchedEffect(Unit) {
        viewModel.initialization()
    }

    BackHandler(enabled = true, onBack = onCloseApp)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    userId: String,
    firstName: String,
    lastName: String,
    emailAddress: String,
    phoneNumber: String,
    kycStatus: String?,
    kycStatusColor: Color,
    kycReferenceId: String,
    kycReferenceNumber: String,
    kycPersonId: String,
    kycId: String,
    isStartKyc: Boolean,
    onSignOut: () -> Unit,
    onStartKyc: () -> Unit,
    onShowSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(enabled = !isStartKyc, onClick = onShowSettings) {
                        Icon(
                            imageVector = Icons.Outlined.ManageAccounts,
                            contentDescription = Icons.Outlined.ManageAccounts.name
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(titleContentColor = MaterialTheme.colorScheme.primary, navigationIconContentColor = MaterialTheme.colorScheme.primary, actionIconContentColor = MaterialTheme.colorScheme.primary)
            )
        },
        contentWindowInsets = MaterialTheme.shapes.scaffoldWindowInsets
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues = paddingValues).verticalScroll(rememberScrollState())) {
            Text(modifier = Modifier.align(alignment = Alignment.End), text = stringResource(id = R.string.main_screen_app_version, BuildConfig.VERSION_NAME))
            Spacer(Modifier.height(24.dp))
            Card {
                Column(modifier = Modifier.padding(all = 8.dp).fillMaxWidth()) {
                    ScreenTitle(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        title = stringResource(id = R.string.main_screen_user_data_title)
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(text = stringResource(id = R.string.main_screen_user_data_user_id, userId))
                    Spacer(Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.main_screen_user_data_first_name, firstName))
                    Spacer(Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.main_screen_user_data_last_name, lastName))
                    Spacer(Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.main_screen_user_data_email, emailAddress))
                    Spacer(Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.main_screen_user_data_phone_number, phoneNumber))
                    Spacer(Modifier.height(24.dp))
                    Button(modifier = Modifier.align(Alignment.End), onClick = { onSignOut() }) {
                        Text(text = stringResource(id = R.string.button_sign_out))
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            ProcessingButton(
                textResId = R.string.main_screen_start_kyc,
                isLoading = isStartKyc,
                isEnabled = true,
                onClick = onStartKyc
            )
            Spacer(Modifier.height(24.dp))
            if (!kycStatus.isNullOrEmpty()) {
                Card {
                    Column(modifier = Modifier.padding(all = 8.dp).fillMaxWidth()) {
                        ScreenTitle(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            title = stringResource(id = R.string.main_screen_kyc_result_title)
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(text = stringResource(id = R.string.main_screen_kyc_result_status, kycStatus), color = kycStatusColor)
                        Spacer(Modifier.height(8.dp))
                        Text(text = stringResource(id = R.string.main_screen_kyc_result_reference_id, kycReferenceId))
                        Spacer(Modifier.height(8.dp))
                        Text(text = stringResource(id = R.string.main_screen_kyc_result_reference_number, kycReferenceNumber))
                        Spacer(Modifier.height(8.dp))
                        Text(text = stringResource(id = R.string.main_screen_kyc_result_person_id, kycPersonId))
                        Spacer(Modifier.height(8.dp))
                        Text(text = stringResource(id = R.string.main_screen_kyc_result_kyc_id, kycId))
                    }
                }
            }
        }
    }
}

