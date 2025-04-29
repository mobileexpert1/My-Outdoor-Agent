package com.myoutdoor.agent.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkBroadcastReceiver(private val onNetworkChange: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        onNetworkChange()
    }
}