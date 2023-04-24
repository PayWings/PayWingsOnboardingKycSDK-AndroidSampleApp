package com.paywings.onboarding.kyc.android.sample_app.ui.screens.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.paywings.onboarding.kyc.android.sdk.PayWingsOnboardingKycContract
import com.paywings.onboarding.kyc.android.sdk.data.model.KycContractData
import com.paywings.onboarding.kyc.android.sdk.data.model.KycCredentials
import com.paywings.onboarding.kyc.android.sdk.data.model.KycSettings
import com.paywings.onboarding.kyc.android.sdk.data.model.KycUserData
import com.paywings.onboarding.kyc.android.sdk.data.model.UserCredentials
import com.paywings.onboarding.kyc.android.sdk.util.PayWingsOnboardingKycResult
import java.util.UUID

@Composable
fun LaunchKycVerificationProcess(
    userFirstName: String?,
    userLastName: String?,
    userEmail: String?,
    userMobileNumber: String?,
    userAccessToken: String,
    userRefreshTone: String,
    kycApiUrl: String,
    kycApiUsername: String,
    kycApiPassword: String,
    referenceNumber: String,
    startKycProcess: Boolean,
    onSuccess: (result: PayWingsOnboardingKycResult.Success) -> Unit,
    onFailure: (result: PayWingsOnboardingKycResult.Failure) -> Unit,
    onError: (kycErrorStatusCode: Int, kycErrorStatusDescription: String) -> Unit,
    onShowKycReferenceNumberAlreadyUsedDialog: () -> Unit,
    onShowKycReferenceNumberInvalidDialog: () -> Unit,
    onShowKycEmailAlreadyUsedDialog: () -> Unit,
    onShowKycEmailNotMatchingReferenceNumberDialog: () -> Unit,
    onShowKycPhoneNumberNotMatchingReferenceNumberDialog: () -> Unit,
    onCancel: () -> Unit
) {
    val kycVerificationProcess = rememberLauncherForActivityResult(
        PayWingsOnboardingKycContract()
    ) { payWingsOnboardingKycResult ->
        when (payWingsOnboardingKycResult) {
            is PayWingsOnboardingKycResult.Success -> {
                onSuccess(payWingsOnboardingKycResult)
            }
            is PayWingsOnboardingKycResult.Failure -> {
                onFailure(payWingsOnboardingKycResult)
                when (payWingsOnboardingKycResult.statusCode) {
                    -10014 ->
                        //Verification was canceled by user
                        onCancel()
                    -10018 -> {
                        onShowKycPhoneNumberNotMatchingReferenceNumberDialog()
                    }
                    -10020 -> {
                        onShowKycReferenceNumberInvalidDialog()
                    }
                    -10021 -> {
                        onShowKycReferenceNumberAlreadyUsedDialog()
                    }
                    -10019 -> {
                        onShowKycEmailNotMatchingReferenceNumberDialog()
                    }
                    -30021 -> {
                        onShowKycEmailAlreadyUsedDialog()
                    }
                    else -> {
                        onError(payWingsOnboardingKycResult.statusCode, payWingsOnboardingKycResult.statusDescription ?: "")

                    }
                }
            }
        }
    }

    if (startKycProcess) {
        kycVerificationProcess.launch(
            KycContractData(
                KycCredentials(
                    kycApiUrl,
                    kycApiUsername,
                    kycApiPassword
                ),
                KycSettings(
                    UUID.randomUUID().toString(),
                    "en",
                    referenceNumber
                ),
                KycUserData(
                    firstName = userFirstName,
                    lastName = userLastName,
                    email = userEmail,
                    mobileNumber = userMobileNumber
                ),
                userCredentials = UserCredentials(
                    accessToken = userAccessToken,
                    refreshToken = userRefreshTone
                )
            )
        )
    }
}