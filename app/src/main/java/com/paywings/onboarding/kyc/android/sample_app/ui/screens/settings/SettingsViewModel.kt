package com.paywings.onboarding.kyc.android.sample_app.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.paywings.onboarding.kyc.android.sample_app.network.NetworkState
import com.paywings.onboarding.kyc.android.sample_app.ui.nav.RouteNavigator
import com.paywings.onboarding.kyc.android.sample_app.util.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val networkState: NetworkState,
    val userSession: UserSession
) : ViewModel(), RouteNavigator by routeNavigator