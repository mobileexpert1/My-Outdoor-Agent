package com.myoutdoor.agent.fragment

import android.app.Dialog
import android.graphics.Color
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity.Companion.loginSignUpActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpViewModel
import com.myoutdoor.agent.fragment.sociallogin.LoginFragment
import com.myoutdoor.agent.models.register.RegisterBody
import com.myoutdoor.agent.utils.BaseFragment
import kotlinx.android.synthetic.main.activity_login_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : BaseFragment() {

    lateinit var viewModel: LoginSignUpViewModel

    var check:Boolean = false
    var showDialogBox: Dialog? = null
    var passwordvisible = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_sign_up
    }

    override fun onCreateView() {

        showDialogBox = Dialog(requireContext())

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        /* tvSignUpScreenBT.setOnClickListener {
             addNewFragment(LoginFragment(), R.id.mainContainer, true)
         }*/

        viewModel = ViewModelProvider(this).get(LoginSignUpViewModel::class.java)
        setObserver()

        /*edtSignupSetEnterpassword.setOnFocusChangeListener { _, hasFocus ->
            etPasswordLayout2.hint = if (hasFocus) {
                ""
            } else {

                if (edtSignupSetEnterpassword.text!!.isEmpty()){
                    getString(R.string.password)
                }
                ""

            }
        }
        edtSignupEnterconfirmpassword.setOnFocusChangeListener { _, hasFocus ->
            etPasswordLayout3.hint = if (hasFocus) {
                ""
            } else {
                if (edtSignupEnterconfirmpassword.text!!.isEmpty()) {
                    getString(R.string.confirmpassword)
                }
                ""
            }
        }*/



        tvTermsAndConditions.setOnClickListener {
        /*    val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("http://orbisuser.csdevhub.com/#/app/terms")
            startActivity(openURL)*/
            termsAndConditionsPopup()
        }


        cbAgreeterms.setOnCheckedChangeListener { _, _ ->
            if (cbAgreeterms.isChecked) {
                check=true
            } else {
                check=false
            }
        }



        passwordsetEyeImv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!passwordvisible) {
                    edtSignupSetEnterpassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                    passwordvisible = true
                    edtSignupSetEnterpassword.setSelection(edtSignupSetEnterpassword.getText().length)
                    passwordsetEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_hide__1_)
                } else {
                    edtSignupSetEnterpassword.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance())
                    passwordvisible = false
                    edtSignupSetEnterpassword.setSelection(edtSignupSetEnterpassword.getText().length)
                    passwordsetEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_show_1)
                }
            }
        })


        passwordconfirmEyeImv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!passwordvisible) {
                    edtSignupEnterconfirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                    passwordvisible = true
                    edtSignupEnterconfirmpassword.setSelection(edtSignupEnterconfirmpassword.getText().length)
                    passwordconfirmEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_hide__1_)
                } else {
                    edtSignupEnterconfirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                    passwordvisible = false
                    edtSignupEnterconfirmpassword.setSelection(edtSignupEnterconfirmpassword.getText().length)
                    passwordconfirmEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_show_1)
                }
            }
        })

        tvSignUpScreenBT.setOnClickListener {
            if (edtSignupEnterFirstName.text.isEmpty()) {
                showShortToast("Please enter first name.")
            } else if (edtSignupEnterlastname.text.isEmpty()) {
                showShortToast("Please enter last name.")
            } else if (edtSignupEnterUserEmail.text.isEmpty()) {
                showShortToast("Please enter email.")
            } else if (!isEmailValid(edtSignupEnterUserEmail.text.toString())) {
                showShortToast("Please enter valid email.")
            } else if (edtSignupSetEnterpassword.text!!.isEmpty()) {
                showShortToast("Please enter password.")
            } else if (!isValidPassword(edtSignupSetEnterpassword.text.toString())) {
                showShortToast("Please enter valid password")
            } else if (edtSignupEnterconfirmpassword.text!!.isEmpty()) {
                showShortToast("Please enter confirm password.")
            } else if (!isValidPassword(edtSignupEnterconfirmpassword.text.toString())) {
                showShortToast("Please enter valid confirm password")
            }else if (check==false) {
                showShortToast("Please agree to the Terms of Service")
            } else if (!edtSignupSetEnterpassword.text.toString()
                    .equals(edtSignupEnterconfirmpassword.text.toString())
            ) {
                showShortToast("Passwords do not match!")
            } else {
                val registerBody= RegisterBody(edtSignupEnterFirstName.text.toString(),edtSignupEnterlastname.text.toString(),edtSignupEnterUserEmail.text.toString(),edtSignupSetEnterpassword.text.toString(),edtSignupEnterconfirmpassword.text.toString(),"0","","orbis","0")
//                registerBody.FirstName = edtSignupEnterFirstName.text.toString()
//                registerBody.LastName= edtSignupEnterlastname.text.toString()
//                registerBody.Email=edtSignupEnterUserEmail.text.toString()
//                registerBody.Password=edtSignupSetEnterpassword.text.toString()
//                registerBody.ConfirmPassword=edtSignupEnterconfirmpassword.text.toString()
//                registerBody.AccountType = "0"
//                registerBody.AuthenticationKey=""
//                registerBody.AuthenticationType="orbis"
//                registerBody.SourceClientID="0"
                viewModel.registerRequest(registerBody)
            }



            /*requireActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            edtSignupEnterconfirmpassword.setOnTouchListener { v, event ->
                edtSignupEnterconfirmpassword.requestLayout()
                requireActivity().getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
                false


        }*/

    }

    }


    private fun termsAndConditionsPopup() {

        showDialogBox!!.setContentView(R.layout.popup_terms_and_conditions)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val tvIAgree: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvIAgree)
        val ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)
        val wvShowTermsAndConditions: WebView = showDialogBox!!.findViewById<WebView>(R.id.wvShowTermsAndConditions)
        wvShowTermsAndConditions.webViewClient = WebViewClient()
        wvShowTermsAndConditions.settings.javaScriptEnabled = true
        wvShowTermsAndConditions.setInitialScale(1);
        wvShowTermsAndConditions.getSettings().setLoadWithOverviewMode(true);
        wvShowTermsAndConditions.getSettings().setUseWideViewPort(true);
        wvShowTermsAndConditions.getSettings().setAllowFileAccess(true);
        wvShowTermsAndConditions.getSettings().setAllowContentAccess(true);
        wvShowTermsAndConditions.setScrollbarFadingEnabled(false);


        wvShowTermsAndConditions.loadUrl("http://orbisuser.csdevhub.com/#/app/terms")
        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
        }
        tvIAgree.setOnClickListener {
            showDialogBox!!.cancel()
        }
        showDialogBox!!.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)
    }


    fun setObserver() {

        viewModel.registerSuccess.observe(requireActivity(), Observer {


            if (it!!.model!=null){
                loginSignUpActivity.tvLogin.setBackgroundResource(R.drawable.login_register_shape_green)
                loginSignUpActivity.tvLogin.setTextColor(Color.parseColor("#FFFFFFFF"))
                loginSignUpActivity.tvSignup.setTextColor(Color.parseColor("#B1B1B1"))
                loginSignUpActivity.tvSignup.setBackgroundResource(R.color.light_white_green)
                Log.d("@@@@", "Success")

                showShortToast("Signup Successfully")
                addNewFragment(LoginFragment(), R.id.mainContainer, false)
            }else {
                Log.d("@@@@", "Success")
                showShortToast(it!!.message.toString())
            }
        }
        )


        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            showShortToast(it)
        }
        )

        viewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (it) {
                progressBarPB.show()
            } else {
                progressBarPB.dismiss()
            }
        }
        )

    }

}