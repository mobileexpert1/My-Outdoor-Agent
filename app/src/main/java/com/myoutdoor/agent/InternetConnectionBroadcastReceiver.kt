package com.myoutdoor.agent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

@Suppress("DEPRECATION")
class InternetConnectionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        if (!isConnectedToNetwork(context)) {
            // Trigger the network state changed event for all registered listeners
            val app = context.applicationContext as MyApplication
            app.onNetworkStateChanged(false)
        } else {
            // Trigger the network state changed event for all registered listeners
            val app = context.applicationContext as MyApplication
            app.onNetworkStateChanged(true)
        }
    }

    private fun isConnectedToNetwork(context: Context?): Boolean {
        if (context == null) return false
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo?.isConnected ?: false
    }
}