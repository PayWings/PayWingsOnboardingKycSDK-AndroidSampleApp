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


    internal const val DEFAULT_OAUTH_API_KEY = "ba337ff0-925a-46be-8c16-1cca65a30a1b"
    internal const val DEFAULT_OAUTH_DOMAIN = "paywings.io"




    internal val DoNothing: Unit = Unit

}