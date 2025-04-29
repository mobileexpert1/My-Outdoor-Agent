package com.myoutdoor.agent.fragment.search

import MyItem
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.TextKeyListener
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.bumptech.glide.Glide
import com.github.barteksc.pdfviewer.BuildConfig
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.NewAdapterOfSearch
import com.myoutdoor.agent.adapter.SearchItemAdapter
import com.myoutdoor.agent.adapter.SelectActivitiesAdapter
import com.myoutdoor.agent.adapter.SelectAmenitiesAdapter
import com.myoutdoor.agent.fragment.DirectiongoogleMapFragment
import com.myoutdoor.agent.fragment.MapViewFragment
import com.myoutdoor.agent.fragment.PreApprovalRequest.PreApprovalRequestFragment
import com.myoutdoor.agent.fragment.licence.LicenceFragment
import com.myoutdoor.agent.models.RluMapDetails.RluBody
import com.myoutdoor.agent.models.ZoomLevel.Zoomlevel
import com.myoutdoor.agent.models.fill_map_areas.FillMapAreasResponse
import com.myoutdoor.agent.models.getavailablecountiesbystate.GetAvailableCountiesByStateBody
import com.myoutdoor.agent.models.getavailablestates.Model
import com.myoutdoor.agent.models.point_layer.Feature
import com.myoutdoor.agent.models.product_type_rlu.CARluResponse
import com.myoutdoor.agent.models.savedsearches.postsavedsearches.PostSaveSearchesBody
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillBody
import com.myoutdoor.agent.models.search.SearchBody
import com.myoutdoor.agent.models.search.searchV2.body.SearchV2Body
import com.myoutdoor.agent.utils.*
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.dialog_progress_circular.*
import kotlinx.android.synthetic.main.empty_list_property_alert.*
import kotlinx.android.synthetic.main.fragment_licence.*
import kotlinx.android.synthetic.main.fragment_map_view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.tvMap
import kotlinx.android.synthetic.main.map_property_details_popup.*
import kotlinx.android.synthetic.main.property_not_available_popup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notifyAll
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : BaseFragment(), SelectActivitiesAdapter.OnItemClickListener,
    SelectAmenitiesAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener,
    NewAdapterOfSearch.OnItemClickListener, OnMapReadyCallback,
    GoogleMap.OnCameraChangeListener {
    val fragment = MapViewFragment()
    var rluNo = ""
    var isLicensec: Int = -1
    var isClicked = true
    var statnamelist: List<String> = ArrayList<String>()
    var selectedCountylist: List<String> = ArrayList<String>()
    private lateinit var searchItemAdapter: SearchItemAdapter
    lateinit var viewModel: SearchViewModel
    lateinit var pref: SharedPref
    var stateNameList = ArrayList<String>()
    var countyNameList = ArrayList<String>()
    var getAllAvailableStatesResponseList = ArrayList<Model>()
    var getAvailableCountiesByStateResponseList =
        ArrayList<com.myoutdoor.agent.models.getavailablecountiesbystate.Model>()

    //    var searchResponseList = mutableListOf<com.myoutdoor.agent.models.search.Model>()
    var searchResponseList = mutableListOf<com.myoutdoor.agent.models.search.searchV2.Model>()
    var getallamenitiesList = ArrayList<com.myoutdoor.agent.models.getallamenities.Model>()
    lateinit var activityamenityarrayList: ArrayList<String>
    var showDialogBox: Dialog? = null
    lateinit var selectActivitiesAdapter: SelectActivitiesAdapter
    lateinit var selectAmenitiesAdapter: SelectAmenitiesAdapter
    var amenitylist1 = ArrayList<com.myoutdoor.agent.models.getallamenities.Model>()
    var amenitylist2 = ArrayList<com.myoutdoor.agent.models.getallamenities.Model>()
    var amenityNameList = ArrayList<com.myoutdoor.agent.models.search.Model>()
    var selectedItemsActivity = ArrayList<String>()
    var selectedItemsAmenities = ArrayList<String>()
    var finalListAmenitiesActivities = ArrayList<String>()
    var statnamearray = ArrayList<String>()
    var freeTextList = ArrayList<String>()
    var dummylist = ArrayList<String>()
    var searchlist = ArrayList<com.myoutdoor.agent.models.savedsearches.searchautofill.Model>()
    var searchListItems: ArrayList<String> = ArrayList()
    lateinit var searchHere: String
    lateinit var permitsProductTypeID: String
    var normal: String? = null
    lateinit var freetextcommon: String
    var selectFromList: String? = null
    lateinit var rluProductTypeID: String
    var productTypeID: String = "0"
    var ProductNo: String = ""
    var countyName: String = ""
    var state: String = ""
    var acres: String = ""
    var licenseFee: String = ""
    private lateinit var searchNewAdapter: NewAdapterOfSearch
    lateinit var item: String

    var isAdapterClicked: Boolean = false
    var common: String? = null
    var commontype: String? = null
    var statename: String? = null
    private var mMap: GoogleMap? = null
    var selectedCounty: String? = null
    lateinit var commonarray: ArrayList<String>
    val bundle = Bundle()
    var permitCheck: Boolean = true
    var rluCheck: Boolean = true
    var amenityarray: String? = null
    var amenityarrayfreet: String? = null
    var rluList: String? = null
    var countyList: String? = null
    var regionList: String? = null
    var propertyList: String? = null
    var freetext: String? = null
    lateinit var psSelectStateSearch: PowerSpinnerView
    lateinit var psCountySpinner: PowerSpinnerView

    var isMapSelected: Boolean = false
    val features: ArrayList<Feature> = ArrayList()
    var accessPointFeatures: ArrayList<com.myoutdoor.agent.models.gate_access_points.Feature> = ArrayList()
    var fillMapFeatures: ArrayList<com.myoutdoor.agent.models.fill_map_areas.Feature> = ArrayList()
    val featuresListCount: ArrayList<Feature> = ArrayList()
    val featuresListCount2: ArrayList<Feature> = ArrayList()
    val coordinatesList: ArrayList<List<Double>> = ArrayList()
    val propertiesList: ArrayList<com.myoutdoor.agent.models.point_layer.Properties> = ArrayList()
    val licenceKeyList: ArrayList<Int> = ArrayList()
    var coordinates: ArrayList<LatLng> = ArrayList()
    var simplecoordinates: ArrayList<LatLng> = ArrayList()

    //    var multiPolygonFeatures: ArrayList<com.myoutdoor.agent.models.multi_polygons.new_models.Feature> = ArrayList()
    var multiPolygonFeatures: JSONArray? = null
    var multiPolygonFourFeatures: JSONArray? = null
    var selectedStatesFeatures: JSONArray? = null

    private var clusterManager: ClusterManager<MyItem?>? = null

    private var latLng: LatLng? = null
    var latLngMultiPolyline: LatLng? = null
    var latLngPolyline: LatLng? = null
    val listMultiArray: ArrayList<LatLng> = ArrayList()
    var listPolyArray: ArrayList<LatLng> = ArrayList()
    var particularLiness: Polyline? = null
    var multi_polyline: Polyline? = null
    var polylines: Polyline? = null
    var polylineList = ArrayList<Polyline>()
    var multiPolylineList = ArrayList<Polyline>()
    var particularLineList = ArrayList<Polyline>()

    var progressSearchBar: CircularProgressDialog? = null

    lateinit var offsetItem: MyItem
    lateinit var clusterIcon: Drawable
    var publickey: String = ""
    var imageFileName: String = ""
    var rluNumber = ""
    var parent: ViewGroup? = null;
    var zoomInFlag = -1
    var zoomOutFlag = -1
    var isfirst = false
    var licencekey = -1
    var particluarItem: Marker? = null
    var titleZoomLevel: ArrayList<Zoomlevel> = ArrayList()
    var markerList: ArrayList<Marker> = ArrayList()

    var isFirstLoad = false
    var isLoading = false

    var multipolygenList = ArrayList<Any>()

    var a: Marker? = null
    var isLicense = 0


    companion object {
        var count = 1
        var sort: String? = null
        var order: String? = null
        var acresMin = ""
        var acresMax = ""
        var priceMin = ""
        var priceMax = ""
    }

    // private var iconGenerator:IconGenerator= IconGenerator(context)
    override fun onStop() {
        super.onStop()
        count = 1
        acresMin = ""
        acresMax = ""
        priceMin = ""
        priceMax = ""
        searchResponseList.clear()
        features.clear()
        accessPointFeatures.clear()
        fillMapFeatures.clear()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    fun init() {
        listMultiArray.clear()
        listPolyArray.clear()
        simplecoordinates.clear()
        multiPolylineList.clear()
        particularLineList.clear()
        polylineList.clear()
        accessPointFeatures.clear()
        fillMapFeatures.clear()
        features.clear()
        titleZoomLevel.clear()
        mMap = null
        progressSearchBar = CircularProgressDialog(activity)
        showDialogBox = Dialog(requireContext())
        showDialogBox!!.setContentView(R.layout.popup_adjust_filters)
        psSelectStateSearch = showDialogBox!!.findViewById(R.id.psSelectStateSearch)
        psCountySpinner = showDialogBox!!.findViewById(R.id.psCountySpinner)

        MainActivity.mainActivity.bottomNav.getMenu().getItem(1).setChecked(true)

        val mapFragment = childFragmentManager.findFragmentById(R.id.Map) as SupportMapFragment
        mapFragment.getMapAsync(this);

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onCreateView() {

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
        init()

        /*     CoroutineScope(Dispatchers.Main).launch {
                 viewModel.pointLayer(
                     "1=1",
                     "*",
                     true,
                     "geojson",
                 )
                 viewModel.multiPolygenLayer(
                     "1=1",
                     "*",
                     true,
                     "geojson"
                 )
             }  */

        setObserver()
        searchObserver()
//        countyObserver()

        psSelectStateSearch.spinnerOutsideTouchListener = object : OnSpinnerOutsideTouchListener {
            override fun onSpinnerOutsideTouch(view: View, event: MotionEvent) {
                Log.e("jfsdjfksdjkfsjdkfjsdfj", "onSpinnerOutsideTouch: ")
                psSelectStateSearch.dismiss()
            }
        }

        pvSortBy.spinnerOutsideTouchListener = object : OnSpinnerOutsideTouchListener {
            override fun onSpinnerOutsideTouch(view: View, event: MotionEvent) {
                pvSortBy.dismiss()
            }
        }

        psCountySpinner.spinnerOutsideTouchListener = object : OnSpinnerOutsideTouchListener {
            override fun onSpinnerOutsideTouch(view: View, event: MotionEvent) {
                psCountySpinner.dismiss()
            }
        }


        /// set scrollview in orientation mode

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            parentScrollview.isEnabled = false
            gv_SearchListView.isNestedScrollingEnabled = true
        } else {
            parentScrollview.isEnabled = true
            gv_SearchListView.isNestedScrollingEnabled = false
            llListViewLayout.isClickable = false
        }

        selectedItemsActivity = ArrayList()
        selectedItemsAmenities = ArrayList()
        finalListAmenitiesActivities = ArrayList()
        pref = SharedPref(requireContext())

        viewModel.getAvailableStatesRequest(pref.getString(Constants.TOKEN))
        statesObserver()
        /* showDialogBox = Dialog(requireContext())*/
        getallamenitiesList = ArrayList()
        dummylist = ArrayList()
        searchlist = ArrayList()
        searchResponseList = ArrayList()
        searchListItems = ArrayList()
        commonarray = ArrayList()
        commonarray = ArrayList()


        viewModel.getAllAmenitiesRequest(pref.getString(Constants.TOKEN))
//        searchResponseList.clear()
        features.clear()
        accessPointFeatures.clear()
        fillMapFeatures.clear()

        val homebundle = this.arguments
        if (homebundle != null) {
            amenityarray = homebundle.getString("amenityarray")
            amenityarrayfreet = homebundle.getString("amenityarrayfreet")
            rluList = homebundle.getString("rluList")
            countyList = homebundle.getString("countyList")
            regionList = homebundle.getString("regionList")
            propertyList = homebundle.getString("propertyList")
            freetext = homebundle.getString("freetext")
            Log.e("call", "amenityarray " + amenityarray)
        }


        if (amenityarray != null) {
            amenityarray = amenityarray
        } else {
            amenityarray = ""
        }

        if (amenityarrayfreet != null) {
            amenityarrayfreet = amenityarrayfreet
        } else {
            amenityarrayfreet = ""
        }

        if (permitCheck == true) {
            showDialogBox!!.cbPermits.isChecked = true
        }
        if (rluCheck == true) {
            showDialogBox!!.cbRul.isChecked = true
        }

        if (pref.getBoolean(Constants.IS_HOME_API_HIT) == true) {
            pref.setBoolean(Constants.IS_HOME_API_HIT, false)
            if (freetext != null) {
                var freetextList: List<String> = ArrayList<String>(Arrays.asList(freetext))
                var amenityfList: List<String> = ArrayList<String>(Arrays.asList(amenityarrayfreet))
                homeApiSearcht(
                    amenityfList,
                    dummylist,
                    dummylist,
                    dummylist,
                    dummylist,
                    freetextList,
                    dummylist,
                    "New Releases",
                    ""
                )
            } else {

                if (rluList != null) {
                    var amenityList: List<String> = ArrayList<String>(Arrays.asList(amenityarray))
                    var frluList: List<String> = ArrayList<String>(Arrays.asList(rluList))
                    homeApiSearcht(
                        amenityList, dummylist, dummylist, dummylist, dummylist, dummylist,
                        frluList, "New Releases", ""
                    )
                } else if (countyList != null) {
                    var amenityList: List<String> = ArrayList<String>(Arrays.asList(amenityarray))
                    var fcountyList: List<String> = ArrayList<String>(Arrays.asList(countyList))
                    homeApiSearcht(
                        amenityList, fcountyList, dummylist, dummylist, dummylist, dummylist,
                        dummylist, "New Releases", ""
                    )
                } else if (regionList != null) {
                    var amenityList: List<String> = ArrayList<String>(Arrays.asList(amenityarray))
                    var fregionList: List<String> = ArrayList<String>(Arrays.asList(regionList))
                    homeApiSearcht(
                        amenityList, dummylist, dummylist, fregionList, dummylist, dummylist,
                        dummylist, "New Releases", ""
                    )
                } else if (propertyList != null) {
                    var amenityList: List<String> = ArrayList<String>(Arrays.asList(amenityarray))
                    var fpropertyList: List<String> = ArrayList<String>(Arrays.asList(propertyList))
                    homeApiSearcht(
                        amenityList, dummylist, dummylist, dummylist, fpropertyList, dummylist,
                        dummylist, "New Releases", ""
                    )
                }
            }


        } else {
            if (rluCheck == true && permitCheck == true) {
                productTypeID = "0"
                dummySearchApiHit(productTypeID)
            } else if (permitCheck == false && rluCheck == true) {
                productTypeID = "1"
                dummySearchApiHit(productTypeID)
            } else if (permitCheck == true && rluCheck == false) {
                productTypeID = "2"
                dummySearchApiHit(productTypeID)
            } else if (permitCheck == false && rluCheck == false) {
                productTypeID = "0"
                dummySearchApiHit(productTypeID)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.pointLayer(
                "1=1",
                "*",
                true,
                "geojson",
            )
            viewModel.multiPolygenLayer(
                "1=1",
                "*",
                true,
                "geojson"
            )

            viewModel.multiPolygonLayerFour(
                "1=1",
                "*",
                true,
                "geojson"
            )


            viewModel.gateAccessPoints(
                "1=1",
                "*",
                true,
                "geojson"
            )

            viewModel.fillAreas(
                "ProductType='Non-Motorized'",
                "*",
                true,
                "geojson"
            )
            setObserver()
        }

        lldropdown.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
            //  val dialog = BottomSheetDialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.sortby_bottom_sheet)
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


            var tvNewRelease = dialog.findViewById<TextView>(R.id.tvNewRelease)
            var tvPriceHTL = dialog.findViewById<TextView>(R.id.tvPriceHTL)
            var tvPriceLTH = dialog.findViewById<TextView>(R.id.tvPriceLTH)
            var tvAcresHTL = dialog.findViewById<TextView>(R.id.tvAcresHTL)
            var tvAcresLTH = dialog.findViewById<TextView>(R.id.tvAcresLTH)
            var tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)

            tvNewRelease!!.setOnClickListener {
                Log.e("call", "@@@callll!!!!!!")
                sort = "New Releases"
                order = ""
                dialog.dismiss()
                callSortedBy()
            }
            tvPriceHTL!!.setOnClickListener {
                sort = "Price"
                order = "desc"
                dialog.dismiss()
                callSortedBy()
            }

            tvPriceLTH!!.setOnClickListener {
                sort = "Price"
                order = "asc"
                dialog.dismiss()
                callSortedBy()
            }
            tvAcresHTL!!.setOnClickListener {
                sort = "Acres"
                order = "desc"
                dialog.dismiss()
                callSortedBy()
            }
            tvAcresLTH!!.setOnClickListener {
                sort = "Acres"
                order = "asc"
                dialog.dismiss()
                callSortedBy()
            }
            tvCancel!!.setOnClickListener {
                dialog.dismiss()
            }

        }

        if (order == "") {
            order = ""
        } else {
            order = order
        }

        if (sort == "") {
            sort = "New Releases"
        } else {
            sort = sort
        }

        ilMapView.visibility = View.GONE
        llListViewLayout.visibility = View.VISIBLE
        tvListView.setOnClickListener {
            listviewGreenButton()
        }

        tvMap.setOnClickListener {
            llResetfilterlayout.visibility = View.GONE
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);
            // showMessageoOfMap()
            mapGreenButton()
        }

        searchHere = autoTextView.text.toString()
        val autoTextView = AutoCompleteTextView(requireActivity())
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        autoTextView.layoutParams = layoutParams
        layoutParams.setMargins(30, 30, 30, 30)

        statnamearray.add(statename.toString())


        showDialogBox!!.cbPermits.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

                showDialogBox!!.dismiss()
//                if (p0!!.isChecked) {
                listviewGreenButton()

                if (showDialogBox!!.cbPermits.isChecked == true) {
                    permitCheck = true
                    gv_SearchListView.visibility = View.VISIBLE
                    if (rluCheck == true && permitCheck == true) {
                        productTypeID = "0"
                    } else if (permitCheck == false && rluCheck == true) {
                        productTypeID = "1"
                    } else if (permitCheck == true && rluCheck == false) {
                        productTypeID = "2"
                    } else if (permitCheck == false && rluCheck == false) {
                        productTypeID = "0"
                    }

                    selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
                    var fcomlist: List<String> = ArrayList<String>(Arrays.asList(selectFromList))
                    statnamelist = ArrayList<String>(Arrays.asList(statename))

                    if (selectFromList != null) {
                        if (commontype == "RLU") {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, dummylist, dummylist,
                                fcomlist, "" + sort, "" + order
                            )
                        } else if (commontype == "County") {
                            priceSort(
                                fcomlist, statnamelist, dummylist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else if (commontype == "Region") {
                            priceSort(
                                selectedCountylist, statnamelist, fcomlist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, fcomlist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        }
                    } else {
                        if (selectFromList == null) {
                            selectFromList = ""
                            normal = autoTextView.text.toString()
                            var freetextlist: List<String> =
                                ArrayList<String>(Arrays.asList(normal))
                            normal?.let { commonarray.add(it) }
                            priceSort(
                                selectedCountylist,
                                statnamelist,
                                dummylist,
                                dummylist,
                                freetextlist,
                                dummylist,
                                "" + sort,
                                "" + order
                            )
                        }
                    }

                } else {
                    permitCheck = false

                    if (rluCheck == true && permitCheck == true) {
                        productTypeID = "0"
                    } else if (permitCheck == false && rluCheck == true) {
                        productTypeID = "1"
                    } else if (permitCheck == true && rluCheck == false) {
                        productTypeID = "2"
                    } else if (permitCheck == false && rluCheck == false) {
                        productTypeID = "0"
                    }

                    gv_SearchListView.visibility = View.VISIBLE
                    selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
                    var fcomlist: List<String> = ArrayList<String>(Arrays.asList(selectFromList))
                    statnamelist = ArrayList<String>(Arrays.asList(statename))

                    if (selectFromList != null) {
                        if (commontype == "RLU") {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, dummylist, dummylist,
                                fcomlist, "" + sort, "" + order
                            )
                        } else if (commontype == "County") {
                            priceSort(
                                fcomlist, statnamelist, dummylist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else if (commontype == "Region") {
                            priceSort(
                                selectedCountylist, statnamelist, fcomlist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, fcomlist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        }
                    } else {
                        if (selectFromList == null) {
                            selectFromList = ""
                            normal = autoTextView.text.toString()
                            var freetextlist: List<String> =
                                ArrayList<String>(Arrays.asList(normal))
                            normal?.let { commonarray.add(it) }
                            priceSort(
                                selectedCountylist,
                                statnamelist,
                                dummylist,
                                dummylist,
                                freetextlist,
                                dummylist,
                                "" + sort,
                                "" + order
                            )
                        }
                    }

                }

            }
        })

        ivResetFilters.setOnClickListener {
            listviewGreenButton()
            productTypeID = "0"
            gv_SearchListView.visibility = View.VISIBLE

            var searchBody = SearchBody(
                Amenities = dummylist,
                Client = "",
                County = dummylist,
                IPAddress = "",
                Order = "",
                PriceMax = "0",
                PriceMin = "0",
                ProductTypeID = productTypeID,
                PropertyName = dummylist,
                RLU = dummylist,
                RLUAcresMax = "0",
                RLUAcresMin = "0",
                Sort = "New Releases",
                RegionName = dummylist,
                StateName = dummylist,
                UserAccountID = pref.getString(Constants.userAccountID),
                freeText = dummylist,
            )

            var searchV2Body = SearchV2Body(
                amenities = dummylist,
                client = "",
                county = dummylist,
                order = "",
                pageNumber = 1,
                product = dummylist,
                productAcresMax = 0,
                productAcresMin = 0,
                productPriceMax = 0,
                productPriceMin = 0,
                productTypeID = productTypeID.toInt(),
                sort = "New Releases",
                state = dummylist
            )
            countLoadingFalse()
            viewModel.searchRequest(searchV2Body, pref.getString(Constants.TOKEN))
        }

        showDialogBox!!.cbRul.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

                showDialogBox!!.dismiss()
                listviewGreenButton()
                if (showDialogBox!!.cbRul.isChecked == true) {
                    rluCheck = true
                    gv_SearchListView.visibility = View.VISIBLE
                    if (rluCheck == true && permitCheck == true) {
                        productTypeID = "0"
                    } else if (permitCheck == false && rluCheck == true) {
                        productTypeID = "1"
                    } else if (permitCheck == true && rluCheck == false) {
                        productTypeID = "2"
                    } else if (permitCheck == false && rluCheck == false) {
                        productTypeID = "0"
                    }

                    selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
                    var fcomlist: List<String> = ArrayList<String>(Arrays.asList(selectFromList))
                    statnamelist = ArrayList<String>(Arrays.asList(statename))

                    if (selectFromList != null) {
                        if (commontype == "RLU") {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, dummylist, dummylist,
                                fcomlist, "" + sort, "" + order
                            )
                        } else if (commontype == "County") {
                            priceSort(
                                fcomlist, statnamelist, dummylist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else if (commontype == "Region") {
                            priceSort(
                                selectedCountylist, statnamelist, fcomlist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, fcomlist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        }
                    } else {
                        if (selectFromList == null) {
                            selectFromList = ""
                            normal = autoTextView.text.toString()
                            var freetextlist: List<String> =
                                ArrayList<String>(Arrays.asList(normal))
                            normal?.let { commonarray.add(it) }
                            priceSort(
                                selectedCountylist,
                                statnamelist,
                                dummylist,
                                dummylist,
                                freetextlist,
                                dummylist,
                                "" + sort,
                                "" + order
                            )
                        }
                    }
                }
                else {
                    gv_SearchListView.visibility = View.VISIBLE
//                    productTypeID = "2"
                    rluCheck = false
                    if (rluCheck == true && permitCheck == true) {
                        productTypeID = "0"
                    } else if (permitCheck == false && rluCheck == true) {
                        productTypeID = "1"
                    } else if (permitCheck == true && rluCheck == false) {
                        productTypeID = "2"
                    } else if (permitCheck == false && rluCheck == false) {
                        productTypeID = "0"
                    }

                    selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
                    val fcomlist: List<String> = ArrayList<String>(Arrays.asList(selectFromList))
                    statnamelist = ArrayList<String>(Arrays.asList(statename))

                    if (selectFromList != null) {
                        if (commontype == "RLU") {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, dummylist, dummylist,
                                fcomlist, "" + sort, "" + order
                            )
                        } else if (commontype == "County") {
                            priceSort(
                                fcomlist, statnamelist, dummylist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else if (commontype == "Region") {
                            priceSort(
                                selectedCountylist, statnamelist, fcomlist, dummylist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        } else {
                            priceSort(
                                selectedCountylist, statnamelist, dummylist, fcomlist, dummylist,
                                dummylist, "" + sort, "" + order
                            )
                        }
                    } else {
                        if (selectFromList == null) {
                            selectFromList = ""
                            normal = autoTextView.text.toString()
                            val freetextlist: List<String> =
                                ArrayList<String>(Arrays.asList(normal))
                            normal?.let { commonarray.add(it) }
                            priceSort(
                                selectedCountylist,
                                statnamelist,
                                dummylist,
                                dummylist,
                                freetextlist,
                                dummylist,
                                "" + sort,
                                "" + order
                            )
                        }
                    }
                }

            }
        })


        gv_SearchListView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        searchNewAdapter = NewAdapterOfSearch(
            requireActivity(),
            requireActivity(),
            searchResponseList as ArrayList,
            this,
            this
        )
        gv_SearchListView.adapter = searchNewAdapter

        ilMapView.visibility = View.GONE
        searchNewAdapter.notifyDataSetChanged()

        gv_SearchListView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        val layoutManager = recyclerView.layoutManager
                        if (layoutManager != null) {
                            layoutManager as LinearLayoutManager
                            val visibleItemCount: Int = layoutManager.getChildCount()
                            launch(Dispatchers.Main) {
                                val firstVisibleItem =
                                    layoutManager.findFirstVisibleItemPosition()
                                val totalItemCount: Int = layoutManager.getItemCount()
                                Log.e(
                                    "asclmalc",
                                    "$visibleItemCount firstVisibleItem $firstVisibleItem totalItemCount $totalItemCount"
                                )

                                if (visibleItemCount + firstVisibleItem >= totalItemCount && firstVisibleItem >= 0) {
                                    isFirstLoad = false
                                    withContext(Dispatchers.Main) {
                                        isLoading = true
                                        loadMore()
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        llFilterSearch.setOnClickListener {
            listviewGreenButton()
            adjustFiltersPopup()
        }


        llToggle.setOnClickListener {
            if (isMapSelected) {
                //    mMap!!.clear();
                imvToggle.setImageResource(R.drawable.toggle_map)
                gv_SearchListView.visibility = View.VISIBLE
                ilMapView.visibility = View.GONE
                searchContainer.visibility = View.GONE
                isMapSelected = false
//                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 0F))
//                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(2f))
                if (fragment != null)
                    requireActivity().supportFragmentManager.beginTransaction().remove(fragment)
                        .commit()
//                simpleMarker?.hideInfoWindow()

            /*
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.pointLayer(
                        "1=1",
                        "*",
                        true,
                        "geojson",
                    )
                    viewModel.multiPolygenLayer(
                        "1=1",
                        "*",
                        true,
                        "geojson"
                    )

                    viewModel.multiPolygonLayerFour(
                        "1=1",
                        "*",
                        true,
                        "geojson"
                    )


                    viewModel.gateAccessPoints(
                        "1=1",
                        "*",
                        true,
                        "geojson"
                    )

                    viewModel.fillAreas(
                        "ProductType='Non-Motorized'",
                        "*",
                        true,
                        "geojson"
                    )
                    setObserver()
                }
*/

            } else {
                val mapFragment = childFragmentManager.findFragmentById(R.id.Map) as SupportMapFragment
                mapFragment.getMapAsync(this);

                ilMapView.visibility = View.VISIBLE
                gv_SearchListView.visibility = View.GONE
                imvToggle.setImageResource(R.drawable.toggle_list)
                isMapSelected = true
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1)
//                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(2f));
//                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2f)) // Temporary position/zoom
//                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 0F))
                try {
//                    fragment.clearMap()
                    mMap!!.clear()
                    replaceFragment(SearchFragment(), R.id.searchContainer, false)
                    setUpClusterer()
                } catch (e: Exception) {
                    Log.e("", "Exception" + e)
                }
//                addItems()
//                viewModel.pointLayer(
//                    "1=1",
//                    "*",
//                    true,
//                    "geojson",
//                )
//                viewModel.multiPolygenLayer(
//                    "1=1",
//                    "*",
//                    true,
//                    "geojson"
//                )
/*
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.pointLayer(
                        "1=1",
                        "*",
                        true,
                        "geojson",
                    )
                    viewModel.multiPolygenLayer(
                        "1=1",
                        "*",
                        true,
                        "geojson"
                    )

                    viewModel.multiPolygonLayerFour(
                        "1=1",
                        "*",
                        true,
                        "geojson"
                    )


                    viewModel.gateAccessPoints(
                        "1=1",
                        "*",
                        true,
                        "geojson"
                    )

                    viewModel.fillAreas(
                        "ProductType='Non-Motorized'",
                        "*",
                        true,
                        "geojson"
                    )

                    setObserver()
                }
*/

            }

        }

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


    fun loadMore() {
        count = count + 1
        var searchV2Body = SearchV2Body(
            amenities = dummylist,
            client = "",
            county = dummylist,
            order = "",
            pageNumber = count,
            product = dummylist,
            productAcresMax = 0,
            productAcresMin = 0,
            productPriceMax = 0,
            productPriceMin = 0,
            productTypeID = productTypeID.toInt(),
            sort = "New Releases",
            state = dummylist
        )

        viewModel.searchRequest(searchV2Body, pref.getString(Constants.TOKEN))
//        searchObserver()
//        setObserver()

        if (!isFirstLoad) {
//            isLoading = true
//            searchObserver()
//            setObserver()
        }
    }


    fun callSortedBy() {
        if (selectedCounty == null) {
            selectedCounty = ""
        }

        if (statename == null) {
            statename = ""
        }

        selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
        var fcomlist: List<String> = ArrayList<String>(Arrays.asList(selectFromList))
        statnamelist = ArrayList<String>(Arrays.asList(statename))
        listviewGreenButton()
        if (selectFromList != null) {
            if (commontype == "RLU") {
                priceSort(
                    selectedCountylist, statnamelist, dummylist, dummylist, dummylist,
                    fcomlist, "" + sort, "" + order
                )
            } else if (commontype == "County") {
                priceSort(
                    fcomlist, statnamelist, dummylist, dummylist, dummylist,
                    dummylist, "" + sort, "" + order
                )
            } else if (commontype == "Region") {
                priceSort(
                    selectedCountylist, statnamelist, fcomlist, dummylist, dummylist,
                    dummylist, "" + sort, "" + order
                )
            } else {
                priceSort(
                    selectedCountylist, statnamelist, dummylist, fcomlist, dummylist,
                    dummylist, "" + sort, "" + order
                )
            }
        } else {
            if (selectFromList == null) {
                selectFromList = ""
                normal = autoTextView.text.toString()
//                      freetextcommon = normal
                var freetextlist: List<String> = ArrayList<String>(Arrays.asList(normal))
                normal?.let { commonarray.add(it) }
                priceSort(
                    selectedCountylist,
                    statnamelist,
                    dummylist,
                    dummylist,
                    freetextlist,
                    dummylist,
                    "" + sort,
                    "" + order
                )
            }
        }
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


    fun dummySearchApiHit(producttypeID: String) {
        gv_SearchListView.visibility = View.VISIBLE
        var searchBody = SearchBody(
            dummylist,
            "",
            dummylist,
            "",
            "",
            "0",
            "0",
            producttypeID,
            dummylist,
            dummylist,
            "0",
            "0",
            "New Releases",
            dummylist,
            dummylist,
            pref.getString(Constants.userAccountID),
            dummylist,
        )


        var searchV2Body = SearchV2Body(
            amenities = dummylist,
            client = "",
            county = dummylist,
            order = "",
            pageNumber = 1,
            product = dummylist,
            productAcresMax = 0,
            productAcresMin = 0,
            productPriceMax = 0,
            productPriceMin = 0,
            productTypeID = productTypeID.toInt(),
            sort = "New Releases",
            state = dummylist
        )

        countLoadingFalse()
        viewModel.searchRequest(searchV2Body, pref.getString(Constants.TOKEN))
    }

    fun listviewGreenButton() {
        tvListView.setBackgroundResource(R.drawable.login_register_shape_green)
        tvMap.setBackgroundResource(R.drawable.account_two_tab_shape)
        tvListView.setTextColor(Color.parseColor("#FFFFFFFF"))
        tvMap.setTextColor(Color.parseColor("#FF000000"))
//          addNewFragment(LoginFragment(),R.id.mainContainer,false)
        // flMap.visibility = View.GONE

        if (searchResponseList.size == 0) {
            llResetfilterlayout.visibility = View.VISIBLE
            gv_SearchListView.visibility = View.GONE
        } else {
            llResetfilterlayout.visibility = View.GONE
            gv_SearchListView.visibility = View.VISIBLE
        }
    }

    fun mapGreenButton() {
        tvMap.setBackgroundResource(R.drawable.login_register_shape_green)
        tvListView.setBackgroundResource(R.drawable.account_two_tab_shape)
        tvListView.setTextColor(Color.parseColor("#FF000000"))
        tvMap.setTextColor(Color.parseColor("#FFFFFFFF"))
//          addNewFragment(SignUpFragment(),R.id.mainContainer,false)
        //flMap.visibility = View.VISIBLE
        gv_SearchListView.visibility = View.GONE
    }

    fun priceSort(
        countyList: List<String>,
        stateNameList: List<String>,
        regionNameList: List<String>,
        product: List<String>,
        freeTextList: List<String>,
        rluList: List<String>,
        sort: String,
        order: String
    ) {
        var searchBody = SearchBody(
            dummylist,
            "",
            countyList,
            "",
            "" + order,
            "0",
            "0",
            productTypeID,
            product,
            rluList,
            "0",
            "0",
            "" + sort,
            regionNameList,
            stateNameList,
            pref.getString(Constants.userAccountID),
            freeTextList
        )

        countLoadingFalse()
        searchResponseList.clear()
        var searchV2Body = SearchV2Body(
            amenities = dummylist,
            client = "",
            county = countyList,
            order = "" + order,
            pageNumber = 1,
            product = product,
            productAcresMax = if (acresMax.isEmpty()) 0 else acresMax.toInt(),
            productAcresMin = if (acresMin.isEmpty()) 0 else acresMin.toInt(),
            productPriceMax = if (priceMax.isEmpty()) 0 else priceMax.toInt(),
            productPriceMin = if (priceMin.isEmpty()) 0 else priceMin.toInt(),
            productTypeID = productTypeID.toInt(),
            sort = sort,
            state = stateNameList
        )
        viewModel.searchRequest(searchV2Body, pref.getString(Constants.TOKEN))
    }

    fun homeApiSearcht(
        amenitylist: List<String>,
        countyList: List<String>,
        stateNameList: List<String>,
        regionNameList: List<String>,
        propertynameList: List<String>,
        freeTextList: List<String>,
        rluList: List<String>,
        sort: String,
        order: String
    ) {
        var searchBody = SearchBody(
            amenitylist,
            "",
            countyList,
            "",
            "" + order,
            "0",
            "0",
            "0",
            propertynameList,
            rluList,
            "0",
            "0",
            "" + sort,
            regionNameList,
            stateNameList,
            pref.getString(Constants.userAccountID),
            freeTextList
        )

        var searchV2Body = SearchV2Body(
            amenities = amenitylist,
            client = "",
            county = countyList,
            order = "" + order,
            pageNumber = 1,
            product = propertynameList,
            productAcresMax = 0,
            productAcresMin = 0,
            productPriceMax = 0,
            productPriceMin = 0,
            productTypeID = productTypeID.toInt(),
            sort = "New Releases",
            state = stateNameList
        )
        countLoadingFalse()
        viewModel.searchRequest(searchV2Body, pref.getString(Constants.TOKEN))
    }


    fun searchApiHit(
        countyList: List<String>,
        stateNameList: List<String>,
        regionNameList: List<String>,
        propertynameList: List<String>,
        freeTextList: List<String>,
        rluList: List<String>,
        sort: String,
        order: String,
        priceMax: String,
        priceMin: String,
        acresMax: String,
        acresMin: String,
        amenitiesList: List<String>
    ) {
        var searchBody = SearchBody(
            amenitiesList,
            "",
            countyList,
            "",
            "" + order,
            "" + priceMax,
            "" + priceMin,
            productTypeID,
            propertynameList,
            rluList,
            "" + acresMax,
            "" + acresMin,
            "" + sort,
            regionNameList,
            stateNameList,
            pref.getString(Constants.userAccountID),
            freeTextList
        )


        val priceMin = if (priceMin.isEmpty()) 0.toString() else priceMin
        val priceMax = if (priceMax.isEmpty()) 0.toString() else priceMax

        val acresMin = if (acresMin.isEmpty()) 0.toString() else acresMin
        val acresMax = if (acresMax.isEmpty()) 0.toString() else acresMax

        var searchV2Body = SearchV2Body(
            amenities = amenitiesList,
            client = "",
            county = countyList,
            order = "" + order,
            pageNumber = 1,
            product = propertynameList,
            productAcresMax = acresMax.toInt(),
            productAcresMin = acresMin.toInt(),
            productPriceMax = priceMax.toInt(),
            productPriceMin = priceMin.toInt(),
            productTypeID = productTypeID.toInt(),
            sort = sort,
            state = stateNameList
        )
        countLoadingFalse()
        searchResponseList.clear()
        viewModel.searchRequest(searchV2Body, pref.getString(Constants.TOKEN))
    }

    fun postSavedSearchApiHit(
        countyList: List<String>,
        stateNameList: List<String>,
        regionNameList: List<String>,
        propertynameList: List<String>,
        freeTextList: List<String>,
        rluList: List<String>,
        priceMax: String,
        priceMin: String,
        acresMax: String,
        acresMin: String,
        amenitiesList: List<String>,
        searchName: String
    ) {
        val postSaveSearchesBody = PostSaveSearchesBody(
            amenitiesList,
            "",
            countyList,
            "",
            "" + priceMax,
            "" + priceMin,
            productTypeID,
            propertynameList,
            rluList,
            "" + acresMax,
            "" + acresMin,
            regionNameList,
            searchName,
            stateNameList,
            "" + pref.getString(Constants.userAccountID),
            freeTextList
        )

        viewModel.postSaveSearchesRequest(
            postSaveSearchesBody,
            pref.getString(Constants.TOKEN)
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedItemsActivity.clear()
        selectedItemsAmenities.clear()
        finalListAmenitiesActivities.clear()

        autoTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (isAdapterClicked) {
                    isAdapterClicked = false
                } else {
                    var searchAutoFillBody = SearchAutoFillBody(
                        "" + autoTextView.text
                    )
                    viewModel.searchAutoFillRequest(
                        searchAutoFillBody,
                        pref.getString(Constants.TOKEN)
                    )
                }
            }
        })
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun closeSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    private fun adjustFiltersPopup() {

        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val edtAcresMin: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtAcresMin)
        val edtAcresMax: EditText = showDialogBox!!.findViewById<EditText>(R.id.edtAcresMax)
        val edtTotalPriceMin: EditText =
            showDialogBox!!.findViewById<EditText>(R.id.edtTotalPriceMin)
        val edtTotalPriceMax: EditText =
            showDialogBox!!.findViewById<EditText>(R.id.edtTotalPriceMax)
        val tvSearch: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvSearch)
//        val tvSavedSearch: TextView = showDialogBox!!.findViewById<TextView>(R.id.tvSavedSearch)
        val ivClose: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivClose)
        val ivRefresh: ImageView = showDialogBox!!.findViewById<ImageView>(R.id.ivRefresh)
        val gvSelectActivities: GridView =
            showDialogBox!!.findViewById<GridView>(R.id.gvSelectActivities)
        val gvSelectAmenities: GridView =
            showDialogBox!!.findViewById<GridView>(R.id.gvSelectAmenities)


        if (acresMin == null || acresMin.isEmpty()) {
            edtAcresMin.setText("")
        } else {
            edtAcresMin.setText(acresMin)
        }

        if (acresMax == null || acresMax.isEmpty()) {
            edtAcresMax.setText("")
        } else {
            edtAcresMax.setText(acresMax)
        }

        if (priceMin == null || priceMin.isEmpty()) {
            edtTotalPriceMin.setText("")
        } else {
            edtTotalPriceMin.setText(priceMin)
        }

        if (priceMax == null || priceMax.isEmpty()) {
            edtTotalPriceMax.setText("")
        } else {
            edtTotalPriceMax.setText(priceMax)
        }


        edtAcresMin.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                edtAcresMin.setEllipsize(null)
                edtAcresMin.setKeyListener(TextKeyListener(TextKeyListener.Capitalize.NONE, false))
                edtAcresMin.setInputType(InputType.TYPE_CLASS_NUMBER)
                showSoftKeyboard(v)
            } else {
                edtAcresMin.setKeyListener(null)
                edtAcresMin.setEllipsize(TextUtils.TruncateAt.END)
            }
        }

        ///  set elipsize end Acress Max
        edtAcresMax.setKeyListener(null);
        edtAcresMax.setEllipsize(TextUtils.TruncateAt.END);
        edtAcresMax.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //Editable status

                edtAcresMax.setEllipsize(null)
                edtAcresMax.setKeyListener(TextKeyListener(TextKeyListener.Capitalize.NONE, false))
                edtAcresMax.setInputType(InputType.TYPE_CLASS_NUMBER)

                showSoftKeyboard(v)
            } else {
                //Not editable[enter image description here][1]
                edtAcresMax.setKeyListener(null)
                edtAcresMax.setEllipsize(TextUtils.TruncateAt.END)
            }
        }

        ///  set elipsize end Price Min
        edtTotalPriceMin.setKeyListener(null);
        edtTotalPriceMin.setEllipsize(TextUtils.TruncateAt.END);
        edtTotalPriceMin.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //Editable status

                edtTotalPriceMin.setEllipsize(null)
                edtTotalPriceMin.setKeyListener(
                    TextKeyListener(
                        TextKeyListener.Capitalize.NONE,
                        false
                    )
                )
                edtTotalPriceMin.setInputType(InputType.TYPE_CLASS_NUMBER)

                showSoftKeyboard(v)
            } else {
                //Not editable[enter image description here][1]
                edtTotalPriceMin.setKeyListener(null)
                edtTotalPriceMin.setEllipsize(TextUtils.TruncateAt.END)
            }
        }

        ///  set elipsize end Price Max
        edtTotalPriceMax.setKeyListener(null);
        edtTotalPriceMax.setEllipsize(TextUtils.TruncateAt.END);
        edtTotalPriceMax.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //Editable status

                edtTotalPriceMax.setEllipsize(null)
                edtTotalPriceMax.setKeyListener(
                    TextKeyListener(
                        TextKeyListener.Capitalize.NONE,
                        false
                    )
                )
                edtTotalPriceMax.setInputType(InputType.TYPE_CLASS_NUMBER)

                showSoftKeyboard(v)
            } else {
                //Not editable[enter image description here][1]
                edtTotalPriceMax.setKeyListener(null)
                edtTotalPriceMax.setEllipsize(TextUtils.TruncateAt.END)
            }
        }


        edtTotalPriceMax.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                edtTotalPriceMax.setEllipsize(TextUtils.TruncateAt.END)
                val imm =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                edtAcresMin.clearFocus()
                edtAcresMax.clearFocus()
                edtTotalPriceMin.clearFocus()
                edtTotalPriceMax.clearFocus()

                edtAcresMin.setKeyListener(null)
                edtAcresMin.setEllipsize(TextUtils.TruncateAt.END)

                return@OnEditorActionListener true
            }
            false
        })


        selectActivitiesAdapter =
            SelectActivitiesAdapter(requireActivity(), amenitylist1, this, this@SearchFragment)
        gvSelectActivities.adapter = selectActivitiesAdapter

        selectAmenitiesAdapter =
            SelectAmenitiesAdapter(requireActivity(), amenitylist2, this, this@SearchFragment)
        gvSelectAmenities.adapter = selectAmenitiesAdapter

        ivRefresh.setOnClickListener {

            for (item in amenitylist1) {
                if (item.isSelected == true) {
                    item.isSelected = false;
                }
            }
            for (item in amenitylist2) {
                if (item.isSelected == true) {
                    item.isSelected = false;
                }
            }

            psSelectStateSearch.clearSelectedItem()
            psCountySpinner.clearSelectedItem()
//            productTypeID = "0"
            gv_SearchListView.visibility = View.VISIBLE
            var searchBody = SearchBody(
                dummylist,
                "",
                dummylist,
                "",
                "",
                "0",
                "0",
                "0",
                dummylist,
                dummylist,
                "0",
                "0",
                "New Releases",
                dummylist,
                dummylist,
                pref.getString(Constants.userAccountID),
                dummylist,
            )

            var searchV2Body = SearchV2Body(
                amenities = dummylist,
                client = "",
                county = dummylist,
                order = "",
                pageNumber = 1,
                product = dummylist,
                productAcresMax = 0,
                productAcresMin = 0,
                productPriceMax = 0,
                productPriceMin = 0,
                productTypeID = productTypeID.toInt(),
                sort = "New Releases",
                state = dummylist
            )

            countLoadingFalse()
            searchResponseList.clear()
            viewModel.searchRequest(searchV2Body, pref.getString(Constants.TOKEN))
            showDialogBox!!.cancel()

        }

        var totalPriceMax: String = edtTotalPriceMax.text.toString()
        var totalPriceMin: String = edtTotalPriceMin.text.toString()

        if (edtTotalPriceMax.text.toString().isEmpty()) {
            totalPriceMax = ""
        } else {
            totalPriceMax = edtTotalPriceMax.text.toString()
        }

        if (edtTotalPriceMin.text.toString().isEmpty()) {
            totalPriceMin = ""
        } else {
            totalPriceMin = edtTotalPriceMax.text.toString()
        }


        tvSearch.setOnClickListener {

            finalListAmenitiesActivities.clear()
            for (item in amenitylist1) {
                if (item.isSelected == true) {
                    item.isSelected = false;
                }
            }
            for (item in amenitylist2) {
                if (item.isSelected == true) {
                    item.isSelected = false;
                }
            }


            finalListAmenitiesActivities.addAll(selectedItemsActivity)
            finalListAmenitiesActivities.addAll(selectedItemsAmenities)
            gv_SearchListView.visibility = View.VISIBLE

            /// hit api
            listviewGreenButton()

            if (selectedCounty == null) {
                selectedCounty = ""
            }

            if (statename == null) {
                statename = ""
            }

            selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
            var fcomlist: List<String> = ArrayList<String>(Arrays.asList(selectFromList))
            statnamelist = ArrayList<String>(Arrays.asList(statename))

            if (selectFromList != null) {
                if (commontype == "RLU") {
                    searchApiHit(
                        selectedCountylist,
                        statnamelist,
                        dummylist,
                        dummylist,
                        dummylist,
                        fcomlist,
                        "" + sort,
                        "" + order,
                        "" + edtTotalPriceMax.text.toString(),
                        "" + edtTotalPriceMin.text.toString(),
                        "" + edtAcresMax.text.toString(),
                        "" + edtAcresMin.text.toString(),
                        finalListAmenitiesActivities
                    )
                    acresMin = edtAcresMin.text.toString()
                    acresMax = edtAcresMax.text.toString()
                    priceMin = edtTotalPriceMin.text.toString()
                    priceMax = edtTotalPriceMax.text.toString()
                    showDialogBox!!.cancel()
                } else if (commontype == "County") {
                    searchApiHit(
                        fcomlist,
                        statnamelist,
                        dummylist,
                        dummylist,
                        dummylist,
                        dummylist,
                        "" + sort,
                        "" + order,
                        "" + edtTotalPriceMax.text.toString(),
                        "" + edtTotalPriceMin.text.toString(),
                        "" + edtAcresMax.text.toString(),
                        "" + edtAcresMin.text.toString(),
                        finalListAmenitiesActivities
                    )
                    acresMin = edtAcresMin.text.toString()
                    acresMax = edtAcresMax.text.toString()
                    priceMin = edtTotalPriceMin.text.toString()
                    priceMax = edtTotalPriceMax.text.toString()
                    showDialogBox!!.cancel()
                } else if (commontype == "Region") {
                    searchApiHit(
                        selectedCountylist,
                        statnamelist,
                        fcomlist,
                        dummylist,
                        dummylist,
                        dummylist,
                        "" + sort,
                        "" + order,
                        "" + edtTotalPriceMax.text.toString(),
                        "" + edtTotalPriceMin.text.toString(),
                        "" + edtAcresMax.text.toString(),
                        "" + edtAcresMin.text.toString(),
                        finalListAmenitiesActivities
                    )
                    acresMin = edtAcresMin.text.toString()
                    acresMax = edtAcresMax.text.toString()
                    priceMin = edtTotalPriceMin.text.toString()
                    priceMax = edtTotalPriceMax.text.toString()
                    showDialogBox!!.cancel()
                } else {
                    searchApiHit(
                        selectedCountylist,
                        statnamelist,
                        dummylist,
                        fcomlist,
                        dummylist,
                        dummylist,
                        "" + sort,
                        "" + order,
                        "" + edtTotalPriceMax.text.toString(),
                        "" + edtTotalPriceMin.text.toString(),
                        "" + edtAcresMax.text.toString(),
                        "" + edtAcresMin.text.toString(),
                        finalListAmenitiesActivities
                    )
                    acresMin = edtAcresMin.text.toString()
                    acresMax = edtAcresMax.text.toString()
                    priceMin = edtTotalPriceMin.text.toString()
                    priceMax = edtTotalPriceMax.text.toString()
                    showDialogBox!!.cancel()
                }
            } else {
                if (selectFromList == null) {
                    selectFromList = ""
                    normal = autoTextView.text.toString()
                    var freetextlist: List<String> = ArrayList<String>(Arrays.asList(normal))
                    normal?.let { commonarray.add(it) }
                    searchApiHit(
                        selectedCountylist,
                        statnamelist,
                        dummylist,
                        dummylist,
                        freetextlist,
                        dummylist,
                        "" + sort,
                        "" + order,
                        "" + edtTotalPriceMax.text.toString(),
                        "" + edtTotalPriceMin.text.toString(),
                        "" + edtAcresMax.text.toString(),
                        "" + edtAcresMin.text.toString(),
                        finalListAmenitiesActivities
                    )
                    acresMin = edtAcresMin.text.toString()
                    acresMax = edtAcresMax.text.toString()
                    priceMin = edtTotalPriceMin.text.toString()
                    priceMax = edtTotalPriceMax.text.toString()
                    showDialogBox!!.cancel()
                }
            }
        }

        ivClose.setOnClickListener {
            showDialogBox!!.cancel()
            hideKeyboard(requireActivity())
        }

        showDialogBox!!.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(showDialogBox!!.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        showDialogBox!!.getWindow()!!.setAttributes(lp)


    }

    fun countLoadingFalse() {
        count = 1
        isLoading = false
    }


    fun statesObserver() {
        viewModel.getAvailableStatesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            if (it.message != "Success") {
            } else {

                if (it.model.size <= 0) {
                    showAlert("No State Available")
                } else {
                    stateNameList.clear()
                    getAllAvailableStatesResponseList.clear()
                    getAllAvailableStatesResponseList.addAll(it.model)
                    for (i in 0 until getAllAvailableStatesResponseList.size) {
                        stateNameList.add(getAllAvailableStatesResponseList.get(i).stateName)
                    }

                    psSelectStateSearch.setItems(stateNameList as List<String>)
                    Log.e("jfsdjfksdjkfsjdkfjsdfj", "statesObserver: $stateNameList")

                    psSelectStateSearch.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                        if(isMapSelected){
                            viewModel.selectedStates(
                                getAllAvailableStatesResponseList.get(newIndex).stateAbbrev,
                                "geojson"
                            )
                            setObserver()
                            statnamearray.clear()
                            statename = newItem
                            selectedCounty = ""
                            statnamelist = ArrayList<String>(Arrays.asList(statename))
                            listviewGreenButton()

                            var getAvailableCountiesByStateBody = GetAvailableCountiesByStateBody(
                                getAllAvailableStatesResponseList.get(newIndex).stateAbbrev
                            )

                            psCountySpinner.clearSelectedItem()
                            getAvailableCountiesByStateResponseList.clear()
                            viewModel.getAvailableCountiesByStateRequest(
                                getAvailableCountiesByStateBody,
                                pref.getString(Constants.TOKEN)
                            )
                            countyObserver()

                            showDialogBox!!.dismiss()
                        }else {
                            Log.e("jfsdjfksdjkfsjdkfjsdfj", "statesObserver: ")
                            // do something
                            statnamearray.clear()
                            statename = newItem
                            selectedCounty = ""
                            statnamelist = ArrayList<String>(Arrays.asList(statename))
                            listviewGreenButton()

                            var getAvailableCountiesByStateBody = GetAvailableCountiesByStateBody(
                                getAllAvailableStatesResponseList.get(newIndex).stateAbbrev
                            )

                            psCountySpinner.clearSelectedItem()
                            getAvailableCountiesByStateResponseList.clear()
                            viewModel.getAvailableCountiesByStateRequest(
                                getAvailableCountiesByStateBody,
                                pref.getString(Constants.TOKEN)
                            )
                            countyObserver()
                        }

                    }
                }
            }
        })
    }


    fun searchObserver() {
        viewModel.searchResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message == "Success") {
                listviewGreenButton()
                if (it.model.size > 0 && it.model.isNotEmpty()) {
                    if (isLoading) {
                        searchNewAdapter.loadMore(it.model as ArrayList)
                        isLoading = false
                    } else {
                        searchResponseList.addAll(it.model)
                        searchNewAdapter.notifyDataSetChanged()
                    }
                }
                if (searchResponseList.size <= 0) {
                    llResetfilterlayout.visibility = View.VISIBLE
                    gv_SearchListView.visibility = View.GONE
                } else {
                    llResetfilterlayout.visibility = View.GONE
                    gv_SearchListView.visibility = View.VISIBLE
                }

            } else {
                showShortToast(it.message!!)
            }

        })
    }


    fun countyObserver() {
        viewModel.getAvailableCountiesByStateResponseSuccess.observe(requireActivity(), Observer {
            if (it.statusCode == 200) {
                countyNameList.clear()
                for (i in it.model.indices) {
                    countyNameList.add(it.model.get(i).countyName)
                }
                setDynamicHeightOnSpinner()
                psCountySpinner.setItems(countyNameList as List<String>)

                psCountySpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                    listviewGreenButton()
                    selectedCounty = newItem
                }

            }
        })
    }


    fun rluObserver(){
        viewModel.onRLUMapDetailsSuccess.observe(requireActivity(), Observer {
//            Log.d("@@@@", "Success")
//            Log.e("KulonRLUMap", "onRLUMapDetailsSuccess Success")
            if (it.statusCode == 200) {
                Log.e("!@@", "isLicensec......." + isLicensec)
                publickey = it!!.model.publicKey
                imageFileName = it.model.imageFilename
                rluNumber = it.model.productNo
                countyName = it.model.countyName
                acres = it.model.acres.toString()
                licenseFee = it.model.licenseFee.toString()
                state = it.model.stateName
                var productNo=it.model.productNo

                if (isLicensec == 0) {
                    showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                    showDialogBox!!.getWindow()!!
                        .setBackgroundDrawableResource(android.R.color.transparent)
                    showDialogBox!!.setCanceledOnTouchOutside(true)
                    showDialogBox!!.imgCancelProperty.setOnClickListener {
                        showDialogBox!!.dismiss()
                    }
                    showDialogBox!!.tvHplPermitHead.setText(it.model.productNo)
                    showDialogBox!!.tvPermitCountry.setText(it.model.countyName + "," + state)
                    showDialogBox!!.tvPermitAcres.setText("" + it.model.acres.toString())
                    showDialogBox!!.tvHplPermitPrice.setText("$" + "" + it.model.licenseFee.toString())
                    showDialogBox!!.tvZoomOutIn.text = "Zoom In"
                    isClicked = true

                    for (i in 0 until titleZoomLevel.size) {
                        if (titleZoomLevel[i].title == it.model.productNo) {
                            showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                            isClicked = titleZoomLevel[i].isZoom
                            break
                        }
                    }

                    showDialogBox!!.rlZoomMarker.setOnClickListener {

                        val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                        if (isClicked) {

                            viewModel.particluarPolygenLayer(
                                "RLUNo='$rluNumber'",
                                "*",
                                "esriSpatialRelIntersects",
                                "geojson"
                            )
                            Log.e("####", "zoomlevelIn $zoomLevel")
                            //  isClicked = false

                            titleZoomLevel.add(Zoomlevel(rluNumber, false))


                            mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                            showDialogBox!!.dismiss()
                        } else {

                            for (i in 0 until titleZoomLevel.size) {
                                if (titleZoomLevel[i].title == rluNumber) {

                                    titleZoomLevel.removeAt(i)
                                    break
                                }
                            }


                            mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                            // isClicked = true
                            Log.e("####", "zoomlevelOut $zoomLevel")
                            showDialogBox!!.dismiss()
                        }
                    }

                    showDialogBox!!.rlDetailsMarker.setOnClickListener {
                        showDialogBox!!.dismiss()
                        val fragment = LicenceFragment()
                        val bundle = Bundle()

                        bundle.putString("publickey", publickey)
                        bundle.putString("productNo", productNo)
                        bundle.putString("productTypeID", productNo)
                        bundle.putSerializable("mapId", productNo)
                        fragment.setArguments(bundle)
                        addNewFragment(fragment, R.id.container, true)
                    }

                    showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                        showDialogBox!!.dismiss()
                        addNewFragment(
                            DirectiongoogleMapFragment(progressBarPB),
                            R.id.container,
                            true
                        )
                    }

                    Glide.with(showDialogBox!!.ivSeachImage)
                        .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.round_logo)
                        .error(R.drawable.round_logo)
                        .centerCrop()
                        .into(showDialogBox!!.ivSeachImage)

                    showDialogBox!!.show()

                } else {
                    PropertyNotAvailable()
                }
            }

            /*

                        if (it.message == "Success") {
                            Log.e("!@@", "isLicensec......." + isLicensec)
                            publickey = it!!.model.publicKey
                            imageFileName = it.model.imageFilename
                            rluNumber = it.model.productNo
                            countyName = it.model.countyName
                            acres = it.model.acres.toString()
                            licenseFee = it.model.licenseFee.toString()
                            state = it.model.stateName
                            if (isLicensec == 0) {
                                showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                                showDialogBox!!.getWindow()!!
                                    .setBackgroundDrawableResource(android.R.color.transparent)
                                showDialogBox!!.setCanceledOnTouchOutside(true)
                                showDialogBox!!.imgCancelProperty.setOnClickListener {
                                    showDialogBox!!.dismiss()
                                }
                                showDialogBox!!.tvHplPermitHead.setText(it.model.productNo)
                                showDialogBox!!.tvPermitCountry.setText(it.model.countyName + "," + state)
                                showDialogBox!!.tvPermitAcres.setText("" + it.model.acres.toString())
                                showDialogBox!!.tvHplPermitPrice.setText("$" + "" + it.model.licenseFee.toString())
                                showDialogBox!!.tvZoomOutIn.text = "Zoom In"
                                isClicked = true

                                for (i in 0 until titleZoomLevel.size) {
                                    if (titleZoomLevel[i].title == it.model.productNo) {
                                        showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                                        isClicked = titleZoomLevel[i].isZoom
                                        break
                                    }
                                }

                                showDialogBox!!.rlZoomMarker.setOnClickListener {

                                    val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                                    if (isClicked) {

                                        viewModel.particluarPolygenLayer(
                                            "RLUNo='$rluNumber'",
                                            "*",
                                            "esriSpatialRelIntersects",
                                            "geojson"
                                        )
                                        Log.e("####", "zoomlevelIn $zoomLevel")
                                        //  isClicked = false

                                        titleZoomLevel.add(Zoomlevel(rluNumber, false))


                                        mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                                        showDialogBox!!.dismiss()
                                    } else {

                                        for (i in 0 until titleZoomLevel.size) {
                                            if (titleZoomLevel[i].title == rluNumber) {

                                                titleZoomLevel.removeAt(i)
                                                break
                                            }
                                        }


                                        mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                                        // isClicked = true
                                        Log.e("####", "zoomlevelOut $zoomLevel")
                                        showDialogBox!!.dismiss()
                                    }
                                }

                                showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                    showDialogBox!!.dismiss()
                                    val fragment = LicenceFragment()
                                    val bundle = Bundle()
                                    bundle.putString("publickey", publickey)
                                    fragment.setArguments(bundle)
                                    addNewFragment(fragment, R.id.container, true)
                                }

                                showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                    showDialogBox!!.dismiss()
                                    addNewFragment(
                                        DirectiongoogleMapFragment(progressBarPB),
                                        R.id.container,
                                        true
                                    )
                                }

                                Glide.with(showDialogBox!!.ivSeachImage)
                                    .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                    .fitCenter()
                                    .centerCrop()
                                    .placeholder(R.drawable.round_logo)
                                    .error(R.drawable.round_logo)
                                    .centerCrop()
                                    .into(showDialogBox!!.ivSeachImage)

                                showDialogBox!!.show()

                            } else {
                                PropertyNotAvailable()
                            }
                        }
                        else {
                            showShortToast(it.message!!)
            //                PropertyNotAvailable()
                        }


                 */


        })

    }


    fun onParticularPolygenLayerSuccessObserver(){
        viewModel.onParticularPolygenLayerSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            if (it.features.isEmpty()) {
                AlertMapPopUp()
            }
            else {
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
//                viewModel.pointLayer(
//                    "1=1",
//                    "*",
//                    true,
//                    "geojson",
//                )
//                setObserver()
//                mapApis()
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
            }

        })

    }

   fun  particularPolygonFourLayerSuccessObserver(){
       viewModel.particularPolygonFourLayerSuccess.observe(requireActivity(), Observer { it
           //            var a: Marker? = null

           if (it.features.isEmpty()) {
               AlertMapPopUp()
           } else {
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

//                    mapApis()
               }
           }


       })

   }


    @SuppressLint("SuspiciousIndentation")
    fun setObserver() {

        viewModel.searchAutoFillResponseSuccess.observe(requireActivity(), Observer {

            Log.d("@@@@", "Success")

            if (it.message != "Success") {
//              showShortToast(it.message!!)
            } else {

                isAdapterClicked = false
                searchlist.clear()
                searchListItems.clear()
                searchlist.addAll(it!!.model)

                for (i in 0 until searchlist.size) {
                    searchListItems.add(searchlist.get(i).searchResult)
                }

                if (autoTextView.text.toString().equals("")) {
                    searchListItems.clear()
                    selectFromList = ""
                    rvSearchItems.visibility = View.GONE
                    searchItemAdapter.notifyDataSetChanged()
                } else {
                    if (searchListItems.size > 0) {
                        rvSearchItems.visibility = View.VISIBLE
                        rvSearchItems.layoutManager = LinearLayoutManager(activity)

                        searchItemAdapter = SearchItemAdapter(
                            this@SearchFragment,
                            searchlist
                        ) { data ->

//                            commontype = type
                            isAdapterClicked = true
                            autoTextView.setText(data.searchResult)
                            selectFromList = autoTextView.text.toString()
                            rvSearchItems.visibility = View.GONE
                            listviewGreenButton()

                            if (selectedCounty == null) {
                                selectedCounty = ""
                            }

                            if (statename == null) {
                                statename = ""
                            }

                            selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
                            var fcomlist: List<String> =
                                ArrayList<String>(Arrays.asList(selectFromList))
                            statnamelist = ArrayList<String>(Arrays.asList(statename))

                            var commonList: List<String> = ArrayList<String>()


                            if (data.type == "RLU") {
                                commonList = ArrayList<String>(Arrays.asList(data.searchResult))
                                priceSort(
                                    countyList = dummylist,
                                    stateNameList = statnamelist,
                                    regionNameList = dummylist,
                                    product = commonList,
                                    freeTextList = dummylist,
                                    rluList = fcomlist,
                                    sort = sort.toString(),
                                    order = order.toString()
                                )
                            } else if (data.type == "County") {
                                commonList = ArrayList<String>(Arrays.asList(data.searchResult))
                                priceSort(
                                    countyList = commonList,
                                    stateNameList = statnamelist,
                                    regionNameList = dummylist,
                                    product = dummylist,
                                    freeTextList = dummylist,
                                    rluList = fcomlist,
                                    sort = sort.toString(),
                                    order = order.toString()
                                )

                            } else if (data.type == "Region") {
                                commonList = ArrayList<String>(Arrays.asList(data.searchResult))
                                priceSort(
                                    countyList = dummylist,
                                    stateNameList = statnamelist,
                                    regionNameList = dummylist,
                                    product = commonList,
                                    freeTextList = dummylist,
                                    rluList = fcomlist,
                                    sort = sort.toString(),
                                    order = order.toString()
                                )
                            } else {
                                commonList = ArrayList<String>(Arrays.asList(data.searchResult))
                                priceSort(
                                    countyList = dummylist,
                                    stateNameList = statnamelist,
                                    regionNameList = dummylist,
                                    product = commonList,
                                    freeTextList = dummylist,
                                    rluList = fcomlist,
                                    sort = sort.toString(),
                                    order = order.toString()
                                )
                            }

                            ilMapView.visibility = View.GONE

                        }
                        // callback ends here


                        rvSearchItems.adapter = searchItemAdapter
                    } else {
                        searchListItems.clear()
                        rvSearchItems.visibility = View.GONE
                        searchItemAdapter.notifyDataSetChanged()
                    }
                }

                listviewGreenButton()

                for (i in 0 until searchlist.size) {

                    if (it.model.get(i).type.equals("RLU")) {
                        commontype = "RLU"
                        common = it.model.get(i).searchResult
                    } else if (it.model.get(i).type.equals("County")) {
                        commontype = "County"
                        common = it.model.get(i).searchResult
                    } else if (it.model.get(i).type.equals("Region")) {
                        commontype = "Region"
                        common = it.model.get(i).searchResult
                    } else {
                        commontype = ""
                        common = it.model.get(i).searchResult
                    }

                    searchListItems.add(searchlist.get(i).searchResult)
                }
            }
        })
        //point api

        viewModel.onPointLayerSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            features.clear()
            features.addAll(it!!.features)
//            setUpClusterer()
            Log.e("@@@@", "size......" + features.size)
        })

        viewModel.gateAccessPointsSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            accessPointFeatures.clear()
            accessPointFeatures.addAll(it!!.features)
            Log.e("@@@@", "size......" + accessPointFeatures.size)
        })

        viewModel.fillMapAreasSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            fillMapFeatures.clear()
            fillMapFeatures.addAll(it!!.features)
            Log.e("@@@@", "size......" + fillMapFeatures.size)
        })

        viewModel.selectedStatesSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "$it ")

//            val Poly = it.string()
//            val `object` = JSONObject(Poly)
//            val features = `object`.getJSONArray("features")
//            selectedStatesFeatures=features


            val geometry = it.features[0].geometry
            var boundsBuilder = LatLngBounds.Builder()

            // Check if coordinates is a List<List<List<Double>>> or List<List<List<List<Double>>>>
            if (geometry.coordinates is List<*>) {
                val firstLevel = geometry.coordinates as List<*>

                // Case 1: List<List<List<Double>>> (standard polygon)
                if (firstLevel.isNotEmpty() && firstLevel[0] is List<*>) {
                    val secondLevel = firstLevel[0] as List<*>

                    if (secondLevel.isNotEmpty() && secondLevel[0] is List<*>) {
                        val thirdLevel = secondLevel[0] as List<*>

                        // Case: List<List<List<Double>>> - Standard Polygon
                        if (thirdLevel.isNotEmpty() && thirdLevel[0] is Double) {
                            simplecoordinates.clear() // Clear old coordinates before adding new ones
                            for (i in secondLevel.indices) {
                                val latLngList = secondLevel[i] as List<Double>
                                simplecoordinates.add(
                                    LatLng(
                                        latLngList[1],  // Latitude
                                        latLngList[0]   // Longitude
                                    )
                                )
                                boundsBuilder.include(LatLng(latLngList[1], latLngList[0])) // Add the LatLng to the bounds
                            }
                        }
                        // Case 2: List<List<List<List<Double>>>> - Nested Polygon
                        else if (thirdLevel.isNotEmpty() && thirdLevel[0] is List<*>) {
                            simplecoordinates.clear() // Clear old coordinates before adding new ones
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
                                    boundsBuilder.include(LatLng(latLngList[1], latLngList[0])) // Add the LatLng to the bounds
                                }
                            }
                        }
                    }
                }
            }

            // If there are coordinates to move to
            if (simplecoordinates.isNotEmpty()) {
                val bounds: LatLngBounds = boundsBuilder.build()
                val padding = 100 // Padding around the polygon (in pixels)

                // Move the camera to a temporary position and zoom to reset it
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2f)) // Temporary position/zoom

                // Add a delay before animating to bounds to ensure map is ready
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
                    } catch (e: Exception) {
                        Log.e("Map Error", "Failed to move camera: ${e.message}")
                    }
                }, 500) // 500ms delay to ensure map is ready
            }



           /* for (i in 0 until features.length()){
                val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                val propertiesObject = features.getJSONObject(i).getJSONObject("properties")

                when (featureObject.getString("type")) {
                    "MultiPolygon" -> {

                    }

                    "Polygon" -> {

                    }

                }
            }*/

//            fillMapFeatures.clear()
//            fillMapFeatures.addAll(it!!.features)
//            Log.e("@@@@", "size......" + fillMapFeatures.size)
        })


        viewModel.onMultiPolygenLayerSuccess.observe(requireActivity(), Observer { it
            //            var a: Marker? = null
            listMultiArray.clear()
            listPolyArray.clear()
            simplecoordinates.clear()
            //            multiPolygonFeatures = it
//            multiPolygonFeatures = it as Any
            val Poly = it.string()
            val jsonObject = JSONObject(Poly)
            val features = jsonObject.getJSONArray("features")

//            multiPolygonFeatures = features

            // Initialize multiPolygonFourFeatures if null
            if (multiPolygonFeatures == null) {
                multiPolygonFeatures = features
            } else {
                // If already initialized, append new features
                for (i in 0 until features.length()) {
                    multiPolygonFeatures?.put(features.getJSONObject(i))
                }
            }

//            multiPolygonFourFeatures?.put(features)

            // Call the method to display visible features
//            displayVisibleFeatures(features, mMap!!)

            /*

                        try {

                            val Poly = it.string()
                            val `object` = JSONObject(Poly)
                            val features = `object`.getJSONArray("features")
                            for (i in 0 until features.length()) {
                                val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                                val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                                var isLicensed = propertiesObject.get("IsLicensed")

                                when (featureObject.getString("type")) {
                                    "MultiPolygon" -> {
                                        for (j in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
                                            for (k in 0 until ((featureObject.getJSONArray("coordinates")
                                                .get(j) as JSONArray).length())) {
                                                for (l in 0 until ((featureObject.getJSONArray("coordinates")
                                                    .get(j) as JSONArray).get(k) as JSONArray).length()) {
                                                    latLngMultiPolyline = convertToLatLng(
                                                        (((featureObject.getJSONArray("coordinates")
                                                            .get(j) as JSONArray).get(k) as JSONArray).get(l) as JSONArray)
                                                    )

                                                    listMultiArray.add(latLngMultiPolyline!!)
                                                    val rluNumber = propertiesObject.get("RLUNo")
                                                    a = mMap!!.addMarker(
                                                        MarkerOptions().position(
                                                            LatLng(
                                                                listMultiArray[l].latitude,
                                                                listMultiArray[l].longitude
                                                            )
                                                        ).title(rluNumber.toString())
                                                            .snippet(isLicensed.toString())
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                                    )!!
                                                }

                                                if (isLicensed == 0) {
                                                    multi_polyline = mMap!!.addPolyline(
                                                        (PolylineOptions().addAll(listMultiArray).width(5f)
                                                            .color(
                                                                requireActivity().getResources()
                                                                    .getColor(R.color.green)
                                                            ).geodesic(true))
                                                    )
                                                    multiPolylineList.add(multi_polyline!!)

                                                } else {
                                                    multi_polyline = mMap!!.addPolyline(
                                                        (PolylineOptions().addAll(listMultiArray).width(5f)
                                                            .color(
                                                                requireActivity().getResources()
                                                                    .getColor(R.color.red_pre_approval)
                                                            ).geodesic(true))
                                                    )
                                                    multiPolylineList.add(multi_polyline!!)
                                                }
                                            }
                                            listMultiArray.clear()
                                        }
                                    }

                                    "Polygon" -> {
                                        for (m in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
                                            for (n in 0 until ((featureObject.getJSONArray("coordinates")
                                                .get(m) as JSONArray).length())) {
                                                latLngPolyline = convertToLatLng(
                                                    ((featureObject.getJSONArray("coordinates")
                                                        .get(m) as JSONArray).get(n) as JSONArray)
                                                )

                                                listPolyArray.add(latLngPolyline!!)
                                                val rluNumber = propertiesObject.get("RLUNo")
                                                a = mMap!!.addMarker(
                                                    MarkerOptions().position(
                                                        LatLng(
                                                            listPolyArray[n].latitude,
                                                            listPolyArray[n].longitude
                                                        )
                                                    )
                                                        .title(rluNumber.toString())
                                                        .snippet(isLicensed.toString())
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                                )!!
                                            }

                                            if (isLicensed == 0) {
                                                polylines = mMap!!.addPolyline(
                                                    (PolylineOptions().addAll(listPolyArray).width(5f).color(
                                                        requireActivity().getResources().getColor(R.color.green)
                                                    ).geodesic(true))
                                                )
                                                polylineList.add(polylines!!)
                                            } else {
                                                polylines = mMap!!.addPolyline(
                                                    (PolylineOptions().addAll(listPolyArray).width(5f).color(
                                                        requireActivity().getResources()
                                                            .getColor(R.color.red_pre_approval)
                                                    ).geodesic(true))
                                                )
                                                polylineList.add(polylines!!)
                                            }
                                            listPolyArray.clear()
                                        }
                                    }

                                }

                                a!!.showInfoWindow()
                            }


                            try {

                                mMap!!.setOnMarkerClickListener { marker ->
                                    var title = marker.title
                                    var snippet = marker.snippet!!.toInt()
                                    if (snippet == 0) {

                                        hitRluMapDetail_API(title.toString())
                                        setObserver()

                                        */
            /*      showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                                              showDialogBox!!.getWindow()!!
                                                  .setBackgroundDrawableResource(android.R.color.transparent)
                                              showDialogBox!!.setCanceledOnTouchOutside(true)
                                              showDialogBox!!.imgCancelProperty.setOnClickListener {
                                                  showDialogBox!!.dismiss()
                                              }
                                              showDialogBox!!.tvHplPermitHead.setText(title)
                                              showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
                                              showDialogBox!!.tvPermitAcres.setText("" + acres)
                                              showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
                                              showDialogBox!!.rlZoomMarker.setOnClickListener {
                                                  val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                                                  if (isClicked) {

                                                      mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
                                                      showDialogBox!!.tvZoomOutIn.setText("Zoom in")
                                                      isClicked = true

                                                  } else {

                                                      mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
                                                      showDialogBox!!.tvZoomOutIn.setText("Zoom Out")
                                                      isClicked = false

                                                  }
                                              }

                                              showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                                  showDialogBox!!.dismiss()
                                                  val fragment = LicenceFragment()
                                                  val bundle = Bundle()
                                                  bundle.putString("publickey", publickey)
                                                  fragment.setArguments(bundle)
                                                  addNewFragment(fragment, R.id.container, true)
                                                  showDialogBox!!.dismiss()
                                              }

                                              showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                                  showDialogBox!!.dismiss()
                                                  addNewFragment(
                                                      DirectiongoogleMapFragment(progressBarPB),
                                                      R.id.container,
                                                      true
                                                  )
                                              }

                                              Glide.with(showDialogBox!!.ivSeachImage)
                                                  .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                                  .fitCenter()
                                                  .placeholder(R.drawable.round_logo)
                                                  .error(R.drawable.round_logo)
                                                  .centerCrop()
                                                  .into(showDialogBox!!.ivSeachImage)

                                              showDialogBox!!.show()
                  *//*


                        } else {
                            PolylineNotAvailable(title)
                        }
                        false
                    }

                } catch (E: Exception) {
                    E.printStackTrace()
                    Log.e("@@@", "ExceptionMarkerclick......." + E)
                }

            } catch (E: Exception) {
                E.printStackTrace()
                Log.e("@@@", "Exception......." + E)
            }
*/

        })


        viewModel.multiPolygonLayerFourSuccess.observe(requireActivity(), Observer { it
            //            var a: Marker? = null
            listMultiArray.clear()
            listPolyArray.clear()
            simplecoordinates.clear()
            val Poly = it.string()
            val jsonObject = JSONObject(Poly)
            val features = jsonObject.getJSONArray("features")
//            multiPolygonFourFeatures = features
// Initialize multiPolygonFourFeatures if null
            if (multiPolygonFeatures == null) {
                multiPolygonFeatures = features
            } else {
                // If already initialized, append new features
                for (i in 0 until features.length()) {
                    multiPolygonFeatures?.put(features.getJSONObject(i))
                }
            }
//            multiPolygonFourFeatures?.put(features)


        })


        /*

               viewModel.onMultiPolygenLayerSuccess.observe(requireActivity(), Observer {
                   it

                   var a: Marker? = null
                   listMultiArray.clear()
                   listPolyArray.clear()
                   simplecoordinates.clear()

       //            multiPolygonFeatures = it as Any
                   try {

                       val Poly = it.string()
                       val `object` = JSONObject(Poly)
                       val features = `object`.getJSONArray("features")
                       for (i in 0 until features.length()) {
                           val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                           val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                           var isLicensed = propertiesObject.get("IsLicensed")

                           when (featureObject.getString("type")) {
                               "MultiPolygon" -> {
                                   for (j in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
                                       for (k in 0 until ((featureObject.getJSONArray("coordinates")
                                           .get(j) as JSONArray).length())) {
                                           for (l in 0 until ((featureObject.getJSONArray("coordinates")
                                               .get(j) as JSONArray).get(k) as JSONArray).length()) {
                                               latLngMultiPolyline = convertToLatLng(
                                                   (((featureObject.getJSONArray("coordinates")
                                                       .get(j) as JSONArray).get(k) as JSONArray).get(l) as JSONArray)
                                               )

                                               listMultiArray.add(latLngMultiPolyline!!)
                                               val rluNumber = propertiesObject.get("RLUNo")
                                               a = mMap!!.addMarker(
                                                   MarkerOptions().position(
                                                       LatLng(
                                                           listMultiArray[l].latitude,
                                                           listMultiArray[l].longitude
                                                       )
                                                   ).title(rluNumber.toString())
                                                       .snippet(isLicensed.toString())
                                                       .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                               )!!
                                           }

                                           if (isLicensed == 0) {
                                               multi_polyline = mMap!!.addPolyline(
                                                   (PolylineOptions().addAll(listMultiArray).width(5f)
                                                       .color(
                                                           requireActivity().getResources()
                                                               .getColor(R.color.green)
                                                       ).geodesic(true))
                                               )
                                               multiPolylineList.add(multi_polyline!!)

                                           } else {
                                               multi_polyline = mMap!!.addPolyline(
                                                   (PolylineOptions().addAll(listMultiArray).width(5f)
                                                       .color(
                                                           requireActivity().getResources()
                                                               .getColor(R.color.red_pre_approval)
                                                       ).geodesic(true))
                                               )
                                               multiPolylineList.add(multi_polyline!!)
                                           }
                                       }
                                       listMultiArray.clear()
                                   }
                               }

                               "Polygon" -> {
                                   for (m in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
                                       for (n in 0 until ((featureObject.getJSONArray("coordinates")
                                           .get(m) as JSONArray).length())) {
                                           latLngPolyline = convertToLatLng(
                                               ((featureObject.getJSONArray("coordinates")
                                                   .get(m) as JSONArray).get(n) as JSONArray)
                                           )

                                           listPolyArray.add(latLngPolyline!!)
                                           val rluNumber = propertiesObject.get("RLUNo")
                                           a = mMap!!.addMarker(
                                               MarkerOptions().position(
                                                   LatLng(
                                                       listPolyArray[n].latitude,
                                                       listPolyArray[n].longitude
                                                   )
                                               )
                                                   .title(rluNumber.toString())
                                                   .snippet(isLicensed.toString())
                                                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                           )!!
                                       }

                                       if (isLicensed == 0) {
                                           polylines = mMap!!.addPolyline(
                                               (PolylineOptions().addAll(listPolyArray).width(5f).color(
                                                   requireActivity().getResources().getColor(R.color.green)
                                               ).geodesic(true))
                                           )
                                           polylineList.add(polylines!!)
                                       } else {
                                           polylines = mMap!!.addPolyline(
                                               (PolylineOptions().addAll(listPolyArray).width(5f).color(
                                                   requireActivity().getResources()
                                                       .getColor(R.color.red_pre_approval)
                                               ).geodesic(true))
                                           )
                                           polylineList.add(polylines!!)
                                       }
                                       listPolyArray.clear()
                                   }
                               }

                           }

                           a!!.showInfoWindow()
                       }


                       try {

                           mMap!!.setOnMarkerClickListener { marker ->
                               var title = marker.title
                               var snippet = marker.snippet!!.toInt()
                               if (snippet == 0) {

                                   hitRluMapDetail_API(title.toString())

                                   showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                                   showDialogBox!!.getWindow()!!
                                       .setBackgroundDrawableResource(android.R.color.transparent)
                                   showDialogBox!!.setCanceledOnTouchOutside(true)
                                   showDialogBox!!.imgCancelProperty.setOnClickListener {
                                       showDialogBox!!.dismiss()
                                   }
                                   showDialogBox!!.tvHplPermitHead.setText(title)
                                   showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
                                   showDialogBox!!.tvPermitAcres.setText("" + acres)
                                   showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
                                   showDialogBox!!.rlZoomMarker.setOnClickListener {
                                       val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                                       if (isClicked) {

                                           mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
                                           showDialogBox!!.tvZoomOutIn.setText("Zoom in")
                                           isClicked = true

                                       } else {

                                           mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
                                           showDialogBox!!.tvZoomOutIn.setText("Zoom Out")
                                           isClicked = false

                                       }
                                   }

                                   showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                       showDialogBox!!.dismiss()
                                       val fragment = LicenceFragment()
                                       val bundle = Bundle()
                                       bundle.putString("publickey", publickey)
                                       fragment.setArguments(bundle)
                                       addNewFragment(fragment, R.id.container, true)
                                       showDialogBox!!.dismiss()
                                   }

                                   showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                       showDialogBox!!.dismiss()
                                       addNewFragment(
                                           DirectiongoogleMapFragment(progressBarPB),
                                           R.id.container,
                                           true
                                       )
                                   }

                                   Glide.with(showDialogBox!!.ivSeachImage)
                                       .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                       .fitCenter()
                                       .placeholder(R.drawable.round_logo)
                                       .error(R.drawable.round_logo)
                                       .centerCrop()
                                       .into(showDialogBox!!.ivSeachImage)

                                   showDialogBox!!.show()

                               } else {
                                   PolylineNotAvailable(title)
                               }
                               false
                           }

                       } catch (E: Exception) {
                           E.printStackTrace()
                           Log.e("@@@", "ExceptionMarkerclick......." + E)
                       }

                   } catch (E: Exception) {
                       E.printStackTrace()
                       Log.e("@@@", "Exception......." + E)
                   }


               })


            */

        /*


                        viewModel.onMultiPolygenLayerSuccess.observe(requireActivity(), Observer {

                            var a: Marker? = null
                            listMultiArray.clear()
                            listPolyArray.clear()
                            simplecoordinates.clear()

                            try {
                                val Poly = it.string();
                                val `object` = JSONObject(Poly)
                                val features = `object`.getJSONArray("features")
                                for (i in 0 until features.length()) {
                                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                                    var isLicensed = propertiesObject.get("IsLicensed")
                                    if (featureObject.getString("type") == "MultiPolygon") {

                                        for (j in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
                                            for (k in 0 until ((featureObject.getJSONArray("coordinates")
                                                .get(j) as JSONArray).length())) {
                                                for (l in 0 until ((featureObject.getJSONArray("coordinates")
                                                    .get(j) as JSONArray).get(k) as JSONArray).length()) {
                                                    latLngMultiPolyline = convertToLatLng(
                                                        (((featureObject.getJSONArray("coordinates")
                                                            .get(j) as JSONArray).get(k) as JSONArray).get(l) as JSONArray)
                                                    )

                                                    listMultiArray.add(latLngMultiPolyline!!)
                                                    val rluNumber = propertiesObject.get("RLUNo")
                                                    a = mMap!!.addMarker(
                                                        MarkerOptions().position(
                                                            LatLng(
                                                                listMultiArray[l].latitude,
                                                                listMultiArray[l].longitude
                                                            )
                                                        ).title(rluNumber.toString())
                                                            .snippet(isLicensed.toString())
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                                    )!!
                                                }

                                                if (isLicensed == 0) {
                                                    multi_polyline = mMap!!.addPolyline(
                                                        (PolylineOptions().addAll(listMultiArray).width(5f).color(
                                                            requireActivity().getResources().getColor(R.color.green)
                                                        ).geodesic(true))
                                                    )
                                                    multiPolylineList.add(multi_polyline!!)

                                                } else {
                                                    multi_polyline = mMap!!.addPolyline(
                                                        (PolylineOptions().addAll(listMultiArray).width(5f).color(
                                                            requireActivity().getResources()
                                                                .getColor(R.color.red_pre_approval)
                                                        ).geodesic(true))
                                                    )
                                                    multiPolylineList.add(multi_polyline!!)
                                                }
                                            }
                                            listMultiArray.clear()
                                        }
                                    } else if (featureObject.getString("type") == "Polygon") {
                                        for (m in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
                                            for (n in 0 until ((featureObject.getJSONArray("coordinates")
                                                .get(m) as JSONArray).length())) {
                                                latLngPolyline = convertToLatLng(
                                                    ((featureObject.getJSONArray("coordinates")
                                                        .get(m) as JSONArray).get(n) as JSONArray)
                                                )

                                                listPolyArray.add(latLngPolyline!!)
                                                val rluNumber = propertiesObject.get("RLUNo")
                                                a = mMap!!.addMarker(
                                                    MarkerOptions().position(
                                                        LatLng(
                                                            listPolyArray[n].latitude,
                                                            listPolyArray[n].longitude
                                                        )
                                                    )
                                                        .title(rluNumber.toString()).snippet(isLicensed.toString())
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                                )!!
                                            }

                                            if (isLicensed == 0) {
                                                polylines = mMap!!.addPolyline(
                                                    (PolylineOptions().addAll(listPolyArray).width(5f).color(
                                                        requireActivity().getResources().getColor(R.color.green)
                                                    ).geodesic(true))
                                                )
                                                polylineList.add(polylines!!)
                                            } else {
                                                polylines = mMap!!.addPolyline(
                                                    (PolylineOptions().addAll(listPolyArray).width(5f).color(
                                                        requireActivity().getResources()
                                                            .getColor(R.color.red_pre_approval)
                                                    ).geodesic(true))
                                                )
                                                polylineList.add(polylines!!)
                                            }
                                            listPolyArray.clear()
                                        }
                                    }
                                    a!!.showInfoWindow()
                                }


                                try {

                                    mMap!!.setOnMarkerClickListener { marker ->
                                        var title = marker.title
                                        var snippet = marker.snippet!!.toInt()
                                        if (snippet == 0) {

                                            hitRluMapDetail_API(title.toString())

                                            showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                                            showDialogBox!!.getWindow()!!
                                                .setBackgroundDrawableResource(android.R.color.transparent)
                                            showDialogBox!!.setCanceledOnTouchOutside(true)
                                            showDialogBox!!.imgCancelProperty.setOnClickListener {
                                                showDialogBox!!.dismiss()
                                            }
                                            showDialogBox!!.tvHplPermitHead.setText(title)
                                            showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
                                            showDialogBox!!.tvPermitAcres.setText("" + acres)
                                            showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
                                            showDialogBox!!.rlZoomMarker.setOnClickListener {
                                                val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                                                if (isClicked) {

                                                    mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
                                                    showDialogBox!!.tvZoomOutIn.setText("Zoom in")
                                                    isClicked = true

                                                } else {

                                                    mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
                                                    showDialogBox!!.tvZoomOutIn.setText("Zoom Out")
                                                    isClicked = false

                                                }
                                            }

                                            showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                                showDialogBox!!.dismiss()
                                                val fragment = LicenceFragment()
                                                val bundle = Bundle()
                                                bundle.putString("publickey", publickey)
                                                fragment.setArguments(bundle)
                                                addNewFragment(fragment, R.id.container, true)
                                                showDialogBox!!.dismiss()
                                            }

                                            showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                                showDialogBox!!.dismiss()
                                                addNewFragment(
                                                    DirectiongoogleMapFragment(progressBarPB),
                                                    R.id.container,
                                                    true
                                                )
                                            }

                                            Glide.with(showDialogBox!!.ivSeachImage)
                                                .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                                .fitCenter()
                                                .placeholder(R.drawable.round_logo)
                                                .error(R.drawable.round_logo)
                                                .centerCrop()
                                                .into(showDialogBox!!.ivSeachImage)

                                            showDialogBox!!.show()

                                        } else {
                                            PolylineNotAvailable(title)
                                        }
                                        false
                                    }


                                } catch (E: Exception) {
                                    E.printStackTrace()
                                    Log.e("@@@", "ExceptionMarkerclick......." + E)
                                }

                            } catch (E: Exception) {
                                E.printStackTrace()
                                Log.e("@@@", "Exception......." + E)
                            }

                        })


        */


        viewModel.postSaveSearchesResponse.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            Log.e("call", "list " + it!!.model.toString())
            if (it.message != "Success") {
//                showShortToast(it.message!!)
            } else {
//                showShortToast(it.message!!)
                showDialog("your search is saved")
            }
        })

        viewModel.getAllAmenitiesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

            Log.e("call", "list " + it!!.model.toString())

            for (i in 0 until it.model.size) {
                if (it.model.get(i).amenityType.equals("Activity")) {
                    amenitylist1.add(it.model.get(i))
                } else if (it.model.get(i).amenityType.equals("Amenity")) {
                    amenitylist2.add(it.model.get(i))
                }
            }

        })


        //   Particluar Api hit
        simplecoordinates.clear()
/*
        viewModel.onParticularPolygenLayerSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")


            showDialogBox!!.dismiss()

            val Particular = it.string();
            val `object` = JSONObject(Particular)
            Log.e("@@@@", "Success" + Particular)
            val features = `object`.getJSONArray("features")
            for (i in 0 until features.length()) {
                val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                var isLicensed = propertiesObject.get("IsLicensed")

                for (m in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
                    for (n in 0 until ((featureObject.getJSONArray("coordinates")
                        .get(m) as JSONArray).length())) {
                        latLngPolyline = convertToLatLng(
                            ((featureObject.getJSONArray("coordinates")
                                .get(m) as JSONArray).get(n) as JSONArray)
                        )
                        listPolyArray.add(latLngPolyline!!)
                        val rluNumber = propertiesObject.get("RLUNo")

                        particluarItem = mMap!!.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    listPolyArray[n].latitude,
                                    listPolyArray[n].longitude
                                )
                            )
                                .title(rluNumber.toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic_nw))
                        )!!

                        markerList.add(particluarItem!!)

                        polylines = mMap!!.addPolyline(
                            (PolylineOptions().addAll(listPolyArray).width(5f)
                                .color(requireActivity().getResources().getColor(R.color.green))
                                .geodesic(true))
                        )
                        mMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                latLngPolyline!!,
                                14f
                            ), null
                        )
                        polylineList.add(polylines!!)

                    }
                    clusterManager!!.getMarkerCollection().showAll()
                    clusterManager!!.getClusterMarkerCollection().showAll()

                }
                listPolyArray.clear()
                particluarItem!!.showInfoWindow()

            }


            mMap!!.setOnMarkerClickListener(clusterManager)


            */
/*
                        mMap!!.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
                            val latLon = marker.title
                            Log.d("@@","titleof marker"+latLon)


                        })*//*



            */
/*   try {

                   mMap!!.setOnMarkerClickListener { marker ->

                       if (marker.title == null) {
                           AlertMapPopUp()
                          return@setOnMarkerClickListener true
                       }

                           var title = marker.title
                           var snippet = marker.snippet!!

                           if (snippet == "0") {

                               hitRluMapDetail_API(title!!)

                               showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                               showDialogBox!!.getWindow()!!
                                   .setBackgroundDrawableResource(android.R.color.transparent)
                               showDialogBox!!.setCanceledOnTouchOutside(true)
                               showDialogBox!!.imgCancelProperty.setOnClickListener {
                                   showDialogBox!!.dismiss()
                               }
                               showDialogBox!!.tvHplPermitHead.setText(title)
                               showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
                               showDialogBox!!.tvPermitAcres.setText("" + acres)
                               showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
                               if (isClicked) {
                                   showDialogBox!!.tvZoomOutIn.text = "Zoom In"
                               } else {
                                   showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                               }

                               showDialogBox!!.rlZoomMarker.setOnClickListener {

                                   val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                                   if (isClicked) {
                                       mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                                       isClicked = false
                                       showDialogBox!!.dismiss()

                                   } else {
                                       mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                                       isClicked = true
                                       Log.e("####", "zoomlevelOut $zoomLevel")
                                       showDialogBox!!.dismiss()
                                   }

                               }

                               showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                   showDialogBox!!.dismiss()
                                   val fragment = LicenceFragment()
                                   val bundle = Bundle()
                                   bundle.putString("publickey", publickey)
                                   fragment.setArguments(bundle)
                                   addNewFragment(fragment, R.id.container, true)
                                   showDialogBox!!.dismiss()
                               }

                               showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                   showDialogBox!!.dismiss()
                                   addNewFragment(
                                       DirectiongoogleMapFragment(progressBarPB),
                                       R.id.container,
                                       true
                                   )
                               }

                               Glide.with(showDialogBox!!.ivSeachImage)
                                   .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                   .fitCenter()
                                   .placeholder(R.drawable.round_logo)
                                   .error(R.drawable.round_logo)
                                   .centerCrop()
                                   .into(showDialogBox!!.ivSeachImage)

                               showDialogBox!!.show()

                           } else {

                               PolylineNotAvailable(title)
                           }
                           true
                       }


               } catch (E: Exception) {

                   E.printStackTrace()
                   Log.e("@@@", "ExceptionMarkerclick......." + E)
               }


   *//*



            //Old Code


            */
/*           mMap!!.setInfoWindowAdapter(object : InfoWindowAdapter {
                           // Use default InfoWindow frame
                           override fun getInfoWindow(arg0: Marker): View? {
                               return null
                           }

                           // Defines the contents of the InfoWindow
                           @SuppressLint("MissingInflatedId")
                           override fun getInfoContents(arg0: Marker): View? {
                               val v: View = layoutInflater.inflate(R.layout.custom_marker, null)

                               //set other fields.....

                               //Snippet:
                               val tSnippet = v.findViewById(R.id.tvCustomMarker) as TextView
                               if (arg0.snippet == "0") {
                                   tSnippet.setTextColor(Color.RED)
                               } else if (arg0.snippet == "1") {
                                   tSnippet.setTextColor(Color.GREEN)
                               }
                               return v
                           }
                       })*//*



            //     mMap!!.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker -> marker.hideInfoWindow() })


            */
/*                     if (it.features.isNullOrEmpty()) {
                                     AlertMapPopUp()
                                 } else {
                                     if (activity != null) {
                                         checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);

                                         for (i in 0 until it.features[0].geometry.coordinates.get(0).size) {

                                             simplecoordinates.add(LatLng(it.features.get(0).geometry.coordinates.get(0).get(i).get(1), it.features.get(0).geometry.coordinates.get(0).get(i).get(0)))
                                         }

                                        val  l = it.features[0].properties.IsLicensed
                                        val  r = it.features[0].properties.RLUNo


                     //                    Log.d("@@@@", "loalicencekey...."+licencekey)
                     //                    Log.d("@@@@", "rluNo...."+rluNo)

                                         onParticularPolyline(l,r)
                                     }
                                 }*//*

        })
*/


        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Api error Failed" + it.toString())
            //    showShortToast(it)
        })
        viewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "loading")
            if (it) {
                progressBarPB.show()

            } else {
                Handler().postDelayed({
                    progressBarPB.dismiss()
                }, 5000)
            }
        })
    }
    var mapId = ""




    var simpleMarker: Marker? = null


    private fun onMapMethod() {
/*
        if (simplecoordinates.isNotEmpty()) {
            for (i in 0 until simplecoordinates.size) {
                particularLiness = mMap!!.addPolyline(
                    PolylineOptions().addAll(simplecoordinates).width(5f)
                        .color(requireActivity().getResources().getColor(R.color.green))
                )
                particularLineList.add(particularLiness!!)
            }
        }

        val simpleMarker = mMap!!.addMarker(
            MarkerOptions().position(
                LatLng(
                    simplecoordinates[1].latitude,
                    simplecoordinates[0].longitude
                )
            ).title(mapId).icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )!!
        simpleMarker.showInfoWindow()

        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(simplecoordinates[0], 12f), null)*/

        /*

        if (simplecoordinates.isNotEmpty()) {
            // Draw polyline for all coordinates at once
            particularLiness = mMap?.addPolyline(
                PolylineOptions().addAll(simplecoordinates)
                    .width(5f)
                    .color(ContextCompat.getColor(requireActivity(), R.color.green))
            )
            particularLineList.add(particularLiness!!)

            // Add a marker at the start of the coordinates
            val initialPosition = simplecoordinates[0]
            val simpleMarker = mMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(initialPosition.latitude, initialPosition.longitude))
                    .title(mapId)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
            )
            simpleMarker?.showInfoWindow()

            // Smoothly animate the camera to the start of the coordinates
//            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 12f))
//            particularLineList.clear() // Clear the list after removing the polylines

            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 12f), object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    // Remove all lines after the camera animation finishes
                    for (polyline in particularLineList) {
                        polyline.remove()
                    }
                    particularLineList.clear() // Clear the list after removing the polylines
                }

                override fun onCancel() {
                    // Handle camera animation cancellation if needed
                }
            })
        }

        */




        // Define the minimum zoom level at which the info window should be shown
        val minZoomLevelToShowInfoWindow = 12f

// Track the marker reference

        if (simplecoordinates.isNotEmpty()) {
            // Draw polyline for all coordinates at once
//            particularLiness = mMap?.addPolyline(
//                PolylineOptions().addAll(simplecoordinates)
//                    .width(5f)
//                    .color(ContextCompat.getColor(requireActivity(), R.color.green))
//            )
//            particularLineList.add(particularLiness!!)

            // Add a marker at the start of the coordinates
            val initialPosition = simplecoordinates[0]
//            simpleMarker = mMap?.addMarker(
//                MarkerOptions()
//                    .position(LatLng(initialPosition.latitude, initialPosition.longitude))
//                    .title(mapId)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
//            )
//            simpleMarker?.showInfoWindow()

            // Smoothly animate the camera to the start of the coordinates
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 12f), object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    // Remove all lines after the camera animation finishes
//                    for (polyline in particularLineList) {
//                        polyline.remove()
//                    }
//                    particularLineList.clear() // Clear the list after removing the polylines
                }

                override fun onCancel() {
                    // Handle camera animation cancellation if needed
                }
            })
        }

// Add an OnCameraIdleListener to handle zoom level changes
//        mMap?.setOnCameraIdleListener {
//            val currentZoomLevel = mMap?.cameraPosition?.zoom ?: minZoomLevelToShowInfoWindow
//
//            // Show or hide the info window based on the zoom level
//            if (currentZoomLevel < minZoomLevelToShowInfoWindow) {
//                simpleMarker?.hideInfoWindow()
//            } else {
//                simpleMarker?.showInfoWindow()
//            }
//        }



        simplecoordinates.clear()

//        setupMarkerClickListener()
        /*
                mMap!!.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker

                    viewModel.rluMapDetails(RluBody(mapId), pref.getString(Constants.TOKEN))
                    showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                    showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
                    showDialogBox!!.setCanceledOnTouchOutside(true)
                    showDialogBox!!.imgCancelProperty.setOnClickListener {
                        showDialogBox!!.dismiss()
                    }
                    showDialogBox!!.tvHplPermitHead.setText(mapId)
                    showDialogBox!!.tvPermitCountry.setText(countryName)
                    showDialogBox!!.tvPermitAcres.setText(acres)
                    showDialogBox!!.tvHplPermitPrice.setText(price)
                    showDialogBox!!.tvZoomOutIn.setText("Zoom in")

                    if (isClicked) {
                        showDialogBox!!.tvZoomOutIn.text = "Zoom In"
                    } else {
                        showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                    }

                    showDialogBox!!.rlZoomMarker.setOnClickListener {

                        if (isClicked) {
                            mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
                            isClicked = false
                            showDialogBox!!.dismiss()

                        } else {
                            mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
                            isClicked = true
                            showDialogBox!!.dismiss()

                        }
                    }

                    showDialogBox!!.rlDetailsMarker.setOnClickListener {
                        showDialogBox!!.dismiss()
                        val fragment = LicenceFragment()
                        val bundle = Bundle()
                        bundle.putString("publickey", key)
                        fragment.setArguments(bundle)
                        addNewFragment(fragment, R.id.container, true)
                    }

                    showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                        showDialogBox!!.dismiss()
                        addNewFragment(DirectiongoogleMapFragment(progressBarPB), R.id.container, true)
                    }

                    Glide.with(showDialogBox!!.ivSeachImage)
                        .load(image)
                        .fitCenter()
                        .placeholder(R.drawable.round_logo)
                        .error(R.drawable.round_logo)
                        .centerCrop()
                        .into(showDialogBox!!.ivSeachImage)

                    showDialogBox!!.show()
                    false

                }
        */

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



/*
    private var polygons: MutableList<Polygon> = mutableListOf() // Store multiple polygons

    // Function to handle the parsed FillMapAreasResponse and add filled polygons accordingly
    private fun fillMapAreas() {
        // Iterate through each feature in the response
        fillMapFeatures.forEach { feature ->
            // Extract coordinates and properties from each feature
            val coordinates = feature.geometry.coordinates
            val rluNo = feature.properties.rLUNo
            val productType = feature.properties.productType

            // Create a list to hold the LatLng points for the polygon
            val polygonPoints = mutableListOf<LatLng>()

            // Iterate through the MultiPolygon coordinates and add LatLng points
            coordinates.forEach { polygonCoords ->
                polygonCoords.forEach { line ->
                    line.forEach { point ->
                        polygonPoints.add(LatLng(point[1], point[0])) // Convert coordinates to LatLng
                    }
                }
            }

            // Create PolygonOptions for each feature
            val polygonOptions = PolygonOptions()
                .addAll(polygonPoints)
                .strokeColor(Color.GREEN) // Stroke color
                .fillColor(Color.BLUE) // Fill color (semi-transparent orange)
                .strokeWidth(5f) // Stroke width

            // Add polygon to the map and store its reference
            val polygon = mMap!!.addPolygon(polygonOptions)
            polygons.add(polygon) // Keep track of all polygons

            // Calculate the centroid to place the marker for RLUNo and ProductType
            val centroid = calculateCentroid(polygonPoints)

//            // Add a marker to show the RLUNo and ProductType
//            val textMarkerOptions = MarkerOptions()
//                .position(centroid)
//                .title(rluNo)
//                .snippet(productType)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) // Marker icon
//
//            mMap!!.addMarker(textMarkerOptions)
        }

        // Set up zoom change listener to show/hide polygons based on zoom level
        mMap!!.setOnCameraIdleListener {
            val zoomLevel = mMap!!.cameraPosition.zoom
            polygons.forEach { polygon ->
                polygon.isVisible = zoomLevel > 9 // Show if zoom is greater than 9, hide otherwise
            }
        }
    }

    // Function to calculate the centroid of a polygon (for placing markers)
    private fun calculateCentroid(points: List<LatLng>): LatLng {
        var latSum = 0.0
        var lngSum = 0.0
        val count = points.size

        points.forEach { point ->
            latSum += point.latitude
            lngSum += point.longitude
        }

        return LatLng(latSum / count, lngSum / count)
    }

*/

    private fun displayVisibleFeatures(features: JSONArray, mMap: GoogleMap) {
        // Get the visible region bounds
        val visibleBounds = mMap.projection.visibleRegion.latLngBounds

        // Clear previous data
        listMultiArray.clear()
        listPolyArray.clear()

        for (i in 0 until features.length()) {
            val featureObject = features.getJSONObject(i).getJSONObject("geometry")
            val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
            val isLicensed = propertiesObject.get("IsLicensed")
            val rluNumber = propertiesObject.get("RLUNo")

            when (featureObject.getString("type")) {
                "MultiPolygon" -> {
                    val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                    for (j in 0 until multiPolygonCoordinates.length()) {
                        val innerPolygon = multiPolygonCoordinates.getJSONArray(j)
                        for (k in 0 until innerPolygon.length()) {
                            val latLngList = innerPolygon.getJSONArray(k)
                            listMultiArray.clear() // Clear the array for each new polygon

                            for (l in 0 until latLngList.length()) {
                                val latLngMultiPolyline =
                                    convertToLatLng(latLngList.getJSONArray(l))

                                // Check if the current LatLng is within the visible region
                                if (visibleBounds.contains(latLngMultiPolyline)) {
                                    listMultiArray.add(latLngMultiPolyline)
                                }
                            }

                            // Only add polyline if we have visible points
                            if (listMultiArray.isNotEmpty()) {
                                val polylineColor = if (isLicensed == 0) {
                                    R.color.green
                                } else {
                                    R.color.red_pre_approval
                                }
                                val multiPolyline = mMap.addPolyline(
                                    PolylineOptions().addAll(listMultiArray)
                                        .width(5f)
                                        .color(requireActivity().resources.getColor(polylineColor))
                                        .geodesic(true)
                                )
                                multiPolylineList.add(multiPolyline!!)
                            }
                        }
                    }
                }

                "Polygon" -> {
                    val polygonCoordinates = featureObject.getJSONArray("coordinates")
                    for (m in 0 until polygonCoordinates.length()) {
                        val latLngList = polygonCoordinates.getJSONArray(m)
                        listPolyArray.clear() // Clear the array for each new polygon

                        for (n in 0 until latLngList.length()) {
                            val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))

                            // Check if the current LatLng is within the visible region
                            if (visibleBounds.contains(latLngPolyline)) {
                                listPolyArray.add(latLngPolyline)
                            }
                        }

                        // Only add polyline if we have visible points
                        if (listPolyArray.isNotEmpty()) {
                            val polylineColor = if (isLicensed == 0) {
                                R.color.green
                            } else {
                                R.color.red_pre_approval
                            }
                            val polyline = mMap.addPolyline(
                                PolylineOptions().addAll(listPolyArray)
                                    .width(5f)
                                    .color(requireActivity().resources.getColor(polylineColor))
                                    .geodesic(true)
                            )
                            polylineList.add(polyline!!)
                        }
                    }
                }
            }
        }
    }

    private suspend fun processMultiPolygonData(data: Any) {
        listMultiArray.clear()
        listPolyArray.clear()
        simplecoordinates.clear()

//        multiPolygonFeatures = data as ArrayList<com.myoutdoor.agent.models.multi_polygons.new_models.Feature>

        try {
            val poly = data.toString()
            val `object` = JSONObject(poly)
            val features = `object`.getJSONArray("features")
            processFeatures(features)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("@@@", "Exception.......$e")
        }
    }

    private fun processFeatures(features: JSONArray) {
        for (i in 0 until features.length()) {
            val featureObject = features.getJSONObject(i).getJSONObject("geometry")
            val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
            var isLicensed = propertiesObject.get("IsLicensed")

            when (featureObject.getString("type")) {
                "MultiPolygon" -> processMultiPolygon(featureObject, propertiesObject, isLicensed)
                "Polygon" -> processPolygon(featureObject, propertiesObject, isLicensed)
            }

            a!!.showInfoWindow()
        }

        setupMarkerClickListener()
    }

    fun processMultiPolygon(
        featureObject: JSONObject,
        propertiesObject: JSONObject,
        isLicensed: Any
    ) {
        for (j in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
            for (k in 0 until ((featureObject.getJSONArray("coordinates")
                .get(j) as JSONArray).length())) {
                for (l in 0 until ((featureObject.getJSONArray("coordinates")
                    .get(j) as JSONArray).get(k) as JSONArray).length()) {
                    latLngMultiPolyline = convertToLatLng(
                        (((featureObject.getJSONArray("coordinates")
                            .get(j) as JSONArray).get(k) as JSONArray).get(l) as JSONArray)
                    )

                    listMultiArray.add(latLngMultiPolyline!!)
                    val rluNumber = propertiesObject.get("RLUNo")
                    a = mMap!!.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                listMultiArray[l].latitude,
                                listMultiArray[l].longitude
                            )
                        ).title(rluNumber.toString())
                            .snippet(isLicensed.toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                    )!!

                    if (isLicensed == 0) {
                        multi_polyline = mMap!!.addPolyline(
                            (PolylineOptions().addAll(listMultiArray).width(5f).color(
                                requireActivity().getResources().getColor(R.color.green)
                            ).geodesic(true))
                        )
                        multiPolylineList.add(multi_polyline!!)
                    } else {
                        multi_polyline = mMap!!.addPolyline(
                            (PolylineOptions().addAll(listMultiArray).width(5f).color(
                                requireActivity().getResources()
                                    .getColor(R.color.red_pre_approval)
                            ).geodesic(true))
                        )
                        multiPolylineList.add(multi_polyline!!)
                    }
                }
                listMultiArray.clear()
            }
        }
    }

    fun processPolygon(featureObject: JSONObject, propertiesObject: JSONObject, isLicensed: Any) {
        for (m in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())) {
            for (n in 0 until ((featureObject.getJSONArray("coordinates")
                .get(m) as JSONArray).length())) {
                latLngPolyline = convertToLatLng(
                    ((featureObject.getJSONArray("coordinates")
                        .get(m) as JSONArray).get(n) as JSONArray)
                )

                listPolyArray.add(latLngPolyline!!)
                val rluNumber = propertiesObject.get("RLUNo")
                a = mMap!!.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            listPolyArray[n].latitude,
                            listPolyArray[n].longitude
                        )
                    )
                        .title(rluNumber.toString()).snippet(isLicensed.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                )

                if (isLicensed == 0) {
                    polylines = mMap!!.addPolyline(
                        (PolylineOptions().addAll(listPolyArray).width(5f).color(
                            requireActivity().getResources().getColor(R.color.green)
                        ).geodesic(true))
                    )
                    polylineList.add(polylines!!)
                } else {
                    polylines = mMap!!.addPolyline(
                        (PolylineOptions().addAll(listPolyArray).width(5f).color(
                            requireActivity().getResources()
                                .getColor(R.color.red_pre_approval)
                        ).geodesic(true))
                    )
                    polylineList.add(polylines!!)
                }
            }
            listPolyArray.clear()
        }
    }

//    fun setupMarkerClickListener() {
//        mMap!!.setOnMarkerClickListener { marker ->
//            var title = marker.title
//            var snippet = marker.snippet!!.toInt()
//            if (snippet == 0) {
//                hitRluMapDetail_API(title.toString())
//
//                showDialogBox(marker.title.toString())
//            } else {
//                PolylineNotAvailable(title)
//            }
//            false
//        }
//    }

    fun showDialogBox(title: String) {
        showDialogBox!!.setContentView(R.layout.map_property_details_popup)
        showDialogBox!!.getWindow()!!
            .setBackgroundDrawableResource(android.R.color.transparent)
//        showDialogBox!!.getWindow()!!.setBackgroundResource(android.R.color.transparent)
        showDialogBox!!.setCanceledOnTouchOutside(true)
        showDialogBox!!.tvHplPermitHead.setText(title)
        showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
        showDialogBox!!.tvPermitAcres.setText("" + acres)
        showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
        showDialogBox!!.rlDirectionsMarker.setOnClickListener {
            showDialogBox!!.dismiss()
            addNewFragment(
                DirectiongoogleMapFragment(progressBarPB),
                R.id.container,
                true
            )
        }


        Glide.with(showDialogBox!!.ivSeachImage)
            .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
            .fitCenter()
            .placeholder(R.drawable.round_logo)
            .error(R.drawable.round_logo)
            .centerCrop()
            .into(showDialogBox!!.ivSeachImage)

        showDialogBox!!.show()
    }


    /*
        fun convertMultiPolygonFeaturesToJSONArray(multiPolygonFeatures: ArrayList<com.myoutdoor.agent.models.multi_polygons.new_models.Feature>): JSONArray {
            val jsonArray = JSONArray()
            for (feature in multiPolygonFeatures) {
                val featureObject = JSONObject()
                featureObject.put("geometry", JSONObject(feature.geometry.toString())) // Assuming geometry is a model
                featureObject.put("properties", JSONObject(feature.properties.toString())) // Assuming properties is a model
                jsonArray.put(featureObject)
            }
            return jsonArray
        }


        fun displaySortedData(referencePoint: LatLng) {
            val sortedFeatures = mutableListOf<Pair<LatLng, JSONObject>>()
            val features = convertMultiPolygonFeaturesToJSONArray(multiPolygonFeatures)
            Log.e("displaySortedData", "displaySortedData: ")
            for (i in 0 until features.length()) {
                try {
                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")

                    // Extract coordinates based on geometry type
                    when (featureObject.getString("type")) {
                        "MultiPolygon" -> {
                            for (j in 0 until featureObject.getJSONArray("coordinates").length()) {
                                for (k in 0 until featureObject.getJSONArray("coordinates").getJSONArray(j).length()) {
                                    for (l in 0 until featureObject.getJSONArray("coordinates").getJSONArray(j).getJSONArray(k).length()) {
                                        val latLng = convertToLatLng(featureObject.getJSONArray("coordinates").getJSONArray(j).getJSONArray(k).getJSONArray(l))

                                        // Check if latLng is not null before proceeding
                                        if (latLng != null && distanceBetween(referencePoint, latLng) <= 100000) { // 100 km
                                            sortedFeatures.add(Pair(latLng, propertiesObject))
                                        }
                                    }
                                }
                            }
                        }
                        "Polygon" -> {
                            for (m in 0 until featureObject.getJSONArray("coordinates").length()) {
                                for (n in 0 until featureObject.getJSONArray("coordinates").getJSONArray(m).length()) {
                                    val latLng = convertToLatLng(featureObject.getJSONArray("coordinates").getJSONArray(m).getJSONArray(n))

                                    // Check if latLng is not null before proceeding
                                    if (latLng != null && distanceBetween(referencePoint, latLng) <= 100000) { // 100 km
                                        sortedFeatures.add(Pair(latLng, propertiesObject))
                                    }
                                }
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.e("JSON Error", "Error parsing JSON at index $i: ${e.message}")
                }
            }

            // Sort features by distance to the reference point
            sortedFeatures.sortBy { distanceBetween(referencePoint, it.first) }

            // Clear existing markers and polylines
            mMap!!.clear()

            // Display sorted features on the map
            for ((latLng, properties) in sortedFeatures) {
                val rluNumber = properties.getString("RLUNo")
                val isLicensed = properties.getInt("IsLicensed")

                // Check if latLng is valid before adding marker
                if (latLng != null) {
                    mMap!!.addMarker(
                        MarkerOptions().position(latLng)
                            .title(rluNumber)
                            .snippet(isLicensed.toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                    )
                } else {
                    Log.e("Map Error", "LatLng is null for RLUNo: $rluNumber")
                }
            }

            try {
                mMap!!.setOnMarkerClickListener { marker ->
                    val title = marker.title
                    val snippet = marker.snippet!!.toInt()
                    if (snippet == 0) {
                        hitRluMapDetail_API(title!!)

                        showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                        showDialogBox!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                        showDialogBox!!.setCanceledOnTouchOutside(true)

                        showDialogBox!!.imgCancelProperty.setOnClickListener { showDialogBox!!.dismiss() }
                        showDialogBox!!.tvHplPermitHead.text = title
                        showDialogBox!!.tvPermitCountry.text = "$countyName, $state"
                        showDialogBox!!.tvPermitAcres.text = "$acres"
                        showDialogBox!!.tvHplPermitPrice.text = "$$licenseFee"

                        showDialogBox!!.rlZoomMarker.setOnClickListener {
                            if (isClicked) {
                                mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                                showDialogBox!!.tvZoomOutIn.text = "Zoom in"
                            } else {
                                mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                                showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                            }
                            isClicked = !isClicked
                        }

                        showDialogBox!!.rlDetailsMarker.setOnClickListener {
                            showDialogBox!!.dismiss()
                            val fragment = LicenceFragment()
                            val bundle = Bundle()
                            bundle.putString("publickey", publickey)
                            fragment.arguments = bundle
                            addNewFragment(fragment, R.id.container, true)
                        }

                        showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                            showDialogBox!!.dismiss()
                            addNewFragment(DirectiongoogleMapFragment(progressBarPB), R.id.container, true)
                        }

                        Glide.with(showDialogBox!!.ivSeachImage)
                            .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                            .fitCenter()
                            .placeholder(R.drawable.round_logo)
                            .error(R.drawable.round_logo)
                            .centerCrop()
                            .into(showDialogBox!!.ivSeachImage)

                        showDialogBox!!.show()
                    } else {
                        PolylineNotAvailable(title)
                    }
                    false // Return false to indicate the event was not consumed
                }
            }
            catch (E: Exception) {
                E.printStackTrace()
                Log.e("@@@", "Exception in Marker Click: ${E.message}")
            }
        }

        // Helper function to calculate distance between two LatLng points
        fun distanceBetween(point1: LatLng, point2: LatLng): Double {
            val results = FloatArray(1)
            Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results)
            return results[0].toDouble() // Distance in meters
        }

    */

    /*



        fun convertMultiPolygonFeaturesToJSONArray(multiPolygonFeatures: ArrayList<com.myoutdoor.agent.models.multi_polygons.new_models.Feature>): JSONArray {
            val jsonArray = JSONArray()
            for (feature in multiPolygonFeatures) {
                jsonArray.put(feature)
            }
            return jsonArray
        }

        fun displaySortedData(referencePoint: LatLng) {
            val sortedFeatures = mutableListOf<Pair<LatLng, JSONObject>>()
            val features = convertMultiPolygonFeaturesToJSONArray(multiPolygonFeatures)

            // Assuming 'features' is already populated with the GeoJSON data
            for (i in 0 until features.length()) {
                try {
                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")

                    // Extract coordinates based on geometry type
                    when (featureObject.getString("type")) {
                        "MultiPolygon" -> {
                            for (j in 0 until featureObject.getJSONArray("coordinates").length()) {
                                for (k in 0 until featureObject.getJSONArray("coordinates").getJSONArray(j).length()) {
                                    for (l in 0 until featureObject.getJSONArray("coordinates").getJSONArray(j).getJSONArray(k).length()) {
                                        val latLng = convertToLatLng(featureObject.getJSONArray("coordinates").getJSONArray(j).getJSONArray(k).getJSONArray(l))

                                        // Check if within 100 km radius
                                        if (distanceBetween(referencePoint, latLng) <= 100000) { // 100,000 meters = 100 km
                                            sortedFeatures.add(Pair(latLng, propertiesObject))
                                        }
                                    }
                                }
                            }
                        }
                        "Polygon" -> {
                            for (m in 0 until featureObject.getJSONArray("coordinates").length()) {
                                for (n in 0 until featureObject.getJSONArray("coordinates").getJSONArray(m).length()) {
                                    val latLng = convertToLatLng(featureObject.getJSONArray("coordinates").getJSONArray(m).getJSONArray(n))

                                    // Check if within 100 km radius
                                    if (distanceBetween(referencePoint, latLng) <= 100000) { // 100,000 meters = 100 km
                                        sortedFeatures.add(Pair(latLng, propertiesObject))
                                    }
                                }
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.e("JSON Error", "Error parsing JSON at index $i: ${e.message}")
                }
            }

            // Sort features by distance to the reference point
            sortedFeatures.sortBy { distanceBetween(referencePoint, it.first) }

            // Clear existing markers and polylines
            mMap!!.clear()

            // Display sorted features on the map
            for ((latLng, properties) in sortedFeatures) {
                val rluNumber = properties.get("RLUNo")
                val isLicensed = properties.get("IsLicensed")

                mMap!!.addMarker(
                    MarkerOptions().position(latLng)
                        .title(rluNumber.toString())
                        .snippet(isLicensed.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                )
            }

            try {
                mMap!!.setOnMarkerClickListener { marker ->
                    val title = marker.title
                    val snippet = marker.snippet!!.toInt()
                    if (snippet == 0) {
                        hitRluMapDetail_API(title.toString())

                        showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                        showDialogBox!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                        showDialogBox!!.setCanceledOnTouchOutside(true)

                        showDialogBox!!.imgCancelProperty.setOnClickListener { showDialogBox!!.dismiss() }
                        showDialogBox!!.tvHplPermitHead.text = title
                        showDialogBox!!.tvPermitCountry.text = "$countyName, $state"
                        showDialogBox!!.tvPermitAcres.text = "$acres"
                        showDialogBox!!.tvHplPermitPrice.text = "$$licenseFee"

                        showDialogBox!!.rlZoomMarker.setOnClickListener {
                            if (isClicked) {
                                mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                                showDialogBox!!.tvZoomOutIn.text = "Zoom in"
                            } else {
                                mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                                showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                            }
                            isClicked = !isClicked
                        }

                        showDialogBox!!.rlDetailsMarker.setOnClickListener {
                            showDialogBox!!.dismiss()
                            val fragment = LicenceFragment()
                            val bundle = Bundle()
                            bundle.putString("publickey", publickey)
                            fragment.arguments = bundle
                            addNewFragment(fragment, R.id.container, true)
                        }

                        showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                            showDialogBox!!.dismiss()
                            addNewFragment(DirectiongoogleMapFragment(progressBarPB), R.id.container, true)
                        }

                        Glide.with(showDialogBox!!.ivSeachImage)
                            .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                            .fitCenter()
                            .placeholder(R.drawable.round_logo)
                            .error(R.drawable.round_logo)
                            .centerCrop()
                            .into(showDialogBox!!.ivSeachImage)

                        showDialogBox!!.show()
                    } else {
                        PolylineNotAvailable(title)
                    }
                    false // Return false to indicate the event was not consumed
                }
            } catch (E: Exception) {
                E.printStackTrace()
                Log.e("@@@", "ExceptionMarkerclick......." + E)
            }
        }

        // Helper function to calculate distance between two LatLng points
        fun distanceBetween(point1: LatLng, point2: LatLng): Double {
            val results = FloatArray(1)
            Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results)
            return results[0].toDouble() // Distance in meters
        }


    */


    /**
     * Adds a marker to the map.
     */
    fun addMapMarker(latLng: LatLng, title: String, snippet: String): Marker? {
        return mMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )
    }


    /**
     * Handles marker click events.
     */
/*
    fun handleMarkerClick(marker: Marker) {
        val title = marker.title
        val snippet = marker.snippet?.toInt() ?: 0
        if (snippet == 0) {
            if (title != null) {
                hitRluMapDetail_API(title)
            }

            showDialogBox?.apply {
                setContentView(R.layout.map_property_details_popup)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                setCanceledOnTouchOutside(true)

                imgCancelProperty.setOnClickListener { dismiss() }
                tvHplPermitHead.text = title
                tvPermitCountry.text = "$countyName, $state"
                tvPermitAcres.text = "$acres"
                tvHplPermitPrice.text = "$$licenseFee"

                rlZoomMarker.setOnClickListener {
                    val zoomLevel = mMap?.cameraPosition?.zoom?.toInt() ?: 0
                    mMap?.animateCamera(CameraUpdateFactory.zoomIn())
                    tvZoomOutIn.text = if (zoomLevel > 0) "Zoom In" else "Zoom Out"
                }

                rlDetailsMarker.setOnClickListener {
                    dismiss()
                    val fragment = LicenceFragment().apply {
                        arguments = Bundle().apply { putString("publickey", publickey) }
                    }
                    addNewFragment(fragment, R.id.container, true)
                }

                rlDirectionsMarker.setOnClickListener {
                    dismiss()
                    addNewFragment(DirectiongoogleMapFragment(progressBarPB), R.id.container, true)
                }

                Glide.with(ivSeachImage)
                    .load("${Constants.PROPERTY_IMAGE_URL}$imageFileName")
                    .fitCenter()
                    .placeholder(R.drawable.round_logo)
                    .error(R.drawable.round_logo)
                    .centerCrop()
                    .into(ivSeachImage)

                show()
            }
        } else {
            PolylineNotAvailable(title)
        }
    }
*/

    private fun PolylineNotAvailable(title: String?) {
        showDialogBox1!!.setContentView(R.layout.property_not_available_popup)
        showDialogBox1!!.setCanceledOnTouchOutside(true)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        showDialogBox1!!.tvPropertyInstruction.setText(title + " Property are currently not available")
        showDialogBox1!!.show()
        showDialogBox1!!.tvPropertyOk.setOnClickListener {
            showDialogBox1!!.dismiss()
        }
    }

    fun convertToLatLng(jsonArray: JSONArray): LatLng {
        val lat = jsonArray.getDouble(1)
        val lng = jsonArray.getDouble(0)
        return LatLng(lat, lng)
    }


    // particulaur Property

    private fun onParticularPolyline(licensec: Int, rlu: String) {


        if (simplecoordinates.isNotEmpty()) {

            for (i in 0 until simplecoordinates.size) {
                particularLiness = mMap!!.addPolyline(
                    (PolylineOptions().addAll(simplecoordinates).width(5f)
                        .color(requireActivity().getResources().getColor(R.color.green))
                        .geodesic(true))
                )

                particularLineList.add(particularLiness!!)

            }


            particluarItem = mMap!!.addMarker(
                MarkerOptions().position(
                    LatLng(
                        simplecoordinates[1].latitude,
                        simplecoordinates[0].longitude
                    )
                )
                    .title(rluNumber)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic_nw))
            )
            particluarItem!!.remove()
            particluarItem!!.showInfoWindow()


            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(simplecoordinates[0], 14f), null)
            simplecoordinates.clear()
            //clusterManager!!.getMarkerCollection().hideAll()
            // clusterManager!!.getClusterMarkerCollection().hideAll()

            Log.e("!!!!!", "licensec...." + licensec)
            Log.e("!!!!!", "rluNo......" + rlu)


            //hitRluMapDetail_API(rluNo)

            mMap!!.setOnMarkerClickListener { marker ->
                Log.e("KulMarkerClick", "marker.title!!" + marker.position)


                Log.e("marker index", "" + marker!!.tag)
                /*
                            var index=Int
                            markerlist.add(marker)*/


                /*   for (i in 0 until markerlist.size) {

                Log.e("!!!","markerlist....."+markerlist.size)
                Log.e("!!!","markerlist....."+markerlist.get(i).title)
                Log.e("!!!","markerlist....."+markerlist.get(i).position) }*/


//            showDialogBox!!.setContentView(R.layout.map_property_details_popup)
//            showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
//            showDialogBox!!.setCanceledOnTouchOutside(true)
//            showDialogBox!!.imgCancelProperty.setOnClickListener {
//                showDialogBox!!.dismiss()
//            }
//            showDialogBox!!.tvHplPermitHead.setText(rlu)
//            showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
//            showDialogBox!!.tvPermitAcres.setText("" + acres)
//            showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
//
//            if (isClicked) {
//                showDialogBox!!.tvZoomOutIn.text = "Zoom In"
//            } else {
//                showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
//            }
//
//            showDialogBox!!.rlZoomMarker.setOnClickListener {
//
//                val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
//                if (isClicked) {
//                    mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
//                    isClicked = false
//                    showDialogBox!!.dismiss()
//
//                } else {
//                    mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
//                    isClicked = true
//                    Log.e("####", "zoomlevelOut $zoomLevel")
//                    showDialogBox!!.dismiss()
//                }
//
//
//            }
//
//            showDialogBox!!.rlDetailsMarker.setOnClickListener {
//                showDialogBox!!.dismiss()
//                val fragment = LicenceFragment()
//                val bundle = Bundle()
//                bundle.putString("publickey", publickey)
//                fragment.setArguments(bundle)
//                addNewFragment(fragment, R.id.container, true)
//            }
//
//            showDialogBox!!.rlDirectionsMarker.setOnClickListener {
//                showDialogBox!!.dismiss()
//                addNewFragment(
//                    DirectiongoogleMapFragment(progressBarPB),
//                    R.id.container,
//                    true
//                )
//            }
//
//            Glide.with(showDialogBox!!.ivSeachImage)
//                .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
//                .fitCenter()
//                .centerCrop()
//                .placeholder(R.drawable.round_logo)
//                .error(R.drawable.round_logo)
//                .centerCrop()
//                .into(showDialogBox!!.ivSeachImage)
//
//            showDialogBox!!.show()

                false

            }
        }
    }


    private fun AlertMapPopUp() {
        showDialogBox1!!.setContentView(R.layout.empty_list_property_alert)
        showDialogBox1!!.setCanceledOnTouchOutside(true)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        showDialogBox1!!.show()
        showDialogBox1!!.tvMapOK.setOnClickListener {
            showDialogBox1!!.dismiss()
        }
    }


    @SuppressLint("PotentialBehaviorOverride", "SuspiciousIndentation")
    private fun setUpClusterer() {
        clusterManager = ClusterManager(requireContext(), mMap)
        mMap?.setOnCameraIdleListener(clusterManager)
        mMap?.setOnMarkerClickListener(clusterManager)
        mMap?.setOnCameraChangeListener(this);
        clusterManager!!.setAnimation(false)
        addItems()
    }

    private fun addItems() {

        for (i in 0..features.size) {
            if (features[i].properties.IsLicensed == 0) {
                latLng = LatLng(
                    features[i].geometry.coordinates.get(1),
                    features[i].geometry.coordinates.get(0)
                )
                offsetItem = MyItem(
                    features[i].geometry.coordinates.get(1),
                    features[i].geometry.coordinates.get(0),
                    "Title $i",
                    "",
                    features[i].properties.IsLicensed.toString(),
                    i
                )
                clusterIcon = requireActivity().getResources().getDrawable(R.drawable.ellips_green)
                //    clusterManager.cluster();
                clusterManager!!.addItem(offsetItem)
                clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "0")
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 0F))

//                if (!isMap){
//                } else{
//                    isMap=false
//                }

            }
            clusterManager!!.setOnClusterItemClickListener {

                for (i in 0 until features.size) {

                    if (features.get(i).geometry.coordinates.get(0) == it!!.latlng.longitude &&
                        features.get(i).geometry.coordinates.get(1) == it.latlng.latitude
                    ) {
                        val coordinates_: ArrayList<Double> = ArrayList<Double>()
                        coordinates_.add(it.latlng.longitude)
                        coordinates_.add(it.latlng.latitude)

                        if (features.get(i).geometry.coordinates == coordinates_) {
                            rluNo = features.get(i).properties.RLUNo
                            isLicensec = features.get(i).properties.IsLicensed
                            hitRluMapDetail_API(rluNo)
                        }
                    }
                }
                true
            }

        }

    }


    private fun PropertyNotAvailable() {
        showDialogBox1!!.setContentView(R.layout.property_not_available_popup)
        showDialogBox1!!.setCanceledOnTouchOutside(true)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        showDialogBox1!!.tvPropertyInstruction.setText(rluNo + " Property are currently not available")
        showDialogBox1!!.show()
        showDialogBox1!!.tvPropertyOk.setOnClickListener {
            showDialogBox1!!.dismiss()
        }
    }


    override fun onClick(index: Int) {

        selectActivitiesAdapter.notifyDataSetChanged()

    }

    override fun onAmenityClick(index: Int) {

        selectAmenitiesAdapter.notifyDataSetChanged()

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (view?.id) {

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    fun searchClickItem(text: String, type: String) {

        commontype = type
        isAdapterClicked = true
        autoTextView.setText(text)
        selectFromList = autoTextView.text.toString()
        rvSearchItems.visibility = View.GONE
        listviewGreenButton()

        if (selectedCounty == null) {
            selectedCounty = ""
        }

        if (statename == null) {
            statename = ""
        }

        selectedCountylist = ArrayList<String>(Arrays.asList(selectedCounty))
        var fcomlist: List<String> = ArrayList<String>(Arrays.asList(selectFromList))
        statnamelist = ArrayList<String>(Arrays.asList(statename))

        if (selectFromList != null) {

            if (commontype == "RLU") {
                priceSort(
                    selectedCountylist, statnamelist, dummylist, dummylist, dummylist,
                    fcomlist, "" + sort, "" + order
                )
            } else if (commontype == "County") {
                priceSort(
                    fcomlist, statnamelist, dummylist, dummylist, dummylist,
                    dummylist, "" + sort, "" + order
                )
            } else if (commontype == "Region") {
                priceSort(
                    selectedCountylist, statnamelist, fcomlist, dummylist, dummylist,
                    dummylist, "" + sort, "" + order
                )
            } else {
                priceSort(
                    selectedCountylist, statnamelist, dummylist, fcomlist, dummylist,
                    dummylist, "" + sort, "" + order
                )
            }
        } else {
            if (selectFromList == null) {
                selectFromList = ""
                normal = autoTextView.text.toString()
//                      freetextcommon = normal
                var freetextlist: List<String> = ArrayList<String>(Arrays.asList(normal))
                normal?.let { commonarray.add(it) }
                priceSort(
                    selectedCountylist,
                    statnamelist,
                    dummylist,
                    dummylist,
                    freetextlist,
                    dummylist,
                    "" + sort,
                    "" + order
                )
            }
        }

        ilMapView.visibility = View.GONE

    }


//    private var markers = mutableListOf<Marker>()
//
//    private fun addMarkersFromGeoJson() {
//        accessPointFeatures.forEach { feature ->
//            val latLng = LatLng(feature.geometry.coordinates[1], feature.geometry.coordinates[0])
//            mMap!!.addMarker(MarkerOptions()
//                .position(latLng)
//                .title("Gate No: ${feature.properties.gateNo}")
//                .snippet("Type: ${feature.properties.gateType}")
//                .icon(getDynamicMarkerIcon(feature.properties.gateType, mMap!!.cameraPosition.zoom))
//                .anchor(0.5f, 0.5f)
//            )?.let { markers.add(it) }
//        }
//
//        mMap!!.setOnCameraIdleListener {
//            val zoomLevel = mMap!!.cameraPosition.zoom
//            markers.forEach { marker ->
//                val shouldBeVisible = zoomLevel >= 10
//                if (marker.isVisible != shouldBeVisible) { // Update visibility only if it changes
//                    marker.isVisible = shouldBeVisible
//                }
//                if (shouldBeVisible) {
//                    marker.setIcon(getDynamicMarkerIcon(marker.snippet!!.split(":")[1].trim(), zoomLevel))
//                }
//            }
//        }
//    }
//
//    private fun getDynamicMarkerIcon(gateType: String, zoomLevel: Float): BitmapDescriptor {
//        val resourceId = if (gateType == "Access") R.drawable.ic_green_record_circle else R.drawable.ic_orange_record_circle
//        val drawable = ContextCompat.getDrawable(requireContext(), resourceId) ?: return BitmapDescriptorFactory.defaultMarker()
//        val scaleFactor = when {
//            zoomLevel >= 15f -> 2.0f
//            zoomLevel >= 12f -> 1.5f
//            else -> 1.0f
//        }
//        return BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(drawableToBitmap(drawable),
//            (drawable.intrinsicWidth * scaleFactor).toInt(), (drawable.intrinsicHeight * scaleFactor).toInt(), false))
//    }
//
//    private fun drawableToBitmap(drawable: Drawable): Bitmap {
//        return Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
//            Canvas(this).also { drawable.setBounds(0, 0, it.width, it.height); drawable.draw(it) }
//        }
//    }



    /*
        private fun addMarkersFromGeoJson() {

            accessPointFeatures.forEach { feature ->
                val coordinates = feature.geometry.coordinates
                val latLng = LatLng(coordinates[1], coordinates[0]) // Lat and Lng

                // Determine marker color based on GateType
                val markerColor = if (feature.properties.gateType == "Access") {
                    BitmapDescriptorFactory.HUE_GREEN
                } else {
                    BitmapDescriptorFactory.HUE_ORANGE
                }

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Gate No: ${feature.properties.gateNo}")
                    .snippet("Type: ${feature.properties.gateType}")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor))

                mMap!!.addMarker(markerOptions)
            }
        }
    */


/// working gates

/*



    private var markers = ArrayList<Marker>()

    private fun addMarkersFromGeoJson() {
        // Add markers to the map based on GeoJSON data
        accessPointFeatures.forEach { feature ->
            val coordinates = feature.geometry.coordinates
            val latLng = LatLng(coordinates[1], coordinates[0]) // Lat and Lng

            // Initialize marker icon size for the current zoom level
            val zoomLevel = mMap!!.cameraPosition.zoom
            val markerIcon = getDynamicMarkerIcon(feature.properties.gateType, zoomLevel)

            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Gate No: ${feature.properties.gateNo}")
//                .snippet("Type: ${feature.properties.gateType}")
                .icon(markerIcon)
                .anchor(0.5f, 0.5f) // Center the marker on the map

            // Add the marker to the map
            val marker = mMap!!.addMarker(markerOptions)
            marker?.let { markers.add(it) }
        }

        // Set listener to adjust marker visibility and size dynamically based on zoom level
        mMap!!.setOnCameraIdleListener {
            val newZoomLevel = mMap!!.cameraPosition.zoom

            markers.forEach { marker ->
                // Find the gate type associated with this marker's position
                val gateType = accessPointFeatures.find { feature ->
                    LatLng(feature.geometry.coordinates[1], feature.geometry.coordinates[0]) == marker.position
                }?.properties?.gateType

                // Hide markers if zoom level is less than 10
                if (newZoomLevel < 10) {
                    marker.isVisible = false

                } else {
                    // Show and update the marker if zoom level is >= 10
                    marker.isVisible = true

                    // Dynamically update marker icon based on new zoom level
                    gateType?.let {
                        val newIcon = getDynamicMarkerIcon(it, newZoomLevel)
                        marker.setIcon(newIcon)
                    }
                }
            }
        }

    }

    // Function to create a dynamic marker icon based on zoom level
    private fun getDynamicMarkerIcon(gateType: String, zoomLevel: Float): BitmapDescriptor {
        // Choose the correct icon based on gate type
        val resourceId = if (gateType == "Access") {
            R.drawable.ic_green_record_circle // Your vector image for Access
        } else {
            R.drawable.ic_orange_record_circle // Your vector image for non-Access
        }

        // Convert drawable to bitmap
        val drawable = ContextCompat.getDrawable(requireContext(), resourceId) ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = drawableToBitmap(drawable)

        // Scale the icon based on the zoom level
        val scaleFactor = getScaleFactorForZoom(zoomLevel)
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * scaleFactor).toInt(),
            (bitmap.height * scaleFactor).toInt(),
            false
        )

        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    // Helper function to convert Drawable to Bitmap
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    // Function to dynamically scale marker icons based on the zoom level
    private fun getScaleFactorForZoom(zoomLevel: Float): Float {
        // Adjust this scale factor logic based on your specific zoom levels and preferences
        return when {
            zoomLevel >= 15f -> 2.0f // Larger icon size for high zoom
            zoomLevel >= 12f -> 1.5f // Medium icon size for mid zoom
            else -> 1.0f // Default icon size for lower zoom levels
        }
    }


*/


/*

    private var markers = ArrayList<Marker>()

    private fun addMarkersFromGeoJson() {
        // Add markers to the map based on GeoJSON data
        accessPointFeatures.forEach { feature ->
            val coordinates = feature.geometry.coordinates
            val latLng = LatLng(coordinates[1], coordinates[0]) // Lat and Lng

            // Initialize marker icon size for the current zoom level
            val zoomLevel = mMap!!.cameraPosition.zoom
            val markerIcon = getDynamicMarkerIcon(feature.properties.gateType, zoomLevel)

            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Gate No: ${feature.properties.gateNo}")
                .icon(markerIcon)
                .anchor(0.5f, 0.5f) // Center the marker on the map

            // Add the marker to the map
            val marker = mMap!!.addMarker(markerOptions)
            marker?.let { markers.add(it) }
        }

        // Set listener to adjust marker visibility and size dynamically based on zoom level and visible area
        mMap!!.setOnCameraIdleListener {
            val newZoomLevel = mMap!!.cameraPosition.zoom

            // Get the current visible bounds of the map
            val visibleBounds = mMap!!.projection.visibleRegion.latLngBounds

            markers.forEach { marker ->
                // Check if the marker is within the visible bounds
                if (visibleBounds.contains(marker.position)) {
                    // Find the gate type associated with this marker's position
                    val gateType = accessPointFeatures.find { feature ->
                        LatLng(feature.geometry.coordinates[1], feature.geometry.coordinates[0]) == marker.position
                    }?.properties?.gateType

                    // Hide markers if zoom level is less than 10
                    if (newZoomLevel < 10) {
                        marker.isVisible = false
                    } else {
                        // Show and update the marker if zoom level is >= 10 and within visible bounds
                        marker.isVisible = true

                        // Dynamically update marker icon based on new zoom level
                        gateType?.let {
                            val newIcon = getDynamicMarkerIcon(it, newZoomLevel)
                            marker.setIcon(newIcon)
                        }
                    }
                } else {
                    // Hide markers outside the visible bounds
                    marker.isVisible = false
                }
            }
        }
    }

    // Function to create a dynamic marker icon based on zoom level
    private fun getDynamicMarkerIcon(gateType: String, zoomLevel: Float): BitmapDescriptor {
        // Choose the correct icon based on gate type
        val resourceId = if (gateType == "Access") {
            R.drawable.ic_green_record_circle // Your vector image for Access
        } else {
            R.drawable.ic_orange_record_circle // Your vector image for non-Access
        }

        // Convert drawable to bitmap
        val drawable = ContextCompat.getDrawable(requireContext(), resourceId) ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = drawableToBitmap(drawable)

        // Scale the icon based on the zoom level
        val scaleFactor = getScaleFactorForZoom(zoomLevel)
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * scaleFactor).toInt(),
            (bitmap.height * scaleFactor).toInt(),
            false
        )

        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    // Helper function to convert Drawable to Bitmap
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    // Function to dynamically scale marker icons based on the zoom level
    private fun getScaleFactorForZoom(zoomLevel: Float): Float {
        // Adjust this scale factor logic based on your specific zoom levels and preferences
        return when {
            zoomLevel >= 15f -> 2.0f // Larger icon size for high zoom
            zoomLevel >= 12f -> 1.5f // Medium icon size for mid zoom
            else -> 1.0f // Default icon size for lower zoom levels
        }
    }



*/


    /*



    private var markers = ArrayList<Marker>()
    private val displayedMarkers = mutableSetOf<Marker>() // Use a set for faster lookups

    // Cache for marker icons
    private val markerIconCache = mutableMapOf<String, BitmapDescriptor>()

    private fun addMarkersFromGeoJson() {
        // Add markers to the map based on GeoJSON data
        accessPointFeatures.forEach { feature ->
            val coordinates = feature.geometry.coordinates
            val latLng = LatLng(coordinates[1], coordinates[0]) // Lat and Lng

            // Create a marker and add it to the markers list
            val marker = createMarker(latLng, feature.properties.gateNo, feature.properties.gateType)
            markers.add(marker)
        }

        // Set listener to adjust marker visibility based on zoom level
        mMap!!.setOnCameraIdleListener {
            updateMarkersVisibility(mMap!!.cameraPosition.zoom)
        }
    }

    // Function to create a marker
    private fun createMarker(latLng: LatLng, gateNo: String, gateType: String): Marker {
        val markerIcon = getMarkerIcon(gateType)

        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Gate No: $gateNo")
            .icon(markerIcon)
            .anchor(0.5f, 0.5f) // Center the marker on the map

        return mMap!!.addMarker(markerOptions)!!
    }

    // Function to get the marker icon based on gate type (with caching)
    private fun getMarkerIcon(gateType: String): BitmapDescriptor {
        // Check if the icon is already cached
        if (markerIconCache.containsKey(gateType)) {
            return markerIconCache[gateType]!!
        }

        // Determine the resource ID based on the gate type
        val resourceId = if (gateType == "Access") {
            R.drawable.ic_green_record_circle // Your vector image for Access
        } else {
            R.drawable.ic_orange_record_circle // Your vector image for non-Access
        }

        // Convert drawable to bitmap
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(resourceId))

        // Cache the bitmap descriptor for future use
        markerIconCache[gateType] = bitmapDescriptor

        return bitmapDescriptor
    }

    // Helper function to get Bitmap from drawable resource
    private fun getBitmapFromDrawable(resourceId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(requireContext(), resourceId)
            ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Fallback to a 1x1 bitmap

        return Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ).also { bitmap ->
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
    }

    // Function to update markers visibility based on zoom level
    private fun updateMarkersVisibility(newZoomLevel: Float) {
        if (newZoomLevel < 10) {
            // Remove markers from the map and clear displayedMarkers set
            displayedMarkers.forEach { marker ->
                marker.remove()
            }
            displayedMarkers.clear()
        } else {
            // Add markers back if zoom level >= 10
            markers.forEach { originalMarker ->
                if (!displayedMarkers.contains(originalMarker)) {
                    displayedMarkers.add(originalMarker) // Track displayed markers

                    // Re-add the existing marker (no new instance)
                    val markerIcon = getMarkerIcon(originalMarker.title?.substringAfter(": ") ?: "")
                    originalMarker.setIcon(markerIcon)
                    originalMarker.isVisible = true // Ensure the marker is visible
                }
            }
        }
    }




*/

    private var markers = ArrayList<Marker>()
    private val displayedMarkers = ArrayList<Marker>()
    private val markerIconCache = mutableMapOf<String, BitmapDescriptor>()

    private fun addMarkersFromGeoJson() {
        accessPointFeatures.forEach { feature ->
            val coordinates = feature.geometry.coordinates
            val latLng = LatLng(coordinates[1], coordinates[0])
            val marker = createMarker(latLng, feature.properties.gateNo, feature.properties.gateType)
            marker.isVisible = false // Ensure markers start as invisible
            markers.add(marker)
        }
        mMap!!.setOnCameraIdleListener {
            updateMarkersVisibility(mMap!!.cameraPosition.zoom)
        }
    }

    private fun createMarker(latLng: LatLng, gateNo: String, gateType: String): Marker {
        val markerIcon = getMarkerIcon(gateType)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Gate No: $gateNo")
            .icon(markerIcon)
            .anchor(0.5f, 0.5f)
        return mMap!!.addMarker(markerOptions)!!
    }

    private fun getMarkerIcon(gateType: String): BitmapDescriptor {
        return markerIconCache[gateType] ?: run {
            val resourceId = if (gateType == "Access") {
                R.drawable.ic_green_record_circle
            } else {
                R.drawable.ic_orange_record_circle
            }
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(resourceId))
            markerIconCache[gateType] = bitmapDescriptor
            bitmapDescriptor
        }
    }

    private fun getBitmapFromDrawable(resourceId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(requireContext(), resourceId)
            ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        return Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ).also { bitmap ->
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
    }

    private fun updateMarkersVisibility(newZoomLevel: Float) {
        if (newZoomLevel < 10) {
            markers.forEach { marker ->
                marker.isVisible = false
            }
            markers.clear()
            Log.e("snckjsbc","sjbcjdbv zoomout ${markers.size}")
            displayedMarkers.clear()
        } else {
            markers.forEach { marker ->
                if (!displayedMarkers.contains(marker)) {
                    marker.isVisible = true
                    displayedMarkers.add(marker)
                } else {
                    marker.isVisible = true
                }
            }
        }
    }



    /*
        private var markers = mutableListOf<Marker>()
        private fun addMarkersFromGeoJson() {
            accessPointFeatures.forEach { feature ->
                val coordinates = feature.geometry.coordinates
                val latLng = LatLng(coordinates[1], coordinates[0]) // Lat and Lng

                // Initialize marker icon size for the current zoom level
                val zoomLevel = mMap!!.cameraPosition.zoom
                val markerIcon = getDynamicMarkerIcon(feature.properties.gateType, zoomLevel)

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Gate No: ${feature.properties.gateNo}")
                    .snippet("Type: ${feature.properties.gateType}")
                    .icon(markerIcon)
                    .anchor(0.5f, 0.5f) // Ensure marker is centered

                val marker = mMap!!.addMarker(markerOptions)
                marker?.let { markers.add(it) }
            }

            // Add zoom change listener to show/hide and adjust marker sizes dynamically
            mMap!!.setOnCameraIdleListener {
                val newZoomLevel = mMap!!.cameraPosition.zoom

                markers.forEach { marker ->
                    val gateType = accessPointFeatures.find { feature ->
                        LatLng(feature.geometry.coordinates[1], feature.geometry.coordinates[0]) == marker.position
                    }?.properties?.gateType

                    // Hide markers if zoom level is less than 10
                    if (newZoomLevel < 10) {
                        marker.isVisible = false
                    } else {
                        marker.isVisible = true

                        // Update marker icon size based on new zoom level if zoom level >= 10
                        gateType?.let {
                            val newIcon = getDynamicMarkerIcon(it, newZoomLevel)
                            marker.setIcon(newIcon)
                        }
                    }
                }
            }
        }

        // Function to create a dynamic marker icon based on zoom level
        private fun getDynamicMarkerIcon(gateType: String, zoomLevel: Float): BitmapDescriptor {
            // Choose the correct icon based on gate type
            val resourceId = if (gateType == "Access") {
                R.drawable.ic_green_record_circle // Your vector image for Access
            } else {
                R.drawable.ic_orange_record_circle // Your vector image for non-Access
            }

            // Create a bitmap from the vector drawable
            val drawable = ContextCompat.getDrawable(requireContext(), resourceId) ?: return BitmapDescriptorFactory.defaultMarker()

            // If it's a vector drawable, use a layer drawable to ensure correct sizing
            val bitmap = drawableToBitmap(drawable)

            // Dynamically scale the icon based on zoom level
            val scaleFactor = getScaleFactorForZoom(zoomLevel)
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * scaleFactor).toInt(),
                (bitmap.height * scaleFactor).toInt(),
                false
            )

            return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
        }

        // Helper function to convert Drawable to Bitmap
        private fun drawableToBitmap(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }


        // Helper function to get scale factor based on zoom level
        private fun getScaleFactorForZoom(zoomLevel: Float): Float {
            return when {
                zoomLevel > 15 -> 1.5f  // Larger icons at higher zoom levels
                zoomLevel > 13 -> 1.3f
                zoomLevel > 10 -> 1.1f
                else -> 0.8f   // Smaller icons at lower zoom levels
            }
        }
    */


/*



    // working on 22 October 2024 12:43 PM

    // Store polygons globally in a list
    private val polygonsList = ArrayList<Polygon>()

    // Function to draw Non-Motorized Polygons on the map in chunks
    fun fillMapAreas() {
        if (fillMapFeatures.isEmpty()) return

        // Set the chunk size, for example, 200 polygons at a time
        val chunkSize = 100

        CoroutineScope(Dispatchers.IO).launch {
            // Split the features into chunks
            val featureChunks = fillMapFeatures.chunked(chunkSize)

            // Process each chunk
            featureChunks.forEach { chunk ->
                // Process chunk in the background thread
                chunk.forEach { feature ->
                    feature.geometry.coordinates.forEach { polygonCoordinates ->
                        val pathPointsList = mutableListOf<List<LatLng>>() // Create list to store multiple parts of polygons

                        polygonCoordinates.forEach { coordinateSet ->
                            val pathPoints = mutableListOf<LatLng>()
                            coordinateSet.forEach { coordinate ->
                                if (coordinate.size >= 2) {
                                    val lng = coordinate[0]
                                    val lat = coordinate[1]
                                    pathPoints.add(LatLng(lat, lng))
                                }
                            }

                            // Store the points for this polygon part
                            if (pathPoints.isNotEmpty()) {
                                pathPointsList.add(pathPoints)
                            }
                        }

                        // Switch to the Main thread to update the UI
                        withContext(Dispatchers.Main) {
                            // Create multiple polygon parts if necessary
                            pathPointsList.forEach { pathPoints ->
                                // Create and configure the PolygonOptions
                                val polygonOptions = PolygonOptions()
                                    .addAll(pathPoints)
                                    .fillColor(Color.parseColor("#e0e09e")) // Fill color (yellow with transparency)
                                    .strokeColor(Color.parseColor("#e0e09e")) // Outline color
                                    .strokeWidth(2f) // Outline width


                                // Add the polygon to the map and store its reference
                                val polygon = mMap!!.addPolygon(polygonOptions)
                                polygon.isVisible = mMap!!.cameraPosition.zoom > 9 // Set visibility based on zoom level
                                polygonsList.add(polygon) // Add polygon to the list
                            }
                        }
                    }
                }
            }
        }

        // Handle zoom changes to hide/show polygons
        mMap!!.setOnCameraIdleListener {
            val zoomLevel = mMap!!.cameraPosition.zoom
            polygonsList.forEach { polygon ->
                polygon.isVisible = zoomLevel > 9
            }
        }
    }






*/



    // Store polygons globally in a list
    private var polygonsList = ArrayList<Polygon>()

    // Function to draw Non-Motorized Polygons on the map in chunks
    fun fillMapAreas(bounds: LatLngBounds) {
        if (fillMapFeatures.isEmpty()) return

        // Set the chunk size, for example, 100 polygons at a time
        val chunkSize = 100

        CoroutineScope(Dispatchers.IO).launch {
            // Split the features into chunks
            val featureChunks = fillMapFeatures.chunked(chunkSize)

            // Process each chunk
            featureChunks.forEach { chunk ->
                // Process chunk in the background thread
                chunk.forEach { feature ->
                    feature.geometry.coordinates.forEach { polygonCoordinates ->
                        val pathPointsList = mutableListOf<List<LatLng>>() // Create list to store multiple parts of polygons

                        polygonCoordinates.forEach { coordinateSet ->
                            val pathPoints = mutableListOf<LatLng>()
                            coordinateSet.forEach { coordinate ->
                                if (coordinate.size >= 2) {
                                    val lng = coordinate[0]
                                    val lat = coordinate[1]
                                    pathPoints.add(LatLng(lat, lng))
                                }
                            }

                            // Store the points for this polygon part
                            if (pathPoints.isNotEmpty()) {
                                pathPointsList.add(pathPoints)
                            }
                        }

                        // Switch to the Main thread to update the UI
                        withContext(Dispatchers.Main) {
                            // Get current map bounds (visible area)
//                            val bounds = mMap!!.projection.visibleRegion.latLngBounds

                            // Check if the polygon is within the bounds
                            pathPointsList.forEach { pathPoints ->
                                val polygonBounds = LatLngBounds.Builder().apply {
                                    pathPoints.forEach { point ->
                                        include(point)
                                    }
                                }.build()

                                if (bounds.intersects(polygonBounds)) {
                                    // Create and configure the PolygonOptions
                                    val polygonOptions = PolygonOptions()
                                        .addAll(pathPoints)
                                        .fillColor(Color.parseColor("#e0e09e")) // Fill color (yellow with transparency)
                                        .strokeColor(Color.parseColor("#e0e09e")) // Outline color
                                        .strokeWidth(2f) // Outline width

                                    // Add the polygon to the map and store its reference
                                    val polygon = mMap!!.addPolygon(polygonOptions)
                                    polygon.isVisible = mMap!!.cameraPosition.zoom > 9 // Set visibility based on zoom level
                                    polygonsList.add(polygon) // Add polygon to the list
                                }
                            }
                        }
                    }
                }
            }
        }

        // Handle zoom and camera changes to hide/show polygons
        mMap!!.setOnCameraIdleListener {
            val zoomLevel = mMap!!.cameraPosition.zoom
            val bounds = mMap!!.projection.visibleRegion.latLngBounds
            polygonsList.forEach { polygon ->
                // Check if the polygon's bounds are within the visible map area
                val polygonPoints = polygon.points
                val polygonBounds = LatLngBounds.Builder().apply {
                    polygonPoints.forEach { point ->
                        include(point)
                    }
                }.build()

                // Update polygon visibility based on zoom and whether it's in the visible area
                polygon.isVisible = zoomLevel > 9 && bounds.intersects(polygonBounds)
            }
        }

    }


//// Store polygons globally in a list
//private val polygonsList = mutableListOf<Polygon>()
//
//    // Function to draw Non-Motorized Polygons on the map
//    fun fillMapAreas() {
//        if (fillMapFeatures.isEmpty()) return
//
//        // Run the processing on a background thread to avoid UI lag
//        CoroutineScope(Dispatchers.IO).launch {
//            fillMapFeatures.forEach { feature ->
//                feature.geometry.coordinates.forEach { polygonCoordinates ->
//                    val pathPoints = mutableListOf<LatLng>()
//
//                    polygonCoordinates.forEach { coordinateSet ->
//                        coordinateSet.forEach { coordinate ->
//                            if (coordinate.size >= 2) {
//                                val lng = coordinate[0]
//                                val lat = coordinate[1]
//                                pathPoints.add(LatLng(lat, lng))
//                            }
//                        }
//                    }
//
//                    // Switch to Main thread to update the map
//                    withContext(Dispatchers.Main) {
//                        // Create and configure the PolygonOptions
//                        val polygonOptions = PolygonOptions()
//                            .addAll(pathPoints)
//                            .fillColor(Color.argb(128, 255, 255, 204)) // Fill color (yellow with transparency)
//                            .strokeColor(Color.GREEN) // Outline color
//                            .strokeWidth(2f) // Outline width
//
//                        // Add the polygon to the map and store its reference
//                        val polygon = mMap!!.addPolygon(polygonOptions)
//                        polygon.isVisible = mMap!!.cameraPosition.zoom > 9 // Set visibility based on initial zoom
//                        polygonsList.add(polygon) // Add polygon to the list
//                    }
//                }
//            }
//        }
//
//        // Manage zoom level for visibility
//        mMap!!.setOnCameraIdleListener {
//            val zoomLevel = mMap!!.cameraPosition.zoom
//            // Iterate through the stored polygons and set visibility
//            polygonsList.forEach { polygon ->
//                polygon.isVisible = zoomLevel > 9
//            }
//        }
//    }
//



/*
    private fun addMarkersFromGeoJson() {
        accessPointFeatures.forEach { feature ->
            val coordinates = feature.geometry.coordinates
            val latLng = LatLng(coordinates[1], coordinates[0]) // Lat and Lng
            // Determine marker icon based on GateType (using custom image)
            val markerIcon = if (feature.properties.gateType == "Access") {
                BitmapDescriptorFactory.fromResource(R.drawable.ic_orange_record_circle) // Replace with your custom image
            } else {
                BitmapDescriptorFactory.fromResource(R.drawable.ic_green_record_circle) // Replace with your custom image
            }

            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Gate No: ${feature.properties.gateNo}")
                .snippet("Type: ${feature.properties.gateType}")
                .icon(markerIcon) // Set custom marker icon

            var marker = mMap!!.addMarker(markerOptions)
            marker?.let { markers.add(it) }

        */
/*   val marker = mMap!!.addMarker(markerOptions)
            marker?.let { markers.add(it) }*//*

        }

        // Add zoom change listener to show/hide markers
        mMap!!.setOnCameraIdleListener {
            val zoomLevel = mMap!!.cameraPosition.zoom
            if (zoomLevel > 10) {
                markers.forEach { it.isVisible = true }
            } else {
                markers.forEach { it.isVisible = false }
            }
        }
    }

*/

    /*


            private var markers = mutableListOf<Marker>()
            private var markerIconCache = mutableMapOf<String, BitmapDescriptor>()

            private fun addMarkersFromGeoJson() {
                val visibleMarkers = mutableListOf<Marker>()

                accessPointFeatures.forEach { feature ->
                    val coordinates = feature.geometry.coordinates
                    val latLng = LatLng(coordinates[1], coordinates[0]) // Lat and Lng

                    // Initialize marker options without the icon for faster processing
                    val markerOptions = MarkerOptions()
                        .position(latLng)
                        .title("Gate No: ${feature.properties.gateNo}")
                        .snippet("Type: ${feature.properties.gateType}")
                        .anchor(0.5f, 0.5f) // Ensure marker is centered

                    val marker = mMap!!.addMarker(markerOptions)
                    marker?.let { markers.add(it) }
                    if (marker != null) {
                        visibleMarkers.add(marker)
                    }
                }

                // Optimize by only updating marker icons when zoom level is sufficient
                mMap!!.setOnCameraIdleListener {
                    val newZoomLevel = mMap!!.cameraPosition.zoom

                    if (newZoomLevel >= 10) {
                        updateVisibleMarkers(newZoomLevel)
                    } else {
                        // Hide markers if zoom level is less than 10
                        markers.forEach { it.isVisible = false }
                    }
                }
            }

            private fun updateVisibleMarkers(zoomLevel: Float) {
                val visibleRegion = mMap!!.projection.visibleRegion.latLngBounds

                // Filter markers within the visible region
                val visibleMarkers = markers.filter { visibleRegion.contains(it.position) }

                visibleMarkers.forEach { marker ->
                    val gateType = accessPointFeatures.find { feature ->
                        LatLng(feature.geometry.coordinates[1], feature.geometry.coordinates[0]) == marker.position
                    }?.properties?.gateType

                    gateType?.let {
                        // Check if cached icon is available, else generate asynchronously
                        val iconKey = "$gateType-$zoomLevel"
                        if (markerIconCache.containsKey(iconKey)) {
                            marker.setIcon(markerIconCache[iconKey])
                        } else {
                            // Load icon asynchronously to avoid blocking the main thread
                            loadMarkerIconAsync(marker, gateType, zoomLevel)
                        }
                    }
                    marker.isVisible = true
                }
            }

            // Load marker icons asynchronously
            private fun loadMarkerIconAsync(marker: Marker, gateType: String, zoomLevel: Float) {
                CoroutineScope(Dispatchers.IO).launch {
                    val markerIcon = getDynamicMarkerIcon(gateType, zoomLevel)

                    withContext(Dispatchers.Main) {
                        marker.setIcon(markerIcon)
                        // Cache the icon for future use
                        markerIconCache["$gateType-$zoomLevel"] = markerIcon
                    }
                }
            }

            // Function to create a dynamic marker icon based on zoom level
            private fun getDynamicMarkerIcon(gateType: String, zoomLevel: Float): BitmapDescriptor {
                // Choose the correct icon based on gate type
                val resourceId = if (gateType == "Access") {
                    R.drawable.green_accesspoint // Replace with your custom image
                } else {
                    R.drawable.red_accesspoint // Replace with your custom image
                }

                // Dynamically scale the icon based on zoom level
                val scaleFactor = getScaleFactorForZoom(zoomLevel)
                val originalBitmap = BitmapFactory.decodeResource(resources, resourceId)
                val scaledBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    (originalBitmap.width * scaleFactor).toInt(),
                    (originalBitmap.height * scaleFactor).toInt(),
                    false
                )

                return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
            }

            // Helper function to get scale factor based on zoom level
            private fun getScaleFactorForZoom(zoomLevel: Float): Float {
                return when {
                    zoomLevel > 15 -> 1.5f  // Larger icons at higher zoom levels
                    zoomLevel > 13 -> 1.3f
                    zoomLevel > 10 -> 1.1f
                    else -> 0.8f   // Smaller icons at lower zoom levels
                }
            }


    */


    fun setDynamicHeightOnSpinner() {
        if (countyNameList.size == 1) {
            psCountySpinner.spinnerPopupHeight =
                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._35sdp)
        } else if (countyNameList.size == 2) {
            psCountySpinner.spinnerPopupHeight =
                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._70sdp)
        } else if (countyNameList.size == 3) {
            psCountySpinner.spinnerPopupHeight =
                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._105sdp)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap!!.getUiSettings().setZoomGesturesEnabled(true);

        /*   mMap!!.setOnCameraMoveStartedListener(OnCameraMoveStartedListener {
              // mCameraMoving = true
               Log.e("testamit","daf")
               parent?.requestDisallowInterceptTouchEvent(false)

           })*/

        imvPlus.setOnClickListener {
            mMap!!.animateCamera(CameraUpdateFactory.zoomIn());

        }
        imvMinus.setOnClickListener {

            mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    override fun onClickListListener(index: Int) {

        val publickey = searchResponseList.get(index).publicKey
        bundle.putString("publickey", publickey)
        Log.e("call", "publickey Active " + publickey)
        val fragment = LicenceFragment()
        val bundle = Bundle()
        bundle.putString("publickey", publickey)
        bundle.putString("productNo", searchResponseList.get(index).productNo.toString())
        bundle.putString("productTypeID", searchResponseList.get(index).productTypeID.toString())
        bundle.putSerializable("mapId", searchResponseList.get(index))
        fragment.setArguments(bundle)
        addNewFragment(fragment, R.id.container, true)
        PreApprovalRequestFragment.isBackPreApproval = false

    }


var isMap:Boolean=false
    override fun onMapClickListener(index: Int) {

        var productNo = searchResponseList.get(index).productNo.toString()
        mapId=productNo
        var productTypeID = searchResponseList.get(index).productTypeID.toString()
        ilMapView.visibility = View.VISIBLE
        gv_SearchListView.visibility = View.GONE
        imvToggle.setImageResource(R.drawable.toggle_list)
        isMapSelected = true
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1)
//        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(2f));
//        isMap=true

        try {
//            fragment.clearMap()
//            replaceFragment(SearchFragment(), R.id.searchContainer, false)
            mMap!!.clear()
            replaceFragment(SearchFragment(), R.id.searchContainer, false)
            setUpClusterer()
        } catch (e: Exception) {
            Log.e("", "Exception" + e)
        }

//                viewModel.pointLayer(
//                    "1=1",
//                    "*",
//                    true,
//                    "geojson",
//                )
//                viewModel.multiPolygenLayer(
//                    "1=1",
//                    "*",
//                    true,
//                    "geojson"
//                )


        CoroutineScope(Dispatchers.Main).launch {

            if (productTypeID == "1") {

                Log.e("particluarPolygenLayer", "type 1 : $productNo" )

                CoroutineScope(Dispatchers.Main).launch {

                    viewModel.particluarPolygenLayer(
                        "RLUNo=" + "'" + productNo + "'",
                        "*",
                        "esriSpatialRelIntersects",
                        "geojson"
                    )
                    onParticularPolygenLayerSuccessObserver()
//                    setObserver()
                }

            }
            else {
                Log.e("particluarfPolygenLayer", "type  : $productNo" )


                CoroutineScope(Dispatchers.Main).launch {

                    viewModel.particularPolygonFourLayer(
                        "RLUNo=" + "'" + productNo + "'",
                        "*",
                        "esriSpatialRelIntersects",
                        "geojson"
                    )
                    particularPolygonFourLayerSuccessObserver()

//                    setObserver()
                }

            }


        }
    }

/*
    override fun onMapClickListener(index: Int) {

        val publickey = searchResponseList.get(index).publicKey
        searchResponseList.get(index).displayName
        searchResponseList.get(index).countyName + " County, " + searchResponseList.get(
            index
        ).state
        searchResponseList.get(index).acres.toString()
        "$" + "," + searchResponseList.get(index).licenseFee.toString()

//        if (searchResponseList.get(index).rluImages != null && searchResponseList.get(index).rluImages.size > 0) {
        if (searchResponseList.get(index).imagefilename != null) {
            Constants.PROPERTY_IMAGE_URL + searchResponseList.get(index).imagefilename
            bundle.putString(
                "image",
                Constants.PROPERTY_IMAGE_URL + searchResponseList.get(index).imagefilename
            )
        }

        ilMapView.visibility = View.VISIBLE
        gv_SearchListView.visibility = View.GONE
        imvToggle.setImageResource(R.drawable.toggle_list)
        // val fragment = MapViewFragment()
        val bundle = Bundle()
        bundle.putSerializable("mapId", searchResponseList.get(index))
        bundle.putString("mapId", searchResponseList.get(index).displayName)
        bundle.putString(
            "conutry",
            searchResponseList.get(index).countyName + " County, " + searchResponseList.get(
                index
            ).state
        )
        bundle.putString("acres", searchResponseList.get(index).acres.toString())
        bundle.putString(
            "price",
            "$" + "" + searchResponseList.get(index).licenseFee.toString()
        )
        bundle.putString("publickey", publickey)
        bundle.putString("productTypeID", searchResponseList.get(index).productTypeID.toString())
        bundle.putString("productNo", searchResponseList.get(index).productNo.toString())
        bundle.putString("RLUNo", searchResponseList.get(index).productTypeID.toString())
        fragment.setArguments(bundle)
        isMapSelected = true
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);
        searchContainer.visibility = View.VISIBLE
//        addNewFragment(fragment, R.id.searchContainer, false)
        showMapViewFragment(fragment)

    }
*/





    private fun hitRluMapDetail_API(rluNo: String) {
//        viewModel.rluMapDetails(requireActivity(),RluBody(rluNo), pref.getString(Constants.TOKEN))
        viewModel.rluMapDetails(RluBody(rluNo), pref.getString(Constants.TOKEN))
        rluObserver()
    }


    override fun onCameraChange(p0: CameraPosition) {
        Log.e("camera moving ", "camera moving...." + p0)

        /*
         // working on 23 october 2024 3:42 PM

        if (p0.zoom >= 3f && p0.zoom < 4f) {


            if (polylineList.isNotEmpty()) {
                Log.e("@@2", "polylinesOuter")
            }


            for (marker in markerList) {
                Log.e("@@2", "markerlinesInner")
                marker.remove()
            }
            for (polyLine in polylineList) {
                Log.e("@@2", "polylinesInner")
                polyLine.remove()
            }

            for (markers in markers) {
                Log.e("@@2", "polylinesInner")
                markers.remove()
            }

            // titleZoomLevel.clear()
            Log.e("2222 ", "333333")

            if (zoomOutFlag == -1) {
                return
            }
            zoomOutFlag = -1


            clusterManager!!.clearItems()
            clearMapData()

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)

                for (i in 0..features.size - 1) {

                    if (features[i].properties.IsLicensed == 0) {
                        latLng = LatLng(
                            features[i].geometry.coordinates.get(1),
                            features[i].geometry.coordinates.get(0)
                        )
                        offsetItem = MyItem(
                            features[i].geometry.coordinates.get(1),
                            features[i].geometry.coordinates.get(0),
                            "Title $i",
                            "",
                            features[i].properties.IsLicensed.toString(),
                            i
                        )
                        clusterIcon =
                            requireActivity().getResources().getDrawable(R.drawable.ellips_green)
                        clusterManager!!.cluster();
                        clusterManager!!.addItem(offsetItem)
                        clusterManager!!.renderer =
                            ClusterRenderer(requireActivity(), mMap, clusterManager, "0")

                    }


                    clusterManager!!.setOnClusterItemClickListener(OnClusterItemClickListener<MyItem?> {

                        for (i in 0 until features.size - 1) {

                            if (features.get(i).geometry.coordinates.get(0) == it.latlng.longitude &&
                                features.get(i).geometry.coordinates.get(1) == it.latlng.latitude
                            ) {
                                val coordinates_: ArrayList<Double> = ArrayList<Double>()
                                coordinates_.add(it.latlng.longitude)
                                coordinates_.add(it.latlng.latitude)

                                if (features.get(i).geometry.coordinates == coordinates_) {
                                    rluNo = features.get(i).properties.RLUNo
                                    isLicensec = features.get(i).properties.IsLicensed
                                    hitRluMapDetail_API(rluNo)
                                }
                            }
                        }
                        true
                    })
                }

            }


            //print(clusterManager.markerCollection.markers[cu])
        }


        */

        if (p0.zoom >= 3f && p0.zoom < 4f) {

            if (polylineList.isNotEmpty()) {
                Log.e("@@2", "polylinesOuter")
            }

            // Use iterators to safely remove markers and polylines
            markerList.forEach { marker -> marker.remove() }
            polylineList.forEach { polyLine -> polyLine.remove() }
            markers.forEach { marker -> marker.remove() }

            Log.e("2222 ", "333333")

            if (zoomOutFlag == -1) return
            zoomOutFlag = -1

            clusterManager!!.clearItems()
            clearMapData()

            // Get the current visible bounds of the map
            val visibleBounds = mMap!!.projection.visibleRegion.latLngBounds

            // Coroutine to add markers based on visible bounds
            CoroutineScope(Dispatchers.Main).launch {
//                for (feature in features) {
//                    // Check if the feature is within the visible bounds
//                    val latLng = LatLng(
//                        feature.geometry.coordinates[1],
//                        feature.geometry.coordinates[0]
//                    )
//
//                    // Only add item if it's within the visible bounds
//                    if (visibleBounds.contains(latLng) && feature.properties.IsLicensed == 0) {
//                        val offsetItem = MyItem(
//                            latLng.latitude,
//                            latLng.longitude,
//                            "Title ${features.indexOf(feature)}",
//                            "",
//                            feature.properties.IsLicensed.toString(),
//                            features.indexOf(feature)
//                        )
//                        clusterManager!!.addItem(offsetItem)
//                    }
//                }
//
//                // Set the renderer for the cluster manager
//                clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "0")
//
//                // Set the cluster item click listener only once after adding all items
//                clusterManager!!.setOnClusterItemClickListener { item ->
//                    features.forEach { feature ->
//                        if (feature.geometry.coordinates[0] == item!!.latlng.longitude &&
//                            feature.geometry.coordinates[1] == item.latlng.latitude) {
//
//                            val coordinates = listOf(item.latlng.longitude, item.latlng.latitude)
//
//                            if (feature.geometry.coordinates == coordinates) {
//                                rluNo = feature.properties.RLUNo
//                                isLicensec = feature.properties.IsLicensed
//                                hitRluMapDetail_API(rluNo)
//                            }
//                        }
//                    }
//                    true
//                }
//
//                // Call cluster once after adding items
//                clusterManager!!.cluster()
//
                for (i in 0..features.size - 1) {

                    if (features[i].properties.IsLicensed == 0) {
                        latLng = LatLng(
                            features[i].geometry.coordinates.get(1),
                            features[i].geometry.coordinates.get(0)
                        )
                        offsetItem = MyItem(
                            features[i].geometry.coordinates.get(1),
                            features[i].geometry.coordinates.get(0),
                            "Title $i",
                            "",
                            features[i].properties.IsLicensed.toString(),
                            i
                        )
                        clusterIcon =
                            requireActivity().getResources().getDrawable(R.drawable.ellips_green)
                        clusterManager!!.cluster();
                        clusterManager!!.addItem(offsetItem)
                        clusterManager!!.renderer =
                            ClusterRenderer(requireActivity(), mMap, clusterManager, "0")

                    }


                    clusterManager!!.setOnClusterItemClickListener(OnClusterItemClickListener<MyItem?> {

                        for (i in 0 until features.size - 1) {

                            if (features.get(i).geometry.coordinates.get(0) == it.latlng.longitude &&
                                features.get(i).geometry.coordinates.get(1) == it.latlng.latitude
                            ) {
                                val coordinates_: ArrayList<Double> = ArrayList<Double>()
                                coordinates_.add(it.latlng.longitude)
                                coordinates_.add(it.latlng.latitude)

                                if (features.get(i).geometry.coordinates == coordinates_) {
                                    rluNo = features.get(i).properties.RLUNo
                                    isLicensec = features.get(i).properties.IsLicensed
                                    hitRluMapDetail_API(rluNo)
                                }
                            }
                        }
                        true
                    })
                }

            }

        }

        print(p0.zoom)

        Log.d("Current Zoom Level", p0.zoom.toString())

        if (p0.zoom>12){
            labelMarkers.forEach { it.isVisible = p0.zoom > 12 }
        }


        if (p0.zoom >= 10f) {
            // Clear all items on zoom level greater than 10
            clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "0")

            clusterManager!!.clearItems()

            // Hide clusters by setting the renderer to null or adjusting visibility
            // clusterManager!!.renderer = null

            // Calculate center latlng of the map
            val centerLatLng: LatLng = p0.target
            Log.e("Center LatLng", "Lat: ${centerLatLng.latitude}, Lng: ${centerLatLng.longitude}")

            // Optionally call your method to display sorted data if needed
            // displaySortedData(centerLatLng)

            val visibleBounds = mMap!!.projection.visibleRegion.latLngBounds
            clearMapData() // Clear any existing map data
            fillMapAreas(visibleBounds) // Fill map areas based on visible bounds

            // Add markers from GeoJSON without clusters
            addMarkersFromGeoJson()

            // Call the method to filter and display visible features within the current bounds
            updateVisibleFeatures(visibleBounds)
//
//            initializeMap()

        }

        /*else{
            Log.e("cnjksdnc","djkcnsjdbv zoom out")
        }*/




        /*



        if (p0.zoom >= 10f) {
            // Clear all items on zoom level greater than 10
            clusterManager!!.clearItems()

            // Calculate center latlng of the map
            val centerLatLng: LatLng = p0.target
            Log.e("Center LatLng", "Lat: ${centerLatLng.latitude}, Lng: ${centerLatLng.longitude}")
//            displaySortedData(centerLatLng)
            val visibleBounds = mMap!!.projection.visibleRegion.latLngBounds
            addMarkersFromGeoJson()
            clearMapData()
            fillMapAreas(visibleBounds)
            // Call the method to filter and display visible features within the current bounds
            updateVisibleFeatures(visibleBounds)
            // Perform operations based on the center point (if needed)
        }



        */

//        if (p0.zoom >= 9f && p0.zoom < 10f) {
//            if (zoomInFlag == 0) {
//                return
//            }
//            zoomOutFlag = 0
//            print("Size---->> ${clusterManager!!.markerCollection.markers.size}")
//
////            progressSearchBar!!.show()
//            zoomInFlag = 0
//
//            clearMapData()
//
//            CoroutineScope(Dispatchers.Main).launch {
////                delay(2000)
//
//                // Prepare items to be added to the cluster manager
//                val newItems = mutableListOf<MyItem>()
//                for (i in 0 until features.size) {
//                    if (features[i].properties.IsLicensed == 1) {
//                        latLng = LatLng(
//                            features[i].geometry.coordinates[1],
//                            features[i].geometry.coordinates[0]
//                        )
//
//                        val offsetItem = MyItem(
//                            features[i].geometry.coordinates[1],
//                            features[i].geometry.coordinates[0],
//                            "Title $i",
//                            "",
//                            features[i].properties.IsLicensed.toString(),
//                            i
//                        )
//
//                        newItems.add(offsetItem)
//                    }
//                }
//
//                // Add all items at once and cluster them
//                clusterManager!!.addItems(newItems as Collection<MyItem?>?)
//                clusterManager!!.cluster()
//
//                clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "1")
//
//                // Set click listener for cluster items
//                clusterManager!!.setOnClusterItemClickListener { clickedItem ->
//                    val clickedCoordinates = listOf(clickedItem?.latlng!!.longitude, clickedItem.latlng.latitude)
//                    for (i in 0 until features.size) {
//                        if (features[i].geometry.coordinates == clickedCoordinates) {
//                            rluNo = features[i].properties.RLUNo
//                            isLicensec = features[i].properties.IsLicensed
//                            hitRluMapDetail_API(rluNo)
//                            break
//                        }
//                    }
//                    true
//                }
//
////                progressSearchBar!!.dismiss()
//                zoomInFlag = -1
//            }
//        }

// Hide clusters when zoom is greater than 9
//        if (p0.zoom >= 10f) {
//            clusterManager!!.clearItems()  // Clear existing cluster items
//            mMap.clear()  // Optionally clear all map markers (depends on your need)
//        }



        if (p0.zoom >= 9f && p0.zoom < 10f) {
            if (zoomInFlag == 0) return

            zoomOutFlag = 0
            print("Size---->> ${clusterManager!!.markerCollection.markers.size}")
            progressSearchBar!!.show()
            zoomInFlag = 0

            clearMapData()

            // Get the current visible bounds of the map
            val visibleBounds = mMap!!.projection.visibleRegion.latLngBounds

            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)



                // Create a list of items to be added
                val itemsToAdd = mutableListOf<MyItem>()

                features.forEachIndexed { index, feature ->
                    if (feature.properties.IsLicensed == 1) {
                        val latLng = LatLng(feature.geometry.coordinates[1], feature.geometry.coordinates[0])

                        // Check if the feature is within the visible bounds
                        if (visibleBounds.contains(latLng)) {
                            val offsetItem = MyItem(
                                latLng.latitude,
                                latLng.longitude,
                                "Title $index",
                                "",
                                feature.properties.IsLicensed.toString(),
                                index
                            )
                            itemsToAdd.add(offsetItem)
                        }
                    }
                }

                // Clear existing items to prevent over-creating clusters
                clusterManager!!.clearItems()

                // Add items to cluster manager
                itemsToAdd.forEach { item -> clusterManager!!.addItem(item) }

                // Set the cluster renderer if not already set
                if (clusterManager!!.renderer == null) {
                    clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "1")
                }

                // Set the cluster item click listener only once
                clusterManager!!.setOnClusterItemClickListener { item ->
                    features.forEachIndexed { i, feature ->
                        if (feature.geometry.coordinates[0] == item!!.latlng.longitude &&
                            feature.geometry.coordinates[1] == item.latlng.latitude) {

                            val coordinates = listOf(item.latlng.longitude, item.latlng.latitude)

                            if (feature.geometry.coordinates == coordinates) {
                                rluNo = feature.properties.RLUNo
                                isLicensec = feature.properties.IsLicensed
                                hitRluMapDetail_API(rluNo)
                            }
                        }
                    }
                    true
                }

                // Trigger clustering after adding items
                clusterManager!!.cluster()

                progressSearchBar!!.dismiss()
                zoomInFlag = -1






            }

//            CoroutineScope(Dispatchers.Main).launch {
////                delay(2000)
//
//                // Prepare items to be added to the cluster manager
//                val newItems = mutableListOf<MyItem>()
//                for (i in 0 until features.size) {
//                    if (features[i].properties.IsLicensed == 1) {
//                        latLng = LatLng(
//                            features[i].geometry.coordinates[1],
//                            features[i].geometry.coordinates[0]
//                        )
//
//                        val offsetItem = MyItem(
//                            features[i].geometry.coordinates[1],
//                            features[i].geometry.coordinates[0],
//                            "Title $i",
//                            "",
//                            features[i].properties.IsLicensed.toString(),
//                            i
//                        )
//
//                        newItems.add(offsetItem)
//                    }
//                }
//
//                // Add all items at once and cluster them
//                clusterManager!!.addItems(newItems as Collection<MyItem?>?)
//                clusterManager!!.cluster()
//
//                clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "1")
//
//                // Set click listener for cluster items
//                clusterManager!!.setOnClusterItemClickListener { clickedItem ->
//                    val clickedCoordinates = listOf(clickedItem?.latlng!!.longitude, clickedItem.latlng.latitude)
//                    for (i in 0 until features.size) {
//                        if (features[i].geometry.coordinates == clickedCoordinates) {
//                            rluNo = features[i].properties.RLUNo
//                            isLicensec = features[i].properties.IsLicensed
//                            hitRluMapDetail_API(rluNo)
//                            break
//                        }
//                    }
//                    true
//                }
//
////                progressSearchBar!!.dismiss()
//                zoomInFlag = -1
//            }


        }

        /*


        // working on 23 october 2024 3:38 PM
        if (p0.zoom >= 9f && p0.zoom < 10f) {
            if (zoomInFlag == 0) {
                return
            }
            zoomOutFlag = 0
            print("Size---->> ${clusterManager!!.markerCollection.markers.size}")
            // if (!pref.getBoolean("Already")) {
            progressSearchBar!!.show()

            zoomInFlag = 0

            clearMapData()

            *//*
                        Handler(Looper.getMainLooper()).postDelayed({
                            //  clusterManager!!.clearItems()
                            for (i in 0..features.size - 1) {

                                if (features[i].properties.IsLicensed == 1) {
                                    latLng = LatLng(
                                        features[i].geometry.coordinates.get(1),
                                        features[i].geometry.coordinates.get(0)
                                    )
                                    offsetItem = MyItem(
                                        features[i].geometry.coordinates.get(1),
                                        features[i].geometry.coordinates.get(0),
                                        "Title $i",
                                        "",
                                        features[i].properties.IsLicensed.toString(),
                                        i
                                    )
                                    clusterIcon = requireActivity().getResources()
                                        .getDrawable(R.drawable.ellips_green)
                                    clusterManager!!.cluster();
                                    clusterManager!!.addItem(offsetItem)
                                    clusterManager!!.renderer =
                                        ClusterRenderer(requireActivity(), mMap, clusterManager, "1")

                                }
                                clusterManager!!.setOnClusterItemClickListener {
                                    for (i in 0 until features.size - 1) {
                                        if (features[i].geometry.coordinates[0] == it?.latlng!!.longitude &&
                                            features[i].geometry.coordinates[1] == it.latlng.latitude
                                        ) {
                                            val coordinates_: ArrayList<Double> = ArrayList<Double>()
                                            coordinates_.add(it.latlng.longitude)
                                            coordinates_.add(it.latlng.latitude)

                                            if (features.get(i).geometry.coordinates == coordinates_) {
                                                rluNo = features[i].properties.RLUNo
                                                isLicensec = features[i].properties.IsLicensed
                                                hitRluMapDetail_API(rluNo)
                                            }
                                        }
                                    }
                                    true
                                }

                                if (i == features.size - 1) {
                                    progressSearchBar!!.dismiss()
                                }
                            }
                            zoomInFlag = -1
                            // Code to show the recyclerview here
                        }, 2000)



            *//*


            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                for (i in 0..features.size - 1) {

                    if (features[i].properties.IsLicensed == 1) {
                        latLng = LatLng(
                            features[i].geometry.coordinates.get(1),
                            features[i].geometry.coordinates.get(0)
                        )
                        offsetItem = MyItem(
                            features[i].geometry.coordinates.get(1),
                            features[i].geometry.coordinates.get(0),
                            "Title $i",
                            "",
                            features[i].properties.IsLicensed.toString(),
                            i
                        )
                        clusterIcon = requireActivity().getResources()
                            .getDrawable(R.drawable.ellips_green)
                        clusterManager!!.cluster();
                        clusterManager!!.addItem(offsetItem)
                        clusterManager!!.renderer =
                            ClusterRenderer(requireActivity(), mMap, clusterManager, "1")

                    }
                    clusterManager!!.setOnClusterItemClickListener {
                        for (i in 0 until features.size - 1) {
                            if (features[i].geometry.coordinates[0] == it?.latlng!!.longitude &&
                                features[i].geometry.coordinates[1] == it.latlng.latitude
                            ) {
                                val coordinates_: ArrayList<Double> = ArrayList<Double>()
                                coordinates_.add(it.latlng.longitude)
                                coordinates_.add(it.latlng.latitude)

                                if (features.get(i).geometry.coordinates == coordinates_) {
                                    rluNo = features[i].properties.RLUNo
                                    isLicensec = features[i].properties.IsLicensed
                                    hitRluMapDetail_API(rluNo)
                                }
                            }
                        }
                        true
                    }

                    if (i == features.size - 1) {
                        progressSearchBar!!.dismiss()
                    }
                }

                zoomInFlag = -1

            }


            // pref.setBoolean("Already", true)
        }



*/
        //  }
    }

    // Method to clear all polylines from the map
    private fun removeAllPolylines() {
        for (polyline in polylineList) {
            polyline.remove() // Removes polyline from the map
        }
        polylineList.clear() // Clear the list
    }

    /*
        // This method updates the visible features based on the bounds and zoom level
        @SuppressLint("PotentialBehaviorOverride")
        private fun updateVisibleFeatures(visibleBounds: LatLngBounds, zoomLevel: Float) {
            // You should have the 'features' JSONArray available, possibly from a ViewModel
            val features = multiPolygonFeatures!! // This should be the JSONArray of features

            // Same logic as before, but now inside this method
            listMultiArray.clear()
            listPolyArray.clear()
            try {
                for (i in 0 until features.length()) {
                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                    val isLicensed = propertiesObject.get("IsLicensed")
                    val rluNumber = propertiesObject.get("RLUNo")

                    when (featureObject.getString("type")) {
                        "MultiPolygon" -> {
                            val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (j in 0 until multiPolygonCoordinates.length()) {
                                val innerPolygon = multiPolygonCoordinates.getJSONArray(j)
                                for (k in 0 until innerPolygon.length()) {
                                    val latLngList = innerPolygon.getJSONArray(k)
                                    listMultiArray.clear() // Clear the array for each new polygon

                                    for (l in 0 until latLngList.length()) {
                                        val latLngMultiPolyline = convertToLatLng(latLngList.getJSONArray(l))

                                        // Check if the current LatLng is within the visible region
                                        if (visibleBounds.contains(latLngMultiPolyline)) {
                                            listMultiArray.add(latLngMultiPolyline)
                                        }
                                    }

                                    // Only add polyline if we have visible points
                                    if (listMultiArray.isNotEmpty()) {
                                        val polylineColor = if (isLicensed == 0) {
                                            R.color.green
                                        } else {
                                            R.color.red_pre_approval
                                        }
                                        val multiPolyline = mMap!!.addPolyline(
                                            PolylineOptions().addAll(listMultiArray)
                                                .width(5f)
                                                .color(requireActivity().resources.getColor(polylineColor))
                                                .geodesic(true)
                                        )
                                        multiPolylineList.add(multiPolyline)
                                    }
                                }
                            }
                        }

                        "Polygon" -> {
                            val polygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (m in 0 until polygonCoordinates.length()) {
                                val latLngList = polygonCoordinates.getJSONArray(m)
                                listPolyArray.clear() // Clear the array for each new polygon

                                for (n in 0 until latLngList.length()) {
                                    val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))

                                    // Check if the current LatLng is within the visible region
                                    if (visibleBounds.contains(latLngPolyline)) {
                                        listPolyArray.add(latLngPolyline)
                                    }
                                }

                                // Only add polyline if we have visible points
                                if (listPolyArray.isNotEmpty()) {
                                    val polylineColor = if (isLicensed == 0) {
                                        R.color.green
                                    } else {
                                        R.color.red_pre_approval
                                    }
                                    val polyline = mMap!!.addPolyline(
                                        PolylineOptions().addAll(listPolyArray)
                                            .width(5f)
                                            .color(requireActivity().resources.getColor(polylineColor))
                                            .geodesic(true)
                                    )
                                    polylineList.add(polyline)

                                }
                            }
                        }
                    }
                }

                try {

                    mMap!!.setOnMarkerClickListener { marker ->
                        var title = marker.title
                        var snippet = marker.snippet!!.toInt()
                        if (snippet == 0) {

                            hitRluMapDetail_API(title.toString())

                            showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                            showDialogBox!!.getWindow()!!
                                .setBackgroundDrawableResource(android.R.color.transparent)
                            showDialogBox!!.setCanceledOnTouchOutside(true)
                            showDialogBox!!.imgCancelProperty.setOnClickListener {
                                showDialogBox!!.dismiss()
                            }
                            showDialogBox!!.tvHplPermitHead.setText(title)
                            showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
                            showDialogBox!!.tvPermitAcres.setText("" + acres)
                            showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
                            showDialogBox!!.rlZoomMarker.setOnClickListener {
                                val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                                if (isClicked) {

                                    mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
                                    showDialogBox!!.tvZoomOutIn.setText("Zoom in")
                                    isClicked = true

                                } else {

                                    mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
                                    showDialogBox!!.tvZoomOutIn.setText("Zoom Out")
                                    isClicked = false

                                }
                            }

                            showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                showDialogBox!!.dismiss()
                                val fragment = LicenceFragment()
                                val bundle = Bundle()
                                bundle.putString("publickey", publickey)
                                fragment.setArguments(bundle)
                                addNewFragment(fragment, R.id.container, true)
                                showDialogBox!!.dismiss()
                            }

                            showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                showDialogBox!!.dismiss()
                                addNewFragment(
                                    DirectiongoogleMapFragment(progressBarPB),
                                    R.id.container,
                                    true
                                )
                            }

                            Glide.with(showDialogBox!!.ivSeachImage)
                                .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                .fitCenter()
                                .placeholder(R.drawable.round_logo)
                                .error(R.drawable.round_logo)
                                .centerCrop()
                                .into(showDialogBox!!.ivSeachImage)

                            showDialogBox!!.show()

                        } else {
                            PolylineNotAvailable(title)
                        }
                        false
                    }

                } catch (E: Exception) {
                    E.printStackTrace()
                    Log.e("@@@", "ExceptionMarkerclick......." + E)
                }
            } catch  (E: Exception) {
                E.printStackTrace()
                Log.e("@@@", "Exception......." + E)
            }

        }

    */


    // This method clears the map's existing data
    private fun clearMapData() {
        // Clear all polylines and markers
//        mMap!!.clear()

        // Remove all polylines from the map
        for (polyline in polylineList) {
            polyline.remove()
        }
        for (multiPolyline in multiPolylineList) {
            multiPolyline.remove()
        }
        polylineList.clear()
        multiPolylineList.clear()
//        drawnPolylines.clear()
        // Optionally, clear markers if you don't want to show them at lower zoom levels
        Log.e("sncjdncd","skncks clear marker")
        markerList.clear()
       // markers.clear()

    }

    /*


        // This method updates the visible features based on the bounds and zoom level
        private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
            // You should have the 'features' JSONArray available, possibly from a ViewModel

                    // Perform network operation or database task
                    val features = multiPolygonFeatures!! // This should be the JSONArray of features

                    // Same logic as before, but now inside this method
                    listMultiArray.clear()
                    listPolyArray.clear()

                    try {
                        for (i in 0 until features.length()) {
                            val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                            val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                            val isLicensed = propertiesObject.get("IsLicensed")
                            val rluNumber = propertiesObject.get("RLUNo")

                            when (featureObject.getString("type")) {
                                "MultiPolygon" -> {
                                    val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                                    for (j in 0 until multiPolygonCoordinates.length()) {
                                        val innerPolygon = multiPolygonCoordinates.getJSONArray(j)
                                        for (k in 0 until innerPolygon.length()) {
                                            val latLngList = innerPolygon.getJSONArray(k)
                                            listMultiArray.clear() // Clear the array for each new polygon

                                            for (l in 0 until latLngList.length()) {
                                                val latLngMultiPolyline = convertToLatLng(latLngList.getJSONArray(l))

                                                // Check if the current LatLng is within the visible region
                                                if (visibleBounds.contains(latLngMultiPolyline)) {
                                                    listMultiArray.add(latLngMultiPolyline)
                                                }
                                            }

                                            // Only add polyline if we have visible points
                                            if (listMultiArray.isNotEmpty()) {
                                                val polylineColor = if (isLicensed == 0) {
                                                    R.color.green
                                                } else {
                                                    R.color.red_pre_approval
                                                }
                                                val multiPolyline = mMap!!.addPolyline(
                                                    PolylineOptions().addAll(listMultiArray)
                                                        .width(5f)
                                                        .color(requireActivity().resources.getColor(polylineColor))
                                                        .geodesic(true)
                                                )
                                                multiPolylineList.add(multiPolyline)
                                            }
                                        }
                                    }
                                }

                                "Polygon" -> {
                                    val polygonCoordinates = featureObject.getJSONArray("coordinates")
                                    for (m in 0 until polygonCoordinates.length()) {
                                        val latLngList = polygonCoordinates.getJSONArray(m)
                                        listPolyArray.clear() // Clear the array for each new polygon

                                        for (n in 0 until latLngList.length()) {
                                            val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))

                                            // Check if the current LatLng is within the visible region
                                            if (visibleBounds.contains(latLngPolyline)) {
                                                listPolyArray.add(latLngPolyline)
                                            }
                                        }

                                        // Only add polyline if we have visible points
                                        if (listPolyArray.isNotEmpty()) {
                                            val polylineColor = if (isLicensed == 0) {
                                                R.color.green
                                            } else {
                                                R.color.red_pre_approval
                                            }
                                            val polyline = mMap!!.addPolyline(
                                                PolylineOptions().addAll(listPolyArray)
                                                    .width(5f)
                                                    .color(requireActivity().resources.getColor(polylineColor))
                                                    .geodesic(true)
                                            )
                                            polylineList.add(polyline)
                                        }
                                    }
                                }
                            }
                        }



                    } catch (E: Exception) {
                        E.printStackTrace()
                        Log.e("@@@", "Exception......." + E)
                    }
                    try {
                        // Ensure markers are already added to the map before setting the listener
                        mMap!!.setOnMarkerClickListener { marker ->
                            val title = marker.title
                            var snippet = marker.snippet!!.toInt()

    //                        val snippet = marker.snippet?.toIntOrNull() ?: -1  // Ensure snippet is an integer or handle invalid values

                            if (snippet == 0) {
                                // Call the API to fetch additional data about the selected marker
                                hitRluMapDetail_API(title!!)

                                // Set up the dialog with the property details
                                showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                                showDialogBox!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                showDialogBox!!.setCanceledOnTouchOutside(true)
                                showDialogBox!!.imgCancelProperty.setOnClickListener {
                                    showDialogBox!!.dismiss()
                                }

                                // Update the dialog UI elements with property details
                                showDialogBox!!.tvHplPermitHead.text = title
                                showDialogBox!!.tvPermitCountry.text = "$countyName, $state"
                                showDialogBox!!.tvPermitAcres.text = acres.toString()
                                showDialogBox!!.tvHplPermitPrice.text = "$$licenseFee"

                                // Handle zoom in/out button functionality
                                showDialogBox!!.rlZoomMarker.setOnClickListener {
                                    if (isClicked) {
                                        mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                                        showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                                        isClicked = false
                                    } else {
                                        mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                                        showDialogBox!!.tvZoomOutIn.text = "Zoom In"
                                        isClicked = true
                                    }
                                }

                                // Open details fragment
                                showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                    showDialogBox!!.dismiss()
                                    val fragment = LicenceFragment()
                                    val bundle = Bundle()
                                    bundle.putString("publickey", publickey)
                                    fragment.arguments = bundle
                                    addNewFragment(fragment, R.id.container, true)
                                }

                                // Open directions fragment
                                showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                    showDialogBox!!.dismiss()
                                    addNewFragment(DirectiongoogleMapFragment(progressBarPB), R.id.container, true)
                                }

                                // Load the property image
                                Glide.with(showDialogBox!!.ivSeachImage)
                                    .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                    .fitCenter()
                                    .placeholder(R.drawable.round_logo)
                                    .error(R.drawable.round_logo)
                                    .centerCrop()
                                    .into(showDialogBox!!.ivSeachImage)

                                showDialogBox!!.show()

                            } else {
                                // Handle case when polyline is not available
                                PolylineNotAvailable(title)
                            }
                            // Return true to indicate that we have handled the click event
                            true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("@@@", "Exception in Marker click listener: $e")
                    }

                // Update UI with result on the main thread

    */
    /*

            try {

                mMap!!.setOnMarkerClickListener { marker ->
                    var title = marker.title
                    var snippet = marker.snippet!!.toInt()
                    if (snippet == 0) {

                        hitRluMapDetail_API(title.toString())

                        showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                        showDialogBox!!.getWindow()!!
                            .setBackgroundDrawableResource(android.R.color.transparent)
                        showDialogBox!!.setCanceledOnTouchOutside(true)
                        showDialogBox!!.imgCancelProperty.setOnClickListener {
                            showDialogBox!!.dismiss()
                        }
                        showDialogBox!!.tvHplPermitHead.setText(title)
                        showDialogBox!!.tvPermitCountry.setText(countyName + "," + state)
                        showDialogBox!!.tvPermitAcres.setText("" + acres)
                        showDialogBox!!.tvHplPermitPrice.setText("$" + "" + licenseFee)
                        showDialogBox!!.rlZoomMarker.setOnClickListener {
                            val zoomLevel = mMap!!.cameraPosition.zoom.toInt()
                            if (isClicked) {

                                mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
                                showDialogBox!!.tvZoomOutIn.setText("Zoom in")
                                isClicked = true

                            } else {

                                mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
                                showDialogBox!!.tvZoomOutIn.setText("Zoom Out")
                                isClicked = false

                            }
                        }

                        showDialogBox!!.rlDetailsMarker.setOnClickListener {
                            showDialogBox!!.dismiss()
                            val fragment = LicenceFragment()
                            val bundle = Bundle()
                            bundle.putString("publickey", publickey)
                            fragment.setArguments(bundle)
                            addNewFragment(fragment, R.id.container, true)
                            showDialogBox!!.dismiss()
                        }

                        showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                            showDialogBox!!.dismiss()
                            addNewFragment(
                                DirectiongoogleMapFragment(progressBarPB),
                                R.id.container,
                                true
                            )
                        }

                        Glide.with(showDialogBox!!.ivSeachImage)
                            .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                            .fitCenter()
                            .placeholder(R.drawable.round_logo)
                            .error(R.drawable.round_logo)
                            .centerCrop()
                            .into(showDialogBox!!.ivSeachImage)

                        showDialogBox!!.show()

                    } else {
                        PolylineNotAvailable(title)
                    }
                    false
                }

            } catch (E: Exception) {
                E.printStackTrace()
                Log.e("@@@", "ExceptionMarkerclick......." + E)
            }
    *//*


    }

*/

    /*


        //working method 9:56PM Dated 4 October 2024
    // This method updates the visible features based on the bounds and zoom level
        private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
            // Ensure 'features' JSONArray is available, possibly from ViewModel or network/database
            val features = multiPolygonFeatures!! // JSONArray of features

            // Clear previous data
            listMultiArray.clear()
            listPolyArray.clear()



            try {
                for (i in 0 until features.length()) {
                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                    val isLicensed = propertiesObject.getInt("IsLicensed") // Ensure type correctness
                    val rluNumber = propertiesObject.getString("RLUNo")

                    when (featureObject.getString("type")) {
                        "MultiPolygon" -> {
                            val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (j in 0 until multiPolygonCoordinates.length()) {
                                val innerPolygon = multiPolygonCoordinates.getJSONArray(j)
                                for (k in 0 until innerPolygon.length()) {
                                    val latLngList = innerPolygon.getJSONArray(k)
                                    listMultiArray.clear() // Clear for each new polygon

                                    for (l in 0 until latLngList.length()) {
                                        val latLngMultiPolyline =
                                            convertToLatLng(latLngList.getJSONArray(l))

                                        // Check if LatLng is within the visible region
                                        if (visibleBounds.contains(latLngMultiPolyline)) {
                                            listMultiArray.add(latLngMultiPolyline)
                                        }
                                    }

                                    // Add polyline if we have visible points
                                    if (listMultiArray.isNotEmpty()) {
                                        isLicense=isLicensed
                                        val polylineColor =
                                            if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                        val multiPolyline = mMap!!.addPolyline(
                                            PolylineOptions().addAll(listMultiArray)
                                                .width(5f)
                                                .color(
                                                    requireActivity().resources.getColor(
                                                        polylineColor
                                                    )
                                                )
                                                .geodesic(true)
                                        )
                                        multiPolylineList.add(multiPolyline)
                                    }
                                }
                            }
                        }

                        "Polygon" -> {
                            val polygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (m in 0 until polygonCoordinates.length()) {
                                val latLngList = polygonCoordinates.getJSONArray(m)
                                listPolyArray.clear() // Clear for each new polygon

                                for (n in 0 until latLngList.length()) {
                                    val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))

                                    // Check if LatLng is within the visible region
                                    if (visibleBounds.contains(latLngPolyline)) {
                                        listPolyArray.add(latLngPolyline)
                                    }
                                }

                                // Add polyline if we have visible points
                                if (listPolyArray.isNotEmpty()) {
                                    isLicense=isLicensed
                                    val polylineColor =
                                        if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                    val polyline = mMap!!.addPolyline(
                                        PolylineOptions().addAll(listPolyArray)
                                            .width(5f)
                                            .color(requireActivity().resources.getColor(polylineColor))
                                            .geodesic(true)
                                    )
                                    polylineList.add(polyline)
                                }
                            }
                        }
                    }

                    // Add marker (if necessary) and set title/snippet properly
                    val firstLatLng = listMultiArray.firstOrNull() ?: listPolyArray.firstOrNull()
                    firstLatLng?.let {
                        val marker = mMap!!.addMarker(
                            MarkerOptions().position(it)
                                .title(rluNumber)
                                .snippet(isLicensed.toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                        )
                        markerList.add(marker!!) // Keep track of markers
                    }




                }

                // Set OnMarkerClickListener
                mMap!!.setOnMarkerClickListener { marker ->
                    try {
                        val title = marker.title
                        val snippet = marker.snippet?.toIntOrNull() // 'isLicensed' is stored in snippet

                        if (title == null || snippet == null) {
                            Log.e("@@@", "Marker has missing information.")
                            return@setOnMarkerClickListener true
                        }

                        Log.d("@@@", "Marker clicked: Title = $title, Snippet (isLicensed) = $snippet")

                        if (snippet != null && snippet == 0) {
                            // If 'isLicensed' equals 0, fetch additional data and show the property details
                            hitRluMapDetail_API(title)
                            setObserver()

                            // Set up the dialog with property details
                            showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                            showDialogBox!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                            showDialogBox!!.setCanceledOnTouchOutside(true)

                            // Update dialog UI elements with property details
                            showDialogBox!!.tvHplPermitHead.text = title
                            showDialogBox!!.tvPermitCountry.text = "$countyName, $state"
                            showDialogBox!!.tvPermitAcres.text = acres.toString()
                            showDialogBox!!.tvHplPermitPrice.text = "$$licenseFee"

                            // Zoom in/out logic
                            showDialogBox!!.rlZoomMarker.setOnClickListener {
                                if (isClicked) {
                                    mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                                    showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                                    isClicked = false
                                } else {
                                    mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                                    showDialogBox!!.tvZoomOutIn.text = "Zoom In"
                                    isClicked = true
                                }
                            }

                            // Open details fragment
                            showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                showDialogBox!!.dismiss()
                                val fragment = LicenceFragment()
                                val bundle = Bundle().apply {
                                    putString("publickey", publickey)
                                }
                                fragment.arguments = bundle
                                addNewFragment(fragment, R.id.container, true)
                            }

                            // Open directions fragment
                            showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                showDialogBox!!.dismiss()
                                addNewFragment(
                                    DirectiongoogleMapFragment(progressBarPB),
                                    R.id.container,
                                    true
                                )
                            }

                            // Close dialog
                            showDialogBox!!.imgCancelProperty.setOnClickListener {
                                showDialogBox!!.dismiss()
                            }

                            // Load property image using Glide
                            Glide.with(showDialogBox!!.ivSeachImage)
                                .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                .fitCenter()
                                .placeholder(R.drawable.round_logo)
                                .error(R.drawable.round_logo)
                                .centerCrop()
                                .into(showDialogBox!!.ivSeachImage)

                            showDialogBox!!.show()

                        } else {
                            // If 'isLicensed' is not 0, call PolylineNotAvailable with the marker title
                            PolylineNotAvailable(title)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("@@@", "Exception in Marker click listener: $e")
                    }
                    true
                }


                */
    /*      // Set OnMarkerClickListener
                  mMap!!.setOnMarkerClickListener { marker ->
                      try {
                          val title = marker.title
                          val snippet = marker.snippet?.toIntOrNull()


                          if (title == null || snippet == null) {
                              Log.e("@@@", "Marker has missing information.")
                              return@setOnMarkerClickListener true
                          }
                          Log.e("@@@", "Marker has missing information.")

                          if (snippet==0) {

                              // Fetch additional data for the selected marker
                              hitRluMapDetail_API(title!!)
                              setObserver()
                              // Set up the dialog with property details
                              showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                              showDialogBox!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                              showDialogBox!!.setCanceledOnTouchOutside(true)

                              // Update dialog UI elements with property details
                              showDialogBox!!.tvHplPermitHead.text = title
                              showDialogBox!!.tvPermitCountry.text = "$countyName, $state"
                              showDialogBox!!.tvPermitAcres.text = acres.toString()
                              showDialogBox!!.tvHplPermitPrice.text = "$$licenseFee"

                              // Zoom in/out logic
                              showDialogBox!!.rlZoomMarker.setOnClickListener {
                                  if (isClicked) {
                                      mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
                                      showDialogBox!!.tvZoomOutIn.text = "Zoom Out"
                                      isClicked = false
                                  } else {
                                      mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
                                      showDialogBox!!.tvZoomOutIn.text = "Zoom In"
                                      isClicked = true
                                  }
                              }

                              // Open details fragment
                              showDialogBox!!.rlDetailsMarker.setOnClickListener {
                                  showDialogBox!!.dismiss()
                                  val fragment = LicenceFragment()
                                  val bundle = Bundle().apply {
                                      putString("publickey", publickey)
                                  }
                                  fragment.arguments = bundle
                                  addNewFragment(fragment, R.id.container, true)
                              }

                              // Open directions fragment
                              showDialogBox!!.rlDirectionsMarker.setOnClickListener {
                                  showDialogBox!!.dismiss()
                                  addNewFragment(
                                      DirectiongoogleMapFragment(progressBarPB),
                                      R.id.container,
                                      true
                                  )
                              }

                              showDialogBox!!.imgCancelProperty.setOnClickListener {
                                  showDialogBox!!.dismiss()
                              }

                              // Load the property image using Glide
                              Glide.with(showDialogBox!!.ivSeachImage)
                                  .load(Constants.PROPERTY_IMAGE_URL + imageFileName)
                                  .fitCenter()
                                  .placeholder(R.drawable.round_logo)
                                  .error(R.drawable.round_logo)
                                  .centerCrop()
                                  .into(showDialogBox!!.ivSeachImage)

                              showDialogBox!!.show()

                          } else {
                              // Handle case where the polyline is not available
                              PolylineNotAvailable(title)
                          }
                      } catch (e: Exception) {
                          e.printStackTrace()
                          Log.e("@@@", "Exception in Marker click listener: $e")
                      }
                      true
                  }

               *//*
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("@@@", "Exception: $e")
        }
    }


    */



  /*



// working main 18 october 2024
    private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
//        var a: Marker? = null // Variable to hold the marker reference
        listMultiArray.clear()
        listPolyArray.clear()
       simplecoordinates.clear()
        // Clear existing markers and polylines
        for (marker in markerList) {
            marker.remove()
        }
        markerList.clear()

        for (polyline in multiPolylineList) {
            polyline.remove()
        }
        multiPolylineList.clear()

        for (polyline in polylineList) {
            polyline.remove()
        }
        polylineList.clear()

        try {
            // Assume multiPolygonFeatures is a JSON array received from a ViewModel or API
            val features = multiPolygonFeatures!! // JSONArray of features

            for (i in 0 until features.length()) {
                val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                val isLicensed = propertiesObject.get("IsLicensed")
                val rluNumber = propertiesObject.get("RLUNo")

                when (featureObject.getString("type")) {
                    "MultiPolygon" -> {
                        val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                        for (j in 0 until multiPolygonCoordinates.length()) {
                            for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                                val innerPolygon =
                                    multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                                listMultiArray.clear() // Clear for each new polygon

                                for (l in 0 until innerPolygon.length()) {
                                    val latLngMultiPolyline =
                                        convertToLatLng(innerPolygon.getJSONArray(l))
                                    if (visibleBounds.contains(latLngMultiPolyline)) {
                                        listMultiArray.add(latLngMultiPolyline)
                                    }
                                }

                                // Add polyline if we have visible points
                                if (listMultiArray.isNotEmpty()) {
                                    val polylineColor =
                                        if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                    val multiPolyline = mMap!!.addPolyline(
                                        PolylineOptions().addAll(listMultiArray)
                                            .width(5f)
                                            .color(
                                                requireActivity().resources.getColor(
                                                    polylineColor
                                                )
                                            )
                                            .geodesic(true)
                                    )
                                    multiPolylineList.add(multiPolyline)
                                }

                                // Add markers for each LatLng in the list
                                for (latLng in listMultiArray) {
                                    a = mMap!!.addMarker(
                                        MarkerOptions().position(latLng)
                                            .title(rluNumber.toString())
                                            .snippet(isLicensed.toString())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                    )!!
                                    markerList.add(a!!) // Keep track of markers
                                }
                            }
                        }
                    }

                    "Polygon" -> {
                        val polygonCoordinates = featureObject.getJSONArray("coordinates")
                        for (m in 0 until polygonCoordinates.length()) {
                            val latLngList = polygonCoordinates.getJSONArray(m)
                            listPolyArray.clear() // Clear for each new polygon

                            for (n in 0 until latLngList.length()) {
                                val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                                if (visibleBounds.contains(latLngPolyline)) {
                                    listPolyArray.add(latLngPolyline)
                                }
                            }

                            // Add polyline if we have visible points
                            if (listPolyArray.isNotEmpty()) {
                                val polylineColor =
                                    if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                val polylines = mMap!!.addPolyline(
                                    PolylineOptions().addAll(listPolyArray)
                                        .width(5f)
                                        .color(requireActivity().resources.getColor(polylineColor))
                                        .geodesic(true)
                                )
                                polylineList.add(polylines)

                                // Add markers for each LatLng in the list
                                for (latLng in listPolyArray) {
                                    a = mMap!!.addMarker(
                                        MarkerOptions().position(latLng)
                                            .title(rluNumber.toString())
                                            .snippet(isLicensed.toString())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
                                    )!!
                                    markerList.add(a!!) // Keep track of markers
                                }
                            }
                        }
                    }
                }

                // Show info window for the last marker
                a?.showInfoWindow()
            }

            // Set OnMarkerClickListener
            mMap!!.setOnMarkerClickListener { marker ->
                val title = marker.title
                val snippet = marker.snippet?.toIntOrNull() // Convert snippet to int safely
                if (snippet != null) {
                    if (snippet == 0) {
                        isLicensec = snippet
                        // Handle the licensed case
                        hitRluMapDetail_API(title.toString())
                        setObserver()

                    } else {
                        // Handle the not licensed case
                        PolylineNotAvailable(title)
                    }
                }
                false // Return false to indicate we haven't consumed the event
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("@@@", "Exception in updateVisibleFeatures: $e")
        }
    }




  */

/*


    // working on 25 october 2024 5:28PM
    private var currentMarker: Marker? = null // Store the current clicked marker

    private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
        listMultiArray.clear()
        listPolyArray.clear()
        simplecoordinates.clear()

        // Clear existing map objects if necessary
        if (markerList.isNotEmpty()) {
            markerList.forEach { it.remove() }
            markerList.clear()
        }
        if (multiPolylineList.isNotEmpty()) {
            multiPolylineList.forEach { it.remove() }
            multiPolylineList.clear()
        }
        if (polylineList.isNotEmpty()) {
            polylineList.forEach { it.remove() }
            polylineList.clear()
        }

        try {
            val features = multiPolygonFeatures!! // Assume this is pre-loaded

            for (i in 0 until features.length()) {
                val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                val isLicensed = propertiesObject.getInt("IsLicensed")
                val rluNumber = propertiesObject.getString("RLUNo")
                val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                val colorResource = requireActivity().resources.getColor(polylineColor)

                when (featureObject.getString("type")) {
                    "MultiPolygon" -> {
                        val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                        processMultiPolygon(multiPolygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                    }
                    "Polygon" -> {
                        val polygonCoordinates = featureObject.getJSONArray("coordinates")
                        processPolygon(polygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                    }
                }

            }

            // Marker click listener
            mMap!!.setOnMarkerClickListener { marker ->
                // If a marker is clicked, ensure we reset any previously clicked marker's info window
                currentMarker?.hideInfoWindow()

                // Set the current marker to the newly clicked one
                currentMarker = marker

                val title = marker.title
                val snippet = marker.snippet?.toIntOrNull()

                if (snippet != null) {
                    if (snippet == 0) {
                        isLicensec = snippet
                        hitRluMapDetail_API(title.toString())
//                        marker.showInfoWindow() // Only show the info window after the API call completes
                        setObserver()
                    } else {
                        PolylineNotAvailable(title)
                        marker.showInfoWindow() // Show info window for unavailable polylines
                    }
                }
                true // Return true to indicate we have handled the marker click
            }

            // Prevent clicks outside markers from triggering info windows
            mMap!!.setOnMapClickListener {
                // When the map (not a marker) is clicked, hide the info window of the last clicked marker
                currentMarker?.hideInfoWindow()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("@@@", "Exception in updateVisibleFeatures: $e")
        }
    }

    private fun processMultiPolygon(
        multiPolygonCoordinates: JSONArray,
        visibleBounds: LatLngBounds,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int
    ) {
        for (j in 0 until multiPolygonCoordinates.length()) {
            for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                val innerPolygon = multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                listMultiArray.clear()

                for (l in 0 until innerPolygon.length()) {
                    val latLngMultiPolyline = convertToLatLng(innerPolygon.getJSONArray(l))
                    if (visibleBounds.contains(latLngMultiPolyline)) {
                        listMultiArray.add(latLngMultiPolyline)
                    }
                }

                if (listMultiArray.isNotEmpty()) {
                    val multiPolyline = mMap!!.addPolyline(
                        PolylineOptions().addAll(listMultiArray)
                            .width(5f)
                            .color(colorResource)
                            .geodesic(false)
                    )
                    multiPolylineList.add(multiPolyline)

                    // Add a marker at the centroid of the polygon
                    val centroid = getCentroid(listMultiArray)
                    addMarkerForFeature(centroid, rluNumber, isLicensed)
                }
            }
        }
    }

    private fun processPolygon(
        polygonCoordinates: JSONArray,
        visibleBounds: LatLngBounds,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int
    ) {
        for (m in 0 until polygonCoordinates.length()) {
            val latLngList = polygonCoordinates.getJSONArray(m)
            listPolyArray.clear()

            for (n in 0 until latLngList.length()) {
                val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                if (visibleBounds.contains(latLngPolyline)) {
                    listPolyArray.add(latLngPolyline)
                }
            }

            if (listPolyArray.isNotEmpty()) {
                val polyline = mMap!!.addPolyline(
                    PolylineOptions().addAll(listPolyArray)
                        .width(5f)
                        .color(colorResource)
                        .geodesic(false)
                )
                polylineList.add(polyline)

                // Add a marker at the centroid of the polygon
                val centroid = getCentroid(listPolyArray)
                addMarkerForFeature(centroid, rluNumber, isLicensed)
            }
        }
    }

    private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int) {
        val marker = mMap!!.addMarker(
            MarkerOptions().position(position)
                .title(rluNumber)
                .snippet(isLicensed.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )
        marker?.let { markerList.add(it) }
    }

    private fun getCentroid(points: List<LatLng>): LatLng {
        var latSum = 0.0
        var lngSum = 0.0
        for (point in points) {
            latSum += point.latitude
            lngSum += point.longitude
        }
        return LatLng(latSum / points.size, lngSum / points.size)
    }




*/

/*

    /// static multipolyline dated 26 october 2024 1:34 PM
    private var currentMarker: Marker? = null // Store the current clicked marker

    private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
        listMultiArray.clear()
        listPolyArray.clear()
        simplecoordinates.clear()

        // Clear existing map objects if necessary
        if (markerList.isNotEmpty()) {
            markerList.forEach { it.remove() }
            markerList.clear()
        }
        if (multiPolylineList.isNotEmpty()) {
            multiPolylineList.forEach { it.remove() }
            multiPolylineList.clear()
        }
        if (polylineList.isNotEmpty()) {
            polylineList.forEach { it.remove() }
            polylineList.clear()
        }

        try {
            val features = multiPolygonFeatures!! // Assume this is pre-loaded

            for (i in 0 until features.length()) {
                val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                val isLicensed = propertiesObject.getInt("IsLicensed")
                val rluNumber = propertiesObject.getString("RLUNo")
                val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                val colorResource = requireActivity().resources.getColor(polylineColor)

                when (featureObject.getString("type")) {
                    "MultiPolygon" -> {
                        val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                        processMultiPolygon(multiPolygonCoordinates, rluNumber, isLicensed, colorResource)
                    }
                    "Polygon" -> {
                        val polygonCoordinates = featureObject.getJSONArray("coordinates")
                        processPolygon(polygonCoordinates, rluNumber, isLicensed, colorResource)
                    }
                }
            }

            // Marker click listener
            mMap!!.setOnMarkerClickListener { marker ->
                // If a marker is clicked, ensure we reset any previously clicked marker's info window
                currentMarker?.hideInfoWindow()

                // Set the current marker to the newly clicked one
                currentMarker = marker

                val title = marker.title
                val snippet = marker.snippet?.toIntOrNull()

                if (snippet != null) {
                    if (snippet == 0) {
                        isLicensec = snippet
                        hitRluMapDetail_API(title.toString())
                        setObserver()
                    } else {
                        PolylineNotAvailable(title)
                        marker.showInfoWindow() // Show info window for unavailable polylines
                    }
                }
                true // Return true to indicate we have handled the marker click
            }

            // Prevent clicks outside markers from triggering info windows
            mMap!!.setOnMapClickListener {
                // When the map (not a marker) is clicked, hide the info window of the last clicked marker
                currentMarker?.hideInfoWindow()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("@@@", "Exception in updateVisibleFeatures: $e")
        }
    }

    private fun processMultiPolygon(
        multiPolygonCoordinates: JSONArray,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int
    ) {
        for (j in 0 until multiPolygonCoordinates.length()) {
            for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                val innerPolygon = multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                listMultiArray.clear()

                for (l in 0 until innerPolygon.length()) {
                    val latLngMultiPolyline = convertToLatLng(innerPolygon.getJSONArray(l))
                    listMultiArray.add(latLngMultiPolyline)
                }

                if (listMultiArray.isNotEmpty()) {
                    val multiPolyline = mMap!!.addPolyline(
                        PolylineOptions().addAll(listMultiArray)
                            .width(5f)
                            .color(colorResource)
                            .geodesic(false)
                    )
                    multiPolylineList.add(multiPolyline)

                    // Add a marker at the centroid of the polygon
                    val centroid = getCentroid(listMultiArray)
                    addMarkerForFeature(centroid, rluNumber, isLicensed)
                }
            }
        }
    }

    private fun processPolygon(
        polygonCoordinates: JSONArray,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int
    ) {
        for (m in 0 until polygonCoordinates.length()) {
            val latLngList = polygonCoordinates.getJSONArray(m)
            listPolyArray.clear()

            for (n in 0 until latLngList.length()) {
                val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                listPolyArray.add(latLngPolyline)
            }

            if (listPolyArray.isNotEmpty()) {
                val polyline = mMap!!.addPolyline(
                    PolylineOptions().addAll(listPolyArray)
                        .width(5f)
                        .color(colorResource)
                        .geodesic(false)
                )
                polylineList.add(polyline)

                // Add a marker at the centroid of the polygon
                val centroid = getCentroid(listPolyArray)
                addMarkerForFeature(centroid, rluNumber, isLicensed)
            }
        }
    }

    private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int) {
        val marker = mMap!!.addMarker(
            MarkerOptions().position(position)
                .title(rluNumber)
                .snippet(isLicensed.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )
        marker?.let { markerList.add(it) }
    }

    private fun getCentroid(points: List<LatLng>): LatLng {
        var latSum = 0.0
        var lngSum = 0.0
        for (point in points) {
            latSum += point.latitude
            lngSum += point.longitude
        }
        return LatLng(latSum / points.size, lngSum / points.size)
    }


    */


/*





//// worked on 26 October 2024 1:50 PM
    private var currentMarker: Marker? = null // Store the current clicked marker

    private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
        listMultiArray.clear()
        listPolyArray.clear()
        simplecoordinates.clear()

        // Clear existing map objects if necessary
        if (markerList.isNotEmpty()) {
            markerList.forEach { it.remove() }
            markerList.clear()
        }
        if (multiPolylineList.isNotEmpty()) {
            multiPolylineList.forEach { it.remove() }
            multiPolylineList.clear()
        }
        if (polylineList.isNotEmpty()) {
            polylineList.forEach { it.remove() }
            polylineList.clear()
        }

        try {
            val features = multiPolygonFeatures!! // Assume this is pre-loaded

            for (i in 0 until features.length()) {
                val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                val isLicensed = propertiesObject.getInt("IsLicensed")
                val rluNumber = propertiesObject.getString("RLUNo")
                val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                val colorResource = requireActivity().resources.getColor(polylineColor)

                when (featureObject.getString("type")) {
                    "MultiPolygon" -> {
                        val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                        processMultiPolygon(multiPolygonCoordinates, rluNumber, isLicensed, colorResource, visibleBounds)
                    }
                    "Polygon" -> {
                        val polygonCoordinates = featureObject.getJSONArray("coordinates")
                        processPolygon(polygonCoordinates, rluNumber, isLicensed, colorResource, visibleBounds)
                    }
                }
            }

            // Marker click listener
            mMap!!.setOnMarkerClickListener { marker ->
                currentMarker?.hideInfoWindow() // Reset any previously clicked marker's info window

                // Set the current marker to the newly clicked one
                currentMarker = marker

                val title = marker.title
                val snippet = marker.snippet?.toIntOrNull()

                if (snippet != null) {
                    if (snippet == 0) {
                        isLicensec = snippet
                        hitRluMapDetail_API(title.toString())
                        setObserver()
                    } else {
                        PolylineNotAvailable(title)
                        marker.showInfoWindow() // Show info window for unavailable polylines
                    }
                }
                true // Return true to indicate we have handled the marker click
            }

            // Prevent clicks outside markers from triggering info windows
            mMap!!.setOnMapClickListener {
                currentMarker?.hideInfoWindow()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("@@@", "Exception in updateVisibleFeatures: $e")
        }
    }

    private fun processMultiPolygon(
        multiPolygonCoordinates: JSONArray,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int,
        visibleBounds: LatLngBounds
    ) {
        for (j in 0 until multiPolygonCoordinates.length()) {
            for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                val innerPolygon = multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                listMultiArray.clear()

                for (l in 0 until innerPolygon.length()) {
                    val latLngMultiPolyline = convertToLatLng(innerPolygon.getJSONArray(l))
                    listMultiArray.add(latLngMultiPolyline)
                }

                // Check if any part of the polygon is within visibleBounds
                if (listMultiArray.any { visibleBounds.contains(it) }) {
                    val multiPolyline = mMap!!.addPolyline(
                        PolylineOptions().addAll(listMultiArray)
                            .width(5f)
                            .color(colorResource)
                            .geodesic(false)
                    )
                    multiPolylineList.add(multiPolyline)

                    // Add a marker at the centroid of the polygon
                    val centroid = getCentroid(listMultiArray)
                    addMarkerForFeature(centroid, rluNumber, isLicensed)
                }
            }
        }
    }

    private fun processPolygon(
        polygonCoordinates: JSONArray,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int,
        visibleBounds: LatLngBounds
    ) {
        for (m in 0 until polygonCoordinates.length()) {
            val latLngList = polygonCoordinates.getJSONArray(m)
            listPolyArray.clear()

            for (n in 0 until latLngList.length()) {
                val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                listPolyArray.add(latLngPolyline)
            }

            // Check if any part of the polygon is within visibleBounds
            if (listPolyArray.any { visibleBounds.contains(it) }) {
                val polyline = mMap!!.addPolyline(
                    PolylineOptions().addAll(listPolyArray)
                        .width(5f)
                        .color(colorResource)
                        .geodesic(false)
                )
                polylineList.add(polyline)

                // Add a marker at the centroid of the polygon
                val centroid = getCentroid(listPolyArray)
                addMarkerForFeature(centroid, rluNumber, isLicensed)
            }
        }
    }

    private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int) {
        val marker = mMap!!.addMarker(
            MarkerOptions().position(position)
                .title(rluNumber)
                .snippet(isLicensed.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )
        marker?.let { markerList.add(it) }
    }

    private fun getCentroid(points: List<LatLng>): LatLng {
        var latSum = 0.0
        var lngSum = 0.0
        for (point in points) {
            latSum += point.latitude
            lngSum += point.longitude
        }
        return LatLng(latSum / points.size, lngSum / points.size)
    }







*/











// Working on 29 October 2024 at 6:41 PM

    private var currentMarker: Marker? = null // Store the current clicked marker
    var markersToAdd = mutableListOf<Triple<LatLng, String, Int>>() // Store markers for batch adding


    private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
        listMultiArray.clear()
        listPolyArray.clear()
        simplecoordinates.clear()

        // Use a CoroutineScope to avoid blocking the main thread
        CoroutineScope(Dispatchers.Main).launch {
            clearExistingMapObjects()

            try {
                // Load features on the IO thread
                val features = withContext(Dispatchers.IO) {
                    multiPolygonFeatures!! // Assume this is pre-loaded
                }


                for (i in 0 until features.length()) {
                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                    val isLicensed = propertiesObject.getInt("IsLicensed")
                    val rluNumber = propertiesObject.getString("RLUNo")
                    val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                    val colorResource = requireActivity().resources.getColor(polylineColor)

                    when (featureObject.getString("type")) {
                        "MultiPolygon" -> {
                            val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                            processMultiPolygon(multiPolygonCoordinates, rluNumber, isLicensed, colorResource, visibleBounds, markersToAdd)
                        }
                        "Polygon" -> {
                            val polygonCoordinates = featureObject.getJSONArray("coordinates")
                            processPolygon(polygonCoordinates, rluNumber, isLicensed, colorResource, visibleBounds, markersToAdd)
                        }
                    }
                }

                // Batch add all markers at once
                markersToAdd.forEach { (position, rluNumber, isLicensed) ->
                    addMarkerForFeature(position, rluNumber, isLicensed)
                    addLabelMarker(position, rluNumber) // Add label markers
                }

                // Setup the marker click listener once here
                setupMarkerClickListener()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("@@@", "Exception in updateVisibleFeatures: $e")
            }
        }
    }

    private suspend fun clearExistingMapObjects() {
        withContext(Dispatchers.Main) {
            // Remove existing markers and polylines from the map
            markerList.forEach { it.remove() }
            markerList.clear()
            multiPolylineList.forEach { it.remove() }
            multiPolylineList.clear()
            polylineList.forEach { it.remove() }
            polylineList.clear()
            labelMarkers.forEach { it.remove() } // Clear label markers
            labelMarkers.clear()
        }
    }

    private fun processMultiPolygon(
        multiPolygonCoordinates: JSONArray,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int,
        visibleBounds: LatLngBounds,
        markersToAdd: MutableList<Triple<LatLng, String, Int>>
    ) {
        for (j in 0 until multiPolygonCoordinates.length()) {
            for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                val innerPolygon = multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                listMultiArray.clear()

                for (l in 0 until innerPolygon.length()) {
                    val latLngMultiPolyline = convertToLatLng(innerPolygon.getJSONArray(l))
                    listMultiArray.add(latLngMultiPolyline)
                }

                // Check if any part of the polygon is within visibleBounds
                if (listMultiArray.any { visibleBounds.contains(it) }) {
                    val multiPolyline = PolylineOptions().addAll(listMultiArray)
                        .width(5f)
                        .color(colorResource)
                        .geodesic(false)

                    multiPolylineList.add(mMap!!.addPolyline(multiPolyline)) // Add polyline directly

                    // Add a marker at the centroid of the polygon
                    val centroid = getCentroid(listMultiArray)
                    markersToAdd.add(Triple(centroid, rluNumber, isLicensed))
                }
            }
        }
    }

    private fun processPolygon(
        polygonCoordinates: JSONArray,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int,
        visibleBounds: LatLngBounds,
        markersToAdd: MutableList<Triple<LatLng, String, Int>>
    ) {
        for (m in 0 until polygonCoordinates.length()) {
            val latLngList = polygonCoordinates.getJSONArray(m)
            listPolyArray.clear()

            for (n in 0 until latLngList.length()) {
                val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                listPolyArray.add(latLngPolyline)
            }

            // Check if any part of the polygon is within visibleBounds
            if (listPolyArray.any { visibleBounds.contains(it) }) {
                val polyline = PolylineOptions().addAll(listPolyArray)
                    .width(5f)
                    .color(colorResource)
                    .geodesic(false)

                polylineList.add(mMap!!.addPolyline(polyline)) // Add polyline directly

                // Add a marker at the centroid of the polygon
                val centroid = getCentroid(listPolyArray)
                markersToAdd.add(Triple(centroid, rluNumber, isLicensed))
            }
        }
    }

//    private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int) {
//        a = mMap!!.addMarker(
//            MarkerOptions().position(position)
//                .title(rluNumber)
//                .snippet(isLicensed.toString())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
//        )
//        a?.let { markerList.add(it) }
    /*val marker = mMap!!.addMarker(
            MarkerOptions().position(position)
                .title(rluNumber)
                .snippet(isLicensed.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )
        marker?.let { markerList.add(it) }*/
//    }


    private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int?) {
//        val snippetText = isLicensed?.toString() ?: "N/A"
        val snippetText = isLicensed?.toString()

        val marker = mMap!!.addMarker(
            MarkerOptions().position(position)
                .title(rluNumber)
                .snippet(isLicensed.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )
        marker?.let {
            Log.d("@@@", "Marker added with title: $rluNumber, snippet: $snippetText") // Debug log
            markerList.add(it)
        }
    }

    private fun setupMarkerClickListener() {
        // Ensure this is only called once by adding a check or setting a flag if necessary
        mMap!!.setOnMarkerClickListener { marker ->
//            if (currentMarker == marker) {
//                return@setOnMarkerClickListener true // Ignore repeated clicks on the same marker
//            }
//
//            currentMarker?.hideInfoWindow() // Hide the info window of any previously clicked marker
//            currentMarker = marker // Update current marker to the newly clicked one

            val title = marker.title
            val snippet = marker.snippet?.toIntOrNull() // Try to parse snippet as integer

            snippet?.let {
                Log.d("@@@", "Marker clicked with title: $title, snippet: $snippet") // Debug log
                if (it == 0) {
                    isLicensec = it
                    hitRluMapDetail_API(title.toString())
                } else {
                    PolylineNotAvailable(title)
//                    marker.showInfoWindow() // Show info window for unavailable polylines
                }
            } ?: run {
                Log.e("@@@", "Error: Marker snippet is null or invalid for marker with title: $title")
            }
            true // Return true to indicate we've handled the click
        }

    }


    private fun getCentroid(points: List<LatLng>): LatLng {
        val latSum = points.sumOf { it.latitude }
        val lngSum = points.sumOf { it.longitude }
        return LatLng(latSum / points.size, lngSum / points.size)
    }















//    private fun setupMarkerClickListener() {
//        mMap!!.setOnMarkerClickListener { marker ->
//            // If the clicked marker is the same as the current one, ignore
//            if (currentMarker == marker) {
//                return@setOnMarkerClickListener true
//            }
//
//            currentMarker?.hideInfoWindow() // Reset any previously clicked marker's info window
//            currentMarker = marker // Set the current marker to the newly clicked one
//
//            val title = marker.title
//            val snippet = marker.snippet?.toIntOrNull()
//
//            snippet?.let {
//                if (it == 0) {
//                    isLicensec = it
//                    hitRluMapDetail_API(title.toString())
////                    setObserver()
//                } else {
//                    PolylineNotAvailable(title)
//                    marker.showInfoWindow() // Show info window for unavailable polylines
//                }
//            }
//            true // Return true to indicate we have handled the marker click
//        }
//
//        // Prevent clicks outside markers from triggering info windows
////        mMap!!.setOnMapClickListener {
////            currentMarker?.hideInfoWindow()
////            currentMarker = null // Clear current marker reference
////        }
//    }

    var labelMarkers = mutableListOf<Marker>() // Store label markers for easy management

    private fun addLabelMarker(position: LatLng, labelText: String) {
        val labelMarker = mMap!!.addMarker(
            MarkerOptions().position(position)
                .icon(BitmapDescriptorFactory.fromBitmap(createLabelBitmap(labelText)))
        )
        labelMarker?.let {
            it.isVisible = mMap!!.cameraPosition.zoom >= 12 // Set initial visibility
            labelMarkers.add(it) // Store label markers
        }
    }


    // Helper function to create a label bitmap
    private fun createLabelBitmap(text: String): Bitmap {
        val label = TextView(context).apply {
            setText(text)
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.rgb(252, 247, 224))
            textSize = 14f // Customize text size as needed
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, measuredWidth, measuredHeight)
        }
        val bitmap = Bitmap.createBitmap(label.measuredWidth, label.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        label.draw(canvas)
        return bitmap
    }






    /*  private fun convertToLatLng(jsonArray: JSONArray): LatLng {
          return LatLng(jsonArray.getDouble(1), jsonArray.getDouble(0))
      }*/



    /*

       // working for static area only
       private var currentMarker: Marker? = null // Store the current clicked marker
       private val drawnPolylines = mutableListOf<Polyline>() // Store drawn polylines

       private var isLicensed: Int = 0 // License status variable

       fun initializeMap() {
           // Set up a camera idle listener to monitor zoom level
           mMap!!.setOnCameraIdleListener {
               updateVisibleFeatures(mMap!!.projection.visibleRegion.latLngBounds)
           }
       }

       private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
           // Clear existing map objects
           markerList.forEach { it.remove() }
           markerList.clear()
           drawnPolylines.forEach { it.remove() }
           drawnPolylines.clear()
           multiPolylineList.forEach { it.remove() }
           multiPolylineList.clear()

           try {
               val features = multiPolygonFeatures!! // Assume this is pre-loaded

               for (i in 0 until features.length()) {
                   val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                   val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                   val isLicensed = propertiesObject.getInt("IsLicensed")
                   val rluNumber = propertiesObject.getString("RLUNo")
                   val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                   val colorResource = requireActivity().resources.getColor(polylineColor)

                   when (featureObject.getString("type")) {
                       "MultiPolygon" -> {
                           val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                           processMultiPolygon(multiPolygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                       }
                       "Polygon" -> {
                           val polygonCoordinates = featureObject.getJSONArray("coordinates")
                           processPolygon(polygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                       }
                   }
               }

               // Set marker click listener
               setMarkerClickListener()

           } catch (e: Exception) {
               e.printStackTrace()
               Log.e("@@@", "Exception in updateVisibleFeatures: $e")
           }
       }

       private fun processMultiPolygon(
           multiPolygonCoordinates: JSONArray,
           visibleBounds: LatLngBounds,
           rluNumber: String,
           isLicensed: Int,
           colorResource: Int
       ) {
           for (j in 0 until multiPolygonCoordinates.length()) {
               for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                   val innerPolygon = multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                   val listMultiArray = mutableListOf<LatLng>()

                   for (l in 0 until innerPolygon.length()) {
                       val latLngMultiPolyline = convertToLatLng(innerPolygon.getJSONArray(l))
                       listMultiArray.add(latLngMultiPolyline) // Keep all coordinates without filtering
                   }

                   // Add a polyline without checking for visibility bounds
                   if (listMultiArray.isNotEmpty()) {
                       val multiPolyline = mMap!!.addPolyline(
                           PolylineOptions().addAll(listMultiArray)
                               .width(5f)
                               .color(colorResource)
                               .geodesic(false)
                       )
                       multiPolylineList.add(multiPolyline)

                       // Add a marker at the centroid of the polygon
                       val centroid = getCentroid(listMultiArray)
                       addMarkerForFeature(centroid, rluNumber, isLicensed)
                   }
               }
           }
       }

       private fun processPolygon(
           polygonCoordinates: JSONArray,
           visibleBounds: LatLngBounds,
           rluNumber: String,
           isLicensed: Int,
           colorResource: Int
       ) {
           for (m in 0 until polygonCoordinates.length()) {
               val latLngList = polygonCoordinates.getJSONArray(m)
               val listPolyArray = mutableListOf<LatLng>()

               for (n in 0 until latLngList.length()) {
                   val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                   listPolyArray.add(latLngPolyline) // Keep all coordinates without filtering
               }

               // Add a polyline without checking for visibility bounds
               if (listPolyArray.isNotEmpty()) {
                   val polyline = mMap!!.addPolyline(
                       PolylineOptions().addAll(listPolyArray)
                           .width(5f)
                           .color(colorResource)
                           .geodesic(false)
                   )
                   drawnPolylines.add(polyline)

                   // Add a marker at the centroid of the polygon
                   val centroid = getCentroid(listPolyArray)
                   addMarkerForFeature(centroid, rluNumber, isLicensed)
               }
           }
       }

       private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int) {
           val marker = mMap!!.addMarker(
               MarkerOptions().position(position)
                   .title(rluNumber)
                   .snippet(isLicensed.toString())
                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
           )
           marker?.let { markerList.add(it) }
       }

       private fun setMarkerClickListener() {
           mMap!!.setOnMarkerClickListener { marker ->
               // If a marker is clicked, reset any previously clicked marker's info window
               currentMarker?.hideInfoWindow()

               // Set the current marker to the newly clicked one
               currentMarker = marker

               val title = marker.title
               val snippet = marker.snippet?.toIntOrNull()

               if (snippet != null) {
                   if (snippet == 0) {
                       isLicensed = snippet
                       hitRluMapDetail_API(title.toString())
                       marker.showInfoWindow() // Show info window post-API call
                       setObserver()
                   } else {
                       PolylineNotAvailable(title)
                       marker.showInfoWindow() // Show info window for unavailable polylines
                   }
               }
               true // Return true to indicate we've handled the marker click
           }

           // Prevent clicks outside markers from triggering info windows
           mMap!!.setOnMapClickListener {
               currentMarker?.hideInfoWindow() // Hide info window of last clicked marker
           }
       }

       private fun getCentroid(points: List<LatLng>): LatLng {
           var latSum = 0.0
           var lngSum = 0.0
           for (point in points) {
               latSum += point.latitude
               lngSum += point.longitude
           }
           return LatLng(latSum / points.size, lngSum / points.size)
       }




       *//* private var currentMarker: Marker? = null // Store the current clicked marker

     private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
         // Remove the old polygons and labels
         markerList.forEach { it.remove() }
         markerList.clear()
         multiPolylineList.forEach { it.remove() }
         multiPolylineList.clear()
         polylineList.forEach { it.remove() }
         polylineList.clear()

         try {
             val features = multiPolygonFeatures!! // Assume this is pre-loaded


             for (i in 0 until features.length()) {
                 val feature = features.getJSONObject(i)
                 val featureObject = feature.getJSONObject("geometry")
                 val propertiesObject = feature.getJSONObject("properties")
                 val isLicensed = propertiesObject.getInt("IsLicensed")
                 val rluNumber = propertiesObject.getString("RLUNo")
                 val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                 val colorResource = requireActivity().resources.getColor(polylineColor)

                 when (featureObject.getString("type")) {
                     "MultiPolygon" -> {
                         val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                         processMultiPolygon(multiPolygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                     }
                     "Polygon" -> {
                         val polygonCoordinates = featureObject.getJSONArray("coordinates")
                         processPolygon(polygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                     }
                 }
             }

             // Marker click listener
             mMap!!.setOnMarkerClickListener { marker ->
                 // If a marker is clicked, ensure we reset any previously clicked marker's info window
                 currentMarker?.hideInfoWindow()

                 // Set the current marker to the newly clicked one
                 currentMarker = marker

                 val title = marker.title
                 val snippet = marker.snippet?.toIntOrNull()

                 if (snippet != null) {
                     if (snippet == 0) {
                         isLicensec = snippet
                         hitRluMapDetail_API(title.toString())
                         marker.showInfoWindow() // Only show the info window after the API call completes

                         setObserver()
                     } else {
                         PolylineNotAvailable(title)
                         marker.showInfoWindow() // Show info window for unavailable polylines
                     }
                 }
                 true // Return true to indicate we have handled the marker click
             }

             // Prevent clicks outside markers from triggering info windows
             mMap!!.setOnMapClickListener {
                 // When the map (not a marker) is clicked, hide the info window of the last clicked marker
                 currentMarker?.hideInfoWindow()
             }

             // Call updateVisibleFeatures when the map is idle
             mMap!!.setOnCameraIdleListener {
                 val visibleBounds = mMap!!.projection.visibleRegion.latLngBounds
                 updateVisibleFeatures(visibleBounds)
             }
         } catch (e: Exception) {
             e.printStackTrace()
             Log.e("@@@", "Exception in updateVisibleFeatures: $e")
         }
     }

     private fun processMultiPolygon(
         multiPolygonCoordinates: JSONArray,
         visibleBounds: LatLngBounds,
         rluNumber: String,
         isLicensed: Int,
         colorResource: Int
     ) {
         for (j in 0 until multiPolygonCoordinates.length()) {
             for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                 val innerPolygon = multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                 listMultiArray.clear()

                 for (l in 0 until innerPolygon.length()) {
                     val latLngMultiPolyline = convertToLatLng(innerPolygon.getJSONArray(l))
                     if (visibleBounds.contains(latLngMultiPolyline)) {
                         listMultiArray.add(latLngMultiPolyline)
                     }
                 }

                 if (listMultiArray.isNotEmpty()) {
                     val multiPolyline = mMap!!.addPolyline(
                         PolylineOptions().addAll(listMultiArray)
                             .width(5f)
                             .color(colorResource)
                             .geodesic(false)
                     )
                     multiPolylineList.add(multiPolyline)

                     // Add a marker at the centroid of the polygon
                     val centroid = getCentroid(listMultiArray)
                     addMarkerForFeature(centroid, rluNumber, isLicensed)
                 }
             }
         }
     }

     private fun processPolygon(
         polygonCoordinates: JSONArray,
         visibleBounds: LatLngBounds,
         rluNumber: String,
         isLicensed: Int,
         colorResource: Int
     ) {
         for (m in 0 until polygonCoordinates.length()) {
             val latLngList = polygonCoordinates.getJSONArray(m)
             listPolyArray.clear()

             for (n in 0 until latLngList.length()) {
                 val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                 if (visibleBounds.contains(latLngPolyline)) {
                     listPolyArray.add(latLngPolyline)
                 }
             }

             if (listPolyArray.isNotEmpty()) {
                 val polyline = mMap!!.addPolyline(
                     PolylineOptions().addAll(listPolyArray)
                         .width(5f)
                         .color(colorResource)
                         .geodesic(false)
                 )
                 polylineList.add(polyline)

                 // Add a marker at the centroid of the polygon
                 val centroid = getCentroid(listPolyArray)
                 addMarkerForFeature(centroid, rluNumber, isLicensed)
             }
         }
     }

     private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int) {
         val marker = mMap!!.addMarker(
             MarkerOptions().position(position)
                 .title(rluNumber)
                 .snippet(isLicensed.toString())
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
         )
         marker?.let { markerList.add(it) }
     }

     private fun getCentroid(points: List<LatLng>): LatLng {
         var latSum = 0.0
         var lngSum = 0.0
         for (point in points) {
             latSum += point.latitude
             lngSum += point.longitude
         }
         return LatLng(latSum / points.size, lngSum / points.size)
     }

 *//*



*/


    /*
      private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
          listMultiArray.clear()
          listPolyArray.clear()
          simplecoordinates.clear()

          // Clear existing map objects if necessary
          if (markerList.isNotEmpty()) {
              markerList.forEach { it.remove() }
              markerList.clear()
          }
          if (multiPolylineList.isNotEmpty()) {
              multiPolylineList.forEach { it.remove() }
              multiPolylineList.clear()
          }
          if (polylineList.isNotEmpty()) {
              polylineList.forEach { it.remove() }
              polylineList.clear()
          }

          try {
              val features = multiPolygonFeatures!! // Assume this is pre-loaded

              for (i in 0 until features.length()) {
                  val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                  val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                  val isLicensed = propertiesObject.getInt("IsLicensed")
                  val rluNumber = propertiesObject.getString("RLUNo")
                  val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                  val colorResource = requireActivity().resources.getColor(polylineColor)

                  when (featureObject.getString("type")) {
                      "MultiPolygon" -> {
                          val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                          processMultiPolygon(multiPolygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                      }
                      "Polygon" -> {
                          val polygonCoordinates = featureObject.getJSONArray("coordinates")
                          processPolygon(polygonCoordinates, visibleBounds, rluNumber, isLicensed, colorResource)
                      }
                  }

                  // Show info window for the last marker
                  a?.showInfoWindow()
              }

              // Set OnMarkerClickListener
              mMap!!.setOnMarkerClickListener { marker ->
                  val title = marker.title
                  val snippet = marker.snippet?.toIntOrNull()
                  if (snippet != null) {
                      if (snippet == 0) {
                          isLicensec = snippet
                          hitRluMapDetail_API(title.toString())
                          setObserver()
                      } else {
                          PolylineNotAvailable(title)
                      }
                  }
                  false
              }
          } catch (e: Exception) {
              e.printStackTrace()
              Log.e("@@@", "Exception in updateVisibleFeatures: $e")
          }
      }

        private fun processMultiPolygon(
            multiPolygonCoordinates: JSONArray,
            visibleBounds: LatLngBounds,
            rluNumber: String,
            isLicensed: Int,
            colorResource: Int
        ) {
            for (j in 0 until multiPolygonCoordinates.length()) {
                for (k in 0 until multiPolygonCoordinates.getJSONArray(j).length()) {
                    val innerPolygon = multiPolygonCoordinates.getJSONArray(j).getJSONArray(k)
                    listMultiArray.clear()

                    for (l in 0 until innerPolygon.length()) {
                        val latLngMultiPolyline = convertToLatLng(innerPolygon.getJSONArray(l))
                        if (visibleBounds.contains(latLngMultiPolyline)) {
                            listMultiArray.add(latLngMultiPolyline)
                        }
                    }

                    if (listMultiArray.isNotEmpty()) {
                        val multiPolyline = mMap!!.addPolyline(
                            PolylineOptions().addAll(listMultiArray)
                                .width(5f)
                                .color(colorResource)
                                .geodesic(true)
                        )
                        multiPolylineList.add(multiPolyline)

                        // Add one marker per polygon
                        addMarkerForFeature(listMultiArray[0], rluNumber, isLicensed)
                    }
                    // Add markers for each LatLng in the list
                  */
/*  for (latLng in listMultiArray) {
//                a = mMap!!.addMarker(
//                    MarkerOptions().position(latLng)
//                        .title(rluNumber.toString())
//                        .snippet(isLicensed.toString())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
//                )!!
//                markerList.add(a!!) // Keep track of markers

                    addMarkerForFeature(latLng, rluNumber, isLicensed)

                }*//*

            }
        }
    }

    private fun processPolygon(
        polygonCoordinates: JSONArray,
        visibleBounds: LatLngBounds,
        rluNumber: String,
        isLicensed: Int,
        colorResource: Int
    ) {
        for (m in 0 until polygonCoordinates.length()) {
            val latLngList = polygonCoordinates.getJSONArray(m)
            listPolyArray.clear()

            for (n in 0 until latLngList.length()) {
                val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                if (visibleBounds.contains(latLngPolyline)) {
                    listPolyArray.add(latLngPolyline)
                }
            }

            if (listPolyArray.isNotEmpty()) {
                val polyline = mMap!!.addPolyline(
                    PolylineOptions().addAll(listPolyArray)
                        .width(5f)
                        .color(colorResource)
                        .geodesic(true)
                )
                polylineList.add(polyline)

                // Add one marker per polygon
                addMarkerForFeature(listPolyArray[0], rluNumber, isLicensed)
            }
            // Add markers for each LatLng in the list
            */
/*for (latLng in listPolyArray) {
//                a = mMap!!.addMarker(
//                    MarkerOptions().position(latLng)
//                        .title(rluNumber.toString())
//                        .snippet(isLicensed.toString())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
//                )!!
//                markerList.add(a!!) // Keep track of markers

                addMarkerForFeature(latLng, rluNumber, isLicensed)

            }*//*

        }
    }

    private fun addMarkerForFeature(position: LatLng, rluNumber: String, isLicensed: Int) {
        val marker = mMap!!.addMarker(
            MarkerOptions().position(position)
                .title(rluNumber)
                .snippet(isLicensed.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )
        marker?.let { markerList.add(it) }

      */
/*  // Add markers for each LatLng in the list
//        for (latLng in listPolyArray) {
            a = mMap!!.addMarker(
                MarkerOptions().position(position)
                    .title(rluNumber.toString())
                    .snippet(isLicensed.toString())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
            )!!
            markerList.add(a!!) // Keep track of markers
//        }*//*


    }


*/


    //Working method for map polylines
    // This method updates the visible features based on the bounds and zoom level
    /*
        private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
            val zoomLevel = mMap!!.cameraPosition.zoom

            // Ensure 'features' JSONArray is available, possibly from ViewModel or network/database
            val features = multiPolygonFeatures!! // JSONArray of features

            // Clear previous data
            listMultiArray.clear()
            listPolyArray.clear()

            // Clear polylines if zoom level is less than 10
            if (zoomLevel < 10) {
                // Remove all polylines from the map
                for (polyline in polylineList) {
                    polyline.remove()
                }
                for (multiPolyline in multiPolylineList) {
                    multiPolyline.remove()
                }
                polylineList.clear()
                multiPolylineList.clear()

                // Remove all markers if zoom level is less than 10
                for (marker in markerList) {
                    marker.remove()
                }
                markerList.clear()

                return // Exit the function early if zoom level is less than 10
            }

            try {
                for (i in 0 until features.length()) {
                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                    val isLicensed = propertiesObject.getInt("IsLicensed") // Ensure type correctness
                    val rluNumber = propertiesObject.getString("RLUNo")

                    when (featureObject.getString("type")) {
                        "MultiPolygon" -> {
                            val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (j in 0 until multiPolygonCoordinates.length()) {
                                val innerPolygon = multiPolygonCoordinates.getJSONArray(j)
                                for (k in 0 until innerPolygon.length()) {
                                    val latLngList = innerPolygon.getJSONArray(k)
                                    listMultiArray.clear() // Clear for each new polygon

                                    for (l in 0 until latLngList.length()) {
                                        val latLngMultiPolyline = convertToLatLng(latLngList.getJSONArray(l))

                                        // Check if LatLng is within the visible region
                                        if (visibleBounds.contains(latLngMultiPolyline)) {
                                            listMultiArray.add(latLngMultiPolyline)
                                        }
                                    }

                                    // Add polyline if we have visible points
                                    if (listMultiArray.isNotEmpty()) {
                                        val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                        val multiPolyline = mMap!!.addPolyline(
                                            PolylineOptions().addAll(listMultiArray)
                                                .width(5f)
                                                .color(requireActivity().resources.getColor(polylineColor))
                                                .geodesic(true)
                                        )
                                        multiPolylineList.add(multiPolyline)
                                    }
                                }
                            }
                        }

                        "Polygon" -> {
                            val polygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (m in 0 until polygonCoordinates.length()) {
                                val latLngList = polygonCoordinates.getJSONArray(m)
                                listPolyArray.clear() // Clear for each new polygon

                                for (n in 0 until latLngList.length()) {
                                    val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))

                                    // Check if LatLng is within the visible region
                                    if (visibleBounds.contains(latLngPolyline)) {
                                        listPolyArray.add(latLngPolyline)
                                    }
                                }

                                // Add polyline if we have visible points
                                if (listPolyArray.isNotEmpty()) {
                                    val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                    val polyline = mMap!!.addPolyline(
                                        PolylineOptions().addAll(listPolyArray)
                                            .width(5f)
                                            .color(requireActivity().resources.getColor(polylineColor))
                                            .geodesic(true)
                                    )
                                    polylineList.add(polyline)
                                }
                            }
                        }
                    }

                    // Add markers based on zoom level
                    val firstLatLng = listMultiArray.firstOrNull() ?: listPolyArray.firstOrNull()
                    firstLatLng?.let {
                        val markerOptions = MarkerOptions()
                            .position(it)
                            .snippet(isLicensed.toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))

                        // Set the title based on the zoom level
                        if (zoomLevel >= 12) {
                            markerOptions.title(rluNumber)  // Show title at zoom level 12 or greater
                        }

                        val marker = mMap!!.addMarker(markerOptions)
                        markerList.add(marker!!) // Keep track of markers

                        // Show the info window only if zoom level is 12 or greater
                        if (zoomLevel >= 12) {
                            marker.showInfoWindow()
                        }
                    }
                }

                // Set OnMarkerClickListener
                mMap!!.setOnMarkerClickListener { marker ->
                    try {
                        val title = marker.title
                        val snippet = marker.snippet?.toIntOrNull()

                        if (title == null || snippet == null) {
                            Log.e("@@@", "Marker has missing information.")
                            return@setOnMarkerClickListener true
                        }

                        if (snippet == 0) {
                            // Fetch additional data for the selected marker
                            hitRluMapDetail_API(title!!)
                        } else {
                            // Handle case where the polyline is not available
                            PolylineNotAvailable(title)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("@@@", "Exception in Marker click listener: $e")
                    }
                    true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("@@@", "Exception: $e")
            }
        }
    */


    /*

        private fun updateVisibleFeatures(visibleBounds: LatLngBounds) {
            // Ensure 'features' JSONArray is available
            val features = multiPolygonFeatures!! // JSONArray of features


            val zoomLevel = mMap!!.cameraPosition.zoom

            // Clear previous data
            listMultiArray.clear()
            listPolyArray.clear()

            try {
                for (i in 0 until features.length()) {
                    val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                    val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                    val isLicensed = propertiesObject.getInt("IsLicensed")
                    val rluNumber = propertiesObject.getString("RLUNo")

                    when (featureObject.getString("type")) {
                        "MultiPolygon" -> {
                            val multiPolygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (j in 0 until multiPolygonCoordinates.length()) {
                                val innerPolygon = multiPolygonCoordinates.getJSONArray(j)
                                for (k in 0 until innerPolygon.length()) {
                                    val latLngList = innerPolygon.getJSONArray(k)
                                    listMultiArray.clear()

                                    for (l in 0 until latLngList.length()) {
                                        val latLngMultiPolyline = convertToLatLng(latLngList.getJSONArray(l))
                                        // Check if LatLng is within the visible region
                                        if (visibleBounds.contains(latLngMultiPolyline)) {
                                            listMultiArray.add(latLngMultiPolyline)
                                        }
                                    }

                                    // Add polyline if we have visible points
                                    if (listMultiArray.isNotEmpty()) {
                                        val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                        val multiPolyline = mMap!!.addPolyline(
                                            PolylineOptions().addAll(listMultiArray)
                                                .width(5f)
                                                .color(requireActivity().resources.getColor(polylineColor))
                                                .geodesic(true)
                                        )
                                        multiPolylineList.add(multiPolyline)
                                    }
                                }
                            }
                        }

                        "Polygon" -> {
                            val polygonCoordinates = featureObject.getJSONArray("coordinates")
                            for (m in 0 until polygonCoordinates.length()) {
                                val latLngList = polygonCoordinates.getJSONArray(m)
                                listPolyArray.clear()

                                for (n in 0 until latLngList.length()) {
                                    val latLngPolyline = convertToLatLng(latLngList.getJSONArray(n))
                                    // Check if LatLng is within the visible region
                                    if (visibleBounds.contains(latLngPolyline)) {
                                        listPolyArray.add(latLngPolyline)
                                    }
                                }

                                // Add polyline if we have visible points
                                if (listPolyArray.isNotEmpty()) {
                                    val polylineColor = if (isLicensed == 0) R.color.green else R.color.red_pre_approval
                                    val polyline = mMap!!.addPolyline(
                                        PolylineOptions().addAll(listPolyArray)
                                            .width(5f)
                                            .color(requireActivity().resources.getColor(polylineColor))
                                            .geodesic(true)
                                    )
                                    polylineList.add(polyline)
                                }
                            }
                        }
                    }

                    // Add marker (if necessary)
                    val firstLatLng = listMultiArray.firstOrNull() ?: listPolyArray.firstOrNull()
                    firstLatLng?.let {
                        val markerOptions = MarkerOptions()
                            .position(it)
                            .snippet(isLicensed.toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))

                        // Set title based on zoom level
                        if (zoomLevel >= 12) {
                            markerOptions.title(rluNumber)  // Show title at zoom level 12 or greater
                        }

                        val marker = mMap!!.addMarker(markerOptions)
                        markerList.add(marker!!) // Keep track of markers

                        // Create custom title view and display it as an info window
                        if (zoomLevel >= 12) {
                            val titleView = createMarkerTitle(rluNumber)
                            val infoWindowAdapter = object : GoogleMap.InfoWindowAdapter {
                                override fun getInfoContents(p0: Marker): View? {
                                    return titleView  // Use the custom title view
                                }

                                override fun getInfoWindow(p0: Marker): View? {
                                    return null  // Use the default info window
                                }
                            }
                            mMap!!.setInfoWindowAdapter(infoWindowAdapter)
                            marker.showInfoWindow()
                        }
                    }
                }

                // Set OnMarkerClickListener
                mMap!!.setOnMarkerClickListener { marker ->
                    if (zoomLevel >= 12) {
                        marker.showInfoWindow() // Show custom title if zoom level is sufficient
                    }
                    true
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("@@@", "Exception: $e")
            }
        }

        // Function to create a custom marker title view
        private fun createMarkerTitle(title: String): View {
            // Inflate the custom layout
            val markerTitleView = LayoutInflater.from(requireContext()).inflate(R.layout.marker_title, null)
            val titleTextView: TextView = markerTitleView.findViewById(R.id.title_text)
            titleTextView.text = title
            return markerTitleView
        }
    */

}









