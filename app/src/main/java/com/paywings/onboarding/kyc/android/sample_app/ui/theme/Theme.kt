package com.paywings.onboarding.kyc.android.sample_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppDarkColorScheme = darkColorScheme(
    primary = Blue400,
    secondary = Blue700,
    tertiary = Blue200
)

private val AppLightColorScheme = lightColorScheme(
    primary = Blue700,
    secondary = Blue900,
    tertiary = Blue200
)


@Composable
fun PayWingsOnboardingKycTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        AppDarkColorScheme
    } else {
        AppLightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}