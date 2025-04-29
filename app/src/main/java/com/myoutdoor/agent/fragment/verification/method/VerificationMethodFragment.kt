package com.myoutdoor.agent.fragment.verification.method

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.SignUpFragment
import com.myoutdoor.agent.fragment.verification.code.VerificationCodeFragment
import com.myoutdoor.agent.fragment.verification.code.VerificationViewModel
import com.myoutdoor.agent.fragment.verification.code.body.SendVerificationBody
import com.myoutdoor.agent.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.ilToolbar
import kotlinx.android.synthetic.main.fragment_verification_method.rbEmail
import kotlinx.android.synthetic.main.fragment_verification_method.rbTextMessage
import kotlinx.android.synthetic.main.fragment_verification_method.tvContinue
import kotlinx.android.synthetic.main.toolbar.ivBackpress
import kotlinx.android.synthetic.main.toolbar.tvToolbar

class VerificationMethodFragment : BaseFragment() {

    var type:String=""
    var email:String=""
    var phone:String=""
    var name:String=""
    var publicKey:String=""
    lateinit var verificationViewModel: VerificationViewModel
    var showDialogBox: Dialog? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_verification_method
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView() {
        showDialogBox = Dialog(requireContext())
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
        val bundle = this.arguments
        if (bundle != null) {
//            type = bundle.getString("type").toString()
            email = bundle.getString("email").toString()
            phone = bundle.getString("phone").toString()
            name = bundle.getString("name").toString()
            publicKey = bundle.getString("publicKey").toString()
            Log.e("call", "type: $type, email: $email, phone: $phone, name: $name, publicKey: $publicKey")
        }
        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun initUI(){
        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
        }
        tvToolbar.setText("")
        ilToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))
        if (phone.equals("")){
            phone=" - "
            rbTextMessage.isEnabled=false
        }
        if (email.equals("")){
            email=" - "
            rbEmail.isEnabled=false
        }

        rbTextMessage.text = Html.fromHtml("<b>Text Message</b> <br><font color='#808080'>$phone</font>", Html.FROM_HTML_MODE_LEGACY)
        rbEmail.text = Html.fromHtml("<b>Email</b> <br> <font color='#808080'>$email</font>", Html.FROM_HTML_MODE_LEGACY)

        rbTextMessage.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                type="phone"
            }
        }

        rbEmail.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                type="email"
            }
        }

        tvContinue.setOnClickListener {
            if (type.equals("")){
                showShortToast("Please select one option")
            }else {
                if (email.contains("-")){
                    email=""
                }
                if (phone.contains("-")){
                    phone=""
                }
                Log.e("Send Verification Body","email: $email, phone: $phone ")
                val sendVerificationBody = SendVerificationBody(Type = type, Email = email, Phone = phone,
                    Name = name, PublicKey = publicKey)
                verificationViewModel.sendVerificationRequest(sendVerificationBody)
                setObserver()
            }
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
            val fragment = VerificationCodeFragment()
                var bundle = Bundle()
                bundle.putString("type", type)
                bundle.putString("phone", phone)
                bundle.putString("email", email)
                bundle.putString("publicKey", publicKey)
                bundle.putString("name", name)
                fragment.setArguments(bundle)
            addNewFragment(fragment, R.id.mainContainerForgotPassword, true)
        }

    }


    fun setObserver() {

        verificationViewModel.sendVerificationCodeResponse.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message!="Success"){
                exitDialog(it.message)
            }
            else {

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