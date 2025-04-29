package com.myoutdoor.agent.fragment.verification.changepassword

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
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
import com.facebook.login.Login
import com.facebook.login.LoginFragment
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import com.myoutdoor.agent.fragment.verification.code.VerificationViewModel
import com.myoutdoor.agent.fragment.verification.code.body.UpdatePasswordBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.SessionData
import kotlinx.android.synthetic.main.fragment_forgot_password.ilToolbar
import kotlinx.android.synthetic.main.fragment_sign_up.edtSignupEnterconfirmpassword
import kotlinx.android.synthetic.main.fragment_sign_up.edtSignupSetEnterpassword
import kotlinx.android.synthetic.main.fragment_sign_up.passwordconfirmEyeImv
import kotlinx.android.synthetic.main.fragment_sign_up.passwordsetEyeImv
import kotlinx.android.synthetic.main.fragment_update_password.edtConfirmpassword
import kotlinx.android.synthetic.main.fragment_update_password.edtpassword
import kotlinx.android.synthetic.main.fragment_update_password.tvSave
import kotlinx.android.synthetic.main.fragment_verification_code.tvContinue
import kotlinx.android.synthetic.main.toolbar.ivBackpress
import kotlinx.android.synthetic.main.toolbar.tvToolbar

class UpdatePasswordFragment : BaseFragment() {

    lateinit var verificationViewModel: VerificationViewModel
    var showDialogBox: Dialog? = null
    var publicKey: String = ""
    var phone: String = ""
    var email: String = ""
    var type: String = ""
    var token: String = ""

    var passwordvisible = false


    override fun getLayoutId(): Int {
        return R.layout.fragment_update_password
    }

    override fun onCreateView() {
        val bundle = this.arguments
        if (bundle != null) {
//            type = bundle.getString("type").toString()
//            email = bundle.getString("email").toString()
//            phone = bundle.getString("phone").toString()
            token = bundle.getString("token").toString()
            publicKey = bundle.getString("publicKey").toString()
//            Log.e("call", "type: $type, email: $email, phone: $phone, name: $name, publicKey: $publicKey")
        }
        showDialogBox = Dialog(requireContext())
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
        tvSave.setOnClickListener {
            if (edtpassword.text.toString()!=edtConfirmpassword.text.toString()){
                exitDialog("New Password and Confirm Password is not same")
            } else if (edtpassword.text.toString().equals("")){
                exitDialog("Enter new password")
            } else if (edtConfirmpassword.text.toString().equals("")){
                exitDialog("Enter confirm password")
            }
            else{
                var updatePasswordBody = UpdatePasswordBody(Password = edtpassword.text.toString(),
                    PublicKey = publicKey, Token =token)
                verificationViewModel.updatePasswordRequest(updatePasswordBody)
                setObserver()
            }
        }


        passwordsetEyeImv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!passwordvisible) {
                    edtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                    passwordvisible = true
                    edtpassword.setSelection(edtpassword.getText().length)
                    passwordsetEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_hide__1_)
                } else {
                    edtpassword.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance())
                    passwordvisible = false
                    edtpassword.setSelection(edtpassword.getText().length)
                    passwordsetEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_show_1)
                }
            }
        })


        passwordconfirmEyeImv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!passwordvisible) {
                    edtConfirmpassword.setTransformationMethod(
                        PasswordTransformationMethod.getInstance())
                    passwordvisible = true
                    edtConfirmpassword.setSelection(edtConfirmpassword.getText().length)
                    passwordconfirmEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_hide__1_)
                } else {
                    edtConfirmpassword.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance())
                    passwordvisible = false
                    edtConfirmpassword.setSelection(edtConfirmpassword.getText().length)
                    passwordconfirmEyeImv.setImageResource(com.myoutdoor.agent.R.drawable.ic_show_1)
                }
            }
        })

        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
        }


        tvToolbar.setText("")
        ilToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))


    }

    fun exitDialog(message: String) {
        showDialogBox!!.setContentView(R.layout.popup_show_response)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val tvShowResponse: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvShowResponse)
        tvShowResponse.setText(message)
        showDialogBox!!.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)
        // Add the OnDismissListener here
        showDialogBox!!.setOnDismissListener {
            // Code to execute when the dialog is dismissed
            //                showShortToast(it.message)
//            val fragment = VerificationCodeFragment()
//                val bundle = Bundle()
//                bundle.putString("email", it.model.email)
//                bundle.putString("phone", it.model.phone)
//                bundle.putString("name", it.model.firstName)
//                bundle.putString("publicKey", it.model.publicKey)
//                fragment.setArguments(bundle)
//            addNewFragment(fragment, R.id.mainContainerForgotPassword, true)
//            addNewFragment(LoginFragment(),R.id.mainContainer,false)
                val intent = Intent(requireActivity(), LoginSignUpActivity::class.java)
                SessionData.I().makeIntentClearHistory(intent)
                startActivity(intent)
        }

    }


    fun setObserver() {

        verificationViewModel.updatePasswordResponse.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message != "Success") {
                exitDialog(it.message)
            } else {
//                showShortToast(it.message)
                exitDialog(it.message)

//                val intent = Intent(requireActivity(), LoginSignUpActivity::class.java)
//                SessionData.I().makeIntentClearHistory(intent)
//                startActivity(intent)
            }
        }
        )

        verificationViewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            showShortToast(it)
        }
        )

        verificationViewModel.isLoading.observe(requireActivity(), Observer {
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