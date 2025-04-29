package com.myoutdoor.agent.fragment.forgotpassword

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.verification.method.VerificationMethodFragment
import com.myoutdoor.agent.models.ForgotPassword.ForgotPasswordBody
import com.myoutdoor.agent.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.edtEnterEmail
import kotlinx.android.synthetic.main.fragment_forgot_password.ilToolbar
import kotlinx.android.synthetic.main.fragment_forgot_password.tvForgotPasswordLogin
import kotlinx.android.synthetic.main.toolbar.ivBackpress
import kotlinx.android.synthetic.main.toolbar.tvToolbar


class ForgotPasswordFragment : BaseFragment() {

    lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    var showDialogBox: Dialog? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_forgot_password
    }

    companion object{
    }

    override fun onCreateView() {

        showDialogBox = Dialog(requireContext())
        forgotPasswordViewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        tvForgotPasswordLogin.setOnClickListener {
            if (edtEnterEmail.text.isEmpty()) {
                showShortToast("Please enter email.")
            } else if (!isEmailValid(edtEnterEmail.text.toString())) {
                showShortToast("Please enter valid email.")
            } else {
                val forgotPasswordBody = ForgotPasswordBody(edtEnterEmail.text.toString())
                forgotPasswordViewModel.forgotRequest(forgotPasswordBody)
                setObserver()
            }
        }

        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
        }
        tvToolbar.setText("")
        ilToolbar.setBackgroundColor (Color.parseColor ("#FFFFFF"))

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
    }

    fun setObserver() {

        forgotPasswordViewModel.forgotSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message!="Success"){
               exitDialog(it.message!!)
//             showShortToast(it.message!!)
               /*  Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(requireActivity(), LoginSignUpActivity::class.java)
                    startActivity(intent)
                }, 3000) */
//                if (it.message.equals("This email has not been registered with us. Please register your account.")){
//                } else {
//                }
            }
            else {
//                showShortToast(it.message)
                val fragment = VerificationMethodFragment()
                val bundle = Bundle()
                bundle.putString("email", it.model.email)
                bundle.putString("phone", it.model.phone)
                bundle.putString("name", it.model.firstName)
                bundle.putString("publicKey", it.model.publicKey)
                fragment.setArguments(bundle)
                addNewFragment(fragment, R.id.mainContainerForgotPassword, true)
//              addNewFragment(VerificationMethodFragment(), R.id.mainContainerForgotPassword, true)
            }
        }
        )

        forgotPasswordViewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            showShortToast(it)
        }
        )

        forgotPasswordViewModel.isLoading.observe(requireActivity(), Observer {
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