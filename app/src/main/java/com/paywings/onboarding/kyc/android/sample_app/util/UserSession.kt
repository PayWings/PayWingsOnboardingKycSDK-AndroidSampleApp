package com.paywings.onboarding.kyc.android.sample_app.util

import com.paywings.onboarding.kyc.android.sample_app.data.repository.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(private val appRepository: AppRepository) {

    val refreshToken: String
        get() {
            return appRepository.refreshToken
        }
    var accessToken: String = ""
    var accessTokenExpirationTime: Long? = null

    val isUserSignIn: Boolean
        get() {
            return appRepository.refreshToken.isNotBlank()
        }

    fun signOutUser() {
        appRepository.setRefreshToken(refreshToken = "")
        accessToken = ""
        accessTokenExpirationTime = null
    }

    fun signInUser(refreshToken: String,
                   accessToken: String,
                   accessTokenExpirationTime: Long) {

        appRepository.setRefreshToken(refreshToken = refreshToken)
        this.accessToken = accessToken
        this.accessTokenExpirationTime = accessTokenExpirationTime
    }

    fun setNewAccessToken(accessToken: String,
                          accessTokenExpirationTime: Long) {
        this.accessToken = accessToken
        this.accessTokenExpirationTime = accessTokenExpirationTime
    }
}