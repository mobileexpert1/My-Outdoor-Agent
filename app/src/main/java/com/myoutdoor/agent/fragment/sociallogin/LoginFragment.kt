package com.myoutdoor.agent.fragment.sociallogin

import android.app.Activity.RESULT_CANCELED
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity.Companion.loginSignUpActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpViewModel
import com.myoutdoor.agent.fragment.forgotpassword.ForgotPasswordFragment
import com.myoutdoor.agent.models.register.LoginBody
import com.myoutdoor.agent.models.sociallogin.SocailLoginBody
import com.myoutdoor.agent.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject


class LoginFragment : BaseFragment(), GoogleApiClient.OnConnectionFailedListener {

    lateinit var viewModel: LoginSignUpViewModel
    lateinit var pref: SharedPref
    lateinit var spref: ShrdPreferences
    var showDialogBox: Dialog? = null
    var signInButton: SignInButton? = null
//    private var googleApiClient: GoogleApiClient? = null
    private val RC_SIGN_IN = 1
    var check:Boolean = false

    private var loginPreferences: SharedPreferences? = null
    private var loginPrefsEditor: SharedPreferences.Editor? = null
    private var saveLogin: Boolean? = null


    var callbackManager: CallbackManager?=null
    private val EMAIL = "email"

    lateinit var loginuser: String
    lateinit var userName: String
    lateinit var userAccountID: String
    lateinit var token: String
    lateinit var authtoken: String
    var passwordvisible = false


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onCreateView() {


        showDialogBox = Dialog(requireContext())
        pref= SharedPref(requireContext())
        spref= ShrdPreferences(requireContext())
        FacebookSdk.sdkInitialize(requireContext())
        AppEventsLogger.activateApp(application = Application())
        viewModel = ViewModelProvider(this).get(LoginSignUpViewModel::class.java)


        tvLoginForgotPassword.setOnClickListener {
            addNewFragment(ForgotPasswordFragment(), R.id.mainContainerForgotPassword, true)
        }

//        hintRemover(etPasswordLayout, edtLoginEnterpassword, R.string.password.toString())
        var email=edtLoginEnterEmail.text.toString()
        var password=edtLoginEnterpassword.text.toString()

       edtLoginEnterpassword.setOnFocusChangeListener { _, hasFocus ->
          // etPasswordLayout.setHintAnimationEnabled(false)
           edtLoginEnterpassword.setOnFocusChangeListener(null)
        }



        if (spref.getBoolean(Constants.IS_CHECKED)==true){
            Log.e("EMAIL","EMAI......"+spref.getString(Constants.EMAIL))
            Log.e("PASSWORD","PASSWORD....."+spref.getString(Constants.PASSWORD))
            edtLoginEnterEmail.setText(spref.getString(Constants.EMAIL))
            edtLoginEnterpassword.setText(spref.getString(Constants.PASSWORD))
            cbAgreeterms.isChecked=true
        }else{
            edtLoginEnterEmail.setText("")
            edtLoginEnterpassword.setText("")
            cbAgreeterms.isChecked=false
        }



       /* if (pref.getBoolean(Constants.IS_CHECKED)==true){
            cbAgreeterms.isChecked=true
        }else{
            cbAgreeterms.isChecked=false
        }*/


        tvLoginmain.setOnClickListener {

            if (edtLoginEnterEmail.text.isEmpty()) {
                showShortToast("Please enter email.")
            } else if (!isEmailValid(edtLoginEnterEmail.text.toString())) {
                showShortToast("Please enter valid email.")
            } else if (edtLoginEnterpassword.text!!.isEmpty()) {
                showShortToast("Please enter password.")
            } /*else if (!isValidPassword(edtLoginEnterpassword.text.toString())) {
                showShortToast("Please enter valid password.")
            }*/ else {
                val loginBody = LoginBody(
                    edtLoginEnterEmail.text.toString(),
                    edtLoginEnterpassword.text.toString(),
                    "orbis",
                    ""
                )
                viewModel.loginRequest(loginBody)
            }
        }

// google login


        ivGooglelogin!!.setOnClickListener {

            val intent = Auth.GoogleSignInApi.getSignInIntent(loginSignUpActivity.googleApiClient!!)
            startActivityForResult(intent, RC_SIGN_IN)
        }

        facebookloginBT.setReadPermissions("email", "public_profile")
        //facebook login
        facebookloginBT.setBackgroundResource(R.drawable.ic_facebook__1)

        facebookloginBT.setOnClickListener {

//            facebookloginBT.setReadPermissions(listOf(EMAIL))
            callbackManager = CallbackManager.Factory.create()
            facebookloginBT.registerCallback(callbackManager, object :
                FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("MainActivity", "Facebook token: " + loginResult!!.accessToken.token)

                    val request = GraphRequest.newMeRequest(
                        loginResult.accessToken,
                        object : GraphRequest.GraphJSONObjectCallback {
                            override   fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                                Log.e("LoginActivity", response.toString())

                                // Application code
                                val id = `object`!!.getString("id")
                                val name = `object`!!.getString("name")
                                val email = `object`!!.getString("email")

                                Log.e("call","name "+name)
                                Log.e("call","email "+email)
                                Log.e("call","id " +id)

                                /// hit api signup
                                val signupBody= SocailLoginBody(
                                    0,""+id,"facebook",""+id,
                                    "",""+email,""+name,false,"",false,
                                    "","",0,"","","")

                                viewModel.signupRequest(signupBody)

                            }
                        })
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email,gender,birthday")
                    request.parameters = parameters
                    request.executeAsync()


                    /*   startActivity(
                        Intent(
                            requireContext(),
                            CustomTabActivity::class.java
                        )
                    )*/// App code
                }

                override fun onCancel() { // App code
                }
                override fun onError(exception: FacebookException) { // App code
                Log.e("FaceBook Error",exception.message.toString())

                }
            })

        }
        setObserver()



        passwordEyeImv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!passwordvisible) {
                    edtLoginEnterpassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                    passwordvisible = true
                    edtLoginEnterpassword.setSelection(edtLoginEnterpassword.getText().length)
                    passwordEyeImv.setImageResource(R.drawable.ic_hide__1_)
                } else {
                    edtLoginEnterpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                    passwordvisible = false
                    edtLoginEnterpassword.setSelection(edtLoginEnterpassword.getText().length)
                    passwordEyeImv.setImageResource(R.drawable.ic_show_1)
                }
            }
        })

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED){
            if (requestCode === RC_SIGN_IN) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
                Log.e("call","result "+result.toString())
                var acct: GoogleSignInAccount = (if (result != null) result.signInAccount else throw NullPointerException("Expression 'result' must not be null"))!!
                var userName = acct.displayName
                var userEmail = acct.email
                var authid = acct.id
                Log.e("userName", userName.toString()+"userEmail"+userEmail.toString())

                ////  hit api signup

                val signupBody= SocailLoginBody(
                    0,""+authid,"google",""+authid,
                    "",""+userEmail,""+userName,false,"",false,
                    "","",0,"","","")

                viewModel.signupRequest(signupBody)

            }
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }

        callbackManager?.onActivityResult(requestCode, resultCode, data)

    }

    fun setObserver() {

        viewModel.loginSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message!="Success"){
//                showShortToast(it.message!!)
                if (it!!.model!=null){

                }else {
                    showAlertResponse(it!!.message.toString())
                   // showShortToast(it!!.message.toString())
                }
            }
            else {
                showShortToast("Login Successfully")
                loginuser=it.model.toString()
                userName= it.model!!.name
                userAccountID= it.model!!.userAccountID
                pref.setString(Constants.USER_NAME,userName)
                pref.setString(Constants.USER_EMAIL,it.model!!.email)
                pref.setString(Constants.userAccountID,userAccountID)
                pref.setString(Constants.IS_FROM_LOGIN,"true")
                token=it.model!!.authToken
                authtoken=it.model!!.authToken
                pref.setString(Constants.TOKEN,token)
                pref.setString(Constants.AUTH_TOKEN,token)

                val intent = Intent(requireActivity(), MainActivity::class.java)
                SessionData.I().makeIntentClearHistory(intent)
                startActivity(intent)


                if (cbAgreeterms.isChecked){
                    check=true
                    spref.setBoolean(Constants.IS_CHECKED,check)
                    spref.setString(Constants.EMAIL,edtLoginEnterEmail.text.toString())
                    spref.setString(Constants.PASSWORD,edtLoginEnterpassword.text.toString())
                }
                else {
                    check=false
                    spref.setBoolean(Constants.IS_CHECKED,check)
                    spref.setString(Constants.EMAIL,"")
                    spref.setString(Constants.PASSWORD,"")
                }


            }
        }
        )

        viewModel.signupResponseSuccess.observe(requireActivity(), Observer {
            // check for unactivated account
            if (it.message!="Success"){
                showShortToast(it.message!!)
            }
            else {

                try {
                    loginuser=it.model.toString()
                    userName= it.model.firstName
                    userAccountID= it.model.userAccountID
                    token=it.model.authToken
                    token=it.model.authToken

                    pref.setString(Constants.USER_NAME,userName)
                    pref.setString(Constants.userAccountID,userAccountID)
                    pref.setString(Constants.IS_FROM_LOGIN,loginuser)
                    pref.setString(Constants.TOKEN,token)
                    pref.setString(Constants.TOKEN,token)

                    val intent2 = Intent(requireActivity(), MainActivity::class.java)
                    SessionData.I().makeIntentClearHistory(intent2)
                    startActivity(intent2)

                }catch (e:Exception){
                    Log.e("call","exp1213 "+e.toString())
                }

            }
        }
        )

        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed "+it!!.toString())
//            showShortToast(it)
        }
        )

        viewModel.apiLoginError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failedloginnnn "+it!!.toString())
            showShortToast(it)

        }
        )

        viewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (it) {
                progressBarPB.show()
            }
            else {
                progressBarPB.dismiss()
            }
        }
        )

    }

    private fun showAlertResponse(message: String) {
        showDialogBox!!.setContentView(R.layout.popup_show_response)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val tvShowResponse: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvShowResponse)
        tvShowResponse.setText(message)
        showDialogBox!!.show()
        Handler(Looper.getMainLooper()).postDelayed({
            showDialogBox!!.dismiss()
        }, 2000)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)
    }


    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}

