package com.myoutdoor.agent

import android.app.Application
import android.content.IntentFilter
import android.graphics.Color
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.myoutdoor.agent.utils.BaseActivity

class MyApplication : Application(), NetworkStateListener {
    private val mListeners = mutableListOf<NetworkStateListener>()
    private lateinit var mReceiver: InternetConnectionBroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        mReceiver = InternetConnectionBroadcastReceiver()
        registerReceiver(mReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(mReceiver)
        unregisterNetworkStateListener(this@MyApplication)
    }

    override fun onNetworkStateChanged(connected: Boolean) {
        // Handle network state change here, e.g., show a toast message
        if (connected) {
//            Toast.makeText(this, "Network connected", Toast.LENGTH_SHORT).show()
        } else {
            showAlertDialog()
//            Toast.makeText(this, "Network disconnected", Toast.LENGTH_SHORT).show()
        }

        // Notify all registered listeners about the network state change
        synchronized(mListeners) {
            for (listener in mListeners) {
                listener.onNetworkStateChanged(connected)
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Internet error")
        builder.setMessage("Please check your network connection")
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.show()
        val negativeButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(Color.parseColor("#FFFF0400"))
        negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"))

        negativeButton.setOnClickListener {
//            finish()
//            dialog.dismiss()
        }
    }


    fun registerNetworkStateListener(listener: NetworkStateListener) {
        synchronized(mListeners) {
            mListeners.add(listener)
        }
    }

    fun unregisterNetworkStateListener(listener: NetworkStateListener) {
        synchronized(mListeners) {
            mListeners.remove(listener)
        }
    }




}