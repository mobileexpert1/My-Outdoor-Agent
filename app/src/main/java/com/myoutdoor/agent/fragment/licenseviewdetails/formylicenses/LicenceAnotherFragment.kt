package com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.example.example.AdditionalInvoices
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.*
import com.myoutdoor.agent.fragment.licence.LicenceFragment
import com.myoutdoor.agent.fragment.licence.PaymentWebViewFragment
import com.myoutdoor.agent.fragment.licenseviewdetails.formylicenses.adapter.DocumentAdapterr
import com.myoutdoor.agent.models.addclubmember.AddClubMemberBody
import com.myoutdoor.agent.models.addupdatevehicle.AddUpdateVehicleBody
import com.myoutdoor.agent.models.clubmemberdetails.ClubMemberDetailsBody
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenBody
import com.myoutdoor.agent.models.invitemember.InviteMemberBody
import com.myoutdoor.agent.models.licensedetails.formylicense.license.*
import com.myoutdoor.agent.models.memberRemove.MemberRemoveBody
import com.myoutdoor.agent.models.propertylicense.PropertyLicenseBody
import com.myoutdoor.agent.models.vehicleremove.VehicleRemoveBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.myoutdoor.agent.utils.setFormattedDollarNumber
import com.myoutdoor.agent.utils.setFormattedNumber
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_licence_another.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList


class LicenceAnotherFragment : BaseFragment(), OnPageChangeListener,InvoiceTypeAdapter.OnItemClickListener {

    var publicKey : String = ""

    var getAllStatesResponseList = ArrayList<com.myoutdoor.agent.models.getallstates.Model>()
    var stateNameList = ArrayList<String>()
    var getAllAvailableStatesResponseList =
        ArrayList<com.myoutdoor.agent.models.getavailablestates.Model>()
    var addMembers: String = ""
    var forAddMember: Boolean = false
    var showDialogBox: Dialog? = null
    lateinit var viewModel: MyLicenseDetailV2ViewModel
    lateinit var pref: SharedPref
    var key: String = ""
    var pageNumber = 0
    val bundle = Bundle()
    var mylicensModel: Model? = null
    lateinit var licenseagreement: String
    lateinit var guidelinesList: ArrayList<String>
    var list1 = ArrayList<Amenity>()
    var list2 = ArrayList<Amenity>()
    var list3 = ArrayList<MapFile>()
    var list4 = ArrayList<VehicleDetails>()
    var additionalinvoicelist= ArrayList<AdditionalInvoices>()
    var selectedState: String = ""
    var producttypeID: String = ""
    var messgepdf: String = ""
    var messgepdf2: String = ""
    var licenseContractID: Int = 0
    var agreementName: String = ""
    var vehicleDetailID: String = ""
    private lateinit var invoiceTypeAdapter: InvoiceTypeAdapter
    var tagsList = ArrayList<String>()
    var colors = ArrayList<IntArray>()
    var adInvoiceID= 0
    var amount:Double? = null
    var productId=0
    var producttypeId=0
    var productNum=""
    var invoiceID=0
    var paymentDate=""
    var dateformat=""
    var renewalKey=""

    var licenseMembers: ArrayList<LicenseMember> = ArrayList()

    var isAddVehicleClicked: Boolean = false

    var specialConditions: ArrayList<SpecialCondition> = ArrayList()

    private lateinit var documentsAdapter: DocumentAdapterr

    override fun getLayoutId(): Int {
        return R.layout.fragment_licence_another
    }

    override fun onCreateView() {

        showDialogBox = Dialog(requireContext())
        tvToolbar.setText("My Licenses")
        pref = SharedPref(requireContext())
        guidelinesList = ArrayList()

        viewModel = ViewModelProvider(this).get(MyLicenseDetailV2ViewModel::class.java)

        setObserver()

        ivBackpress.setOnClickListener {
//            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
            requireActivity().onBackPressed()
        }



        val bundle = this.arguments
        if (bundle != null) {
            key = bundle.getString("publickey2")!!
            Log.e("call", "key " + key)
        }



        var myLicenseDetailV2Body = MyDetailsV2Body(
            //"" + "lsc_upbj5gpj"
        ""+key
        )



        viewModel.myLicenseDetailV2Request(myLicenseDetailV2Body, pref.getString(Constants.TOKEN))

        tvPropertyLicenseAgrment.setOnClickListener {

            var propertyLicenseBody = PropertyLicenseBody(
                licenseContractID.toInt()
            )
            viewModel.propertyLicenseRequest(propertyLicenseBody, pref.getString(Constants.TOKEN))
        }

        tvClubMembershipCards.setOnClickListener {

            var clubMemberDetailsBody = ClubMemberDetailsBody(
                licenseContractID.toInt()
            )
            viewModel.clubMemberDetailsRequest(
                clubMemberDetailsBody,
                pref.getString(Constants.TOKEN)
            )
        }

        tvAddVehicleInfo.setOnClickListener {
            isAddVehicleClicked = true
            viewModel.getAllStatesRequest(pref.getString(Constants.TOKEN))

//            progressBarPB.show()
        }

        tvInviteMembers.setOnClickListener {

            if (forAddMember) {
                isAddVehicleClicked = false
                viewModel.getAllStatesRequest(pref.getString(Constants.TOKEN))
            } else {
                inviteMembersPopup()
            }

        }

    }

    private fun showPDFPopup() {

        showDialogBox!!.setContentView(R.layout.popup_show_pdf)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)
        val pdfView = showDialogBox!!.findViewById(R.id.pdfView) as PDFView

        var url: String = Constants.PDF_URL + messgepdf2


        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
        }

        RetrievePDFFromURL(pdfView).execute(url)

        showDialogBox!!.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)

    }

    private fun showClubPDFPopup() {

        showDialogBox!!.setContentView(R.layout.popup_show_pdf)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)
        val pdfView = showDialogBox!!.findViewById(R.id.pdfView) as PDFView

        var url: String = Constants.PDF_URL + messgepdf

        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
        }

        RetrievePDFFromURL(pdfView).execute(url)

        showDialogBox!!.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)

    }

    private fun addVehiclePopup() {

        showDialogBox!!.setContentView(R.layout.popup_vehicle_information)
        progressBarPB.setCanceledOnTouchOutside(true)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val tvSaveChanges: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvSaveChanges)
        val tvCancelChanges: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvCancelChanges)
        val edtVehicleMake: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtVehicleMake)
        val edtVehicleColor: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtVehicleMake)
        val edtVehicleModel: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtVehicleModel)
        val edtVehicleLicensePlate: EditText =
            showDialogBox!!.findViewById<EditText>(R.id.edtVehicleLicensePlate)
        val psSelectState: PowerSpinnerView =
            showDialogBox!!.findViewById<PowerSpinnerView>(R.id.psSelectState)
        psSelectState.setItems(stateNameList)
        psSelectState.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            // do something
            selectedState = newItem
        }

        tvSaveChanges.setOnClickListener {
            if (edtVehicleMake.text.toString().contains("") && edtVehicleModel.text.toString()
                    .contains("") &&
                edtVehicleModel.text.toString().contains("") &&
                edtVehicleLicensePlate.text.toString().contains("") && selectedState == ""
            ) {
                showAlert("All parameters are required")
            } else {
                var addUpdateVehicleBody = AddUpdateVehicleBody(
                    licenseContractID, "" + edtVehicleColor.text.toString(),
                    "0", "" + edtVehicleLicensePlate.text.toString(),
                    "" + edtVehicleMake.text.toString(),
                    "" + edtVehicleModel.text.toString(), "" + selectedState, "",
                )

                viewModel.addUpdateVehicleRequest(
                    addUpdateVehicleBody,
                    pref.getString(Constants.TOKEN)
                )
                showDialogBox!!.cancel()
            }
        }

        tvCancelChanges.setOnClickListener {
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

    private fun addClientFeature() {


        showDialogBox!!.setContentView(R.layout.pop_up_client_features)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val etFirstName: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtFirstName)
        val etLastName: EditText = showDialogBox!!.findViewById<EditText>(R.id.etLastName)
        val edtEmail: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtEmail)
        val etAddress: EditText = showDialogBox!!.findViewById<EditText>(R.id.etAddress)
        val edtPhoneNumber: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtPhoneNumber)
        val etZipCode: EditText = showDialogBox!!.findViewById<EditText>(R.id.etZipCode)
        val edtCity: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtCity)
        val tvClientSaveChanges: TextView =
            showDialogBox!!.findViewById<TextView>(R.id.tvClientSaveChanges)
        val tvClientCancel: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvClientCancel)

        val psStateClientfeatures: PowerSpinnerView =
            showDialogBox!!.findViewById<PowerSpinnerView>(R.id.psStateClientfeatures)

        Log.e(
            "call",
            "getAllAvailableStatesResponseList :   " + getAllAvailableStatesResponseList.toString()
        )

        psStateClientfeatures.setItems(stateNameList)

        psStateClientfeatures.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            // do something
            selectedState = newItem
        }

        tvClientSaveChanges.setOnClickListener {

            if (etFirstName.text.isEmpty()) {
                showShortToast("Please enter name.")
            } else if (etLastName.text.isEmpty()) {
                showShortToast("Please enter last name.")
            } else if (edtEmail.text.isEmpty()) {
                showShortToast("Please enter email.")
            } else if (!isEmailValid(edtEmail.text.toString())) {
                showShortToast("Please enter valid email.")
            } else if (etAddress.text!!.isEmpty()) {
                showShortToast("Please enter address.")
            } else if (edtPhoneNumber.text.toString().isEmpty()) {
                showShortToast("Please enter phone number.")
            } else if (!android.util.Patterns.PHONE.matcher(edtPhoneNumber.text.toString())
                    .matches()
            ) {
                showShortToast("Please enter valid phone number")
            } else if (edtPhoneNumber.text.toString().length != 10) {
                showShortToast("Phone Number must contain 10 digits")
            } else if (etZipCode.text!!.isEmpty()) {
                showShortToast("Please enter zipcode.")
            } else if (etZipCode.text!!.toString().length != 5) {
                showShortToast("ZipCode must contain 5 digits")
            } else if (edtCity.text.toString()!!.isEmpty()) {
                showShortToast("Please enter city.")
            } else if (selectedState.toString()!!.isEmpty()) {
                showShortToast("Please select state.")
            } else {
                val addClubMemberBody = AddClubMemberBody(
                    etAddress.text.toString(),
                    edtCity.text.toString(),
                    edtEmail.text.toString(),
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    licenseContractID,
                    edtPhoneNumber.text.toString(),
                    selectedState,
                    etZipCode.text.toString()
                )
                viewModel.addClubMemberRequest(addClubMemberBody, pref.getString(Constants.TOKEN))
            }
        }

        tvClientCancel.setOnClickListener {
            showDialogBox!!.dismiss()
        }
        showDialogBox!!.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)

    }


    private fun inviteMembersPopup() {

        showDialogBox!!.setContentView(R.layout.popup_invite_members)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val tvAddMember: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvAddMember)
        val tvCancelMember: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvCancelMember)
        val edtEmailAddress: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtEmailAddress)
        val tagContainerLayout: TagContainerLayout =
            showDialogBox!!.findViewById<TagContainerLayout>(R.id.tagContainerLayout)
        tagContainerLayout.isEnableCross = true
        tagsList = ArrayList<String>()


        edtEmailAddress.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                Log.e("call", "@@@12 " + s)
                if (s.contains(",")) {
                    var email = edtEmailAddress.text.toString()
                    email = email.replace(",", "")
                    if (!isEmailValid(email)) {
                        showShortToast("Please enter valid email.")
                    } else {
                        tagsList.add(email)
                        tagContainerLayout.setTags(tagsList)
                        edtEmailAddress.setText("")
                    }
                }
            }
        })

        edtEmailAddress.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {

                if (!edtEmailAddress.text.toString().equals("")) {
                    if (!isEmailValid(edtEmailAddress.text.toString())) {
                        showShortToast("Please enter valid email.")
                    } else {
                        tagsList.add(edtEmailAddress.text.toString())
                        tagContainerLayout.setTags(tagsList)
                        edtEmailAddress.setText("")
                    }
                }
                true
            } else {
                false
            }
        }

        tagContainerLayout.setOnTagClickListener(object : OnTagClickListener {
            override fun onTagClick(position: Int, text: String) {

            }

            override fun onTagLongClick(position: Int, text: String) {

            }

            override fun onSelectedTagDrag(position: Int, text: String) {

            }

            override fun onTagCrossClick(position: Int) {
                if (tagsList.size > 0) {
                    tagsList.remove(tagsList.get(position))
                }
                tagContainerLayout.setTags(tagsList)
            }
        })



        tvAddMember.setOnClickListener {

            Log.e("call", "@@@!23 contractiD" + licenseContractID.toString())
            Log.e("call", "@@@!23 taglist " + tagsList.toString())

            if (tagsList.size > 0) {
                for (i in 0 until tagsList.size) {
                    if (i == 0) {
                        addMembers = tagsList.get(i)
                    } else {
                        addMembers = addMembers + "," + tagsList.get(i)
                    }
                }
            }

            if (tagsList.size > 0) {
                var inviteMemberBody = InviteMemberBody(
                    "" + licenseContractID,
                    "" + addMembers
                )

                Log.e("call", "@@@!23 taglisttt body " + inviteMemberBody.toString())

                viewModel.inviteMemberRequest(inviteMemberBody, pref.getString(Constants.TOKEN))
                showDialogBox!!.cancel()
            } else {
                if (edtEmailAddress.text.toString().equals("")) {
                    showAlert("Please enter email address")
                } else if (!isEmailValid(edtEmailAddress.text.toString())) {
                    showShortToast("Please enter valid email.")
                } else {
                    addMembers = edtEmailAddress.text.toString()
                    var inviteMemberBody = InviteMemberBody(
                        "" + licenseContractID,
                        "" + addMembers
                    )

                    viewModel.inviteMemberRequest(inviteMemberBody, pref.getString(Constants.TOKEN))
                    showDialogBox!!.cancel()
                }
            }
        }

        tvCancelMember.setOnClickListener {
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

    fun removeitem(vehicleDetailID: String?) {
        pref = SharedPref(requireContext())
        val vehicleRemoveBody = VehicleRemoveBody(
            "" + vehicleDetailID
        )
        viewModel.vehicleRemoveRequest(vehicleRemoveBody, pref.getString(Constants.TOKEN))
    }

    fun removeMember(memberId: String?) {
        pref = SharedPref(requireContext())
        val memberRemoveBody = MemberRemoveBody(
            "" + memberId
        )
        Log.e("call", "@@@!@#  " + memberId)
        viewModel.memberRemoveRequest(memberRemoveBody, pref.getString(Constants.TOKEN))
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
    }

    private fun showDocumentsAdapter(clientDocuments: List<com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3.ClientDocument>) {
//        Toast.makeText(requireContext(), "clientDocuments", Toast.LENGTH_SHORT).show()
        if (clientDocuments.isNullOrEmpty()) {
            rvDocuments.visibility = View.GONE
            tvNoDocumentAvailable.visibility = View.VISIBLE
        } else {
            rvDocuments.visibility = View.VISIBLE
            tvNoDocumentAvailable.visibility = View.GONE
            rvDocuments.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            documentsAdapter = DocumentAdapterr(clientDocuments) { pos, data ->
//                var pdfUrl=Constants.AMENITIES_URL+"/"+data.documentName
                var pdfUrl = Constants.DOC_URL+"${data.clientID}/" + data.documentName
//             data.documentName
//             openPdfInBrowser(requireContext(),pdf)
                // Google Drive PDF viewer URL
                Log.e("PDFUrl", "$pdfUrl")
                showPDFPopup(pdfUrl)
//                openPdfInBrowser(requireContext(),pdf)
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(pdfUrl)
//                startActivity(intent)
            }
            rvDocuments.adapter = documentsAdapter
        }
    }

    fun showPDFPopup(pdfUrl: String) {
        showDialogBox = Dialog(requireContext())

        showDialogBox!!.setContentView(R.layout.popup_show_pdf)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)

        val pdfView = showDialogBox!!.findViewById(R.id.pdfView) as PDFView
        val progressBar = showDialogBox!!.findViewById(R.id.progressBar) as ProgressBar

        var url: String = pdfUrl

        com.myoutdoor.agent.utils.RetrievePDFFromURL(pdfView, progressBar).execute(url)

        ivClose.setOnClickListener {
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


        viewModel.myLicenseDetailV2ResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())


         //   publicKey =   it!!.model!!.licenseDetails.publicKey.toString()


            //Renewal status


            if (it!!.model.licenseDetails.renewalStatus == 1){
                layout_renew.visibility = View.VISIBLE
            }else {
                layout_renew.visibility = View.GONE
            }




            if (it.message != "Success") {
            }
            else {
                renewalKey=it!!.model.renewalActivity.publicKey
                if (it.model.clientFeatures.addClubMembers) {
                    forAddMember = true
                    tvInviteMembers.setText("Add Members")
                } else {
                    forAddMember = false
                    tvInviteMembers.setText("Invite Members")
                }


                licenseagreement = it!!.model!!.licenseAgreement
                producttypeID = it!!.model!!.licenseDetails.productTypeID.toString()
                licenseContractID = it!!.model!!.licenseDetails.licenseContractID

                licenseagreement = it!!.model!!.licenseAgreement

                tvHead.setText("" + it!!.model!!.licenseDetails.productNo)

                tvLicLocation.setText("" + it!!.model!!.licenseDetails.countyName + ", " + it!!.model!!.licenseDetails.stateName)
                tvPropertyLicenceHolder.setText("" + it!!.model!!.licenseDetails.firstName + " " + it!!.model!!.licenseDetails.lastName)
                tvEmail.setText("" + it!!.model!!.licenseDetails.email)
//                 tvPropertyContact.setText(""+it!!.model!!.licenseDetails.contactNumber)




                // Payment Date


                val dateInString = it!!.model.renewalActivity.paymentDueDate
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val outputFormat = SimpleDateFormat("dd-MMMM-yyyy")
                try {
                    val date: Date = inputFormat.parse(dateInString)
                    dateformat=outputFormat.format(date)
                    System.out.println("Date ->" + outputFormat.format(date))
                } catch (e: ParseException) {
                    System.out.println("Date ->" + e.toString())
                    e.printStackTrace()
                }

                tvPaymentDate.setText(" Payment Due Date:" + dateformat)
                tvPaymentDate.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.blink_animation));




                var accPhone: String = it!!.model!!.licenseDetails.phone

                Log.e("@@@@@","contactnuymber...."+accPhone)

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
                        tvPropertyContact.setText(code)
                    } else {
                        tvPropertyContact.setText(accPhone)
                    }
                }

                tvGroupClub.setText("" + if (it!!.model!!.licenseDetails.groupName!=null) it!!.model!!.licenseDetails.groupName else "-" )
                tvPropertyNumber.setText("" + it!!.model!!.licenseDetails.productNo)
                tvPriceDec.setFormattedDollarNumber(it!!.model!!.licenseDetails.licenseFee)
                tvllAcres.setFormattedNumber(it!!.model!!.licenseDetails.acres)
                var startDate = dateFormat(it!!.model!!.licenseDetails.licenseStartDate)
                var endDate = dateFormat(it!!.model!!.licenseDetails.licenseEndDate)
                tvLicenceStartDateDesc.setText("" + startDate)
                tvLicenceEndDateDec.setText("" + endDate)
                tvLicenseStatus.setText("" + it!!.model!!.licenseDetails.contractStatus)
                tvPaymentStatus.setText("" + it!!.model!!.licenseDetails.paymentStatus)

                if (it.model.licenseDetails.displayDescription.isNullOrEmpty()) {
                    tvPropertyOverview.setText("No Property Description Available")
                    tvPropertyOverview.gravity = View.TEXT_ALIGNMENT_CENTER
                } else {
                    tvPropertyOverview.setText("" + it!!.model!!.licenseDetails.displayDescription)
                }


                specialConditions.clear()
                specialConditions.addAll(it!!.model!!.specialConditions)
                rvSpecialConditionsGuidelines.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvSpecialConditionsGuidelines.adapter =
                    LicenceGuideLinesAdapter(specialConditions, activity)

                for (i in 0 until it!!.model.amenities.size) {
                    if (it!!.model.amenities.get(i).amenityType.equals("Activity")) {
                        list1.add(it.model.amenities.get(i))
                    } else if (it!!.model.amenities.get(i).amenityType.equals("Amenity")) {
                        list2.add(it.model.amenities.get(i))
                    }
                }

                if (list1.isEmpty()) {
                    rvActivities.visibility = View.GONE
                    tvActivities.visibility = View.VISIBLE
                } else {
                    rvActivities.visibility = View.VISIBLE
                    tvActivities.visibility = View.GONE
                }

                if (list2.isEmpty()) {
                    rvAmenities.visibility = View.GONE
                    tvAmenities.visibility = View.VISIBLE
                } else {
                    rvAmenities.visibility = View.VISIBLE
                    tvAmenities.visibility = View.GONE
                }

                rvActivities.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvActivities.adapter = LicenseAmenityAdapter(list1, activity)

                rvAmenities.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvAmenities.adapter = LicenseAmenityAdapter(list2, activity)


                for (i in 0 until it!!.model.mapFiles.size) {
                    list3.add(it.model.mapFiles.get(i))
                }

                rvPropertyMaps.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvPropertyMaps.adapter = PropertyMapsAdapter(list3, activity)

                if (list3.isEmpty()) {
                    rvPropertyMaps.visibility = View.GONE
                    tvNoMapsAvailable.visibility = View.VISIBLE
                } else {
                    rvPropertyMaps.visibility = View.VISIBLE
                    tvNoMapsAvailable.visibility = View.GONE
                }

                if (!it!!.model.licenseDetails.activityType.equals("Day Pass")) {
                    if (it!!.model.licenseDetails.allowMemberActions == true) {
                        tvInviteMembers.visibility = View.VISIBLE
                    } else {
                        tvInviteMembers.visibility = View.GONE
                    }
                }


                showDocumentsAdapter(it.model.clientDocument)

//                if (it!!.model.licenseDetails.allowMemberActions == true && it!!.model.clientFeatures.addClubMembers == true) {
//                    tvInviteMembers.setText(getString(R.string.add_members))
//                } else {
//                    tvInviteMembers.setText(getString(R.string.invite_members))
//                }

                licenseMembers.clear()
                licenseMembers.addAll(it.model.licenseMembers)
                if (licenseMembers.size > 0) {
                    tvCurrentMembers.visibility = View.GONE
                    rvCurrentMembers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    rvCurrentMembers.adapter = ShowCurrentMembersAdapter(
                        licenseMembers,
                        activity,
                        this,
                        it!!.model.licenseDetails
                    )
                } else {
                    tvCurrentMembers.visibility = View.VISIBLE
                }

                Log.e("call", "@@@@444 vehicle detail " + it!!.model.vehicleDetails.toString())


                list4.clear()
                list4.addAll(it.model.vehicleDetails)


//                if (it!!.model.vehicleDetails.size == 0) {
//                    vehicleView.visibility = View.GONE
//                    tvVehicleDetails.visibility = View.VISIBLE
//
//                } else {
//                    vehicleView.visibility = View.VISIBLE
//
//                }


                if (it!!.model.licenseDetails.motorizedAccess == true && it!!.model.licenseDetails.productTypeID == 2) {

                    tvAddVehicleInfo.visibility = View.VISIBLE

                    if (it!!.model.licenseDetails.allowMemberActions == true && it!!.model.vehicleDetails.size < 2) {

                        tvAddVehicleInfo.visibility = View.VISIBLE
                    } else {
                        tvAddVehicleInfo.visibility = View.GONE
                    }
                } else {
                     vehicleView.visibility = View.GONE

                    //tvVehicleDetails.visibility = View.GONE

                }

                rvVehicleDetails.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvVehicleDetails.adapter = VehichleDetailsAdapter(list4, activity, this)

                if (list4.isEmpty()) {
                    rvVehicleDetails.visibility = View.GONE
                    tvVehicleDetails.visibility = View.VISIBLE

                } else {
                    rvVehicleDetails.visibility = View.VISIBLE
                    tvVehicleDetails.visibility = View.GONE
                }

//                showShortToast(it.message!!)


                //    Invoice Adapter

                Log.e("call","additionalInvoices  "+it!!.model.additionalInvoices)

                if (it!!.model.additionalInvoices.size.equals(0)){

                    tvInvoicesDetails.visibility=View.VISIBLE
                }else{
                    if(it!!.model.licenseDetails.allowMemberActions  && it!!.model.additionalInvoices.size > 0){
                        additionalinvoicelist = it!!.model.additionalInvoices
                        rvInvoiceDetails.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        invoiceTypeAdapter= InvoiceTypeAdapter(additionalinvoicelist,activity,this, this)
                        rvInvoiceDetails.adapter = invoiceTypeAdapter

                        invoiceTypeAdapter.setOnItemClick(object : InvoiceTypeAdapter.OnItemClickListener {

                            override fun onClickListListener(index: Int) {
                                                          val getPaymentTokenBody = GetPaymentTokenBody(
                                                              "https://myoutdooragent.com/#/app/property?id="+key+"&PaymentStatus=fail",
                                                              adInvoiceID,
                                                              pref.getString(Constants.USER_EMAIL),
                                                              "https://myoutdooragent.com/#/app/property?id="+key+"&Error=fail",
                                                              "acct_1GaRZZLWZ7bSejfX",
                                                              amount!!,
                                                              pref.getString(Constants.USER_NAME),
                                                              productId,
                                                              producttypeId,
                                                              "MOA",
                                                              "https://myoutdooragent.com/",
                                                              productNum,
                                                              pref.getString(Constants.userAccountID).toInt(),
                                                              invoiceID
                                                          )


/*                                val getPaymentTokenBody = GetPaymentTokenBody(
                                    "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&PaymentStatus=fail",
                                    adInvoiceID,
                                    pref.getString(Constants.USER_EMAIL),
                                    "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&Error=fail",
                                    "acct_1GaRZZLWZ7bSejfX",
                                    amount!!,
                                    pref.getString(Constants.USER_NAME),
                                    productId,
                                    producttypeId,
                                    "MOA",
                                    "https://demov2.myoutdooragent.com/",
                                    productNum,
                                    pref.getString(Constants.userAccountID).toInt(),
                                    invoiceID
                                )*/



                                Log.e("call","@@@@!@34   "+getPaymentTokenBody.toString())


                                viewModel.getPaymentToken(getPaymentTokenBody, pref.getString(Constants.TOKEN))
                            }
                        })


                    }
                }





                // Renew Liecense

                tvRenewLicense.setOnClickListener {
                    callToLineceNowFragment()
                }



                adInvoiceID= it!!.model.additionalInvoices[0].adInvoiceID!!
                invoiceID=it!!.model.additionalInvoices[0].invoiceTypeID!!
                amount=it!!.model.additionalInvoices[0].amount!!.toDouble()
                productId= it!!.model.licenseDetails.productID
                producttypeId=it!!.model.licenseDetails.productTypeID
                productNum= it!!.model.licenseDetails.productNo


            }

        }
        )








        viewModel.clubMemberDetailsResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")


            if (producttypeID.contains("1")) {
                if (it.message == "null") {
//                showShortToast(it.message!!)
                    showAlert(it.message!!)
                } else {

//                showShortToast(it.message!!)

                    messgepdf = it.message
                    showClubPDFPopup()

                }

            } else {
                showAlert("No Club Members for the License")
            }


        }
        )

        viewModel.getPaymentTokenResponse.observe(requireActivity(), Observer {


            val fragment = PaymentWebViewFragment()
            bundle.putString(Constants.PAYMENT_TOKEN, it.model.response.paymentToken)
            bundle.putString("publickey", key)
            fragment.setArguments(bundle)
            addNewFragment(fragment, R.id.container, true)

            showShortToast(""+it!!.message)

        }
        )

        viewModel.propertyLicenseResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")


            if (it.message == "null") {
                showAlert(it.message!!)
            } else {
                messgepdf2 = it.message
                showPDFPopup()
            }

        }
        )

        viewModel.vehicleRemoveResponsecSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")


            if (it.message == "null") {

            } else {
            }

        })


        viewModel.addClubMemberResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")

            showShortToast("club member added successfully")

            if (it.message == "null") {

            } else {

            }
        })




        viewModel.inviteMemberResponsecSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")


            if (it.message != "Success") {

            } else {
                showAlertInviteMember("Member added successful to this contract")
            }

        }
        )


        viewModel.addUpdateVehicleResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")


            if (it.message != "Success") {

            } else {
                showAlertInviteMember("Vehicle Info added successfully")
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

                if (isAddVehicleClicked) {
                    addVehiclePopup()
                } else {
                    addClientFeature()
                }

//                showShortToast(it.message!!)
            }
        }
        )




        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
//            showShortToast(it)

            if (list1.isEmpty()) {
                rvActivities.visibility = View.GONE
                tvActivities.visibility = View.VISIBLE
            }

            if (list2.isEmpty()) {
                rvAmenities.visibility = View.GONE
                tvAmenities.visibility = View.VISIBLE
            }

            if (list3.isEmpty()) {
                rvPropertyMaps.visibility = View.GONE
                tvNoMapsAvailable.visibility = View.VISIBLE
            }

            if (list4.isEmpty()) {
                rvVehicleDetails.visibility = View.GONE
                tvVehicleDetails.visibility = View.VISIBLE
            }

            if (licenseMembers.isEmpty()) {
                rvCurrentMembers.visibility = View.GONE
                tvCurrentMembers.visibility = View.VISIBLE
            }

            if (rvInvoiceDetails.isEmpty()){
                rvInvoiceDetails.visibility=View.GONE
                tvInvoicesDetails.visibility=View.VISIBLE
            }

            if (specialConditions.isEmpty()) {
                rvSpecialConditionsGuidelines.visibility = View.GONE
                tvGuidelines.visibility = View.VISIBLE
            }

        }
        )
  viewModel.apiPropertyLicenseError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
      showPDFErrorPopup(it)
  }
        )

        viewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (it) {
                progressBarPB.show()
            } else {
                progressBarPB.dismiss()
            }
        })

    }

    fun callToLineceNowFragment() {
        Log.e("LinecensAnotherKey","publickey "+renewalKey)
        val fragment = LicenceFragment()
        val bundle = Bundle()
        bundle.putString("publickey", renewalKey)
        fragment.setArguments(bundle)
        addNewFragment(fragment, R.id.container, true)
        MainActivity.mainActivity.bottomNav.visibility = View.GONE

    }


    private fun acceptAndPayPopup(agreementName: String) {

        showDialogBox!!.setContentView(R.layout.popup_accept_and_pay)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
//        val layoutManager = LinearLayoutManager(requireContext())
//        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val lp_ = WindowManager.LayoutParams()
        lp_.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp_.width = WindowManager.LayoutParams.MATCH_PARENT
        lp_.height = WindowManager.LayoutParams.WRAP_CONTENT
        showDialogBox!!.getWindow()!!.setAttributes(lp_)

        var pdfViewAP: PDFView = showDialogBox!!.findViewById<PDFView>(R.id.pdfViewAP)
//        var documentWebView: WebView = showDialogBox!!.findViewById<WebView>(R.id.documentWebView)
        var tvAcceptAndPay: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvAcceptAndPay)

        var ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)

        var url: String = Constants.AMENITIES_URL + "Assets/Agreements/" + agreementName


//        RetrievePDFFromURL(pdfViewAP).execute(url)

        tvAcceptAndPay.setOnClickListener {

            //Live Url

/*            val getPaymentTokenBody = GetPaymentTokenBody(

                "https://myoutdooragent.com/#/app/property?id="+key+"&PaymentStatus=fail",
                adInvoiceID,
                pref.getString(Constants.USER_EMAIL),
                "https://myoutdooragent.com/#/app/property?id="+key+"&Error=fail",
                "acct_1GaRZZLWZ7bSejfX",
                amount!!,
                pref.getString(Constants.USER_NAME),
                productId,
                producttypeId,
                "MOA",
                "https://myoutdooragent.com/",
                productNum,
                pref.getString(Constants.userAccountID).toInt(),
                invoiceID
            )*/




            val getPaymentTokenBody = GetPaymentTokenBody(
                "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&PaymentStatus=fail",
                adInvoiceID,
                pref.getString(Constants.USER_EMAIL),
                "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&Error=fail",
                "acct_1GaRZZLWZ7bSejfX",
                amount!!,
                pref.getString(Constants.USER_NAME),
                productId,
                producttypeId,
                "MOA",
                "https://demov2.myoutdooragent.com/",
                productNum,
                pref.getString(Constants.userAccountID).toInt(),
                invoiceID
            )




            viewModel.getPaymentToken(getPaymentTokenBody, pref.getString(Constants.TOKEN))
            showDialogBox!!.cancel()
        }

        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
        }

        showDialogBox!!.show()
    }




    fun showAlertInviteMember(text: String) {
        builder = AlertDialog.Builder(requireActivity())
        builder?.setMessage("")
            ?.setCancelable(false)
            ?.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->

                dialog.dismiss()

                var myLicenseDetailV2Body = MyDetailsV2Body(
                    "" + "key"
                )
                viewModel.myLicenseDetailV2Request(
                    myLicenseDetailV2Body,
                    pref.getString(Constants.TOKEN)
                )
                setObserver()

            })
            ?.setNegativeButton("", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()

            })
        val alert: AlertDialog = builder!!.create()
        alert.setTitle(text)
        alert.show()
    }

    fun showPDFErrorPopup(text: String) {
        builder = AlertDialog.Builder(requireActivity())
        builder?.setMessage(text)
            ?.setCancelable(false)
            ?.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->

                dialog.dismiss()
            })

        val alert: AlertDialog = builder!!.create()
        alert.show()
    }


    class RetrievePDFFromURL(pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        // on below line we are creating a variable for our pdf view.
        val mypdfView: PDFView = pdfView

        override fun onPreExecute() {
            super.onPreExecute()
//            licenceAnotherFragment.progressBarPB.show()
        }

        // on below line we are calling our do in background method.
        override fun doInBackground(vararg params: String?): InputStream? {
            // on below line we are creating a variable for our input stream.

            var inputStream: InputStream? = null
            try {
                // on below line we are creating an url
                // for our url which we are passing as a string.
                val url = URL(params.get(0))

                // on below line we are creating our http url connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

                // on below line we are checking if the response
                // is successful with the help of response code
                // 200 response code means response is successful

                if (urlConnection.responseCode == 200) {
                    // on below line we are initializing our input stream
                    // if the response is successful.
                    inputStream = BufferedInputStream(urlConnection.inputStream)

                }
            }
            // on below line we are adding catch block to handle exception
            catch (e: Exception) {
                // on below line we are simply printing
                // our exception and returning null
                e.printStackTrace()
                return null;
            }
            // on below line we are returning input stream.
            return inputStream;
        }

        // on below line we are calling on post execute
        // method to load the url in our pdf view.
        override fun onPostExecute(result: InputStream?) {
            // on below line we are loading url within our
            // pdf view on below line using input stream.
//            licenceAnotherFragment.progressBarPB.dismiss()
            mypdfView.fromStream(result).load()

        }
    }



    override fun onClickListListener(index: Int) {
        acceptAndPayPopup(agreementName)
    }


}