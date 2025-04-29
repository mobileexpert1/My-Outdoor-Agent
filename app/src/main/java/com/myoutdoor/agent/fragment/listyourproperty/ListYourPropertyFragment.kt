package com.myoutdoor.agent.fragment.listyourproperty


import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.models.listyourproperty.ListYourPropertyBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener
import kotlinx.android.synthetic.main.fragment_list_your_property.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.toolbar.*


class ListYourPropertyFragment : BaseFragment() {

    lateinit var viewModel: ListYourPropertyViewModel
    lateinit var pref: SharedPref
    var landOwnerType:String=""


    override fun getLayoutId(): Int {
        return R.layout.fragment_list_your_property
    }

    override fun onCreateView() {
        tvToolbar.setText("List Your Property")
//        backpress button
        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
        }

        pref= SharedPref(requireContext())
        viewModel = ViewModelProvider(this).get(ListYourPropertyViewModel::class.java)

        psvState.spinnerOutsideTouchListener = object : OnSpinnerOutsideTouchListener {
            override fun onSpinnerOutsideTouch(view: View, event: MotionEvent) {
                psvState.dismiss()
            }
        }

        psvState.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            // do something
            landOwnerType=newItem
        }

        setObserver()

        tvListYourPropertySubmit.setOnClickListener {
            if (edtFirstName.text.isEmpty()) {
            showShortToast("Please enter first name.")
            } else if (edtLastName.text.isEmpty()) {
                showShortToast("Please enter last name.")
            } else if (edtPhone.text.isEmpty()) {
                showShortToast("Please enter phone no.")
            } else if (edtEmail.text.isEmpty()) {
                showShortToast("Please enter email.")
            }  else if (!isEmailValid(edtEmail.text.toString())) {
                showShortToast("Please enter valid email.")
            } else if (landOwnerType=="") {
                showShortToast("Please select type.")
            } else  {
                val listYourPropertyBody = ListYourPropertyBody(
                    edtEmail.text.toString(),
                    edtFirstName.text.toString(),
                    landOwnerType,
                    edtLastName.text.toString(),
                    edtPhone.text.toString()
                )
                viewModel.listYourPropertyRequest(listYourPropertyBody,pref.getString(Constants.TOKEN))
            }

        }
        fragmentBackPressHandle()



    }

    fun setObserver() {
        viewModel.listYourPropertyResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message!="Success"){
                showShortToast(it.message!!)
            }
            else {
                showShortToast(it.message!!)
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
            }
            else {
                progressBarPB.dismiss()
            }
        }
        )

    }




}