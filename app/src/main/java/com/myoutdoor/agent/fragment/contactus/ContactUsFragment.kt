package com.myoutdoor.agent.fragment.contactus

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myoutdoor.agent.R

import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.models.contactus.ContactUsBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.toolbar.*


class ContactUsFragment : BaseFragment() {

    lateinit var viewModel: ContactUsViewModel
    lateinit var pref: SharedPref
    lateinit var rlSpinner: RelativeLayout
    lateinit var checkBox: CheckBox


    override fun getLayoutId(): Int {
        return R.layout.fragment_contact_us
    }

    override fun onCreateView() {
        tvToolbar.setText("Contact Us")
        pref = SharedPref(requireContext())
        viewModel = ViewModelProvider(this).get(ContactUsViewModel::class.java)
        rlSpinner = requireView().findViewById(R.id.rlSpinner)
        checkBox = requireView().findViewById(R.id.checkbox)
        val ivSpinner: ImageView = requireView().findViewById(R.id.ivSpinner)
        val edtPermitname: EditText = requireView().findViewById(R.id.edtPermitname)
        setObserver()
//        backpress button
        ivBackpress.setOnClickListener {
            MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
            requireActivity().onBackPressed()
        }

        fragmentBackPressHandle()

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tvChooseoption.visibility = View.GONE
                ivSpinner.visibility = View.GONE
                tvRtu.visibility = View.VISIBLE
                edtPermitname.isEnabled = true
            } else {
                tvChooseoption.visibility = View.VISIBLE
                ivSpinner.visibility = View.VISIBLE
                tvRtu.visibility = View.GONE
                edtPermitname.isEnabled = false


            }
        }
        ivSpinner.setOnClickListener {
            showOptionsDialog(requireContext(), ivSpinner, edtPermitname)
        }

        tvContactusSubmit.setOnClickListener {
            if (edtName.text.isEmpty()) {
                showShortToast("Please enter name.")
            } else if (edtEmail.text.isEmpty()) {
                showShortToast("Please enter Email name.")
            } else if (edtDescription.text.isEmpty()) {
                showShortToast("Please enter description")
            } else if (!isEmailValid(edtEmail.text.toString())) {
                showShortToast("Please enter valid email.")
            } else if (edtPermitname.text.isEmpty()) {
                showShortToast("Please enter this")
            } else {
                val contactUsBody = ContactUsBody(
                    edtDescription.text.toString(),
                    edtEmail.text.toString(),
                    edtName.text.toString(),
                    edtPermitname.text.toString()
                )
                viewModel.contactUsRequest(contactUsBody, pref.getString(Constants.TOKEN))
            }
        }

    }

    fun setObserver() {

        viewModel.contactUsSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            // check for unactivated account
            if (it.message != "Success") {
//                showShortToast(it.message!!)
                edtName.setText("")
                edtEmail.setText("")
                edtDescription.setText("")
                showPopup()
            } else {
//                showShortToast(it.message!!)
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

    fun showPopup() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage(getString(R.string.thank_you_we_have_received_your_message_someone_from_our_time_will_contact_you_soon))

        // builder.setNegativeButton("Ok", null)
        builder.setPositiveButton(
            "Ok"
        ) { dialog, which ->
            requireActivity().onBackPressed()
        }

        val dialog = builder.create()

        dialog.show()
    }

    fun showOptionsDialog(context: Context, ivSpinner: View, edtPermitname: EditText) {
        val options = arrayOf("Account login issue", "Inquiring about a property", "Others")

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.spinner_dialog, null)
        val listViewOptions = dialogView.findViewById<ListView>(R.id.listViewOptions)

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, options)
        listViewOptions.adapter = adapter

        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        listViewOptions.setOnItemClickListener { _, _, position, _ ->
            edtPermitname.setText(options[position])
            dialog.dismiss()
        }

        ivSpinner.setOnClickListener {
            val yOffset = ivSpinner.bottom
            dialog.window?.apply {
                setGravity(Gravity.CENTER)
                attributes = attributes.apply {
                    y = yOffset
                    width = WindowManager.LayoutParams.WRAP_CONTENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                }
            }
            dialog.show()
        }
    }
}



