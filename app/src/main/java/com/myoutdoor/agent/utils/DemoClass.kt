package com.myoutdoor.agent.utils

class DemoClass{} /*: BaseFragment(), View.OnClickListener{

    var setMpin = ""
    var enterPin = ""

    val handler = Handler()

    override fun getLayoutId(): Int {
        return R.layout.fragment_enter_mpin
    }

    override fun onCreateView() {

        setMpin = SessionData.I().localData.mPin
        Log.d("", setMpin)
        enterMpin()

        if (SessionData.I().localData.isFingerprintcheck){
            fingerprintLogin()
        }

        tvEnterPinLogin.setOnClickListener(this)
        tvForgotPin.setOnClickListener(this)

    }

    private fun fingerprintLogin() {

        val biometricManager: BiometricManager = BiometricManager.from(requireContext())
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            showShortToast("You can use the fingerprint sensor to login")

        } else if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            showShortToast("This device doesnot have a fingerprint sensor")

        } else if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            showShortToast("The biometric sensor is currently unavailable")

        } else if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            showShortToast("Your device doesn't have fingerprint saved,please check your security settings")

        }


        val executor: Executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt =
            BiometricPrompt(
                this.requireActivity(),
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)

                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)

                        SessionData.I().localData.isUserLogin = true
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        SessionData.I().makeIntentClearHistory(intent)
                        startActivity(intent)

                        showShortToast("Logged In Successfully")
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })



        handler.postDelayed({

            // BIOMETRIC DIALOG
            val promptInfo = PromptInfo.Builder().setTitle("MiM")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel")
                .build()

            biometricPrompt.authenticate(
                promptInfo
            )

        }, 1000)


    }


    private fun enterMpin() {


        pin_lock_view.attachIndicatorDots(indicator_dots)

        val mPinLockListener: PinLockListener = object : PinLockListener {
            override fun onComplete(pin: String) {

                enterPin = pin

                if (pin == SessionData.I().localData.mPin) {
                    SessionData.I().localData.isUserLogin = true

//                 Handler().postDelayed({
//                    val intent = Intent(requireContext(), MainActivity::class.java)
//                    SessionData.I().makeIntentClearHistory(intent)
//                    startActivity(intent)
//                 }, 5000)

                } else {

                    Toast.makeText(requireContext(), "Enter a valid Mpin", Toast.LENGTH_SHORT)
                        .show()

                }
            }

            override fun onEmpty() {
                Log.d(TAG, "Pin empty")
            }

            override fun onPinChange(pinLength: Int, intermediatePin: String) {

            }
        }

        pin_lock_view.setPinLockListener(mPinLockListener)

    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.tvEnterPinLogin -> {

                if (setMpin == enterPin.toString()) {
                    SessionData.I().localData.isUserLogin = true
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    SessionData.I().makeIntentClearHistory(intent)
                    startActivity(intent)
                } else {

                    Toast.makeText(requireContext(), "Enter a valid Mpin", Toast.LENGTH_SHORT)
                        .show()

                }

            }
            R.id.tvForgotPin -> {

                addDialog()


            }
        }
    }

    private fun addDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_custom_dialog)
        dialog.show()

        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            ViewGroup.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.FILL_PARENT
        )
        window.setGravity(Gravity.CENTER)
        dialog.tvRecoveryCode.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("phoneNumber", SessionData.I().localData.mobileNumber)


            val fragment = SignUpOtpFragment(Constants.TYPE_ENTER_MPIN_OTP)
            fragment.arguments = bundle

            replaceFragment(fragment, R.id.loginRegisterContainer, true)
            dialog.dismiss()

        }

        dialog.tvRecoveryCancel.setOnClickListener {
            dialog.dismiss()

        }

    }
}


class EnterMpinFragment : BaseFragment(), View.OnClickListener {

*/



