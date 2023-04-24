package com.paywings.onboarding.kyc.android.sample_app.ui.screens.initialization

import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paywings.oauth.android.sdk.data.enums.EnvironmentType
import com.paywings.onboarding.kyc.android.sample_app.network.NetworkState
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.RouteNavigator
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.graph.MAIN_ROUTE
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.graph.OAUTH_ROUTE
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.SystemDialogUiState
import com.paywings.onboarding.kyc.android.sample_app.util.UserSession
import com.paywings.onboarding.kyc.android.sample_app.util.asOneTimeEvent
import com.paywings.oauth.android.sdk.data.enums.OAuthErrorCode
import com.paywings.oauth.android.sdk.initializer.PayWingsOAuthClient
import com.paywings.oauth.android.sdk.service.callback.GetNewAccessTokenCallback
import com.paywings.onboarding.kyc.android.sample_app.data.repository.AppRepository
import com.paywings.onboarding.kyc.android.sample_app.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltViewModel
class InitializationViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val routeNavigator: RouteNavigator,
    private val networkState: NetworkState,
    private val userSession: UserSession,
    private val appRepository: AppRepository
) : ViewModel(), RouteNavigator by routeNavigator {

    var uiState: InitializationUiState by mutableStateOf(value = InitializationUiState())

    fun initialization(oauthApiKey: String, oauthDomain: String) {
        PayWingsOAuthClient.init(context = applicationContext, environmentType = EnvironmentType.TEST, oauthApiKey, oauthDomain)
        checkUserSignIn()
    }

    private fun checkUserSignIn() {
        when(userSession.isUserSignIn){
            true -> checkAccessTokenValidity()
            false -> navigateToRoute(OAUTH_ROUTE)
        }
    }

    private fun checkAccessTokenValidity() {
        when (userSession.accessToken.isBlank() && ((userSession.accessTokenExpirationTime
            ?: 0) < TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))) {
            true -> getNewAccessToken()
            false -> navigateToRoute(MAIN_ROUTE)
        }
    }

    private fun getNewAccessToken() {
        Log.d("OAUTH", "refreshToken = ${userSession.refreshToken}")
        viewModelScope.launch {
            PayWingsOAuthClient.instance.getNewAccessToken(refreshToken = userSession.refreshToken, callback = getNewAccessTokenCallback)
        }
    }

    private val getNewAccessTokenCallback = object : GetNewAccessTokenCallback {
        override fun onError(error: OAuthErrorCode, errorMessage: String?) {
            when(error) {
                OAuthErrorCode.NO_INTERNET ->  uiState = uiState.updateState(systemDialogUiState = SystemDialogUiState.ShowNoInternetConnection.asOneTimeEvent())
                OAuthErrorCode.MISSING_REFRESH_TOKEN -> navigateToRoute(OAUTH_ROUTE)
                else -> uiState = uiState.updateState(systemDialogUiState = SystemDialogUiState.ShowError(errorMessage = error.description).asOneTimeEvent())
            }
        }

        override fun onNewAccessToken(accessToken: String, accessTokenExpirationTime: Long) {
            Log.d("OAUTH", "accessToken = $accessToken")
            userSession.setNewAccessToken(accessToken = accessToken, accessTokenExpirationTime = accessTokenExpirationTime)
            navigateToRoute(MAIN_ROUTE)

        }

        override fun onUserSignInRequired() {
            navigateToRoute(OAUTH_ROUTE)
        }
    }

    fun recheckInternetConnection() {
        when (networkState.isConnected) {
            true -> checkUserSignIn()
            false -> uiState =
                uiState.updateState(systemDialogUiState = SystemDialogUiState.ShowNoInternetConnection.asOneTimeEvent())
        }
    }
}

