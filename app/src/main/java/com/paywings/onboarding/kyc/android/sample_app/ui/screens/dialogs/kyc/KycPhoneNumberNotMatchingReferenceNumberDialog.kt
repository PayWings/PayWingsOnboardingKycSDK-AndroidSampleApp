package com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.kyc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.paywings.onboarding.kyc.android.sample_app.R
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.ScreenTitle
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.components.SpacerDialogTitleBody
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.dialog
import com.paywings.onboarding.kyc.android.sample_app.ui.theme.dialogEdgeDefaultPadding
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Preview(showBackground = true)
@Composable
fun Preview_KycPhoneNumberNotMatchingReferenceNumberDialog() {
    val kycPhoneNumberNotMatchingReferenceNumberDialogState = rememberMaterialDialogState(initialValue = true)
    KycPhoneNumberNotMatchingReferenceNumberDialog(
        dialogState = kycPhoneNumberNotMatchingReferenceNumberDialogState,
    )
}

@Composable
fun KycPhoneNumberNotMatchingReferenceNumberDialog(
    dialogState: MaterialDialogState,
    onCloseButton: (() -> Unit)? = null,
) {
    val hideDialog: () -> Unit = remember {
        return@remember { dialogState.takeIf { it.showing }?.hide() }
    }

    MaterialDialog(
        dialogState = dialogState,
        autoDismiss = false,
        shape = MaterialTheme.shapes.dialog,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false),
        buttons = {
            negativeButton(res = R.string.button_close, onClick = { onCloseButton?:hideDialog })
        }
    ) {
        KycPhoneNumberNotMatchingReferenceNumberDialogContent()
    }
}

@Composable
private fun KycPhoneNumberNotMatchingReferenceNumberDialogContent(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(all = MaterialTheme.shapes.dialogEdgeDefaultPadding)) {
        ScreenTitle(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            stringResId = R.string.kyc_phone_number_not_matching_reference_number_dialog_title
        )
        SpacerDialogTitleBody()
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(id = R.string.kyc_phone_number_not_matching_reference_number_dialog_description)
        )
    }
}