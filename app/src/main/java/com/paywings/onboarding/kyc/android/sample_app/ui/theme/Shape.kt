package com.paywings.onboarding.kyc.android.sample_app.ui.theme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

@get:Composable
val Shapes.dialog: RoundedCornerShape
    get() = RoundedCornerShape(16.dp)

@get:Composable
val Shapes.dialogWidthFraction: Float
    get() = 0.95f

@get:Composable
val Shapes.dialogEdgeDefaultPadding: Dp
    get() = 24.dp

@get:Composable
val Shapes.dialogTitleBodyDefaultPadding: Dp
    get() = 16.dp

@get:Composable
val Shapes.contentEdgePadding: Dp
    get() = 16.dp


@get:Composable
val Shapes.scaffoldWindowInsets: WindowInsets
    get() = WindowInsets(
        left = 16.dp,
        top = 16.dp,
        right = 16.dp,
        bottom = 16.dp
    )
