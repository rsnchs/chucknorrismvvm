package com.ronaldosanches.chucknorrisapitmvvm.core.platform

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import javax.inject.Inject


class DeviceNetworkSettings @Inject constructor() {
    fun networkRequest() : NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
            .build()
    }
}