package com.example.broadcastreceiverwificonnectiononoff

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private lateinit var inflateLayout: View
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkConnectionCallBack: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inflateLayout = findViewById(R.id.networkError)

        initConnectionCallBack()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(networkConnectionCallBack)
            }
            else -> {
                registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    private fun initConnectionCallBack() {
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
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val networkConnection: NetworkInfo? = connectivityManager.activeNetworkInfo
            postValue(networkConnection?.isConnected)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
        connectivityManager.unregisterNetworkCallback(networkConnectionCallBack)
    }

    private fun postValue(networkConnection: Boolean?) {
        Handler(Looper.getMainLooper()).post {
            inflateLayout.isVisible = !(networkConnection != null && networkConnection)
        }

        Log.d("TAGGG", "postValue: $networkConnection")
    }
}