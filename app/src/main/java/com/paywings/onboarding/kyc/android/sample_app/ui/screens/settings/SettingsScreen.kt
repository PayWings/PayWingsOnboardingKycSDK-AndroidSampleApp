package com.paywings.onboarding.kyc.android.sample_app.ui.screens.settings

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import com.jamal.composeprefs3.ui.GroupHeader
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.EditTextPref
import com.paywings.onboarding.kyc.android.sample_app.R
import com.paywings.onboarding.kyc.android.sample_app.dataStore
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.NavRoute
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.ScreenTopAppBarWithBackNavigation
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.settings.AppRestartRequiredDialog
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.scaffoldWindowInsets
import com.paywings.onboarding.kyc.android.sample_app.util.Constants
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

/**
 * Every screen has a route, so that we don't have to add the route setup of all screens in one file.
 *
 * Inherits NavRoute, to be able to navigate away from this screen. All navigation logic is in there.
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
object SettingsNav : NavRoute<SettingsViewModel> {

    override val route = "settings_screen"

    @Composable
    override fun viewModel(): SettingsViewModel = hiltViewModel()

    @Composable
    override fun Content(
        viewModel: SettingsViewModel,
        arguments: Bundle?,
        onCloseApp: () -> Unit
    ) = SettingsScreen(viewModel = viewModel, onCloseApp = onCloseApp)
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onCloseApp: () -> Unit) {

    val settings by LocalContext.current.dataStore.data.collectAsState(initial = null)

    val appRestartRequiredDialogState = rememberMaterialDialogState(initialValue = false)

    var showAppRestartDialog: Boolean by remember {
        mutableStateOf(false)
    }

    val navigateBack: () -> Unit = remember {
        return@remember viewModel::navigateUp
    }

    Scaffold(
        topBar = {
            ScreenTopAppBarWithBackNavigation(
                title = stringResource(id = R.string.sdk_settings),
                backNavigationEnabled = true,
                onBackNavigation = {
                    when (showAppRestartDialog) {
                        true -> appRestartRequiredDialogState.show()
                        false -> navigateBack()
                    }
                }
            )
        },
        contentWindowInsets = MaterialTheme.shapes.scaffoldWindowInsets
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
            PrefsScreen(dataStore = LocalContext.current.dataStore) {
                prefsGroup({
                    GroupHeader(
                        title = stringResource(id = R.string.sdk_endpoint),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }) {
                    prefsItem {
                        EditTextPref(
                            key = "sdkEndpointUrl",
                            title = stringResource(id = R.string.url_title),
                            dialogTitle = stringResource(id = R.string.url_title),
                            summary = settings?.get(stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_URL))?:""
                        )
                    }
                    prefsItem {
                        EditTextPref(
                            key = "sdkEndpointUsername",
                            title = stringResource(id = R.string.username_title),
                            dialogTitle = stringResource(id = R.string.username_title),
                            summary = settings?.get(stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_USERNAME))?:""
                        )
                    }
                    prefsItem {
                        EditTextPref(
                            key = "sdkEndpointPassword",
                            title = stringResource(id = R.string.password_title),
                            dialogTitle = stringResource(id = R.string.password_title),
                            summary = settings?.get(stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_PASSWORD))?:""
                        )
                    }
                }
                prefsGroup({
                    GroupHeader(
                        title = stringResource(id = R.string.oauth_data),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }) {
                    prefsItem {
                        EditTextPref(
                            key = Constants.SETTINGS_PREFERENCE_KEY_OAUTH_API_KEY,
                            title = stringResource(id = R.string.oauth_api_key),
                            dialogTitle = stringResource(id = R.string.oauth_api_key),
                            summary = settings?.get(stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_OAUTH_API_KEY))?:"",
                            onValueSaved = { showAppRestartDialog = true }
                        )
                    }
                    prefsItem {
                        EditTextPref(
                            key = Constants.SETTINGS_PREFERENCE_KEY_OAUTH_DOMAIN,
                            title = stringResource(id = R.string.oauth_domain),
                            dialogTitle = stringResource(id = R.string.oauth_domain),
                            summary = settings?.get(stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_OAUTH_DOMAIN))?:"",
                            onValueSaved = { showAppRestartDialog = true }
                        )
                    }
                }
                prefsGroup({
                    GroupHeader(
                        title = stringResource(id = R.string.user_data),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }) {
                    prefsItem {
                        EditTextPref(
                            key = "referenceNumber",
                            title = stringResource(id = R.string.reference_number),
                            dialogTitle = stringResource(id = R.string.reference_number),
                            summary = settings?.get(stringPreferencesKey(Constants.SETTINGS_PREFERENCE_KEY_REFERENCE_NUMBER))?:""
                        )
                    }
                }
            }
        }
    }

    AppRestartRequiredDialog(
        dialogState = appRestartRequiredDialogState,
        onClose = {
            viewModel.userSession.signOutUser()
            onCloseApp()
        }
    )

    BackHandler(enabled = true, onBack = {
        when (showAppRestartDialog) {
            true -> appRestartRequiredDialogState.show()
            false -> navigateBack()
        }
    })
}