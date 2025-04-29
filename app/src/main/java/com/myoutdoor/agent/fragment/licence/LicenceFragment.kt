package com.myoutdoor.agent.fragment.licence

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.*
import com.myoutdoor.agent.fragment.DirectiongoogleMapFragment
import com.myoutdoor.agent.fragment.PreApprovalRequest.PreApprovalRequestFragment
import com.myoutdoor.agent.fragment.licence.model.*
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageRequest
import com.myoutdoor.agent.fragment.mylicences.MyLicencesFragment
import com.myoutdoor.agent.fragment.search.adapter.document.DocumentAdapter
import com.myoutdoor.agent.models.DayPassAvailibility.DayPassRequest
import com.myoutdoor.agent.models.RluMapDetails.RluBody
import com.myoutdoor.agent.models.freeaccess.FreeAccessBody
import com.myoutdoor.agent.models.freeaccess.response.FreeAccessResponse
import com.myoutdoor.agent.models.getPaymentToken.GetPaymentTokenBody
import com.myoutdoor.agent.models.licence.newModel.response.ClientDocuments
import com.myoutdoor.agent.models.licensedetails.renable_licence.add_harvasting.AddHarvestingRequest
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.preapprovalrequest.request.PreApprovalRequest
import com.myoutdoor.agent.models.rightofentry.RightOfEntryBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.BaseFragment.RetrievePDFFromURL
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.myoutdoor.agent.utils.setFormattedDollarNumber
import com.myoutdoor.agent.utils.setFormattedNumber
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

import kotlinx.android.synthetic.main.calender_date_proceed_pop_up.*
import kotlinx.android.synthetic.main.calender_pop_up.*
import kotlinx.android.synthetic.main.fragment_licence.*
import kotlinx.android.synthetic.main.fragment_licence_now.SliderDots
import kotlinx.android.synthetic.main.fragment_licence_now.ivBackpress
import kotlinx.android.synthetic.main.fragment_licence_now.rvActivitiesLN
import kotlinx.android.synthetic.main.fragment_licence_now.rvAmenitiesLN
import kotlinx.android.synthetic.main.fragment_licence_now.rvSimilarPropertiesHV
import kotlinx.android.synthetic.main.fragment_licence_now.tvAcres
import kotlinx.android.synthetic.main.fragment_licence_now.tvAddress
import kotlinx.android.synthetic.main.fragment_licence_now.tvContactPAgent
import kotlinx.android.synthetic.main.fragment_licence_now.tvHplNorthDesc
import kotlinx.android.synthetic.main.fragment_licence_now.tvHplNorthPrice
import kotlinx.android.synthetic.main.fragment_licence_now.tvLicenceEndDateDec
import kotlinx.android.synthetic.main.fragment_licence_now.tvLicenceStartDateDesc
import kotlinx.android.synthetic.main.fragment_licence_now.tvNoActivityAvailable
import kotlinx.android.synthetic.main.fragment_licence_now.tvNoAmenityAvailableHome
import kotlinx.android.synthetic.main.fragment_licence_now.tvPriceDec
import kotlinx.android.synthetic.main.fragment_licence_now.tvPropertyDescription
import kotlinx.android.synthetic.main.fragment_licence_now.tvPropertyID
import kotlinx.android.synthetic.main.fragment_licence_now.tvRequestTempAccess
import kotlinx.android.synthetic.main.fragment_licence_now.tvSimilarProperties
import kotlinx.android.synthetic.main.fragment_licence_now.tvSpecialConditions
import kotlinx.android.synthetic.main.fragment_licence_now.tvTime
import kotlinx.android.synthetic.main.fragment_licence_now.viewPager
import kotlinx.android.synthetic.main.fragment_map_view.tvHybridView
import kotlinx.android.synthetic.main.fragment_map_view.tvMapView
import kotlinx.android.synthetic.main.harvesting_infomation_button.*
import kotlinx.android.synthetic.main.layout_previous_member.*
import kotlinx.android.synthetic.main.leave_us_message_box.*
import kotlinx.android.synthetic.main.leave_us_message_box.tvCancelMessage
import kotlinx.android.synthetic.main.leave_us_message_box.tvSubmitMessage
import kotlinx.android.synthetic.main.license_renewal_pop_up.*
import kotlinx.android.synthetic.main.license_renewel_shape.*
import kotlinx.android.synthetic.main.map_property_details_popup.imgCancelProperty
import kotlinx.android.synthetic.main.map_property_details_popup.ivSeachImage
import kotlinx.android.synthetic.main.map_property_details_popup.rlDetailsMarker
import kotlinx.android.synthetic.main.map_property_details_popup.rlDirectionsMarker
import kotlinx.android.synthetic.main.map_property_details_popup.rlZoomMarker
import kotlinx.android.synthetic.main.map_property_details_popup.tvHplPermitHead
import kotlinx.android.synthetic.main.map_property_details_popup.tvHplPermitPrice
import kotlinx.android.synthetic.main.map_property_details_popup.tvPermitAcres
import kotlinx.android.synthetic.main.map_property_details_popup.tvPermitCountry
import kotlinx.android.synthetic.main.map_property_details_popup.tvZoomOutIn
import kotlinx.android.synthetic.main.pre_approval_request_popup.*
import kotlinx.android.synthetic.main.previous_button.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection


class LicenceFragment : BaseFragment(), SimilarPropertiesAdapter.OnItemClickListener,
    OnMapReadyCallback {


    var list1 = ArrayList<com.myoutdoor.agent.models.licence.newModel.response.Amenity>()
    var list2 = ArrayList<com.myoutdoor.agent.models.licence.newModel.response.Amenity>()

    //  var list3 = ArrayList<SpecialCondition>()
    var list3 = ArrayList<SpecialCondition>()
    var data = ArrayList<com.myoutdoor.agent.models.licence.newModel.response.SimilarProperty>()
    var renewalList = ArrayList<RenewMembers>()
    lateinit var viewModel: LicenceViewModel
    private var dotscount = 0
    val bundle = Bundle()
    var builder2: AlertDialog.Builder? = null
    var key: String = ""
    var productNo: String = ""
    var renewalKey = ""
    lateinit var productID: String
    private lateinit var previousMemberAdapter: PreviousMemberAdapter
    private lateinit var documentsAdapter: DocumentAdapter
    var invoicetypeID = ""
    var productTypeID: Int = 0
    var producttypeID = ""
    var prodID: Int = 0
    var agreementName: String = ""
    lateinit var rluNo: String
    var clientInvoiceId: Int = 0
    var accountId: Int = 0
    var licenceFee: Double? = null
    lateinit var preApprRequestID: String
    var isUserProfileComplete: Boolean = false

    var showDialogBox: Dialog? = null
    lateinit var pref: SharedPref
    var cal = Calendar.getInstance()

    var lastDaySelect: Int = 0
    var dateformat = ""
    var licenseActivityid = 0

    var type: Int = 1

    private var mMap: GoogleMap? = null


    var mapId = ""

    //    private lateinit var modelLicence: Model
//    private lateinit var modelLicence: com.myoutdoor.agent.models.licence.newModel.response.Model
    var clientSite = ""
    override fun getLayoutId(): Int {
        return R.layout.fragment_licence
    }

    override fun onCreateView() {

        showDialogBox = Dialog(requireContext())

        val bundle = this.arguments

        if (bundle != null) {
            mapId = bundle.getSerializable("mapId").toString()
            key = bundle.getString("publickey")!!
            productNo = bundle.getString("productNo")!!
            producttypeID = bundle.getString("productTypeID")!!
            Log.e("call", "LicenceFragmentkay " + key)
            Log.e("call", "LicenceFragmentproductNo " + productNo)
            Log.e("call", "LicenceFragmentproductTypeID " + productTypeID)
        }
        pref = SharedPref(requireContext())

        viewModel = ViewModelProvider(this).get(LicenceViewModel::class.java)

        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
            if (PreApprovalRequestFragment.isBackPreApproval) {
                PreApprovalRequestFragment.preApprovalRequestFragment.refreshApi()
            }
        }

        backPress_RefreshAPi()

        var licenceKeyBody = LicenceKeyBody("" + key)


        Log.e("call", "publickey " + key)
        Log.e("call", "productNo " + productNo)


        /*Log.e(
            "@@@@APi",
            "licenceKeyBody....." + viewModel.licenceDetails(
                licenceKeyBody,
                pref.getString(Constants.TOKEN)
            )
        )*/

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        /*  val mapFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
          mapFragment?.getMapAsync(this)*/

        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);

        viewModel.licenceDetails(licenceKeyBody, pref.getString(Constants.TOKEN))


        setObserver()


//        viewModel.licenceDetails(licenceKeyBody, pref.getString(Constants.TOKEN))
//
//        setObserver()


        //   fragmentBackPressHandle()

        tvRequestTempAccess.setOnClickListener {
            if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
                requestAccessPopup()
            } else {
                guestAlertMessage()
            }
        }

        tvLicenceNow.setOnClickListener {

            if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
//                if (::modelLicence.isInitialized) {
//                    Log.e("call", "@@@@ modelLicence:    " + modelLicence.toString())
                licenceNowFunctionality()
//                } else {
//                    // Handle the case where modelLicence is not initialized
//                    guestAlertMessage()
//                }
            } else {
                guestAlertMessage()
            }
            fragmentBackPressHandle()
        }


//        tvLicenceNow.setOnClickListener {
//
//            if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
//
//                Log.e("call","@@@@ modelLicence:    "+modelLicence.toString())
//                licenceNowFunctionality()
//            }else {
//                guestAlertMessage()
//            }
//        }

        //  fragmentBackPressHandle()

    }


    fun initUI() {


    }


    fun licenceNowFunctionality() {

        if (tvLicenceNow.text.toString().contains("Select")) {
            if (isUserProfileComplete == true) {

                if (tvDayPass.getVisibility() == View.VISIBLE) {
                    calenderPopUp()
                } else {
                    acceptAndPayPopup(agreementName)
                }

                //   acceptAndPayPopup(agreementName)
            } else {
                showAlert("Incomplete profile. Phone number and mailing address are required for purchasing.")
            }
        } else if (tvLicenceNow.text.toString().contains("Cancel Request")) {
            showAddDeleteDialog()
        } else if (tvLicenceNow.text.toString().contains("Request Pre-Approval")) {
            showPreApprovalRequestDialog()
        } else if (tvLicenceNow.text.toString().contains("Renew License")) {

            if (clientSite == "moa") {
                // Show PDF screen only
                acceptAndPayPopup(agreementName)
            }/*if (modelLicence.clientDetails.clientSite == "moa") {
                // Show PDF screen only
                acceptAndPayPopup(agreementName)
            }*/ /*else if (modelLicence.clientDetails.clientSite.equals("custom") && modelLicence.clientDetails.deerHarvestInfo == false && modelLicence.activityDetail.activityType.equals(
                    "Renewal"
                )
            ) {
                // Show 2nd & 3rd screen (Members & PDF)
                type = 2
                openHarvestInformation()
            } else if (modelLicence.clientDetails.clientSite.equals("custom") && modelLicence.clientDetails.deerHarvestInfo == true && modelLicence.activityDetail.activityType.equals(
                    "Renewal"
                )
            ) {
                //     Show all 3 screens
                type = 1
                openHarvestInformation()
            }*/
        }
    }


    fun backPress_RefreshAPi() {
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                requireActivity().onBackPressed()
                MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE

                if (PreApprovalRequestFragment.isBackPreApproval) {
                    PreApprovalRequestFragment.preApprovalRequestFragment.refreshApi()
                }

                return@OnKeyListener true
            }
            false
        })
    }

    fun showAddDeleteDialog() {
        builder = AlertDialog.Builder(requireActivity())
        builder?.setTitle("Cancel Your Request")
        builder?.setMessage("Do you want to cancel your request for " + tvHplNorthDesc.text.toString() + "?")
            ?.setCancelable(false)
            ?.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, id ->
                removeitem(preApprRequestID)
                dialog.dismiss()
            })
            ?.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert: AlertDialog = builder!!.create()
        alert.setOnShowListener(DialogInterface.OnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(requireActivity().getResources().getColor(R.color.green))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(requireActivity().getResources().getColor(R.color.property_text_grey))
        })

        alert.show()
    }


    fun showPreApprovalRequestDialog() {
        showDialogBox!!.setContentView(R.layout.pre_approval_request_popup)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        showDialogBox!!.setCanceledOnTouchOutside(true)

        showDialogBox!!.etMessage.setText("" + pref.getString(Constants.USER_NAME) + " is requesting Pre-Approval.")

        showDialogBox!!.tvSubmitMessage.setOnClickListener {

            if (showDialogBox!!.etMessage.text.toString().equals("")) {
                showShortToast("Please enter comments")
            } else {
                // hit api request

                Log.e("call", "user account id:  " + pref.getString(Constants.userAccountID))
                Log.e("call", "clientInvoiceId:  " + clientInvoiceId)
                Log.e("call", "productID:  " + productID)

                val preApprovalRequest = PreApprovalRequest(
                    clientInvoiceId.toString(),
                    productID,
                    showDialogBox!!.tvSubmitMessage.text.toString(),
                    pref.getString(Constants.userAccountID)
                )
                viewModel.preapprovalrequestv2(preApprovalRequest, pref.getString(Constants.TOKEN))

            }
        }

        showDialogBox!!.tvCancelMessage.setOnClickListener {
            showDialogBox!!.hide()
        }
        showDialogBox!!.show()
    }


    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {

        }
    }


    private fun calenderPopUp() {

        showDialogBox!!.setContentView(R.layout.calender_pop_up)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
//        val layoutManager = LinearLayoutManager(requireContext())
//        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        showDialogBox!!.getWindow()!!.setAttributes(lp)
        showDialogBox!!.show()

        // calender functionality

        //calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        showDialogBox!!.calendarView.setDateSelected(CalendarDay.today(), true);
        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
        lastDaySelect = showDialogBox!!.calendarView.selectedDate!!.day

        showDialogBox!!.calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->

            if (selected) {

                var lastDayOfMonth = getLastDayOf(
                    showDialogBox!!.calendarView!!.selectedDate!!.month - 2,
                    showDialogBox!!.calendarView!!.selectedDate!!.year - 1
                )

                Log.e("@@@", "CHECK LAST MONTH DAY  " + lastDayOfMonth)

                try {

                    Log.e("call", "date " + date.day)
                    Log.e(
                        "call",
                        "dateeeee " + showDialogBox!!.calendarView.selectedDates.get(0).day
                    )

                    if (showDialogBox!!.calendarView.selectedDates.size == 1) {

                        val calender: Calendar = Calendar.getInstance()
                        calender.add(Calendar.DATE, 1);
                        var day = showDialogBox!!.calendarView!!.selectedDate!!.day
                        val month = showDialogBox!!.calendarView.selectedDate!!.month
                        val year = showDialogBox!!.calendarView.selectedDate!!.year
                        showDialogBox!!.calendarView.setDateSelected(
                            CalendarDay.from(
                                year,
                                month,
                                day
                            ), true
                        );
                        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)

                        lastDaySelect = showDialogBox!!.calendarView.selectedDate!!.day

                    } else if (date.day == 1 && lastDaySelect == lastDayOfMonth) {
                        val calender: Calendar = Calendar.getInstance()
                        calender.add(Calendar.DATE, 1);
                        var day = showDialogBox!!.calendarView!!.selectedDate!!.day
                        val month = showDialogBox!!.calendarView.selectedDate!!.month
                        val year = showDialogBox!!.calendarView.selectedDate!!.year
                        showDialogBox!!.calendarView.setDateSelected(
                            CalendarDay.from(
                                year,
                                month,
                                day
                            ), true
                        );

                        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                        lastDaySelect = showDialogBox!!.calendarView.selectedDate!!.day
                    } else {
                        if (date.day == lastDaySelect + 1) {
                            val calender: Calendar = Calendar.getInstance()
                            calender.add(Calendar.DATE, 1);
                            var day = date.day
                            val month = showDialogBox!!.calendarView.selectedDate!!.month
                            val year = showDialogBox!!.calendarView.selectedDate!!.year
                            showDialogBox!!.calendarView.setDateSelected(
                                CalendarDay.from(
                                    year,
                                    month,
                                    day
                                ), true
                            );

                            showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)

                            lastDaySelect = showDialogBox!!.calendarView.selectedDate!!.day
                        } else {

                            var day = date.day
                            val month = date.month
                            val year = date.year
                            showDialogBox!!.calendarView.setDateSelected(
                                CalendarDay.from(
                                    year,
                                    month,
                                    day
                                ), false
                            );
                            lastDaySelect =
                                showDialogBox!!.calendarView.selectedDates.get(showDialogBox!!.calendarView.selectedDates.size - 1).day
                        }
                    }

                } catch (e: java.lang.Exception) {
                    Log.e("call", "exception: " + e.toString())
                }


            } else {


                if (showDialogBox!!.calendarView.selectedDates.size == 0) {
                    var day = date.day
                    val month = date.month
                    val year = date.year
                    showDialogBox!!.calendarView.setDateSelected(
                        CalendarDay.from(year, month, day),
                        false
                    );
                    showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                } else if (showDialogBox!!.calendarView.selectedDates.size >= 2) {

                    if (date.day == showDialogBox!!.calendarView.selectedDates.get(showDialogBox!!.calendarView.selectedDates.size - 1).day + 1) {
                        var day = date.day
                        val month = date.month
                        val year = date.year
                        showDialogBox!!.calendarView.setDateSelected(
                            CalendarDay.from(
                                year,
                                month,
                                day
                            ), false
                        );
                        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                    } else if (date.day + 1 == showDialogBox!!.calendarView.selectedDates.get(0).day) {
                        var day = date.day
                        val month = date.month
                        val year = date.year
                        showDialogBox!!.calendarView.setDateSelected(
                            CalendarDay.from(
                                year,
                                month,
                                day
                            ), false
                        );
                        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                    } else {
                        var day = date.day
                        val month = date.month
                        val year = date.year
                        showDialogBox!!.calendarView.setDateSelected(
                            CalendarDay.from(
                                year,
                                month,
                                day
                            ), true
                        );
                        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                    }
                } else {
                    var day = date.day
                    val month = date.month
                    val year = date.year
                    showDialogBox!!.calendarView.setDateSelected(
                        CalendarDay.from(year, month, day),
                        false
                    );
                    showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                }

            }

        })

        showDialogBox!!.calendarView.addDecorator(PrimeDayDisableDecorator())

        // plus button
        showDialogBox!!.llPlus.setOnClickListener {


            try {

                if (showDialogBox!!.calendarView.selectedDates.size > 0) {
                    Log.e("call", "@@@!23  " + showDialogBox!!.calendarView!!.selectedDate!!.day)

                    ////  go to next
                    var month_ = showDialogBox!!.calendarView.selectedDate!!.month - 1
                    var lastDayOfMonth =
                        getLastDayOf(month_, showDialogBox!!.calendarView!!.selectedDate!!.year)

                    if (lastDayOfMonth == showDialogBox!!.calendarView.selectedDate!!.day) {
                        showDialogBox!!.calendarView.goToNext()

                        val calender: Calendar = Calendar.getInstance()
                        calender.add(Calendar.DATE, 1);

                        if (showDialogBox!!.calendarView.selectedDate!!.month + 1 > 12) {
                            var year = showDialogBox!!.calendarView.selectedDate!!.year + 1

                            showDialogBox!!.calendarView.setDateSelected(
                                CalendarDay.from(
                                    year,
                                    1,
                                    1
                                ), true
                            );
                        } else {
                            showDialogBox!!.calendarView.setDateSelected(
                                CalendarDay.from(
                                    showDialogBox!!.calendarView.selectedDate!!.year,
                                    showDialogBox!!.calendarView.selectedDate!!.month + 1,
                                    1
                                ), true
                            );
                        }

                        Log.e(
                            "call",
                            "@@@ selected " + showDialogBox!!.calendarView.selectedDates.size
                        )
                        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                    } else {
                        ////  increment dates

                        val calender: Calendar = Calendar.getInstance()
                        calender.add(Calendar.DATE, 1);
                        var day = showDialogBox!!.calendarView!!.selectedDate!!.day + 1
                        val month = showDialogBox!!.calendarView.selectedDate!!.month
                        val year = showDialogBox!!.calendarView.selectedDate!!.year
                        showDialogBox!!.calendarView.setDateSelected(
                            CalendarDay.from(
                                year,
                                month,
                                day
                            ), true
                        );


                        showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                    }

                    Log.e(
                        "call",
                        "DAY SELECT:-----   " + showDialogBox!!.calendarView.selectedDates.toString()
                    )

                    lastDaySelect = showDialogBox!!.calendarView.selectedDate!!.day
                } else {
                    Log.e("call", "@@@ NO DATE SELECTED...")
                    showDialogBox!!.calendarView.setDateSelected(CalendarDay.today(), true);
                    showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)
                }

            } catch (exption: java.lang.Exception) {
                Log.e("call", "@@@exception  " + exption.toString())
            }


        }

        // minus button
        showDialogBox!!.llminus.setOnClickListener {

            try {

                if (showDialogBox!!.calendarView.selectedDates.size > 0) {
                    if (showDialogBox!!.calendarView.selectedDate!!.day == 1) {
                        showDialogBox!!.calendarView.goToPrevious()
                    }

                    val calender: Calendar = Calendar.getInstance()
                    calender.add(Calendar.DATE, 1);
                    var day = showDialogBox!!.calendarView!!.selectedDate!!.day
                    val month = showDialogBox!!.calendarView.selectedDate!!.month
                    val year = showDialogBox!!.calendarView.selectedDate!!.year
                    showDialogBox!!.calendarView.setDateSelected(
                        CalendarDay.from(year, month, day),
                        false
                    );
                    showDialogBox!!.tvCount.setText("" + showDialogBox!!.calendarView.selectedDates.size)

                    lastDaySelect =
                        showDialogBox!!.calendarView.selectedDates.get(showDialogBox!!.calendarView.selectedDates.size - 1).day

                    Log.e(
                        "call",
                        "DAY SELECT:-----   " + showDialogBox!!.calendarView.selectedDates.toString()
                    )
                }

            } catch (e: java.lang.Exception) {
                Log.e("call", "exception " + e.toString())
            }


        }

        // check availability

        showDialogBox!!.tvCheckAvailability.setOnClickListener {

            if (showDialogBox!!.calendarView.selectedDates.size > 0) {
                var date =
                    "" + showDialogBox!!.calendarView.selectedDates.get(0).year + "-" + showDialogBox!!.calendarView.selectedDates.get(
                        0
                    ).month + "-" + showDialogBox!!.calendarView.selectedDates.get(0).day
                var daysCount = showDialogBox!!.calendarView.selectedDates.size

                Log.e("call", "@@@123 date  " + date)
                Log.e("call", "@@@123 daysCount  " + daysCount)
                Log.e("call", "@@@123 clientInvoiceId  " + clientInvoiceId)

                var dayPassBody = DayPassRequest(
                    "" + clientInvoiceId, "" + date, "" + daysCount
                )
                viewModel.dayPassAvailibilityRequest(dayPassBody, pref.getString(Constants.TOKEN))
                showDialogBox!!.dismiss()
            } else {
                showShortToast("please select any date first")
            }
        }

        showDialogBox!!.tvCancelAvailability.setOnClickListener {
            showDialogBox!!.dismiss()
        }

    }

    var simplecoordinates: ArrayList<LatLng> = ArrayList()


    fun particularpolygonObserver() {
        viewModel.onParticularPolygenLayerSuccess.observe(requireActivity(), {
            it
            Log.d("@@@@", "Success")


            if (activity != null) {
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);

                val geometry = it.features[0].geometry

                // Check if coordinates is a List<List<List<Double>>>
                if (geometry.coordinates is List<*>) {
                    val firstLevel = geometry.coordinates as List<*>

                    // Case 1: List<List<List<Double>>> (standard polygon)
                    if (firstLevel.isNotEmpty() && firstLevel[0] is List<*>) {
                        val secondLevel = firstLevel[0] as List<*>

                        if (secondLevel.isNotEmpty() && secondLevel[0] is List<*>) {
                            val thirdLevel = secondLevel[0] as List<*>

                            // Case: List<List<List<Double>>> - Process this
                            if (thirdLevel.isNotEmpty() && thirdLevel[0] is Double) {
                                // Handling List<List<List<Double>>>
                                for (i in secondLevel.indices) {
                                    val latLngList = secondLevel[i] as List<Double>
                                    simplecoordinates.add(
                                        LatLng(
                                            latLngList[1],  // Latitude
                                            latLngList[0]   // Longitude
                                        )
                                    )
                                }
                            }
                            // Case 2: List<List<List<List<Double>>>> (nested polygon)
                            else if (thirdLevel.isNotEmpty() && thirdLevel[0] is List<*>) {
                                for (polygon in secondLevel) {
                                    val outerList = polygon as List<*>
                                    for (i in outerList.indices) {
                                        val latLngList = outerList[i] as List<Double>
                                        simplecoordinates.add(
                                            LatLng(
                                                latLngList[1],  // Latitude
                                                latLngList[0]   // Longitude
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                onMapMethod()
                /*for (i in 0 until it.features.get(0).geometry.coordinates.get(0).size) {
                    simplecoordinates.add(
                        LatLng(
                            it.features.get(0).geometry.coordinates.get(0).get(i).get(1),
                            it.features.get(0).geometry.coordinates.get(0).get(i).get(0)
                        )
                    )
                }
                */
            }


        })

    }


    fun polygonFourObserver() {
        viewModel.particularPolygonFourLayerSuccess.observe(requireActivity(), {
            it
            //            var a: Marker? = null


            if (activity != null) {
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);

                val geometry = it.features[0].geometry

                // Check if coordinates is a List<List<List<Double>>>
                if (geometry.coordinates is List<*>) {
                    val firstLevel = geometry.coordinates as List<*>

                    // Case 1: List<List<List<Double>>> (standard polygon)
                    if (firstLevel.isNotEmpty() && firstLevel[0] is List<*>) {
                        val secondLevel = firstLevel[0] as List<*>

                        if (secondLevel.isNotEmpty() && secondLevel[0] is List<*>) {
                            val thirdLevel = secondLevel[0] as List<*>

                            // Case: List<List<List<Double>>> - Process this
                            if (thirdLevel.isNotEmpty() && thirdLevel[0] is Double) {
                                // Handling List<List<List<Double>>>
                                for (i in secondLevel.indices) {
                                    val latLngList = secondLevel[i] as List<Double>
                                    simplecoordinates.add(
                                        LatLng(
                                            latLngList[1],  // Latitude
                                            latLngList[0]   // Longitude
                                        )
                                    )
                                }
                            }
                            // Case 2: List<List<List<List<Double>>>> (nested polygon)
                            else if (thirdLevel.isNotEmpty() && thirdLevel[0] is List<*>) {
                                for (polygon in secondLevel) {
                                    val outerList = polygon as List<*>
                                    for (i in outerList.indices) {
                                        val latLngList = outerList[i] as List<Double>
                                        simplecoordinates.add(
                                            LatLng(
                                                latLngList[1],  // Latitude
                                                latLngList[0]   // Longitude
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                /*for (i in 0 until it.features.get(0).geometry.coordinates.get(0).size) {
                    simplecoordinates.add(
                        LatLng(
                            it.features.get(0).geometry.coordinates.get(0).get(i).get(1),
                            it.features.get(0).geometry.coordinates.get(0).get(i).get(0)
                        )
                    )
                }*/
                onMapMethod()
            }


        })

    }

    /*    fun freeAccessDialog(data: FreeAccessResponse){


            var status=data.statusCode
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.freeaccess_dialog)

            val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
            val btnOk = dialog.findViewById<Button>(R.id.btnOk)

            if (status == 200) {
                tvTitle.text = "Success"
                tvMessage.text = data.message
            }else{
                tvTitle.text = "Error"
                tvMessage.text = data.message
            }


            btnOk.setOnClickListener {
                if (status == 200) {
                    addNewFragment(MyLicencesFragment(),R.id.container,false)
                    dialog.dismiss()
                }else{
                    dialog.dismiss()
                }

            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

        }*/


    fun freeAccessDialog(data: FreeAccessResponse) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.freeaccess_dialog)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        val btnOk = dialog.findViewById<TextView>(R.id.btnOk)

        val status = data.statusCode
        tvTitle.text = if (status == 200) "Success" else "Error"
        tvMessage.text = data.message

        btnOk.setOnClickListener {
            if (status == 200) {
                addNewFragment(MyLicencesFragment(), R.id.container, true)
            }
            dialog.dismiss()
        }

        // Apply Blur Effect (for API 31+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requireActivity().window.setBackgroundBlurRadius(50) // Adjust blur intensity
        } else {
            // Fallback: Dim effect if blur is not supported
            dialog.window?.setDimAmount(0.5f)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    fun setFreeAccessObserver() {
        viewModel.freeAccessSuccess.observe(requireActivity(), {
            freeAccessDialog(it)
        })
    }



    private fun setObserver() {

        viewModel.dayPassSuccess.observe(requireActivity(), {

            if (it.message != "Success") {

//                showShortToast(it.message!!)
            } else {

                showDialogBox!!.setContentView(R.layout.calender_date_proceed_pop_up)
                showDialogBox!!.setCanceledOnTouchOutside(true)
                showDialogBox!!.getWindow()!!
                    .setBackgroundDrawableResource(android.R.color.transparent)
                val layoutManager = LinearLayoutManager(requireContext())
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                showDialogBox!!.show()
                showDialogBox!!.tvResponse.setText("Total cost for selected date(s) :" + "$" + it!!.model!!.daypassTotalCost)
                showDialogBox!!.tvProceed.setOnClickListener {

                    acceptAndPayPopup(agreementName)
                }
                showDialogBox!!.tvCancel.setOnClickListener {
                    showDialogBox!!.dismiss()
                }

                //    showShortToast(it.message!!)
            }

        })

        viewModel.preApprovalRequestSuccess.observe(requireActivity(), {

            showDialogBox!!.dismiss()
            tvLicenceNow.setText(getString(R.string.cancel_request))
            showRequestSubmitted()

        })

        viewModel.getPaymentTokenResponse.observe(requireActivity(), {

            val fragment = PaymentWebViewFragment()
            bundle.putString(Constants.PAYMENT_TOKEN, it.model.response.paymentToken)
            bundle.putString("publickey", key)
            fragment.setArguments(bundle)
            addNewFragment(fragment, R.id.container, true)

        })


        viewModel.cancelRequestSuccess.observe(requireActivity(), {

            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                tvLicenceNow.setText("Request Pre-Approval")
//                showShortToast(it.message!!)
            }

        })


        tvSubmitPA.setOnClickListener {

            if (pref.getString(Constants.IS_FROM_LOGIN).contains("true")) {
                LeaveUsMessage()
            } else {
                guestAlertMessage()
            }
        }


        viewModel.rightOfEntryResponseSuccess.observe(requireActivity(), {

            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())

            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                showDialog("Your request for temporary access is submitted.")
//                showShortToast(it.message!!)
            }

        })


        viewModel.dayPassSuccess.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it.message != "Success") {
                showShortToast(it.message!!)
            } else {

            }
        })

        viewModel.sendMessageResponseSuccess.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                if (it.message != "Success") {
                    showShortToast(it.message!!)
                } else {
                    showDialogBox!!.dismiss()
                    leaveusMessagesSuccess()
                }
            })

        viewModel.licenceSuccess.observe(requireActivity(), androidx.lifecycle.Observer {


            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())


            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                try {
                    if (it!!.model.images.size > 0 && it!!.model.images != null) {
                        ivPlaceHolder.visibility = View.GONE
                        val viewPagerAdapter = ViewPagerAdapter(requireContext(), it!!.model.images)
                        viewPager.adapter = viewPagerAdapter

                        if (it!!.model.images.size == 1) {

                        } else {
                            dotFunctionality(viewPagerAdapter)
                        }

                    } else {
                        viewPager.visibility = View.GONE
                        ivPlaceHolder.visibility = View.VISIBLE
                    }

                    clientSite = it.model.clientDetails.clientSite
//                    modelLicence = it!!.model


                    if (it!!.model.activityDetail.activityType == "Day Pass") {

                        tvDayPass.visibility = View.VISIBLE
                    } else {

                        tvDayPass.visibility = View.GONE
                    }

                    renewalList.addAll(it.model.members)

                } catch (e: Exception) {
                    Log.e("call", "Exception " + e.toString())
                }


                if (it.model.activityDetailPageChecks.showRequestEntry == true) {
                    llRequestTemporaryAccess.visibility = View.VISIBLE
                } else {
                    llRequestTemporaryAccess.visibility = View.GONE
                }


                if (it.model.activityDetail.agreementName != null) {
                    agreementName = it.model.activityDetail.agreementName
                }

                isFreeAccessPermit = it.model.activityDetailPageChecks.isFreeAccessPermit

                clientInvoiceId = it.model.activityDetail.licenseActivityID
                licenceFee = it.model.activityDetail.licenseFee
                productTypeID = it.model.activityDetail.productTypeID
                prodID = it.model.activityDetail.productID
                productID = it.model.activityDetail.productID.toString()

                rluNo = it.model.activityDetail.displayName
                isUserProfileComplete = it.model.activityDetailPageChecks.isUserProfileComplete
                preApprRequestID = it.model.activityDetailPageChecks.preApprRequestID.toString()
                licenseActivityid = it.model.activityDetail.licenseActivityID



                if (it!!.model!!.activityDetailPageChecks.preApprovalStatus == "Accepted") {
                    tvLicenceNow.setText("Select")
                    tvLicenceNow.setClickable(true)

                } else if (it!!.model!!.activityDetailPageChecks.isPreApprovalRequested == true && it!!.model!!.activityDetailPageChecks.preApprovalStatus == "Requested") {
                    tvLicenceNow.setText("Cancel Request")
                    tvLicenceNow.setClickable(true)

                } else if (it!!.model!!.activityDetailPageChecks.showPreApprovalRequestButton == true) {
                    tvLicenceNow.setText("Request Pre-Approval")
                    tvLicenceNow.setClickable(true)
                } else if (it!!.model!!.activityDetailPageChecks.showRenewButton == true) {
                    tvLicenceNow.setText("Renew License")
                    tvLicenceNow.setClickable(true)
                } else if (it!!.model!!.activityDetailPageChecks.showLicenseNowButton == true && it!!.model!!.activityDetail.activityType != "Day Pass") {
                    tvLicenceNow.setText("Select")
                    tvLicenceNow.setClickable(true)

                } else if (it!!.model!!.activityDetail.activityType == "Day Pass") {
                    tvLicenceNow.setText("Select")
                    tvLicenceNow.setClickable(true)

                } else if (it!!.model!!.activityDetailPageChecks.showAlreadyPurchasedButton == true) {
                    tvLicenceNow.setText("Already Purchased")
                    tvLicenceNow.setBackgroundResource(R.drawable.mehndi_color_shape)
                    tvLicenceNow.setClickable(false)

                } else if (it!!.model!!.activityDetailPageChecks.showSoldOutButton == true) {
                    tvLicenceNow.setText("Sold Out")
                    tvLicenceNow.visibility = View.GONE
                    tvSoldout.visibility = View.VISIBLE
                    tvLicenceNow.setClickable(false)
                }


                tvHplNorthDesc.setText("" + it!!.model!!.activityDetail.displayName)

                tvHplNorthPrice.setFormattedDollarNumber(it.model.activityDetail.licenseFee)
                tvAddress.setText("" + it!!.model!!.activityDetail.countyName + ", " + it!!.model!!.activityDetail.state)

                tvPropertyID.setSelected(true);

                tvPropertyID.setText("" + it!!.model!!.activityDetail.productNo)
                tvPriceDec.setFormattedDollarNumber(it.model.activityDetail.licenseFee)
                tvAcres.setFormattedNumber(it.model.activityDetail.acres)


                Log.e("!!!", "  stratr  " + it!!.model.activityDetail.saleStartDateTime)

                val dateInString = it!!.model.activityDetail.saleStartDateTime
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val outputFormat = SimpleDateFormat("MMMM dd , yyyy hh:mm a")


                try {
                    val date: Date = inputFormat.parse(dateInString)
                    dateformat = outputFormat.format(date)
                    System.out.println("Date ->" + date)
                } catch (e: ParseException) {
                    System.out.println("Date ->" + e.toString())
                    e.printStackTrace()
                }



                if (it!!.model!!.activityDetailPageChecks.showComingSoonButton == true) {
                    tvLicenceNow.setText("Coming Soon")
                    tvLicenceNow.setClickable(true)
                    tvLicenceNow.visibility = View.GONE
                    tvLicence.visibility = View.VISIBLE

                    tvAvailabilie.visibility = View.VISIBLE

                    if (it!!.model.activityDetail.saleStartDateTime != null) {
                        Log.e("@@@", "dateformat...." + dateformat)

                        val str: String = dateformat.replace("am", "AM").replace("pm", "PM")

                        //  val yourDate=dateFormat(it!!.model.activityDetail.saleStartDateTime)

                        Log.e("@@@", "strstr...." + str)

                        tvAvailabilie.setText("Available on:" + " " + str + " " + it!!.model.activityDetail.timeZone)

                    } else {
                        tvAvailabilie.visibility = View.GONE
                    }
                }


                //tvAvailabilie.setText("Available on: "+it!!.model.activityDetail.saleStartDateTime+  it!!.model.activityDetail.timeZone)
                var startDate = dateFormat(it!!.model!!.activityDetail.licenseStartDate)
                var endDate = dateFormat(it!!.model!!.activityDetail.licenseEndDate)

                tvLicenceStartDateDesc.setText("" + startDate)
                tvLicenceEndDateDec.setText("" + endDate)


                tvPropertyDescription.setText("" + it!!.model!!.activityDetail.displayDescription)
                // tvContactPAgent.setText("" + it!!.model!!.activityDetail.phone)


                if (it!!.model!!.activityDetail.phone == null) {
                    tvContactPAgent.setText("---")
                    tvContactPAgent.setTextColor(resources.getColor(R.color.black))
                } else {

                    var accPhone: String = it!!.model!!.activityDetail.phone

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
                            tvContactPAgent.setText(code)
                        } else {
                            tvContactPAgent.setText(accPhone)
                        }
                    }


                }


                showDocumentsAdapter(it.model.clientDocuments)

                tvTime.setText("8:00 AM - 5:00 PM M-F EST")


                //    tvTime.setText("" + it!!.model!!.activityDetail.timeZone + "" + it!!.model!!.activityDetail.timeZone)


                if (it!!.model!!.activityDetail.displayDescription == null) {
                    tvPropertyDescription.setText("No Description Available")
                } else {
                    tvPropertyDescription.setText("" + it!!.model!!.activityDetail.displayDescription)
                }

                //       Log.e("call","@@@ amenities list "+it!!.model.amenities)

                list1.clear()
                list2.clear()

                try {
                    if (it!!.model.amenities != null) {
                        for (i in 0 until it!!.model.amenities.size) {
                            if (it!!.model.amenities.get(i).amenityType.equals("Activity")) {
                                list1.add(it.model.amenities.get(i))
                            }
                        }
                        rvActivitiesLN.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        rvActivitiesLN.adapter = LicenseNowAmenityAdapter(list1, activity)
                    }
                } catch (e: Exception) {
                    Log.e("call", "exp23333:  " + e.toString())
                }


                try {
                    if (it!!.model.amenities != null) {
                        for (i in 0 until it!!.model.amenities.size) {
                            if (it!!.model.amenities.get(i).amenityType.equals("Amenity")) {
                                list2.add(it.model.amenities.get(i))
                            }
                        }
                        rvAmenitiesLN.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        rvAmenitiesLN.adapter = LicenseNowAmenityAdapter(list2, activity)
                    }
                } catch (e: Exception) {
                    Log.e("call", "exp23333:  " + e.toString())
                }

                if (list1.isEmpty()) {
                    rvActivitiesLN.visibility = View.GONE
                    tvNoActivityAvailable.visibility = View.VISIBLE
                } else {
                    rvActivitiesLN.visibility = View.VISIBLE
                    tvNoActivityAvailable.visibility = View.GONE
                }

                if (list2.isEmpty()) {
                    rvAmenitiesLN.visibility = View.GONE
                    tvNoAmenityAvailableHome.visibility = View.VISIBLE
                } else {
                    rvAmenitiesLN.visibility = View.VISIBLE
                    tvNoAmenityAvailableHome.visibility = View.GONE
                }

                Log.e("call", "@@@ SPECIAL CONDITION: " + it.model.specialConditions.toString())

                try {
                    list3.clear()

                    for (i in 0 until it!!.model.specialConditions.size) {
                        list3.add(it.model.specialConditions.get(i))
                    }

                    /*for (i in 0 until it!!.model.specialConditions.size) {
                        list3.add(it.model.specialConditions.get(i))
                    }*/

                    rvSpecialConditions.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    rvSpecialConditions.adapter = LicenseGuideLineHomeAdapter(list3, activity)
                } catch (e: Exception) {
                    Log.e("call", "Exception:  " + e.toString())
                }

                Log.e("call", "@@@@ LIST3 " + list3.toString())

                if (list3.isEmpty()) {
                    rvSpecialConditions.visibility = View.GONE
                    tvSpecialConditions.visibility = View.VISIBLE

                    Log.e("call", "@@@@ LIST3 232")
                } else {
                    rvSpecialConditions.visibility = View.VISIBLE
                    tvSpecialConditions.visibility = View.GONE

                    Log.e("call", "@@@@ LIST3 677")
                }
                data.clear()
                if (it!!.model.similarProperties.size > 0 && it!!.model.similarProperties != null) {
                    for (i in 0 until it!!.model.similarProperties.size) {
                        data.addAll(listOf(it!!.model.similarProperties.get(i)))
                    }
                }

                rvSimilarPropertiesHV.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                //creating a  arraylist of data
                rvSimilarPropertiesHV.adapter = SimilarPropertiesAdapter(data, this)
//                showShortToast(it.message!!)

                if (data.isEmpty()) {
                    rvSimilarPropertiesHV.visibility = View.GONE
                    tvSimilarProperties.visibility = View.VISIBLE
                } else {
                    rvSimilarPropertiesHV.visibility = View.VISIBLE
                    tvSimilarProperties.visibility = View.GONE
                }




                if (productTypeID == 1) {
//            viewModel.licenceDetails(licenceKeyBody, pref.getString(Constants.TOKEN))

                    viewModel.particluarPolygenLayer(
                        "RLUNo=" + "'" + productNo + "'",
                        "*",
                        "esriSpatialRelIntersects",
                        "geojson"
                    )
                    particularpolygonObserver()

                } else {
//            viewModel.licenceDetails(licenceKeyBody, pref.getString(Constants.TOKEN))

                    viewModel.particularPolygonFourLayer(
                        "RLUNo=" + "'" + productNo + "'",
                        "*",
                        "esriSpatialRelIntersects",
                        "geojson"
                    )
                    polygonFourObserver()

                }

            }
        })


        viewModel.addHarvestingResponse.observe(requireActivity(), androidx.lifecycle.Observer {

            // showShortToast(it.message!!)
            type = 2
            openHarvestInformation()

        })

        viewModel.generateContractResponse.observe(requireActivity(), androidx.lifecycle.Observer {
            showDialogBox!!.dismiss()
            Log.e("@#@#$$", "pdff  " + it!!.message)
            // var url = LICENCEPDF + it!!.message
            // RenewalPDFPopup(url)

        })



        viewModel.isLoading.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it) {
                progressBarPB.show()
            } else {
                Handler().postDelayed({

                    progressBarPB.dismiss()

                }, 8000)
            }
        })

    }


    var particularLiness: Polyline? = null

    var particularLineList = java.util.ArrayList<Polyline>()

    private fun onMapMethod() {

        if (simplecoordinates.isNotEmpty()) {
            for (i in 0 until simplecoordinates.size) {
                particularLiness = mMap!!.addPolyline(
                    PolylineOptions().addAll(simplecoordinates).width(5f)
                        .color(requireActivity().getResources().getColor(R.color.green))
                )
                particularLineList.add(particularLiness!!)
            }
        }

//        val simpleMarker = mMap!!.addMarker(
//            MarkerOptions().position(
//                LatLng(
//                    simplecoordinates[1].latitude,
//                    simplecoordinates[0].longitude
//                )
//            ).title(mapId).icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
//        )!!
//        simpleMarker.showInfoWindow()
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(simplecoordinates[0], 11f), null)
        simplecoordinates.clear()


//        mMap!!.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
//
//            viewModel.rluMapDetails(RluBody(mapId), pref.getString(Constants.TOKEN))
//            showDialogBox!!.setContentView(R.layout.map_property_details_popup)
//            showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
//            showDialogBox!!.setCanceledOnTouchOutside(true)
//            showDialogBox!!.imgCancelProperty.setOnClickListener {
//                showDialogBox!!.dismiss()
//            }
//            showDialogBox!!.tvHplPermitHead.setText(mapId)
//            showDialogBox!!.tvPermitCountry.setText(countryName)
//            showDialogBox!!.tvPermitAcres.setText(acres)
//            showDialogBox!!.tvHplPermitPrice.setText(price)
//            showDialogBox!!.tvZoomOutIn.setText("Zoom in")
//
//            if (isClicked) {
//                showDialogBox!!.tvZoomOutIn.text = "Zoom In"
//            } else {
//                showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
//            }
//
//            showDialogBox!!.rlZoomMarker.setOnClickListener {
//
//                if (isClicked) {
//                    mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
//                    isClicked = false
//                    showDialogBox!!.dismiss()
//
//                } else {
//                    mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
//                    isClicked = true
//                    showDialogBox!!.dismiss()
//
//                }
//            }
//
//            showDialogBox!!.rlDetailsMarker.setOnClickListener {
//                showDialogBox!!.dismiss()
//                val fragment = LicenceFragment()
//                val bundle = Bundle()
//                bundle.putString("publickey", key)
//                fragment.setArguments(bundle)
//                addNewFragment(fragment, R.id.container, true)
//            }
//
//            showDialogBox!!.rlDirectionsMarker.setOnClickListener {
//                showDialogBox!!.dismiss()
//                addNewFragment(DirectiongoogleMapFragment(progressBarPB), R.id.container, true)
//            }
//
//            Glide.with(showDialogBox!!.ivSeachImage)
//                .load(image)
//                .fitCenter()
//                .placeholder(R.drawable.round_logo)
//                .error(R.drawable.round_logo)
//                .centerCrop()
//                .into(showDialogBox!!.ivSeachImage)
//
//            showDialogBox!!.show()
//            false
//
//        }

        mMap!!.getUiSettings().setScrollGesturesEnabled(true);


        tvMapView.setOnClickListener {
            tvMapView.setTextColor(Color.parseColor("#FF000000"))
            tvMapView.setTypeface(tvMapView.getTypeface(), Typeface.BOLD)
            tvHybridView.setTextColor(Color.parseColor("#898989"))
            tvHybridView.setBackgroundResource(R.color.my_account_tab_grey)
            tvMapView.setBackgroundResource(R.color.white)
            mMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
        tvHybridView.setOnClickListener {
            tvHybridView.setTextColor(Color.parseColor("#FF000000"))
            tvHybridView.setTypeface(tvHybridView.getTypeface(), Typeface.BOLD)
            tvMapView.setTextColor(Color.parseColor("#898989"))
            tvMapView.setBackgroundResource(R.color.my_account_tab_grey)
            tvHybridView.setBackgroundResource(R.color.white)
            mMap!!.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        }

    }


    private fun showDocumentsAdapter(clientDocuments: List<ClientDocuments>) {
        if (clientDocuments.isNullOrEmpty()) {
            rvDocuments.visibility = View.GONE
            tvNoDocumentAvailable.visibility = View.VISIBLE
        } else {
            rvDocuments.visibility = View.VISIBLE
            tvNoDocumentAvailable.visibility = View.GONE
            rvDocuments.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            documentsAdapter = DocumentAdapter(clientDocuments) { pos, data ->
                var pdfUrl = Constants.DOC_URL + "${data.clientID}/" + data.documentName
//             data.documentName
//             openPdfInBrowser(requireContext(),pdf)
                // Google Drive PDF viewer URL
                Log.e("PDFUrl", "$pdfUrl")
                showPDFPopup(pdfUrl)
//                val googleDriveViewerUrl = "https://drive.google.com/viewerng/viewer?embedded=true&url=$pdfUrl"
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(googleDriveViewerUrl)
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


    private fun leaveusMessagesSuccess() {

        showDialogBox1!!.setContentView(R.layout.leave_us_message_success)
        showDialogBox1!!.setCanceledOnTouchOutside(true)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val tvShowResponse: TextView = showDialogBox1!!.findViewById<TextView>(R.id.tvShowResponse)
        tvShowResponse.setText("Property Agent has been notified.You can view all your messages and responses in the Message Center.")
        showDialogBox1!!.show()
        Handler(Looper.getMainLooper()).postDelayed({
            showDialogBox1!!.cancel()
        }, 2000)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox1!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox1!!.getWindow()!!.setAttributes(lp)

    }


    private fun getDayNumberSuffix(day: Int): String? {
        return if (day >= 11 && day <= 13) {
            "th"
        } else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }


    private fun LeaveUsMessage() {

        showDialogBox!!.setContentView(R.layout.leave_us_message_box)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.tvSubmitMessage.setOnClickListener {

            if (showDialogBox!!.edtLeaveUsMessage.text.toString().equals("")) {
                showShortToast("Please enter message")
            } else {
                // hit api send messages
                val sendMessageRequest = SendMessageRequest(
                    showDialogBox!!.edtLeaveUsMessage.text.toString(),
                    productID.toInt()
                )
//                viewModel.sendMessagesRequest(sendMessageRequest, pref.getString(Constants.TOKEN))
                viewModel.sendMessagesRequest(
                    MessageText = showDialogBox!!.edtLeaveUsMessage.text.toString(),
                    ProductID = productID.toInt(),
                    token = pref.getString(Constants.TOKEN)
                )
                setObserver()
            }

        }

        showDialogBox!!.tvCancelMessage.setOnClickListener {
            showDialogBox!!.hide()
        }
        showDialogBox!!.show()
    }

    private fun dotFunctionality(viewPagerAdapter: ViewPagerAdapter) {

        dotscount = viewPagerAdapter.count

        Log.e("####", "dotscount    ........" + dotscount)
        //   dots = arrayOf(arrayOf<ImageView>()[dotscount])
        val dots: Array<ImageView?> = arrayOfNulls(dotscount)
        //  var dots = arrayOfNulls(dotscount)
        for (i in 0 until dotscount) {
            dots[i] = ImageView(context)
            dots[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.non_active_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            SliderDots.addView(dots[i], params)
        }
        dots[0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.active_dot
            )
        )
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(pos: Int) {
                Log.e("####", "onPageSelected........" + dotscount)
                for (i in 0 until dotscount) {
                    dots[i]!!.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            requireActivity()!!.getResources(),
                            R.drawable.non_active_dot,
                            null
                        )
                    )
                }
                dots[pos]!!.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireActivity().getResources(),
                        R.drawable.active_dot,
                        null
                    )
                )
                //showLongToast("Dots visible")

            }

            override fun onPageScrollStateChanged(state: Int) {}

        })
    }

    var isFreeAccessPermit: Boolean = false


    @SuppressLint("SuspiciousIndentation")
    private fun acceptAndPayPopup(agreementName: String) {


//        if (modelLicence.clientDetails.clientSite != "moa") {
        /* if (clientSite != "moa") {
             var generateContractRequest = GenerateContractRequest(licenseActivityid)
             viewModel!!.generateContractRequest(
                 generateContractRequest,
                 pref.getString(Constants.TOKEN)
             )
         } */

//        else {
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
        var progressBar: ProgressBar = showDialogBox!!.findViewById<ProgressBar>(R.id.progressBar)


//            var url: String = Constants.AMENITIES_URL + "Assets/Agreements/" + agreementName
        var url: String = Constants.DOC_LICENSE_URL + agreementName


        Log.e(
            "@@@@",
            "urllllll     " + Constants.AMENITIES_URL + "Assets/Agreements/" + agreementName
        )

        RetrievePDFFromURL(pdfViewAP, progressBar).execute(url)


        if (isFreeAccessPermit == true) {
            tvAcceptAndPay.text = getString(R.string.accept)
        } else {
            tvAcceptAndPay.text = getString(R.string.accept_and_pay)
        }

        tvAcceptAndPay.setOnClickListener {

            /*  val getPaymentTokenBody = GetPaymentTokenBody(
                  "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&PaymentStatus=fail",
                  clientInvoiceId,
                  pref.getString(Constants.USER_EMAIL),
                  "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&Error=fail",
                  "acct_1GaRZZLWZ7bSejfX",
                  licenceFee!!,
                  pref.getString(Constants.USER_NAME),
                  prodID,
                  productTypeID,
                  "MOA",
                  "https://demov2.myoutdooragent.com/",
                  rluNo,
                  pref.getString(Constants.userAccountID).toInt(),
                  0
              )*/

            // Live url

//            Send Return url in payload.
//            https://demov2.myoutdooragent.com/property?id=lsa_ps4r2pks(License
//            Activity public key') cancelUrl: "
//            https://demov2.myoutdooragent.com/property?id=lsa_ps4r2pks&PaymentStatus=fail"
//            clientInvoiceId: 4250 email: "mailto:shivam-asp@cssoftsolutions.com" errorUrl: "
//            https://demov2.myoutdooragent.com/property?id=lsa_ps4r2pks&Error=fail"
//            fundAccountKey: "acct_1GaRZZLWZ7bSejfX" licenseFee: 800 paidBy: "Shivam Nagpal" productID: 232 productTypeId: 1 requestType: "MOA" returnUrl: "
//            https://demov2.myoutdooragent.com/property?id=lsa_ps4r2pks"
//            rluNo: "NFS - 4 Test P 001" userAccountId: 640
//            https://testoneconnect.myoutdooragent.com/payment/ddbedbb3-0832-499c-ab02-ae2dd83c2922


            if (isFreeAccessPermit == true) {
                var freeAccessBody = FreeAccessBody("" + licenseActivityid)
                viewModel.freeaccess(freeAccessBody,pref.getString(Constants.TOKEN))
                showDialogBox!!.cancel()
                setFreeAccessObserver()

            } else {

                val getPaymentTokenBody = GetPaymentTokenBody(
                    "https://demov2.myoutdooragent.com/property?=" + key + "&PaymentStatus=fail",
                    clientInvoiceId,
                    pref.getString(Constants.USER_EMAIL),
                    "https://demov2.myoutdooragent.com/property?=" + key + "&Error=fail",
                    "acct_1GaRZZLWZ7bSejfX",
                    licenceFee!!,
                    pref.getString(Constants.USER_NAME),
                    prodID,
                    productTypeID,
                    "MOA",
                    "https://demov2.myoutdooragent.com/property?=" + key,
                    rluNo,
                    pref.getString(Constants.userAccountID).toInt(),
                    0
                )

                /* val getPaymentTokenBody = GetPaymentTokenBody(
                    "https://myoutdooragent.com/#/app/property?id=" + key + "&PaymentStatus=fail",
                    clientInvoiceId,
                    pref.getString(Constants.USER_EMAIL),
                    "https://myoutdooragent.com/#/app/property?id=" + key + "&Error=fail",
                    "acct_1GaRZZLWZ7bSejfX",
                    licenceFee!!,
                    pref.getString(Constants.USER_NAME),
                    prodID,
                    productTypeID,
                    "MOA",
                    "https://myoutdooragent.com/",
                    rluNo,
                    pref.getString(Constants.userAccountID).toInt(),
                    0
                )*/

                viewModel.getPaymentToken(getPaymentTokenBody, pref.getString(Constants.TOKEN))
                showDialogBox!!.cancel()
            }


        }

        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
        }

        showDialogBox!!.show()
//        }


    }


    class RetrievePDFFromURL(pdfView: PDFView, progressBar: ProgressBar) :
        AsyncTask<String, Void, InputStream>() {
        private val mypdfView: PDFView = pdfView
        private val progressBarPB: ProgressBar = progressBar

        override fun onPreExecute() {
            super.onPreExecute()
            progressBarPB.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): InputStream? {
            var inputStream: InputStream? = null
            try {
                val url = URL(params.get(0))
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return inputStream
        }

        override fun onPostExecute(result: InputStream?) {
            mypdfView.fromStream(result).load()
            progressBarPB.visibility = View.GONE
        }
    }


    private fun RenewalPDFPopup(url: String) {

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


        var url: String = url

        Log.e("@@@@", "urllllll     " + url)

        RetrievePDFFromURL(pdfViewAP).execute(url)

        tvAcceptAndPay.setOnClickListener {

/*                val getPaymentTokenBody = GetPaymentTokenBody(
                    "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&PaymentStatus=fail",
                    clientInvoiceId,
                    pref.getString(Constants.USER_EMAIL),
                    "https://demov2.myoutdooragent.com/#/app/property?id="+key+"&Error=fail",
                    "acct_1GaRZZLWZ7bSejfX",
                    licenceFee!!,
                    pref.getString(Constants.USER_NAME),
                    prodID,
                    productTypeID,
                    "MOA",
                    "https://demov2.myoutdooragent.com/",
                    rluNo,
                    pref.getString(Constants.userAccountID).toInt(),
                    0
                )*/

            // Live url

            val getPaymentTokenBody = GetPaymentTokenBody(
                "https://myoutdooragent.com/#/app/property?id=" + key + "&PaymentStatus=fail",
                clientInvoiceId,
                pref.getString(Constants.USER_EMAIL),
                "https://myoutdooragent.com/#/app/property?id=" + key + "&Error=fail",
                "acct_1GaRZZLWZ7bSejfX",
                licenceFee!!,
                pref.getString(Constants.USER_NAME),
                prodID,
                productTypeID,
                "MOA",
                "https://myoutdooragent.com/",
                rluNo,
                pref.getString(Constants.userAccountID).toInt(),
                0
            )

            viewModel.getPaymentToken(getPaymentTokenBody, pref.getString(Constants.TOKEN))
            showDialogBox!!.cancel()
        }




        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
        }

        showDialogBox!!.show()


    }


    /*    class RetrievePDFFromURL(pdfView: PDFView) :
            AsyncTask<String, Void, InputStream>() {

            // on below line we are creating a variable for our pdf view.
            val mypdfView: PDFView = pdfView

            override fun onPreExecute() {
                super.onPreExecute()
    //            LicenceAnotherFragment.licenceAnotherFragment.progressBarPB.show()
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
                return inputStream;
            }

            override fun onPostExecute(result: InputStream?) {
                mypdfView.fromStream(result).load()

            }
        }*/


    fun removeitem(preApprRequestID: String?) {
        pref = SharedPref(requireContext())
        val preApprovalCancelbody = PreApprovalCancelRequestBody(preApprRequestID)


        viewModel.preApprovalCancelRequest(preApprovalCancelbody, pref.getString(Constants.TOKEN))
    }


    private fun requestAccessPopup() {

        showDialogBox!!.setContentView(R.layout.poup_right_of_entry_request_form)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var tvDateName: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvDateName)
        var edtFLNameOne: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtFLNameOne)
        var edtFLNameTwo: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtFLNameTwo)
        var edtFLNameThree: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtFLNameThree)
        var edtFLNameFour: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtFLNameFour)
        var edtFLNameFive: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtFLNameFive)
        var tvCancelDialog: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvCancelDialog)
        var tvSaveDialog: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvSaveDialog)

        var combinedNames = ""

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(tvDateName)
            }
        }

        tvDateName.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        tvSaveDialog.setOnClickListener {
            combinedNames =
                edtFLNameOne.text.toString() + edtFLNameTwo.text.toString() + edtFLNameThree.text.toString() + edtFLNameFour.text.toString() + edtFLNameFive.text.toString()

            if (tvDateName.text.toString().equals("")) {
                showAlert("Please select entry date")
            } else if (combinedNames.equals("")) {
                showAlert("Enter at least one name")
            } else {
//                if (combinedNames.isEmpty()) {
//                    combinedNames = ""
//                }
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                tvDateName!!.text = sdf.format(cal.getTime())
                var rightOfEntryBody = RightOfEntryBody(
                    "", "", "" + tvDateName.text.toString(), "", "" + productID, "",
                    "0", "" + combinedNames, "", "", pref.getString(Constants.userAccountID), ""
                )
                viewModel.rightOfEntryRequest(rightOfEntryBody, pref.getString(Constants.TOKEN))

                Handler(Looper.getMainLooper()).postDelayed({
                    showDialogBox!!.cancel()
                }, 3500)
            }

//            showDialogBox!!.cancel()

        }

        tvCancelDialog.setOnClickListener {
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

    private fun updateDateInView(textview: TextView) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview!!.text = sdf.format(cal.getTime())
    }

    override fun onChatListListener(index: Int) {
        var publickey = data.get(index).publicKey
        bundle.putString("publickey", publickey)
        Log.e("call", "publickey Active " + publickey)
        val fragment = LicenceFragment()
        val bundle = Bundle()
        bundle.putString("publickey", publickey)
        bundle.putString("mapId", mapId)
        bundle.putString("productNo", productNo)
        bundle.putString("productTypeID", producttypeID)
        fragment.setArguments(bundle)
        addNewFragment(fragment, R.id.container, true)

        PreApprovalRequestFragment.isBackPreApproval = false
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.clear()
        googleMap.getUiSettings().setZoomControlsEnabled(true);





        tvMap.setOnClickListener {
            tvMap.setTextColor(Color.parseColor("#FF000000"))
            tvMap.setTypeface(tvMap.getTypeface(), Typeface.BOLD)
            tvHybrid.setTextColor(Color.parseColor("#898989"))
            tvHybrid.setBackgroundResource(R.color.my_account_tab_grey)
            tvMap.setBackgroundResource(R.color.white)
            mMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
        tvHybrid.setOnClickListener {
            tvHybrid.setTextColor(Color.parseColor("#FF000000"))
            tvHybrid.setTypeface(tvHybrid.getTypeface(), Typeface.BOLD)
            tvMap.setTextColor(Color.parseColor("#898989"))
            tvMap.setBackgroundResource(R.color.my_account_tab_grey)
            tvHybrid.setBackgroundResource(R.color.white)
            mMap!!.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        }

    }

    private class PrimeDayDisableDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val date = CalendarDay.today()
            return if (day.isBefore(date)) true else false
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true)
        }
    }

    fun getLastDayOf(month: Int, year: Int): Int {
        return when (month) {
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> {
                if (year % 4 == 0) {
                    29
                } else 28
            }

            else -> 31
        }
    }


    fun showRequestSubmitted() {
        builder = AlertDialog.Builder(requireActivity())
        builder?.setTitle("Submitted")
        builder?.setMessage("Pre-Approval Request Submitted.")
            ?.setCancelable(false)
            ?.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })

        val alert: AlertDialog = builder!!.create()
        alert.setOnShowListener(DialogInterface.OnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(requireActivity().getResources().getColor(R.color.green))
        })

        alert.show()
    }


    private fun openHarvestInformation() {
        showDialogBox!!.setContentView(R.layout.license_renewal_pop_up)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        showDialogBox!!.setCanceledOnTouchOutside(true)

        /// harvest information


        if (type == 1) {

            showDialogBox!!.layout_harvest_information.visibility = View.VISIBLE
            showDialogBox!!.layout_previous_member.visibility = View.GONE

            var edHog: EditText = showDialogBox!!.findViewById<EditText>(R.id.edHog)
            var edBuck: EditText = showDialogBox!!.findViewById<EditText>(R.id.edBuck)
            var edDoe: EditText = showDialogBox!!.findViewById<EditText>(R.id.edDoe)
            var edTurkey: EditText = showDialogBox!!.findViewById<EditText>(R.id.edTurkey)
            var tvSaveAndContinue: TextView =
                showDialogBox!!.findViewById<EditText>(R.id.tvSaveAndContinue)


            ///  set elipsize end Buck

            /*
               edBuck.setKeyListener(null);
            edBuck.setEllipsize(TextUtils.TruncateAt.END);
            edBuck.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    //Editable status

                    edBuck.setEllipsize(null)
                    edBuck.setKeyListener(TextKeyListener(TextKeyListener.Capitalize.NONE, false))
                    edBuck.setInputType(InputType.TYPE_CLASS_NUMBER)

                    showSoftKeyboard(v)
                } else {
                    edBuck.setKeyListener(null)
                    edBuck.setEllipsize(TextUtils.TruncateAt.END)
                }
            }

            ///  set elipsize end Doe

            edDoe.setKeyListener(null);
            edDoe.setEllipsize(TextUtils.TruncateAt.END);
            edDoe.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    //Editable status

                    edDoe.setEllipsize(null)
                    edDoe.setKeyListener(TextKeyListener(TextKeyListener.Capitalize.NONE, false))
                    edDoe.setInputType(InputType.TYPE_CLASS_NUMBER)

                    showSoftKeyboard(v)
                } else {
                    edDoe.setKeyListener(null)
                    edDoe.setEllipsize(TextUtils.TruncateAt.END)
                }
            }

            ///  set elipsize end Turkey

            edTurkey.setKeyListener(null);
            edTurkey.setEllipsize(TextUtils.TruncateAt.END);
            edTurkey.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    //Editable status

                    edTurkey.setEllipsize(null)
                    edTurkey.setKeyListener(TextKeyListener(TextKeyListener.Capitalize.NONE, false))
                    edTurkey.setInputType(InputType.TYPE_CLASS_NUMBER)

                    showSoftKeyboard(v)
                } else {
                    edTurkey.setKeyListener(null)
                    edTurkey.setEllipsize(TextUtils.TruncateAt.END)
                }
            }

            ///  set elipsize end Hog

            edHog.setKeyListener(null);
            edHog.setEllipsize(TextUtils.TruncateAt.END);
            edHog.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    //Editable status

                    edHog.setEllipsize(null)
                    edHog.setKeyListener(TextKeyListener(TextKeyListener.Capitalize.NONE, false))
                    edHog.setInputType(InputType.TYPE_CLASS_NUMBER)

                    showSoftKeyboard(v)
                } else {
                    edHog.setKeyListener(null)
                    edHog.setEllipsize(TextUtils.TruncateAt.END)
                }
            }


            edHog.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    edHog.setEllipsize(TextUtils.TruncateAt.END)
                    val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)

                    edBuck.clearFocus()
                    edDoe.clearFocus()
                    edHog.clearFocus()
                    edTurkey.clearFocus()

                    edBuck.setKeyListener(null)
                    edBuck.setEllipsize(TextUtils.TruncateAt.END)

                    return@OnEditorActionListener true
                }
                false
            })
             */

            tvSaveAndContinue.setOnClickListener {

                if (edBuck.text.toString().equals("")) {
                    showShortToast("please enter buck")
                } else if (edDoe.text.toString().equals("")) {
                    showShortToast("please enter doe")
                } else if (edTurkey.text.toString().equals("")) {
                    showShortToast("please enter turkey")
                } else if (edHog.text.toString().equals("")) {
                    showShortToast("please enter hog")
                } else {
                    var addHarvestingRequest = AddHarvestingRequest(
                        edHog.text.toString(),
                        edBuck.text.toString(),
                        edDoe.text.toString(),
                        2021,
                        productID.toInt(),
                        edTurkey.text.toString()
                    )
                    viewModel!!.addHarvestingRequest(
                        addHarvestingRequest,
                        pref.getString(Constants.TOKEN)
                    )
                }
            }
        }

        /// previous member

        if (type == 2) {

            showDialogBox!!.layout_harvest_information.visibility = View.GONE
            showDialogBox!!.layout_previous_member.visibility = View.VISIBLE
            showDialogBox!!.rvPreviousMembers.visibility = View.VISIBLE
            showDialogBox!!.harvest_previous_Button.visibility = View.VISIBLE
            showDialogBox!!.harvest_information_Button.visibility = View.GONE

            showDialogBox!!.rvPreviousMembers.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            previousMemberAdapter = PreviousMemberAdapter(renewalList, activity)
            showDialogBox!!.rvPreviousMembers.adapter = previousMemberAdapter
            showDialogBox!!.viewHarvest.setBackgroundColor(resources.getColor(R.color.green))
            showDialogBox!!.tvSecond.background =
                resources.getDrawable(R.drawable.round_green_shape)

            showDialogBox!!.tvContinue.setOnClickListener {

                acceptAndPayPopup(agreementName)
            }
        }


        showDialogBox!!.tvClosePrevious.setOnClickListener {
            showDialogBox!!.dismiss()
        }
        showDialogBox!!.tvClose.setOnClickListener {
            showDialogBox!!.dismiss()
        }


        showDialogBox!!.show()
    }


    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

}





