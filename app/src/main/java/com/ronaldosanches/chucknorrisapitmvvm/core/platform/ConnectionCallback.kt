package com.ronaldosanches.chucknorrisapitmvvm.core.platform

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.MutableLiveData
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkStatus.*
import javax.inject.Inject

class ConnectionCallback @Inject constructor() : ConnectivityManager.NetworkCallback() {

    val result = MutableLiveData<NetworkStatus>()

    var simpleResult = false

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        result.postValue(CONNECTED)
        simpleResult = true
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        result.postValue(DISCONNECTING)
        simpleResult = false
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        result.postValue(DISCONNECTED)
        simpleResult = false
    }

}