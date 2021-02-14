package com.ronaldosanches.chucknorrisapitmvvm.core.platform

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import javax.inject.Inject

class NetworkInfo @Inject constructor(
        context: Context,
        private val factory: DeviceNetworkSettings,
        private val callback : ConnectionCallback
) {

    private val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE)
            as ConnectivityManager

    val result : LiveData<NetworkStatus>
        get() = callback.result

    fun registerCallback() {
        val request = factory.networkRequest()
        connectivityManager.registerNetworkCallback(request,callback)
    }

    fun unregisterCallback() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    fun isConnected() : Boolean {
        return callback.simpleResult
    }
}