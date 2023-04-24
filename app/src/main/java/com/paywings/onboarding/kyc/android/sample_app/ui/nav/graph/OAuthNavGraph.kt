package com.paywings.onboarding.kyc.android.sample_app.ui.nav.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.email_change.ChangeUnverifiedEmailNav
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.email_verification.EmailVerificationRequiredNav
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.signin.SignInOtpVerificationNav
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.signin.SignInRequestOtpNav
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.user_registration.UserRegistrationNav

const val OAUTH_ROUTE = "oauth"

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.oauthNavGraph(navHostController: NavHostController, onCloseApp: () -> Unit) {
    navigation(
        startDestination = SignInRequestOtpNav.route,
        route = OAUTH_ROUTE
    ) {
        SignInRequestOtpNav.composable(builder = this, navHostController = navHostController, onCloseApp = onCloseApp)
        SignInOtpVerificationNav.composable(builder = this, navHostController = navHostController, onCloseApp = onCloseApp)
        UserRegistrationNav.composable(builder = this, navHostController = navHostController, onCloseApp = onCloseApp)
        EmailVerificationRequiredNav.composable(builder = this, navHostController = navHostController, onCloseApp = onCloseApp)
        ChangeUnverifiedEmailNav.composable(builder = this, navHostController = navHostController, onCloseApp = onCloseApp)
        mainNavGraph(navHostController = navHostController, onCloseApp = onCloseApp)
    }
}