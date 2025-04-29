package com.myoutdoor.agent.fragment.licenseviewdetails.forhome

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.viewpager.widget.ViewPager
import com.github.barteksc.pdfviewer.PDFView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.*
import com.myoutdoor.agent.models.licensedetails.forhome.*
import com.myoutdoor.agent.models.preapprovalrequest.cancelrequest.PreApprovalCancelRequestBody
import com.myoutdoor.agent.models.rightofentry.RightOfEntryBody
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref

import kotlinx.android.synthetic.main.fragment_licence_now.*
import kotlinx.android.synthetic.main.fragment_licence_now.tvLicenceEndDateDec
import kotlinx.android.synthetic.main.fragment_licence_now.tvLicenceStartDateDesc
import kotlinx.android.synthetic.main.fragment_licence_now.tvPriceDec
import java.text.SimpleDateFormat
import java.util.*


class LicenceNowFragment : Fragment() {

    // BaseFragment()

    // , SimilarPropertiesAdapter.OnItemClickListener
   /* //    OnMapReadyCallback
    var cal = Calendar.getInstance()

    //    var bundle = arguments
    lateinit var viewModel: LicenceNowViewModel
    lateinit var data: ArrayList<SimilarProperty>
    lateinit var data2: ArrayList<Model>

    lateinit var pref: SharedPref
    var key: String = ""

    lateinit var productID: String
    lateinit var agreementName: String
    lateinit var preApprRequestID: String
    var isUserProfileComplete: Boolean = false

    var showDialogBox: Dialog? = null

    var list1 = ArrayList<Amenity>()
    var list2 = ArrayList<Amenity>()
    var list3 = ArrayList<SpecialCondition>()
    val bundle = Bundle()
    var builder2: AlertDialog.Builder? = null

    private var dotscount = 0
    private lateinit var dots: Array<ImageView>

    override fun getLayoutId(): Int {
        return R.layout.fragment_licence_now
    }

    override fun onCreateView() {
        showDialogBox = Dialog(requireContext())
        data = ArrayList()
        viewModel = ViewModelProvider(this).get(LicenceNowViewModel::class.java)
        setObserver()

        val bundle = this.arguments
        if (bundle != null) {
            key = bundle.getString("publickey")!!
            Log.e("call", "key " + key)
        }
        pref = SharedPref(requireContext())
        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
        }

        var licenseViewDetailsBody = LicenceViewDetailHomeBody(
            "" + key
        )
        Log.e("call", "publickey " + key)

        viewModel.licenseViewDetailsRequest(licenseViewDetailsBody, pref.getString(Constants.TOKEN))


        webView1.webViewClient = WebViewClient()
        webView1.settings.javaScriptEnabled = true
//        webView1.loadUrl("https://www.google.com/maps/")
        webView1.loadUrl("https://www.google.com/maps/@29.1280178,103.5976885,3z")
        fragmentBackPressHandle()

        tvRequestTempAccess.setOnClickListener {
            requestAccessPopup()
        }

        tvLicenceNow.setOnClickListener {

            if (tvLicenceNow.text.toString().contains("Select")) {
                if (isUserProfileComplete == true) {
                    acceptAndPayPopup(agreementName)
                } else {
                    showAlert("Incomplete profile. Phone number and mailing address are required for purchasing.")
                }
            } else if (tvLicenceNow.text.toString().contains("Cancel Request")) {
                showAddDeleteDialog()
            } else if (tvLicenceNow.text.toString().contains("Request Pre-Approval")) {
                showAddDeleteDialog()
            }


        }

        fragmentBackPressHandle()


    }

    fun showAddDeleteDialog() {
        builder2 = AlertDialog.Builder(requireContext())
        builder2?.setMessage("")
            ?.setCancelable(false)
            ?.setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, id ->
                removeitem(preApprRequestID)
                dialog.dismiss()

            })
            ?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()

            })
        val alert: AlertDialog = builder!!.create()
        alert.setTitle("Do you want to cancel your request for" + preApprRequestID)
        alert.show()
    }

    fun removeitem(preApprRequestID: String?) {
        pref = SharedPref(requireContext())
        val preApprovalCancelbody = PreApprovalCancelRequestBody(preApprRequestID)
        viewModel.preApprovalCancelRequest(preApprovalCancelbody, pref.getString(Constants.TOKEN))
    }

    fun setObserver() {

        viewModel.cancelRequestSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")


            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                tvLicenceNow.setText("Request Pre-Approval")
//                showShortToast(it.message!!)
            }

        }
        )
        viewModel.rightOfEntryResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())

            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
                showDialog("Your request for temporary access is submitted.")
//                showShortToast(it.message!!)
            }

        }
        )

        viewModel.licenseViewDetailsResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())

            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
//                setViewPager
                val viewPagerAdapter = ViewPagerAdapter(requireContext(), it!!.model.images)
                viewPager.adapter = viewPagerAdapter
                // startAutoSlider()
                dotFunctionality(viewPagerAdapter)


                productID = it.model.activityDetail.productID.toString()
                agreementName = it.model.activityDetail.agreementName
                isUserProfileComplete = it.model.activityDetailPageChecks.isUserProfileComplete
                preApprRequestID = it.model.activityDetailPageChecks.preApprRequestID.toString()


                if (it!!.model!!.activityDetailPageChecks.showComingSoonButton == true) {
                    tvLicenceNow.setText("Coming Soon")
                    tvLicenceNow.setClickable(false)
                } else if (it!!.model!!.activityDetailPageChecks.preApprovalStatus == "Accepted") {
                    tvLicenceNow.setText("Select")
                    tvLicenceNow.setClickable(true)

                } else if (it!!.model!!.activityDetailPageChecks.isPreApprovalRequested == true && it!!.model!!.activityDetailPageChecks.preApprovalStatus == "Requested") {
                    tvLicenceNow.setText("Cancel Request")
                    tvLicenceNow.setClickable(false)

                } else if (it!!.model!!.activityDetailPageChecks.showPreApprovalRequestButton == true) {
                    tvLicenceNow.setText("Request Pre-Approval")
                    tvLicenceNow.setClickable(false)
                } else if (it!!.model!!.activityDetailPageChecks.showRenewButton == true) {
                    tvLicenceNow.setText("Renew License")
                    tvLicenceNow.setClickable(false)

                } else if (it!!.model!!.activityDetailPageChecks.showLicenseNowButton == true && it!!.model!!.activityDetail.activityType != "Day Pass") {
                    tvLicenceNow.setText("Select")
                    tvLicenceNow.setClickable(true)

                } else if (it!!.model!!.activityDetail.activityType == "Day Pass") {
                    tvLicenceNow.setText("Select")
                    tvLicenceNow.setClickable(true)
                } else if (it!!.model!!.activityDetailPageChecks.showAlreadyPurchasedButton == true) {
                    tvLicenceNow.setText("Already Purchased")
                    tvLicenceNow.setClickable(false)
                } else if (it!!.model!!.activityDetailPageChecks.showSoldOutButton == true) {
                    tvLicenceNow.setText("Sold Out")
                    tvLicenceNow.setClickable(false)

                }

                tvHplNorthDesc.setText("" + it!!.model!!.activityDetail.displayName)
                tvHplNorthPrice.setText("$" + it!!.model!!.activityDetail.licenseFee)
                tvAddress.setText("" + it!!.model!!.activityDetail.countyName + ", " + it!!.model!!.activityDetail.state)
                tvPropertyID.setText("" + it!!.model!!.activityDetail.productNo)
                tvPriceDec.setText("$" + it!!.model!!.activityDetail.licenseFee)
                tvAcres.setText("" + it!!.model!!.activityDetail.acres)
                var startDate = dateFormat(it!!.model!!.activityDetail.licenseStartDate)
                var endDate = dateFormat(it!!.model!!.activityDetail.licenseEndDate)

                tvLicenceStartDateDesc.setText("" + startDate)
                tvLicenceEndDateDec.setText("" + endDate)
                tvSpecialConditions.setText("$" + it!!.model!!.specialConditions)

                if (it!!.model!!.activityDetail.displayDescription == null) {
                    tvPropertyDescription.setText("No Property Available")
                } else {
                    tvPropertyDescription.setText("" + it!!.model!!.activityDetail.displayDescription)
                }

                tvContactPAgent.setText("" + it!!.model!!.activityDetail.phone)
                tvTime.setText("" + it!!.model!!.activityDetail.timeZone + "" + it!!.model!!.activityDetail.timeZone)


                for (i in 0 until it!!.model.amenities.size) {
                    if (it!!.model.amenities.get(i).amenityType.equals("Activity")) {
                        list1.add(it.model.amenities.get(i))
                    } else if (it!!.model.amenities.get(i).amenityType.equals("Amenity")) {
                        list2.add(it.model.amenities.get(i))

                    }
                }

                rvActivitiesLN.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvActivitiesLN.adapter = LicenseNowAmenityAdapter(list1, activity)

                rvAmenitiesLN.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvAmenitiesLN.adapter = LicenseNowAmenityAdapter(list2, activity)

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


                for (i in 0 until it!!.model.amenities.size) {
                    if (it!!.model.amenities.get(i).amenityType.equals("Activity")) {
                        list1.clear()
                        list1.add(it.model.amenities.get(i))
                    } else if (it!!.model.amenities.get(i).amenityType.equals("Amenity")) {
                        list2.clear()
                        list2.add(it.model.amenities.get(i))

                    }
                }

                for (i in 0 until it!!.model.specialConditions.size) {
                    list3.clear()
                    list3.add(it.model.specialConditions.get(i))
                }

                rvSpecialConditionsGuidelinesHome.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvSpecialConditionsGuidelinesHome.adapter =
                    LicenseGuideLineHomeAdapter(list3, activity)


                if (list3.isEmpty()) {
                    rvSpecialConditionsGuidelinesHome.visibility = View.GONE
                    tvSpecialConditions.visibility = View.VISIBLE
                } else {
                    rvSpecialConditionsGuidelinesHome.visibility = View.VISIBLE
                    tvSpecialConditions.visibility = View.GONE
                }

                for (i in 0 until it!!.model.similarProperties.size) {
                    data.clear()
                    data.addAll(listOf(it!!.model.similarProperties.get(i)))
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

            }

        }
        )


        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed" + it)
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
        })


        fun setViewPager(images: List<Image>) {

            val viewPagerAdapter = ViewPagerAdapter(requireContext(), images)
            viewPager.adapter = viewPagerAdapter
//dots = new ImageView[dotscount];
            dotscount = viewPagerAdapter.getCount()
            val dots: Array<ImageView?> = arrayOfNulls(dotscount)
            // dots = arrayOf<ImageView>() =

            for (i in 0 until dotscount) {
                dots[i] = ImageView(requireContext())
                dots.get(i)!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.non_active_dot
                    )
                )

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                params.setMargins(8, 0, 8, 0)
                SliderDots.addView(dots.get(i), params)
            }

            dots.get(0)?.setImageDrawable(
                ContextCompat.getDrawable(
                    getApplicationContext(),
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

                override fun onPageSelected(position: Int) {
                    for (i in 0 until dotscount) {
                        dots.get(i)!!.setImageDrawable(
                            ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.non_active_dot
                            )
                        )
                    }
                    dots.get(position)!!.setImageDrawable(
                        ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.active_dot
                        )
                    )
                }

                override fun onPageScrollStateChanged(state: Int) {


                }
            })

        }


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


    // dots = new ImageView[dotscount];
    //  private lateinit var dots: Array<ImageView>

    private fun setViewPager(images: List<Image>) {

        val viewPagerAdapter = ViewPagerAdapter(requireContext(), images)
        viewPager.adapter = viewPagerAdapter

        dotscount = viewPagerAdapter.getCount()
        dots = arrayOf<ImageView>()

        for (i in 0 until dotscount) {
            dots[i] = ImageView(requireContext())
            dots.get(i).setImageDrawable(
                ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.non_active_dot
                )
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(8, 0, 8, 0)
            SliderDots.addView(dots.get(i), params)
        }

        dots.get(0).setImageDrawable(
            ContextCompat.getDrawable(
                getApplicationContext(),
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

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotscount) {
                    dots.get(i).setImageDrawable(
                        ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.non_active_dot
                        )
                    )
                }
                dots.get(position).setImageDrawable(
                    ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.active_dot
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }


    *//*  override fun onMapReady(googleMap: GoogleMap) {
          googleMap.addMarker(
              MarkerOptions()
                  .position(LatLng(30.7046, 76.7179))
                  .title("Marker")
          )    }*//*


    private fun acceptAndPayPopup(agreementName: String) {

        showDialogBox!!.setContentView(R.layout.popup_accept_and_pay)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var pdfViewAP: PDFView = showDialogBox!!.findViewById<PDFView>(R.id.pdfViewAP)
        var tvAcceptAndPay: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvAcceptAndPay)

        var ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)


        var url: String = Constants.AMENITIES_URL + "Assets/Agreements/" + agreementName
        RetrievePDFFromURL(pdfViewAP).execute(url)

        tvAcceptAndPay.setOnClickListener {

            showDialogBox!!.cancel()
        }

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

        var combinedNames =
            edtFLNameOne.text.toString() + edtFLNameTwo.text.toString() + edtFLNameThree.text.toString() + edtFLNameFour.text.toString() + edtFLNameFive.text.toString()

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
            if (tvDateName.text.toString().equals("")) {
                showAlert("Please select entry date")
            } else {
                if (combinedNames.isEmpty()) {
                    combinedNames = ""
                }
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

    private fun updateDateInView2(textview: TextView) {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview!!.text = sdf.format(cal.getTime())
    }

    override fun onChatListListener(index: Int) {
        var publickey = data.get(index).publicKey
        bundle.putString("publickey", publickey)
        Log.e("call", "publickey Active " + publickey)
        val fragment = LicenceNowFragment()
        val bundle = Bundle()
        bundle.putString("publickey", publickey)
        fragment.setArguments(bundle)
        replaceFragment(fragment, R.id.container, true)
    }
*/
}