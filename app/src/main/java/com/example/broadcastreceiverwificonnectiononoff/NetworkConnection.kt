package com.example.broadcastreceiverwificonnectiononoff

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.LiveData

/**
 * @author Shajib
 * @since Jul 12, 2024
 **/
class NetworkConnection(private val context: Context) : LiveData<Boolean>() {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkConnectionCallBack: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateNetworkConnection()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(connectionCallBack())
            } else -> {
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkConnectionCallBack)
    }

    private fun connectionCallBack(): ConnectivityManager.NetworkCallback {
        networkConnectionCallBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }
        }
        return networkConnectionCallBack
    }

    private fun updateNetworkConnection() {
        val networkConnection: NetworkInfo? = connectivityManager.activeNetworkInfo
        /*if (networkConnection != null && networkConnection.isConnected) {
            postValue(true)
        } else {
            postValue(false)
        }*/
        postValue(networkConnection?.isConnected)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            updateNetworkConnection()
        }
    }

}