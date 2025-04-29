package com.myoutdoor.agent.activities.loginsignup

import android.content.Context
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.sociallogin.LoginFragment
import com.myoutdoor.agent.fragment.SignUpFragment
import com.myoutdoor.agent.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_login_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class LoginSignUpActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {
    public var googleApiClient: GoogleApiClient? = null
    override fun getLayoutId(): Int {
      return  R.layout.activity_login_sign_up
    }

    companion object{
        lateinit var loginSignUpActivity:LoginSignUpActivity
    }

    override fun onLayoutCreated() {

        loginSignUpActivity = this

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()


        if (googleApiClient == null || !googleApiClient!!.isConnected()) {
            try {
                googleApiClient = GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 1,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        addNewFragment(LoginFragment(),R.id.mainContainer,false)

        tvLogin.setOnClickListener {

            tvLogin.setBackgroundResource(R.drawable.login_register_shape_green)
            tvLogin.setTextColor(Color.parseColor("#FFFFFFFF"))
            tvSignup.setTextColor(Color.parseColor("#B1B1B1"))
            tvSignup.setBackgroundResource(R.color.light_white_green)
            addNewFragment(LoginFragment(),R.id.mainContainer,false)

            hideKeyboard()

        }

        tvSignup.setOnClickListener {
            tvSignup.setBackgroundResource(R.drawable.login_register_shape_green)
            tvLogin.setBackgroundResource(R.color.light_white_green)
            tvLogin.setTextColor(Color.parseColor("#B1B1B1"))
            tvSignup.setTextColor(Color.parseColor("#FFFFFFFF"))
            addNewFragment(SignUpFragment(),R.id.mainContainer,false)

            hideKeyboard()
        }


    }
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}