package com.paywings.onboarding.kyc.android.sample_app.ui.screens.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.RouteNavigator
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.graph.OAUTH_ROUTE
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.SystemDialogUiState
import com.paywings.onboarding.kyc.android.sample_app.util.Constants.DoNothing
import com.paywings.onboarding.kyc.android.sample_app.util.UserSession
import com.paywings.onboarding.kyc.android.sample_app.util.asOneTimeEvent
import com.paywings.oauth.android.sdk.data.enums.OAuthErrorCode
import com.paywings.oauth.android.sdk.initializer.PayWingsOAuthClient
import com.paywings.oauth.android.sdk.service.callback.GetNewAccessTokenCallback
import com.paywings.oauth.android.sdk.service.callback.GetUserDataCallback
import com.paywings.onboarding.kyc.android.sdk.util.PayWingsOnboardingKycResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    val userSession: UserSession
): ViewModel(), RouteNavigator by routeNavigator  {

    var uiState: MainUiState by mutableStateOf(value = MainUiState())

    fun signOutUser() {
        userSession.signOutUser()
        navigateToRoute(OAUTH_ROUTE)
    }

    fun initialization() {
        getUserData()
    }

    fun startKyc() {
        uiState = uiState.updateState(isKycInProgress = true, startKyc = true.asOneTimeEvent())
    }

    fun kycSuccessResult(successResult: PayWingsOnboardingKycResult.Success) {
        uiState = uiState.updateState(
            kycStatus = "Successful",
            kycStatusColor = Color.Green,
            kycId = successResult.kycID,
            kycPersonId = successResult.personID,
            kycReferenceId = successResult.kycReferenceID?:"",
            kycReferenceNumber = successResult.referenceNumber
        )
    }

    fun kycResultFailure(failureResult: PayWingsOnboardingKycResult.Failure) {
        uiState = uiState.updateState(
            kycStatus = "Failed with status code: ${failureResult.statusCode} (${failureResult.statusDescription})",
            kycStatusColor = Color.Red,
            kycId = failureResult.kycID?:"",
            kycPersonId = failureResult.personID?:"",
            kycReferenceId = failureResult.kycReferenceID?:"",
            kycReferenceNumber = failureResult.referenceNumber?:""
        )
    }

    fun kycCanceled() {
        uiState = uiState.updateState(
            kycStatus = "Canceled by user",
            kycStatusColor = Color.Black,
            kycId = "",
            kycPersonId = "",
            kycReferenceId = "",
            kycReferenceNumber = ""
        )
    }

    fun kycError(kycErrorStatusCode: Int, kycErrorStatusDescription: String) {
        uiState = uiState.updateState(systemDialogUiState = SystemDialogUiState.ShowError(errorMessage = "$kycErrorStatusCode - $kycErrorStatusDescription").asOneTimeEvent())
    }

    private fun getUserData() {
        viewModelScope.launch {
            PayWingsOAuthClient.instance.getUserData(
                userSession.accessToken,
                callback = getUserDataCallback
            )
        }
    }

    private val getUserDataCallback = object : GetUserDataCallback {
        override fun onError(error: OAuthErrorCode, errorMessage: String?) {
            when(error) {
                OAuthErrorCode.MISSING_ACCESS_TOKEN -> when(userSession.isUserSignIn) {
                    true -> getNewAccessToken()
                    false -> navigateToRoute(OAUTH_ROUTE)
                }
                OAuthErrorCode.INVALID_ACCESS_TOKEN -> navigateToRoute(OAUTH_ROUTE)
                else -> DoNothing
            }
        }

        override fun onUserData(
            userId: String,
            firstName: String?,
            lastName: String?,
            email: String?,
            emailConfirmed: Boolean,
            phoneNumber: String?
        ) {
            uiState = uiState.updateState(userId = userId, firstName = firstName?:"", lastName = lastName?:"", email = email?:"", phoneNumber = phoneNumber?:"")
        }
    }

    private fun getNewAccessToken() {
        viewModelScope.launch {
            PayWingsOAuthClient.instance.getNewAccessToken( refreshToken = userSession.refreshToken, callback = getNewAccessTokenCallback)
        }
    }

    private val getNewAccessTokenCallback = object : GetNewAccessTokenCallback {
        override fun onError(error: OAuthErrorCode, errorMessage: String?) {
            when(error) {
                OAuthErrorCode.MISSING_REFRESH_TOKEN -> navigateToRoute(OAUTH_ROUTE)
                else -> uiState.updateState(systemDialogUiState = SystemDialogUiState.ShowError(errorMessage = error.description).asOneTimeEvent())
            }
        }

        override fun onNewAccessToken(accessToken: String, accessTokenExpirationTime: Long) {
            userSession.accessToken = accessToken
            userSession.accessTokenExpirationTime = accessTokenExpirationTime
        }

        override fun onUserSignInRequired() {
            navigateToRoute(OAUTH_ROUTE)
        }

    }

}