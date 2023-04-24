package com.paywings.onboarding.kyc.android.sample_app.network

import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.paywings.onboarding.kyc.android.sample_app.network.NetworkStateHolder

class NetworkStateHolderImp : NetworkStateHolder {
    override var isConnected: Boolean = false
    override var network: Network? = null
    override var linkProperties: LinkProperties? = null
    override var networkCapabilities: NetworkCapabilities? = null
}