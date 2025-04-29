package com.myoutdoor.agent.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import kotlinx.android.synthetic.main.guest_user_alert_pop_up.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    lateinit var progressBarPB: CircularProgressDialog
    var doubleBackToExitPressedOnce = false
    var showDialogBox1: Dialog? = null

    /*  private val networkReceiver = NetworkBroadcastReceiver {
          showAlertDialog()
      }*/
    private lateinit var mApp: MyApplication


    companion object {

      lateinit var baseActivity: BaseActivity
    }


    abstract fun getLayoutId(): Int

    abstract fun onLayoutCreated()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        baseActivity = this
        onLayoutCreated()
        showDialogBox1 = Dialog(this)
        progressBarPB = CircularProgressDialog(this)
//        FirebaseApp.initializeApp(this)
        getKeyHash()

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        if (!Utils.isNetworkConnected(this)) {
            showAlertDialog()
        }
    }

    override fun onResume() {
        super.onResume()
//        registerReceiver(
//            networkReceiver,
//            IntentFilter(Constants.ACTION_CHECK_NETWORK)
//        )
    }

    override fun onPause() {
        super.onPause()
//        unregisterReceiver(networkReceiver)
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
            finish()
        }
    }

    fun addNewFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = this.supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.add(container, fragment, tag).commitAllowingStateLoss()
    }

    fun replaceFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = this.supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.replace(container, fragment, tag).commit()
    }

    fun getKeyHash() {
        try {
            val info = packageManager.getPackageInfo(
                "com.e.snapreader",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    public fun guestAlertMessage() {
        showDialogBox1!!.setContentView(R.layout.guest_user_alert_pop_up)
        showDialogBox1!!.setCanceledOnTouchOutside(false)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        showDialogBox1!!.tvGuestLogin.setOnClickListener {
            val intent = Intent(this, LoginSignUpActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        }

        showDialogBox1!!.tvCancelGuest.setOnClickListener {
            showDialogBox1!!.dismiss()
        }

        showDialogBox1!!.show()
    }
}

/*
@SuppressLint
abstract class BaseActivity : AppCompatActivity() {
    lateinit var progressBarPB: CircularProgressDialog
    var doubleBackToExitPressedOnce = false
    var showDialogBox1: Dialog? = null

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            showAlertDialog()
        }
    }

    abstract fun getLayoutId(): Int

    abstract fun onLayoutCreated()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        onLayoutCreated()
        showDialogBox1 = Dialog(this)
        progressBarPB = CircularProgressDialog(this)
//        FirebaseApp.initializeApp(this)
        getKeyHash()

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        if (!Utils.isNetworkConnected(this)) {
            showAlertDialog()
        }

    }


    override fun onResume() {
        super.onResume()
        registerReceiver(
            broadcastReceiver,
            IntentFilter(Constants.ACTION_CHECK_NETWORK)
        )
    }

    override fun onPause() {
        super.onPause()
       unregisterReceiver(broadcastReceiver)
    }

    private fun showAlertDialog() {
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        alertDialogBuilder.setTitle("Internet error")
//        alertDialogBuilder.setMessage("Please check your network connection")z
//        alertDialogBuilder.setNegativeButton("Cancel") { dialogInterface, i ->
//            dialogInterface.dismiss()
////            if (!Utils.isNetworkConnected(this)) {
////                showAlertDialog()
////            } else {
////                onStart()
////            }
//        }
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.show()

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

            finish()

        }

    }


    fun addNewFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = this.supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.add(container, fragment, tag).commitAllowingStateLoss()

    }

    fun replaceFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        val fragmentmanager = this.supportFragmentManager
        val transaction = fragmentmanager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.replace(container, fragment, tag).commit()
    }

    fun getKeyHash() {

        try {
            val info = packageManager.getPackageInfo(
                "com.e.snapreader",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }


    public fun guestAlertMessage() {

        showDialogBox1!!.setContentView(R.layout.guest_user_alert_pop_up)
        showDialogBox1!!.setCanceledOnTouchOutside(false)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        showDialogBox1!!.tvGuestLogin.setOnClickListener {
            val intent = Intent(this, LoginSignUpActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        }

        showDialogBox1!!.tvCancelGuest.setOnClickListener {
            showDialogBox1!!.dismiss()
        }

        showDialogBox1!!.show()
    }



//
//    override fun onBackPressed() {
//
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//
//        doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
//    }

}*/
