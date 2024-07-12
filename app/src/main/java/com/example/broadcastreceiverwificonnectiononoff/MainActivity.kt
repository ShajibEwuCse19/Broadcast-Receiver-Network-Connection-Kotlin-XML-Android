package com.example.broadcastreceiverwificonnectiononoff

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inflateLayout = findViewById<View>(R.id.networkError)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                Toast.makeText(this, "Network Connected", Toast.LENGTH_SHORT).show()
                inflateLayout.visibility = View.GONE
            }
            else {
                Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show()
                inflateLayout.visibility = View.VISIBLE
            }
        }
    }
}