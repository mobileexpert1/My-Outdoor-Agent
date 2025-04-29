package com.myoutdoor.agent.fragment.verification.code

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
import com.myoutdoor.agent.fragment.verification.changepassword.UpdatePasswordFragment
import com.myoutdoor.agent.fragment.verification.code.body.SendVerificationBody
import com.myoutdoor.agent.fragment.verification.code.body.VerifyCodeBody
import com.myoutdoor.agent.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.ilToolbar
import kotlinx.android.synthetic.main.fragment_verification_code.pvOtp
import kotlinx.android.synthetic.main.fragment_verification_code.tvContinue
import kotlinx.android.synthetic.main.fragment_verification_code.tvDesc
import kotlinx.android.synthetic.main.fragment_verification_code.tvResendCode
import kotlinx.android.synthetic.main.toolbar.ivBackpress
import kotlinx.android.synthetic.main.toolbar.tvToolbar

class VerificationCodeFragment : BaseFragment() {

    lateinit var verificationViewModel: VerificationViewModel
    var showDialogBox: Dialog? = null
    var publicKey: String = ""
    var phone: String = ""
    var email: String = ""
    var type: String = ""
    var name: String = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_verification_code
    }

    override fun onCreateView() {
        val bundle = this.arguments
        if (bundle != null) {
            type = bundle.getString("type").toString()
            email = bundle.getString("email").toString()
            phone = bundle.getString("phone").toString()
            publicKey = bundle.getString("publicKey").toString()
            name = bundle.getString("name").toString()
        }

        showDialogBox = Dialog(requireContext())
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)

        if (type.equals("email")){
            tvDesc.text = "${getString(R.string.we_sent_you_a_verification_code_by_email)} $email"
        } else {
            tvDesc.text = "${getString(R.string.we_sent_you_a_verification_code_by_text_message_to_xxx_xxx_7456)} $phone"
        }

        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
        }

        tvToolbar.setText("")
        ilToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))

        tvContinue.setOnClickListener {
            var sendVerificationCodeBody = VerifyCodeBody(PublicKey = publicKey, Token = pvOtp.getText().toString())
            verificationViewModel.verifyCodeRequest(sendVerificationCodeBody)
            setObserver()
        }
        tvResendCode.setOnClickListener {
            val sendVerificationBody = SendVerificationBody(Type = type, Email = email, Phone = phone,
                Name = name, PublicKey = publicKey)
            verificationViewModel.sendVerificationRequest(sendVerificationBody)
            setObserver()
        }

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
//            UpdatePasswordFragment

            val fragment = UpdatePasswordFragment()
            val bundle = Bundle()
//                bundle.putString("email", it.model.email)
//                bundle.putString("phone", it.model.phone)
            bundle.putString("publicKey", publicKey)
            bundle.putString("token", pvOtp.getText().toString())
            fragment.setArguments(bundle)
            addNewFragment(fragment, R.id.mainContainerForgotPassword, true)
        }

    }


    fun setObserver() {

        verificationViewModel.verifyCodeResponse.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message != "Success") {
                exitDialog(it.message)
            } else {
                showShortToast(it.message)


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
