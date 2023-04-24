package com.paywings.onboarding.kyc.android.sample_app.ui.nav.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.main.MainNav
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.settings.SettingsNav

const val MAIN_ROUTE = "main"

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.mainNavGraph(navHostController: NavHostController, onCloseApp: () -> Unit) {
    navigation(
        startDestination = MainNav.route,
        route = MAIN_ROUTE
    ) {
        MainNav.composable(builder = this, navHostController = navHostController, onCloseApp = onCloseApp)
        SettingsNav.composable(builder = this, navHostController = navHostController, onCloseApp = onCloseApp)
    }
}