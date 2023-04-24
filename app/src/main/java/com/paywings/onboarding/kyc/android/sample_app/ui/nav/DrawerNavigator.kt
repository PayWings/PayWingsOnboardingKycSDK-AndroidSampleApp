package com.paywings.onboarding.kyc.android.sample_app.ui.nav

import androidx.compose.ui.graphics.vector.ImageVector

interface DrawerNavigator {
    val icon: ImageVector
    val nameResId: Int
    val route: String
}