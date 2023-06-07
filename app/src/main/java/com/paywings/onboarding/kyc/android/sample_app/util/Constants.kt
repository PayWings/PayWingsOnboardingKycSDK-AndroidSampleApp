package com.paywings.onboarding.kyc.android.sample_app.util

object Constants {

    //region SharedPreferences
    internal const val SHARED_PREFERENCES_FILE_NAME = "PayWingsOAuthSharedPreferences"
    internal const val SHARED_PREFERENCES_KEY_REFRESH_TOKEN = "RefreshToken"
    //endregion

    internal const val SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_URL = "sdkEndpointUrl"
    internal const val SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_USERNAME = "sdkEndpointUsername"
    internal const val SETTINGS_PREFERENCE_KEY_SDK_ENDPOINT_PASSWORD = "sdkEndpointPassword"
    internal const val SETTINGS_PREFERENCE_KEY_OAUTH_API_KEY = "oauthApiKey"
    internal const val SETTINGS_PREFERENCE_KEY_OAUTH_DOMAIN = "oauthDomain"
    internal const val SETTINGS_PREFERENCE_KEY_REFERENCE_NUMBER = "referenceNumber"


    internal const val DEFAULT_OAUTH_API_KEY = "a44c8894-d54f-4eb5-a8ce-276b7ec8eaed"
    internal const val DEFAULT_OAUTH_DOMAIN = "paywings.io"




    internal val DoNothing: Unit = Unit

}