package com.paywings.onboarding.kyc.android.sample_app.ui.screens.initialization

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import com.paywings.onboarding.kyc.android.sample_app.R
import com.paywings.onboarding.kyc.android.sample_app.dataStore
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.NavRoute
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.ErrorDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.NoInternetConnectionDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.SystemDialogUiState
import com.paywings.onboarding.kyc.android.sample_app.util.Constants
import com.paywings.onboarding.kyc.android.sample_app.util.consume
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch


/**
 * Every screen has a route, so that we don't have to add the route setup of all screens in one file.
 *
 * Inherits NavRoute, to be able to navigate away from this screen. All navigation logic is in there.
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
object InitializationNav : NavRoute<InitializationViewModel> {

    override val route = "initialization_screen"

    @Composable
    override fun viewModel(): InitializationViewModel = hiltViewModel()

    @Composable
    override fun Content(
        viewModel: InitializationViewModel,
        arguments: Bundle?,
        onCloseApp: () -> Unit
    ) = InitializationScreen(viewModel = viewModel, onCloseApp = onCloseApp)
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun InitializationScreen(viewModel: InitializationViewModel, onCloseApp: () -> Unit) {

    val settings by LocalContext.current.dataStore.data.collectAsState(initial = null)

    val noInternetConnectionDialogState = rememberMaterialDialogState(initialValue = false)
    val errorDialogState = rememberMaterialDialogState(initialValue = false)
    var errorMessage: String by remember { mutableStateOf("") }

    val uiState = viewModel.uiState

    val dataStore = LocalContext.current.dataStore

    uiState.systemDialogUiState?.consume {
        when (it) {
            is SystemDialogUiState.ShowNoInternetConnection -> noInternetConnectionDialogState.show()
            is SystemDialogUiState.ShowError -> {
                errorMessage = it.errorMessage
                errorDialogState.show()
            }
        }
    }

    LaunchedEffect(settings) {
        settings?.let {

        }

    }

    InitializationContent()

    NoInternetConnectionDialog(
        dialogState = noInternetConnectionDialogState,
        cancelButtonNameResId = R.string.button_exit,
        onRecheckInternetConnection = {
            noInternetConnectionDialogState.takeIf { it.showing }?.hide()
            viewModel.recheckInternetConnection()
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

    LaunchedEffect(settings) {
        settings?.let { settingPreferences ->
            val oauthApiKey = settingPreferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_OAUTH_API_KEY)]
            val oauthDomain = settingPreferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_OAUTH_DOMAIN)]

            if (settingPreferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_URL)].isNullOrBlank()) {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_URL)] =
                        "https://kyc-dev.paywings.io/mobile/"
                }
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_USERNAME)] =
                        "ubcUser"
                }
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_PASSWORD)] =
                        "ubcPass"
                }
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_REFERENCE_NUMBER)] =
                        ""
                }
            }

            if (oauthApiKey.isNullOrBlank()) {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_OAUTH_API_KEY)] =
                        Constants.DEFAULT_OAUTH_API_KEY
                }
            }
            if (oauthDomain.isNullOrBlank()) {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_OAUTH_DOMAIN)] =
                        Constants.DEFAULT_OAUTH_DOMAIN
                }
            }

            if (!oauthApiKey.isNullOrBlank() && !oauthDomain.isNullOrBlank()) {
                viewModel.initialization(
                    oauthApiKey = oauthApiKey,
                    oauthDomain = oauthDomain
                )
            }
        }
    }

    BackHandler(enabled = true, onBack = { onCloseApp() })
}

@Composable
fun InitializationContent() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (logoWithText, progressIndicatorWithDescription) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier
                .requiredSize(200.dp)
                .constrainAs(logoWithText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(progressIndicatorWithDescription) {
                    top.linkTo(logoWithText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            CircularProgressIndicator(strokeWidth = 2.dp)
            Text(text = stringResource(R.string.initialization_screen_progress_message))
        }
    }
}