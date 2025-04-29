package com.myoutdoor.agent

interface NetworkStateListener {
    fun onNetworkStateChanged(connected: Boolean)
}