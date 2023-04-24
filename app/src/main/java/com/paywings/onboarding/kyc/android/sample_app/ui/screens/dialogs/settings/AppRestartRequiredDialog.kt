package com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.paywings.onboarding.kyc.android.sample_app.R
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.ScreenTitle
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.SpacerDialogTitleBody
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.dialog
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.dialogEdgeDefaultPadding
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState

@ExperimentalComposeUiApi
@Composable
fun AppRestartRequiredDialog(
    dialogState: MaterialDialogState,
    onClose: () -> Unit
) {
    MaterialDialog(
        dialogState = dialogState,
        autoDismiss = false,
        shape = MaterialTheme.shapes.dialog,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false),
        buttons = {
            negativeButton(res = R.string.button_exit, onClick = onClose)
        }
    ) {
        AppRestartRequiredDialogContent()
    }
}

@Composable
private fun AppRestartRequiredDialogContent(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(all = MaterialTheme.shapes.dialogEdgeDefaultPadding)) {
        ScreenTitle(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            stringResId = R.string.app_restart_required_dialog_title
        )
        SpacerDialogTitleBody()
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(id = R.string.app_restart_required_dialog_description)
        )
    }
}