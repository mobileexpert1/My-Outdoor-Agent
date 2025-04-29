package com.myoutdoor.agent.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Base64
import android.util.Log
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import com.myoutdoor.agent.utils.BaseActivity
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SessionData
import com.myoutdoor.agent.utils.SharedPref
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class SplashActivity : BaseActivity() {

    lateinit var pref: SharedPref

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onLayoutCreated() {


        dateFormat()

        pref= SharedPref(this)

        Log.e("call","token "+ pref.getString(Constants.TOKEN))
        Handler().postDelayed({
            init()
         },2000)

        printHashKey(applicationContext)
    }


    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.getPackageManager()
                .getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
//                val hashKey: String = String(Base64.encode(md.digest(), 0))
                val hashKey: String = String(Base64.encode(md.digest(), 0))

                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }



    private fun init() {

      /*
      if (pref.getString(Constants.IS_FROM_LOGIN).isNotEmpty()) {
            val intent = Intent(this@SplashActivity, LoginSignUpActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        } else {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        }
     */


        //Change


        if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        }else {
            pref.setString(Constants.userAccountID,"0")
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
        }

/*
        if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
         }else {
            val intent = Intent(this@SplashActivity, LoginSignUpActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
         }*/

    }

    private fun dateFormat(){
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val dateInString = "2022-08-01T00:00:00"

        val formatterOut = SimpleDateFormat("MMM dd, yyyy")

        try {
            val date: Date = formatter.parse(dateInString)
            Log.e("call","date format:    "+date)
            Log.e("call","date format:    "+formatterOut.format(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

}