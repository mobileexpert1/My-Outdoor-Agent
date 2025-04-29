package com.myoutdoor.agent.fragment.UserProfile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import com.myoutdoor.agent.adapter.HorizontalAdapter
import com.myoutdoor.agent.adapter.SavedSearchesAdapter
import com.myoutdoor.agent.models.changepassword.ChangePasswordBody
import com.myoutdoor.agent.models.deletesearch.DeleteSearchBody
import com.myoutdoor.agent.models.getallstates.Model
import com.myoutdoor.agent.models.managenotifications.ManageNotificationsBody
import com.myoutdoor.agent.models.userdetails.EditProfileBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SessionData
import com.myoutdoor.agent.utils.SharedPref
import com.skydoves.powerspinner.PowerSpinnerView
import com.suke.widget.SwitchButton
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.toolbar.*


class MyAccountFragment : BaseFragment(), HorizontalAdapter.OnItemClickListener,
    SavedSearchesAdapter.OnItemClickListener {

    // var commonList = java.util.ArrayList<String>();
    var savedSearchesList =
        ArrayList<com.myoutdoor.agent.models.savedsearches.getsavedsearches.Model>()

    lateinit var pref: SharedPref
    lateinit var viewModel: UserdetailsViewModel
    var getAllStatesResponseList = ArrayList<Model>()

    var stateNameList = ArrayList<String>()
    var accFirstName: String = ""
    var accLastName: String = ""
    var accEmail: String = ""
    var accPhone: String = ""
    var accStreetAddress: String = ""
    var accCity: String = ""
    var accSt: String = ""
    var accClubGroupName: String = ""
    var accZipCode: String = ""
    var accAddress: String = ""
    lateinit var data: ArrayList<String>
    private var mGoogleApiClient: GoogleApiClient? = null
    private val RC_SIGN_IN = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_account
    }


    override fun onCreateView() {
        pref = SharedPref(requireContext())
        viewModel = ViewModelProvider(this).get(UserdetailsViewModel::class.java)

        setObserver()
        data = ArrayList()

//        rvSavedSearchesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        rvSavedSearches.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        bottomNav.visibility= View.GONE
        tvToolbar.setText(" Profile ")
//        backpress button
        ivBackpress.setOnClickListener {

            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
        }

        tvAccountSettings.setOnClickListener {
            tvAccountSettings.setBackgroundResource(R.drawable.login_register_shape_green)
            tvAccountSettings.setTextColor(Color.parseColor("#FFFFFFFF"))
//            tvSavedSearches.setTextColor(Color.parseColor("#898989"))
//            tvSavedSearches.setBackgroundResource(R.drawable.account_two_tab_shape)
//                addNewFragment(LoginFragment(),R.id.mainContainer,false)
            llAccountSettings.visibility = View.VISIBLE
//            llSavedSearches.visibility = View.GONE
        }

//        tvSavedSearches.setOnClickListener {
//            tvSavedSearches.setBackgroundResource(R.drawable.login_register_shape_green)
//            tvAccountSettings.setBackgroundResource(R.drawable.account_two_tab_shape)
//            tvAccountSettings.setTextColor(Color.parseColor("#898989"))
//            tvSavedSearches.setTextColor(Color.parseColor("#FFFFFFFF"))
////                addNewFragment(SignUpFragment(),R.id.mainContainer,false)
//            llSavedSearches.visibility = View.VISIBLE
//            llAccountSettings.visibility = View.GONE
//
//        }

        viewModel.userDetailsRequest(pref.getString(Constants.TOKEN))

        viewModel.getAllStatesRequest(pref.getString(Constants.TOKEN))

        viewModel.getSavedSearchesRequest(pref.getString(Constants.TOKEN))



        fragmentBackPressHandle()
        ivEdit.setOnClickListener {
            bottomSheetDialog()
        }

        llLogout.setOnClickListener {
            /*pref.setString(Constants.IS_FROM_LOGIN,"")
            pref.setString(Constants.USER_NAME,"")
            pref.setString(Constants.userAccountID,"")
            pref.setString(Constants.TOKEN,"")*/

            LoginManager.getInstance().logOut();

//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//
//
//            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
//            googleSignInClient.signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut()

            pref.clearPref()

            val intent = Intent(requireContext(), LoginSignUpActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        }

        llChangePassword.setOnClickListener {
            changePasswordbottomSheetDialog()
        }

        llDeleteAccount.setOnClickListener {
            dialogDeleteAccount()
        }

        tvDeleteAccount.setOnClickListener {
            dialogDeleteAccount()
        }

        tvChangePassword.setOnClickListener {
            changePasswordbottomSheetDialog()
        }


        if (pref.getBoolean(Constants.TOGGLE_SWITCH) == true) {
            buttonSwitch.setChecked(true);
        } else if (pref.getBoolean(Constants.TOGGLE_SWITCH) == false) {
            buttonSwitch.setChecked(false);
        }

        buttonSwitch.setShadowEffect(true);//disable shadow effect
        buttonSwitch.setOnCheckedChangeListener(SwitchButton.OnCheckedChangeListener { view, isChecked ->
            if (buttonSwitch.isChecked) {
                var manageNotificationsBody = ManageNotificationsBody(true)
                pref.setBoolean(Constants.TOGGLE_SWITCH, true)
                viewModel.manageNotificationRequest(
                    manageNotificationsBody,
                    pref.getString(Constants.TOKEN)
                )
            } else {
                var manageNotificationsBody = ManageNotificationsBody(false)
                pref.setBoolean(Constants.TOGGLE_SWITCH, false)
                viewModel.manageNotificationRequest(
                    manageNotificationsBody,
                    pref.getString(Constants.TOKEN)
                )
            }
        })

    }

    fun setObserver() {

        // delete user
        viewModel.deleteUserResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
                LoginManager.getInstance().logOut()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
                googleSignInClient.signOut()

                pref.clearPref()

                val intent = Intent(requireContext(), LoginSignUpActivity::class.java)
                SessionData.I().makeIntentClearHistory(intent)
                startActivity(intent)

                Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show()
            } else {
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.manageNotificationsResponseResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.editProfileResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                viewModel.userDetailsRequest(pref.getString(Constants.TOKEN))
                //       showShortToast(it.message!!)

            }
        }
        )

        viewModel.getAllStatesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                getAllStatesResponseList.addAll(it.model)
                for (i in 0 until getAllStatesResponseList.size) {
                    stateNameList.add(getAllStatesResponseList.get(i).stateName)
                }
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.getUserDetailsResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                if (it.model.firstName != null) {
                    accFirstName = it.model.firstName
                }
                if (it.model.lastName != null) {
                    accLastName = it.model.lastName
                }

                accEmail = it.model.email
                pref.setString(Constants.USER_EMAIL, it.model.email)

                accPhone = it.model.phone
                accStreetAddress = it.model.streetAddress
                accCity = it.model.city
                accSt = it.model.st
                accClubGroupName = it.model.groupName
                accZipCode = it.model.zip
                accAddress = accStreetAddress + ", " + accCity + ", " + accSt + ", " + accZipCode
                tvAccountName.setText(accFirstName + accLastName)
                tvAccountEmail.setText(accEmail)
                if (accPhone.isNullOrEmpty()) {

                } else {

                    Log.e("$$$$", "tvAccountPhoneNumber...." + accPhone.length)

                    if (accPhone != null) {
                        if (accPhone.length >= 10) {
                            var phoneNumber = "" + accPhone
                            var open = "("
                            var close = ")"
                            var code = " "
                            code = open + phoneNumber.substring(
                                0,
                                3
                            ) + close + " " + phoneNumber.substring(
                                3,
                                6
                            ) + "-" + phoneNumber.substring(6, 10)
                            tvAccountPhoneNumber.setText(code)
                        } else {
                            tvAccountPhoneNumber.setText(accPhone)
                        }
                    }
                }

                if (!accAddress.contains("null")) {
                    tvAccountAddress.setText(accAddress)
                }


                if (!accClubGroupName.isNullOrBlank()) {
                    tvClubname.setText(accClubGroupName)
                }
                /* if (accAddress.contains("")||accAddress.isNullOrBlank()){
                     Log.e("$$$$","accAddress...empty  ."+accAddress)

                     tvAccountAddress.setText("Address")
                 }else{
                     tvAccountAddress.setText(accAddress)
                     Log.e("$$$$","tvAccountAddress...."+accAddress)
                 }*/
                /*if (accClubGroupName.contains("")||accClubGroupName.isNullOrBlank()){
                    tvClubname.setText("Club Name")
                }else{
                    tvClubname.setText(accClubGroupName)
                    Log.e("$$$$","tvClubname...."+accClubGroupName)
                }
*/
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.changePasswordResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                showShortToast(it.message!!)
            }
        }
        )
////
        viewModel.getSavedSearchesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
                showShortToast(it.message!!)
            } else {

                rvSavedSearches.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


                savedSearchesList.addAll(it!!.model!!)

                Log.e("call", "@@@@ 123 " + savedSearchesList.toString())



                for (i in 0 until it!!.model!!.size) {
                    if (it.model!!.get(i).RegionName.size > 0
                        || it.model!!.get(i).PropertyName.size > 0
                        || it.model!!.get(i).StateName.size > 0
                        || it.model!!.get(i).Amenities.size > 0
                        || it.model!!.get(i).SearchName != null
                    ) {
                    } else {
                        savedSearchesList!!.remove(it.model!!.get(i))
                    }
                }

                Log.e("call", "@@@@ 1234444 " + savedSearchesList.toString())


                rvSavedSearches.adapter =
                    SavedSearchesAdapter(savedSearchesList, activity, this, this@MyAccountFragment)
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.apiError.observe(requireActivity(), Observer {
            showShortToast(it)
        }
        )

        viewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (it) {
                if (progressBarPB != null)
                    progressBarPB.show()
            } else {
                if (progressBarPB != null)
                    progressBarPB.dismiss()
            }
        }
        )

    }


    fun bottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        //  val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_dialog_box)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.setPeekHeight(0);
        dialog.show()
        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.BOTTOM)

//    var edtAccountEnterName = dialog.findViewById<TextView>(R.id.edtAccountEnterName) as EditText?
        var edtAccountEnterFirstName = dialog.findViewById<EditText>(R.id.edtAccountEnterFirstName)
        var edtAccountEnterlastName = dialog.findViewById<EditText>(R.id.edtAccountEnterlastName)
        var edtAccountEnterEmail = dialog.findViewById<EditText>(R.id.edtAccountEnterEmail)
        var edtstreetAddress = dialog.findViewById<EditText>(R.id.edtstreetAddress)
        var edtCity = dialog.findViewById<EditText>(R.id.edtCity)
        var pvStateName = dialog.findViewById<PowerSpinnerView>(R.id.pvStateName)
        var edtAccountEnterPhoneNumber =
            dialog.findViewById<EditText>(R.id.edtAccountEnterPhoneNumber)
        var edtZipCode = dialog.findViewById<EditText>(R.id.edtZipCode)
        var edtGroupClubName = dialog.findViewById<EditText>(R.id.edtGroupClubName)
        var tvSaveDialog = dialog.findViewById<TextView>(R.id.tvSaveDialog)
        var tvCancelDialog = dialog.findViewById<TextView>(R.id.tvCancelDialog)

        edtAccountEnterFirstName!!.setText(accFirstName)
        edtAccountEnterlastName!!.setText(accLastName)
        edtAccountEnterEmail!!.setText(accEmail)
        edtstreetAddress!!.setText(accStreetAddress)
        edtCity!!.setText(accCity)
        pvStateName!!.setText(accSt)
        edtAccountEnterPhoneNumber!!.setText(accPhone)
        edtZipCode!!.setText(accZipCode)
        edtGroupClubName!!.setText(accClubGroupName)


        pvStateName?.setItems(stateNameList)

        edtAccountEnterFirstName.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })

        edtAccountEnterlastName.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })
        edtAccountEnterEmail.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })
        edtstreetAddress.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })
        edtCity.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })
        edtAccountEnterPhoneNumber.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })
        edtZipCode.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })
        edtGroupClubName.setOnTouchListener(View.OnTouchListener { v, event ->
            pvStateName.dismiss()
            false
        })


        edtAccountEnterFirstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                pvStateName.dismiss()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                pvStateName.dismiss()
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                pvStateName.dismiss()
            }
        })



        tvSaveDialog!!.setOnClickListener {
            var enterfirstname: String = edtAccountEnterFirstName!!.getText().toString()
            var enterlastname: String = edtAccountEnterlastName!!.getText().toString()
            var enteremail: String = edtAccountEnterEmail!!.getText().toString()
            var enterstreetaddress: String = edtstreetAddress!!.getText().toString()
            var entercity: String = edtCity!!.getText().toString()
            var enterphoneno: String = edtAccountEnterPhoneNumber!!.getText().toString()
            var enterzipcode: String = edtZipCode!!.getText().toString()
            var entergroupclubname: String = edtGroupClubName!!.getText().toString()


            if (edtAccountEnterFirstName.text.isEmpty()) {
                showShortToast("Please enter first name.")
            } else if (edtAccountEnterlastName.text.isEmpty()) {
                showShortToast("Please enter lasr name.")
            } else if (edtAccountEnterEmail.text.isEmpty()) {
                showShortToast("Please enter Email name")
            } else if (edtstreetAddress.text.isEmpty()) {
                showShortToast("Please enter street address")
            } else if (edtCity.text.isEmpty()) {
                showShortToast("Please enter city")
            } else if (edtAccountEnterPhoneNumber.text.isEmpty()) {
                showShortToast("Please enter phone number")
            } else if (edtAccountEnterPhoneNumber.text.toString().length != 10) {
                showShortToast("Phone Number must contain 10 digits")
            } else if (edtZipCode.text.isEmpty()) {
                showShortToast("Please enter zip code")
            } else if (edtZipCode.text.toString().length != 5) {
                showShortToast("ZipCode must contain 5 digits")
            } else if (edtGroupClubName.text.isEmpty()) {
                showShortToast("Please enter club name")
            } else if (!isEmailValid(edtAccountEnterEmail.text.toString())) {
                showShortToast("Please enter valid email")
            } else {
                val editProfileBody = EditProfileBody(
                    "",
                    "" + entercity,
                    "",
                    enteremail,
                    enterfirstname,
                    true,
                    "" + entergroupclubname,
                    true,
                    enterlastname,
                    "" + enterphoneno,
                    "" + enterstreetaddress,
                    pvStateName.text.toString(),
                    1,
                    enterstreetaddress,
                    23,
                    2,
                    "" + enterzipcode
                )
                viewModel.editProfileRequest(editProfileBody, pref.getString(Constants.TOKEN))
                /*tvAccountEnterName.setText(enterfirstname+enterlastname)
                tvAccountEnterEmail.setText(enteremail)
                tvAccountEnterPhoneNumber.setText(enterphoneno)
                tvAccountEnterAddressOne.setText(enterstreetaddress+entercity)
                tvClubname.setText(entergroupclubname)*/
                dialog.dismiss()
            }

        }

        tvCancelDialog!!.setOnClickListener {
            dialog.dismiss()
        }

    }

    @SuppressLint("InflateParams")
    fun dialogDeleteAccount() {
        // Create a Dialog instance
        val dialog = Dialog(requireActivity())

        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_account_simple, null)

        // Set the custom view to the dialog
        dialog.setContentView(dialogView)

        // Set the dialog width and height to wrap_content
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Reference to the checkbox and buttons
        val checkBoxConfirm = dialogView.findViewById<CheckBox>(R.id.checkBoxConfirm)
        val deleteButton = dialogView.findViewById<Button>(R.id.btnDelete)
        val cancelButton = dialogView.findViewById<Button>(R.id.btnCancel)

        // Disable the Delete button initially
        deleteButton.isEnabled = false

        // Enable the Delete button when the checkbox is checked
        checkBoxConfirm.setOnCheckedChangeListener { _, isChecked ->
            deleteButton.isEnabled = isChecked
        }

        // Set click listeners for the buttons
        deleteButton.setOnClickListener {
            // Handle account deletion logic
            viewModel.deleteUserRequest(pref.getString(Constants.TOKEN))
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // Display the dialog
        dialog.show()
    }



    fun changePasswordbottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.change_password_popup)
        dialog.show()
        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.BOTTOM)


        var edtCurrentPassword = dialog.findViewById<EditText>(R.id.edtCurrentPassword)
        var edtNewPassword = dialog.findViewById<EditText>(R.id.edtNewPassword)
        var edtConfirmNewPassword = dialog.findViewById<EditText>(R.id.edtConfirmNewPassword)
        var tvResetPassword = dialog.findViewById<TextView>(R.id.tvResetPassword)
        var tvCancelPwd = dialog.findViewById<TextView>(R.id.tvCancelPwd)


        tvResetPassword!!.setOnClickListener {
            var entercurrentpassword: String = edtCurrentPassword!!.getText().toString()
            var enternewpassword: String = edtNewPassword!!.getText().toString()
            var enterconfirmnewpassword: String = edtConfirmNewPassword!!.getText().toString()

            if (edtCurrentPassword.text.isEmpty()) {
                showShortToast("Please enter current password.")
            } else if (edtNewPassword.text.isEmpty()) {
                showShortToast("Please enter new password.")
            } else if (edtConfirmNewPassword.text.isEmpty()) {
                showShortToast("Please enter confirm password.")
            } else if (enternewpassword != enterconfirmnewpassword) {
                showShortToast("Password and confirm password did not match!")
            } else {
                val changePasswordBody = ChangePasswordBody(
                    entercurrentpassword, enternewpassword
                )
                viewModel.changePasswordRequest(changePasswordBody, pref.getString(Constants.TOKEN))
                dialog.dismiss()
            }

        }

        tvCancelPwd!!.setOnClickListener { dialog.dismiss() }


    }

    override fun onChatListListener(index: Int) {
    }

    override fun onClickListListener(index: Int) {
    }

    fun removeitem(userId: String?) {
        pref = SharedPref(requireContext())
        val deleteSearchBody = DeleteSearchBody(
            "" + userId
        )
        viewModel.deletesearchRequest(deleteSearchBody, pref.getString(Constants.TOKEN))
    }


}
/*
package com.myoutdoor.agent.fragment.UserProfile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.activities.loginsignup.LoginSignUpActivity
import com.myoutdoor.agent.adapter.HorizontalAdapter
import com.myoutdoor.agent.adapter.SavedSearchesAdapter
import com.myoutdoor.agent.models.changepassword.ChangePasswordBody
import com.myoutdoor.agent.models.deletesearch.DeleteSearchBody
import com.myoutdoor.agent.models.getallstates.Model
import com.myoutdoor.agent.models.managenotifications.ManageNotificationsBody
import com.myoutdoor.agent.models.userdetails.EditProfileBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SessionData
import com.myoutdoor.agent.utils.SharedPref
import com.skydoves.powerspinner.PowerSpinnerView
import com.suke.widget.SwitchButton
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.toolbar.*


class MyAccountFragment : BaseFragment(), HorizontalAdapter.OnItemClickListener,
    SavedSearchesAdapter.OnItemClickListener {

    // var commonList = java.util.ArrayList<String>();
    var savedSearchesList =
        ArrayList<com.myoutdoor.agent.models.savedsearches.getsavedsearches.Model>()

    lateinit var pref: SharedPref
    lateinit var viewModel: UserdetailsViewModel
    var getAllStatesResponseList = ArrayList<Model>()

    var stateNameList = ArrayList<String>()
    var accFirstName: String = ""
    var accLastName: String = ""
    var accEmail: String = ""
    var accPhone: String = ""
    var accStreetAddress: String = ""
    var accCity: String = ""
    var accSt: String = ""
    var accClubGroupName: String = ""
    var accZipCode: String = ""
    var accAddress: String = ""
    lateinit var data: ArrayList<String>
    private var mGoogleApiClient: GoogleApiClient? = null
    private val RC_SIGN_IN = 1


    override fun getLayoutId(): Int {
        return R.layout.fragment_my_account
    }


    override fun onCreateView() {
        pref = SharedPref(requireContext())
        viewModel = ViewModelProvider(this).get(UserdetailsViewModel::class.java)

        setObserver()
        data = ArrayList()

//        rvSavedSearchesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        rvSavedSearches.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        bottomNav.visibility= View.GONE
        tvToolbar.setText("My Account")
//        backpress button
        ivBackpress.setOnClickListener {

            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
        }

        tvAccountSettings.setOnClickListener {
            tvAccountSettings.setBackgroundResource(R.drawable.login_register_shape_green)
            tvAccountSettings.setTextColor(Color.parseColor("#FFFFFFFF"))
            tvSavedSearches.setTextColor(Color.parseColor("#898989"))
            tvSavedSearches.setBackgroundResource(R.drawable.account_two_tab_shape)
//                addNewFragment(LoginFragment(),R.id.mainContainer,false)
            llAccountSettings.visibility = View.VISIBLE
            llSavedSearches.visibility = View.GONE
        }

        tvSavedSearches.setOnClickListener {
            tvSavedSearches.setBackgroundResource(R.drawable.login_register_shape_green)
            tvAccountSettings.setBackgroundResource(R.drawable.account_two_tab_shape)
            tvAccountSettings.setTextColor(Color.parseColor("#898989"))
            tvSavedSearches.setTextColor(Color.parseColor("#FFFFFFFF"))
//                addNewFragment(SignUpFragment(),R.id.mainContainer,false)
            llSavedSearches.visibility = View.VISIBLE
            llAccountSettings.visibility = View.GONE

        }

        viewModel.userDetailsRequest(pref.getString(Constants.TOKEN))

        viewModel.getAllStatesRequest(pref.getString(Constants.TOKEN))

        viewModel.getSavedSearchesRequest(pref.getString(Constants.TOKEN))



        fragmentBackPressHandle()
        ivEdit.setOnClickListener {
            bottomSheetDialog()
        }

        llLogout.setOnClickListener {
            */
/*pref.setString(Constants.IS_FROM_LOGIN,"")
            pref.setString(Constants.USER_NAME,"")
            pref.setString(Constants.userAccountID,"")
            pref.setString(Constants.TOKEN,"")*/
/*


            LoginManager.getInstance().logOut();

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut()


            pref.clearPref()

            val intent = Intent(requireContext(), LoginSignUpActivity::class.java)
            SessionData.I().makeIntentClearHistory(intent)
            startActivity(intent)
        }

        llChangePassword.setOnClickListener {
            changePasswordbottomSheetDialog()
        }

        tvChangePassword.setOnClickListener {
            changePasswordbottomSheetDialog()
        }


        if (pref.getBoolean(Constants.TOGGLE_SWITCH) == true) {
            buttonSwitch.setChecked(true);
        } else if (pref.getBoolean(Constants.TOGGLE_SWITCH) == false) {
            buttonSwitch.setChecked(false);
        }

        buttonSwitch.setShadowEffect(true);//disable shadow effect
        buttonSwitch.setOnCheckedChangeListener(SwitchButton.OnCheckedChangeListener { view, isChecked ->
            if (buttonSwitch.isChecked) {
                var manageNotificationsBody = ManageNotificationsBody(true)
                pref.setBoolean(Constants.TOGGLE_SWITCH, true)
                viewModel.manageNotificationRequest(
                    manageNotificationsBody,
                    pref.getString(Constants.TOKEN)
                )
            } else {
                var manageNotificationsBody = ManageNotificationsBody(false)
                pref.setBoolean(Constants.TOGGLE_SWITCH, false)
                viewModel.manageNotificationRequest(
                    manageNotificationsBody,
                    pref.getString(Constants.TOKEN)
                )
            }
        })

    }

    fun setObserver() {

        viewModel.manageNotificationsResponseResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.editProfileResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                viewModel.userDetailsRequest(pref.getString(Constants.TOKEN))
                //       showShortToast(it.message!!)

            }
        }
        )

        viewModel.getAllStatesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                getAllStatesResponseList.addAll(it.model)
                for (i in 0 until getAllStatesResponseList.size) {
                    stateNameList.add(getAllStatesResponseList.get(i).stateName)
                }
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.getUserDetailsResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                accFirstName = it.model.firstName
                accLastName = it.model.lastName
                accEmail = it.model.email
                accPhone = it.model.phone
                accStreetAddress = it.model.streetAddress
                accCity = it.model.city
                accSt = it.model.st
                accClubGroupName = it.model.groupName
                accZipCode = it.model.zip
                accAddress = accStreetAddress + ", " + accCity + ", " + accSt + ", " + accZipCode
                tvAccountName.setText(accFirstName + accLastName)
                tvAccountEmail.setText(accEmail)
                if (accPhone.isEmpty()) {
                    tvAccountPhoneNumber.setText("Mobile Number")
                } else {

                    Log.e("$$$$", "tvAccountPhoneNumber...." + accPhone.length)

                    if (accPhone != null) {
                        if (accPhone.length >= 10) {
                            var phoneNumber = "" + accPhone
                            var open = "("
                            var close = ")"
                            var code = " "
                            code = open + phoneNumber.substring(
                                0,
                                3
                            ) + close + " " + phoneNumber.substring(
                                3,
                                6
                            ) + "-" + phoneNumber.substring(6, 10)
                            tvAccountPhoneNumber.setText(code)
                        } else {
                            tvAccountPhoneNumber.setText(accPhone)
                        }
                    }
                }

                tvAccountAddress.setText(accAddress)

                if (accClubGroupName != null) {
                    tvClubname.setText(accClubGroupName)
                }
                */
/* if (accAddress.contains("")||accAddress.isNullOrBlank()){
                     Log.e("$$$$","accAddress...empty  ."+accAddress)

                     tvAccountAddress.setText("Address")
                 }else{
                     tvAccountAddress.setText(accAddress)
                     Log.e("$$$$","tvAccountAddress...."+accAddress)
                 }*//*

                */
/*if (accClubGroupName.contains("")||accClubGroupName.isNullOrBlank()){
                    tvClubname.setText("Club Name")
                }else{
                    tvClubname.setText(accClubGroupName)
                    Log.e("$$$$","tvClubname...."+accClubGroupName)
                }
*//*

//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.changePasswordResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                showShortToast(it.message!!)
            }
        }
        )
////
        viewModel.getSavedSearchesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
                showShortToast(it.message!!)
            } else {

                rvSavedSearches.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)




                savedSearchesList.addAll(it!!.model!!)

                Log.e("call", "@@@@ 123 " + savedSearchesList.toString())



                for (i in 0 until it!!.model!!.size) {
                    if (it.model!!.get(i).RegionName.size > 0
                        || it.model!!.get(i).PropertyName.size > 0
                        || it.model!!.get(i).StateName.size > 0
                        || it.model!!.get(i).Amenities.size > 0
                        || it.model!!.get(i).SearchName != null
                    ) {
                    } else {
                        savedSearchesList!!.remove(it.model!!.get(i))
                    }
                }

                Log.e("call", "@@@@ 1234444 " + savedSearchesList.toString())


                rvSavedSearches.adapter =
                    SavedSearchesAdapter(savedSearchesList, activity, this, this@MyAccountFragment)
//                showShortToast(it.message!!)
            }
        }
        )

        viewModel.apiError.observe(requireActivity(), Observer {
            showShortToast(it)
        }
        )

        viewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (it) {
                if (progressBarPB != null)
                    progressBarPB.show()
            } else {
                if (progressBarPB != null)
                    progressBarPB.dismiss()
            }
        }
        )

    }


    fun bottomSheetDialog() {

        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_dialog_box)

        dialog.show()

        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.BOTTOM)

//    var edtAccountEnterName = dialog.findViewById<TextView>(R.id.edtAccountEnterName) as EditText?
        var edtAccountEnterFirstName = dialog.findViewById<EditText>(R.id.edtAccountEnterFirstName)
        var edtAccountEnterlastName = dialog.findViewById<EditText>(R.id.edtAccountEnterlastName)
        var edtAccountEnterEmail = dialog.findViewById<EditText>(R.id.edtAccountEnterEmail)
        var edtstreetAddress = dialog.findViewById<EditText>(R.id.edtstreetAddress)
        var edtCity = dialog.findViewById<EditText>(R.id.edtCity)
        var pvStateName = dialog.findViewById<PowerSpinnerView>(R.id.pvStateName)
        var edtAccountEnterPhoneNumber =
            dialog.findViewById<EditText>(R.id.edtAccountEnterPhoneNumber)
        var edtZipCode = dialog.findViewById<EditText>(R.id.edtZipCode)
        var edtGroupClubName = dialog.findViewById<EditText>(R.id.edtGroupClubName)

        var tvSaveDialog = dialog.findViewById<TextView>(R.id.tvSaveDialog)
        var tvCancelDialog = dialog.findViewById<TextView>(R.id.tvCancelDialog)


        edtAccountEnterFirstName!!.setText(accFirstName)
        edtAccountEnterlastName!!.setText(accLastName)
        edtAccountEnterEmail!!.setText(accEmail)
        edtstreetAddress!!.setText(accStreetAddress)
        edtCity!!.setText(accCity)
        pvStateName!!.setText(accSt)
        edtAccountEnterPhoneNumber!!.setText(accPhone)
        edtZipCode!!.setText(accZipCode)
        edtGroupClubName!!.setText(accClubGroupName)

        pvStateName?.setItems(stateNameList)


        tvSaveDialog!!.setOnClickListener {
            var enterfirstname: String = edtAccountEnterFirstName!!.getText().toString()
            var enterlastname: String = edtAccountEnterlastName!!.getText().toString()
            var enteremail: String = edtAccountEnterEmail!!.getText().toString()
            var enterstreetaddress: String = edtstreetAddress!!.getText().toString()
            var entercity: String = edtCity!!.getText().toString()
            var enterphoneno: String = edtAccountEnterPhoneNumber!!.getText().toString()
            var enterzipcode: String = edtZipCode!!.getText().toString()
            var entergroupclubname: String = edtGroupClubName!!.getText().toString()


            if (edtAccountEnterFirstName.text.isEmpty()) {
                showShortToast("Please enter first name.")
            } else if (edtAccountEnterlastName.text.isEmpty()) {
                showShortToast("Please enter lasr name.")
            } else if (edtAccountEnterEmail.text.isEmpty()) {
                showShortToast("Please enter Email name")
            } else if (edtstreetAddress.text.isEmpty()) {
                showShortToast("Please enter street address")
            } else if (edtCity.text.isEmpty()) {
                showShortToast("Please enter city")
            } else if (edtAccountEnterPhoneNumber.text.isEmpty()) {
                showShortToast("Please enter phone number")
            } else if (edtAccountEnterPhoneNumber.text.toString().length !=10) {
                showShortToast("Phone Number must contain 10 digits")
            } else if (edtZipCode.text.isEmpty()) {
                showShortToast("Please enter zip code")
            } else if (edtZipCode.text.toString().length!=5) {
                showShortToast("ZipCode must contain 5 digits")
            }  else if (edtGroupClubName.text.isEmpty()) {
                showShortToast("Please enter group name")
            } else if (!isEmailValid(edtAccountEnterEmail.text.toString())) {
                showShortToast("Please enter valid email")
            } else {
                val editProfileBody = EditProfileBody(
                    "",
                    "" + entercity,
                    "",
                    enteremail,
                    enterfirstname,
                    true,
                    "" + entergroupclubname,
                    true,
                    enterlastname,
                    "" + enterphoneno,
                    "" + enterstreetaddress,
                    pvStateName.text.toString(),
                    1,
                    enterstreetaddress,
                    23,
                    2,
                    "" + enterzipcode
                )
                viewModel.editProfileRequest(editProfileBody, pref.getString(Constants.TOKEN))
                */
/*tvAccountEnterName.setText(enterfirstname+enterlastname)
                tvAccountEnterEmail.setText(enteremail)
                tvAccountEnterPhoneNumber.setText(enterphoneno)
                tvAccountEnterAddressOne.setText(enterstreetaddress+entercity)
                tvClubname.setText(entergroupclubname)*//*

                dialog.dismiss()
            }

        }

        tvCancelDialog!!.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun changePasswordbottomSheetDialog() {

        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.change_password_popup)

        dialog.show()

        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.BOTTOM)


        var edtCurrentPassword = dialog.findViewById<EditText>(R.id.edtCurrentPassword)
        var edtNewPassword = dialog.findViewById<EditText>(R.id.edtNewPassword)
        var edtConfirmNewPassword = dialog.findViewById<EditText>(R.id.edtConfirmNewPassword)
        var tvResetPassword = dialog.findViewById<TextView>(R.id.tvResetPassword)
        var tvCancelPwd = dialog.findViewById<TextView>(R.id.tvCancelPwd)


        tvResetPassword!!.setOnClickListener {
            var entercurrentpassword: String = edtCurrentPassword!!.getText().toString()
            var enternewpassword: String = edtNewPassword!!.getText().toString()
            var enterconfirmnewpassword: String = edtConfirmNewPassword!!.getText().toString()

            if (edtCurrentPassword.text.isEmpty()) {
                showShortToast("Please enter current password.")
            } else if (edtNewPassword.text.isEmpty()) {
                showShortToast("Please enter new password.")
            } else if (edtConfirmNewPassword.text.isEmpty()) {
                showShortToast("Please enter confirm password.")
            } else if (enternewpassword != enterconfirmnewpassword) {
                showShortToast("Password and confirm password did not match!")
            } else {
                val changePasswordBody = ChangePasswordBody(
                    entercurrentpassword, enternewpassword
                )
                viewModel.changePasswordRequest(changePasswordBody, pref.getString(Constants.TOKEN))
                dialog.dismiss()
            }

        }

        tvCancelPwd!!.setOnClickListener { dialog.dismiss() }


    }

    override fun onChatListListener(index: Int) {
    }

    override fun onClickListListener(index: Int) {
    }

    fun removeitem(userId: String?) {
        pref = SharedPref(requireContext())
        val deleteSearchBody = DeleteSearchBody(
            "" + userId
        )
        viewModel.deletesearchRequest(deleteSearchBody, pref.getString(Constants.TOKEN))
    }


}*/