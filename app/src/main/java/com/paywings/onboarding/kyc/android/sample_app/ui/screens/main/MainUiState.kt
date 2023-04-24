package com.paywings.onboarding.kyc.android.sample_app.ui.screens.main

import androidx.compose.ui.graphics.Color
import com.paywings.onboarding.kyc.android.sample_app.ui.screens.dialogs.system.SystemDialogUiState
import com.paywings.onboarding.kyc.android.sample_app.util.OneTimeEvent

data class MainUiState(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val kycStatus: String? = null,
    val kycStatusColor: Color = Color.Black,
    val kycReferenceId: String = "",
    val kycReferenceNumber: String = "",
    val kycPersonId: String = "",
    val kycId: String = "",
    val isKycInProgress: Boolean = false,
    val startKyc: OneTimeEvent<Boolean>? = null,
    val systemDialogUiState: OneTimeEvent<SystemDialogUiState>? = null
)

fun MainUiState.updateState(
    userId: String = this.userId,
    firstName: String = this.firstName,
    lastName: String = this.lastName,
    email: String = this.email,
    phoneNumber: String = this.phoneNumber,
    kycStatus: String? = this.kycStatus,
    kycStatusColor: Color = this.kycStatusColor,
    kycReferenceId: String = this.kycReferenceId,
    kycReferenceNumber: String = this.kycReferenceNumber,
    kycPersonId: String = this.kycPersonId,
    kycId: String = this.kycId,
    isKycInProgress: Boolean = false,
    startKyc: OneTimeEvent<Boolean>? = null,
    systemDialogUiState: OneTimeEvent<SystemDialogUiState>? = null,
) : MainUiState {
    return MainUiState(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        kycStatus = kycStatus,
        kycStatusColor = kycStatusColor,
        kycReferenceId = kycReferenceId,
        kycReferenceNumber = kycReferenceNumber,
        kycPersonId = kycPersonId,
        kycId = kycId,
        isKycInProgress = isKycInProgress,
        startKyc = startKyc,
        systemDialogUiState = systemDialogUiState
    )
}

fun MainUiState.resetState(): MainUiState {
    return MainUiState()
}
