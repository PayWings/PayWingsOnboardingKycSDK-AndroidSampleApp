package com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.progress

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.paywings.onboarding.kyc.android.sample_app.R
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.dialog
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState

@ExperimentalComposeUiApi
@Composable
fun ProcessingDialog(
    dialogState: MaterialDialogState,
    @StringRes descriptionResId: Int = R.string.progress_dialog_text
) {
    MaterialDialog(
        dialogState = dialogState,
        autoDismiss = false,
        shape = MaterialTheme.shapes.dialog,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            Text(text = stringResource(descriptionResId))
        }
    }
}