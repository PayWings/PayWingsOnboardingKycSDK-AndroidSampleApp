package com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system

sealed class SystemDialogUiState {
    class ShowError(val errorMessage: String): SystemDialogUiState()
    object ShowNoInternetConnection: SystemDialogUiState()
}
