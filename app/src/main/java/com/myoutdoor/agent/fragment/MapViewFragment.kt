package com.myoutdoor.agent.fragment

import MyItem
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.licence.LicenceFragment
import com.myoutdoor.agent.fragment.search.SearchFragment
import com.myoutdoor.agent.models.RluMapDetails.RluBody
import com.myoutdoor.agent.models.ZoomLevel.Zoomlevel
import com.myoutdoor.agent.models.point_layer.Feature
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.CircularProgressDialog
import com.myoutdoor.agent.utils.ClusterRenderer
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import com.myoutdoor.agent.utils.intersects
import com.myoutdoor.agent.viewmodel.MapFragmentViewModel
import kotlinx.android.synthetic.main.empty_list_property_alert.*
import kotlinx.android.synthetic.main.fragment_map_view.*
import kotlinx.android.synthetic.main.map_property_details_popup.*
import kotlinx.android.synthetic.main.property_not_available_popup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


class MapViewFragment : BaseFragment(), OnMapReadyCallback,
    ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>,
    ClusterManager.OnClusterItemClickListener<MyItem>,
    GoogleMap.OnCameraChangeListener, GoogleMap.OnInfoWindowClickListener {
    lateinit var viewModel: MapFragmentViewModel
    var searchResponseList = mutableListOf<com.myoutdoor.agent.models.search.Model>()
    lateinit var pref: SharedPref
    var mapId = ""
    var countryName = ""
    var acres = ""
    var price = ""
    var key = ""
    var image = ""
    var productTypeID = ""
    var productNo = ""
    var isClicked: Boolean = true
    var mapPosition: Int? = null
    private var mMap: GoogleMap? = null
    var showDialogBox: Dialog? = null
    lateinit var offsetItem: MyItem
    lateinit var clusterIcon: Drawable
    var coordinates: ArrayList<LatLng> = ArrayList()
    var simplecoordinates: ArrayList<LatLng> = ArrayList()
    private var latLng: LatLng? = null
    var latLngMultiPolyline: LatLng? = null
    var latLngPolyline: LatLng? = null
    val listMultiArray: java.util.ArrayList<LatLng> = java.util.ArrayList()
    var listPolyArray: java.util.ArrayList<LatLng> = java.util.ArrayList()
    var licenceddArray: java.util.ArrayList<Int> = java.util.ArrayList()
    val features: java.util.ArrayList<Feature> = java.util.ArrayList()
    var polyline: Polyline? = null
    var multi_polyline: Polyline? = null
    var polylineList = java.util.ArrayList<Polyline>()
    var multiPolylineList = java.util.ArrayList<Polyline>()
    var particularLiness: Polyline? = null
    var particularLineList = java.util.ArrayList<Polyline>()
    private var clusterManager: ClusterManager<MyItem?>? = null
    var isLicensec: Int = 0

    var alreadyHit: Boolean = false

    var zoomInFlag = -1
    var zoomOutFlag = -1

    var accessPointFeatures: ArrayList<com.myoutdoor.agent.models.gate_access_points.Feature> = ArrayList()

    var progressSearchBar: CircularProgressDialog? = null


    //val coordinates_temp1: ArrayList<ArrayList<Double>> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.fragment_map_view
    }

    override fun onStop() {
        super.onStop()
        fillMapFeatures.clear()
        accessPointFeatures.clear()

    }


    override fun onCreateView() {
        Log.e("TAG", "onCreateView: MapViewFragment" )
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

        showDialogBox = Dialog(requireContext())
        pref = SharedPref(requireContext())
        val bundle = this.arguments
        if (bundle != null) {
            mapId = bundle.getSerializable("mapId").toString()
            countryName = bundle.getSerializable("conutry").toString()
            acres = bundle.getSerializable("acres").toString()
            price = bundle.getSerializable("price").toString()
            image = bundle.getSerializable("image").toString()
            Log.e("call", "mapId " + mapId)
        }

        if (bundle != null) {
            mapPosition = bundle.getInt("mapPosition")
            Log.e("call", "mapPositionframent1 " + mapPosition)
        }


        if (bundle != null) {
            key = bundle.getString("publickey")!!
            productTypeID = bundle.getString("productTypeID")!!
            productNo = bundle.getString("productNo")!!
            Log.e("call123", "publickey " + key)
        }

        titleZoomLevel.clear()

        progressSearchBar = CircularProgressDialog(activity)

        viewModel = ViewModelProvider(this)[MapFragmentViewModel::class.java]

        val mapFragment = childFragmentManager.findFragmentById(R.id.Map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


//        if (productTypeID == "1") {
//
//            Log.e("particluarPolygenLayer", "type 1 : $productNo" )
//            viewModel.particluarPolygenLayer(
//                "RLUNo=" + "'" + productNo + "'",
//                "*",
//                "esriSpatialRelIntersects",
//                "geojson"
//            )
//            onParticularPolygenLayerSuccessObserver()
//            /* viewModel.particluarPolygenLayer(
//                "RLUNo=" + "'" + mapId + "'",
//                "*",
//                "esriSpatialRelIntersects",
//                "geojson"
//            )*/
//
////            viewModel.multiPolygenLayer(
////                "1=1",
////                "*",
////                true,
////                "geojson"
////            )
////            mapApis()
//
//            setObserver()
//        }
//        else {
//            Log.e("particluarPolygenLayer", "type  : $productNo" )
//
//            viewModel.particularPolygonFourLayer(
//                "RLUNo=" + "'" + productNo + "'",
//                "*",
//                "esriSpatialRelIntersects",
//                "geojson"
//            )
//particularPolygonFourLayerSuccessObserver()
////            viewModel.multiPolygonLayerFour(
////                "1=1",
////                "*",
////                true,
////                "geojson"
////            )
//
////            mapApis()
//            setObserver()
//        }
//
    /*        if (productTypeID == "1") {

            Log.e("particluarPolygenLayer", "type 1 : $productNo" )
            viewModel.particluarPolygenLayer(
                "RLUNo=" + "'" + productNo + "'",
                "*",
                "esriSpatialRelIntersects",
                "geojson"
            )
            onParticularPolygenLayerSuccessObserver()
            *//* viewModel.particluarPolygenLayer(
                "RLUNo=" + "'" + mapId + "'",
                "*",
                "esriSpatialRelIntersects",
                "geojson"
            )*//*

//            viewModel.multiPolygenLayer(
//                "1=1",
//                "*",
//                true,
//                "geojson"
//            )
//            mapApis()

            setObserver()
        } else {
            Log.e("particluarPolygenLayer", "type  : $productNo" )

            viewModel.particularPolygonFourLayer(
                "RLUNo=" + "'" + productNo + "'",
                "*",
                "esriSpatialRelIntersects",
                "geojson"
            )
particularPolygonFourLayerSuccessObserver()
//            viewModel.multiPolygonLayerFour(
//                "1=1",
//                "*",
//                true,
//                "geojson"
//            )

//            mapApis()
            setObserver()
        }*/

        mapApis()
    }




    fun mapApis(){
        CoroutineScope(Dispatchers.Main).launch {

            if (productTypeID == "1") {

                Log.e("particluarPolygenLayer", "type 1 : $productNo" )
                viewModel.particluarPolygenLayer(
                    "RLUNo=" + "'" + productNo + "'",
                    "*",
                    "esriSpatialRelIntersects",
                    "geojson"
                )
                onParticularPolygenLayerSuccessObserver()
                viewModel.pointLayer(
                    "1=1",
                    "*",
                    true,
                    "geojson",
                )
                setObserver()
            }
            else {
                Log.e("particluarPolygenLayer", "type  : $productNo" )

                viewModel.particularPolygonFourLayer(
                    "RLUNo=" + "'" + productNo + "'",
                    "*",
                    "esriSpatialRelIntersects",
                    "geojson"
                )
                particularPolygonFourLayerSuccessObserver()
                viewModel.pointLayer(
                    "1=1",
                    "*",
                    true,
                    "geojson",
                )
                setObserver()

            }


        }
    }


    // Function to check the depth of the coordinates
    fun getCoordinateDepth(coordinates: Any): Int {
        var depth = 0
        var element = coordinates
        while (element is List<*>) {
            depth++
            element = element[0] ?: break
        }
        return depth
    }


    var publickey: String = ""
    var imageFileName: String = ""
    var rluNumber = ""
    var countyName: String = ""
    var licenseFee: String = ""
    var state: String = ""
    var titleZoomLevel: ArrayList<Zoomlevel> = ArrayList()


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

    fun particularPolygonFourLayerSuccessObserver(){
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

    fun  onMultiPolygenLayerSuccessObserver(){
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

            viewModel.multiPolygonLayerFour(
                "1=1",
                "*",
                true,
                "geojson"
            )

            multiPolygonLayerFourSuccessObserver()

        })

    }


    fun multiPolygonLayerFourSuccessObserver(){
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

            viewModel.gateAccessPoints(
                "1=1",
                "*",
                true,
                "geojson"
            )

            gateAccessPointsSuccessObserver()

        })

    }


    fun gateAccessPointsSuccessObserver(){
        viewModel.gateAccessPointsSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            accessPointFeatures.clear()
            accessPointFeatures.addAll(it!!.features)
            Log.e("@@@@", "size......" + accessPointFeatures.size)
        })
        viewModel.fillAreas(
            "ProductType='Non-Motorized'",
            "*",
            true,
            "geojson"
        )
        fillMapAreasSuccessObserver()

    }


    fun fillMapAreasSuccessObserver(){
        viewModel.fillMapAreasSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            fillMapFeatures.clear()
            fillMapFeatures.addAll(it!!.features)
            Log.e("@@@@", "size......" + fillMapFeatures.size)


        })

    }

    fun onRLUMapDetailsSuccessObserver(){
        viewModel.onRLUMapDetailsSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")

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

        }
        )

    }

    private fun setObserver() {
        //point api
        viewModel.onPointLayerSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            features.clear()
            features.addAll(it!!.features)
            setUpClusterer()
            Log.e("@@@@", "size......" + features.size)
            viewModel.multiPolygenLayer(
                "1=1",
                "*",
                true,
                "geojson"
            )
            onMultiPolygenLayerSuccessObserver()

        })


        //MultiPolyGon
/*
        viewModel.onMultiPolygenLayerSuccess.observe(requireActivity(), Observer {

            var a: Marker? = null
            listMultiArray.clear()
            listPolyArray.clear()
            simplecoordinates.clear()

            if (it.toString().isEmpty()) {
                AlertMapPopUp()
            } else {
                try {
                    val Poly = it.string();
                    val `object` = JSONObject(Poly)
                    val features = `object`.getJSONArray("features")
//                    multiPolygonFeatures = features

                    // Initialize multiPolygonFourFeatures if null
                    if (multiPolygonFeatures == null) {
                        multiPolygonFeatures = features
                    } else {
                        // If already initialized, append new features
                        for (i in 0 until features.length()) {
                            multiPolygonFeatures?.put(features.getJSONObject(i))
                        }
                    }

                    */
/* for (i in 0 until features.length()) {
                         val featureObject = features.getJSONObject(i).getJSONObject("geometry")
                         val propertiesObject = features.getJSONObject(i).getJSONObject("properties")
                         var isLicensed = propertiesObject.get("IsLicensed")
                         if (featureObject.getString("type") == "MultiPolygon") {

                             for (j in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())){
                                 for (k in 0 until ((featureObject.getJSONArray("coordinates").get(j) as JSONArray).length())){
                                     for (l in 0 until ((featureObject.getJSONArray("coordinates").get(j) as JSONArray).get(k) as JSONArray).length()){
                                         latLngMultiPolyline = convertToLatLng((((featureObject.getJSONArray("coordinates").get(j) as JSONArray).get(k) as JSONArray).get(l) as JSONArray))

                                         listMultiArray.add(latLngMultiPolyline!!)
                                         val rluNumber = propertiesObject.get("RLUNo")
                                         a = mMap!!.addMarker(MarkerOptions().position(LatLng(listMultiArray[l].latitude, listMultiArray[l].longitude)).title(rluNumber.toString())
                                             .snippet(isLicensed.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic)))!!

                                     }

                                     if (isLicensed==0){
                                         print("isLicensed  0")
                                         multi_polyline = mMap!!.addPolyline((PolylineOptions().addAll(listMultiArray).width(5f)
                                             .color(requireActivity().getResources().getColor(R.color.green))
                                             .geodesic(true)))
                                         multiPolylineList.add(multi_polyline!!)

                                     }else{
                                         print("isLicensed  1")
                                         multi_polyline = mMap!!.addPolyline((PolylineOptions().addAll(listMultiArray).width(5f)
                                             .color(requireActivity().getResources().getColor(R.color.red_pre_approval))
                                             .geodesic(true)))
                                         multiPolylineList.add(multi_polyline!!)

                                     }
                                     listMultiArray.clear()
                                 }
                             }
                         }else if (featureObject.getString("type") == "Polygon") {
                             for (m in 0 until ((featureObject.getJSONArray("coordinates") as JSONArray).length())){
                                 for (n in 0 until ((featureObject.getJSONArray("coordinates").get(m) as JSONArray).length())){
                                     latLngPolyline = convertToLatLng(((featureObject.getJSONArray("coordinates").get(m) as JSONArray).get(n) as JSONArray))

                                     listPolyArray.add(latLngPolyline!!)
                                     val rluNumber = propertiesObject.get("RLUNo")
                                     a = mMap!!.addMarker(MarkerOptions().position(LatLng(listPolyArray[n].latitude, listPolyArray[n].longitude))
                                         .title(rluNumber.toString()).snippet(isLicensed.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic)))!!
                                 }
                                 if (isLicensed==0){
                                     polyline = mMap!!.addPolyline((PolylineOptions().addAll(listPolyArray).width(5f).color(requireActivity().getResources().getColor(R.color.green)).geodesic(true)))
                                     polylineList.add(polyline!!)

                                 }else{
                                     polyline = mMap!!.addPolyline((PolylineOptions().addAll(listPolyArray).width(5f).color(requireActivity().getResources().getColor(R.color.red_pre_approval)).geodesic(true)))
                                     polylineList.add(polyline!!)
                                 }
                                 listPolyArray.clear()
                             }
                         }
                         a!!.showInfoWindow()

                     }

                     try {

                         mMap!!.setOnMarkerClickListener { marker ->
                             var title=marker.title
                             var snippet =marker.snippet!!.toInt()
                             if (snippet==0){

                                 showDialogBox!!.setContentView(R.layout.map_property_details_popup)
                                 showDialogBox!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
                                 showDialogBox!!.setCanceledOnTouchOutside(true)
                                 showDialogBox!!.imgCancelProperty.setOnClickListener {
                                     showDialogBox!!.dismiss()
                                 }
                                 showDialogBox!!.tvHplPermitHead.setText(title)
                                 showDialogBox!!.tvPermitCountry.setText(countryName)
                                 showDialogBox!!.tvPermitAcres.setText("" + acres)
                                 showDialogBox!!.tvHplPermitPrice.setText("$" + "" + price)
                                 showDialogBox!!.rlZoomMarker.setOnClickListener {
                                     if (isClicked) {
                                         showDialogBox!!.tvZoomOutIn.setText("Zoom in")
                                         mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
                                         isClicked = false

                                     } else {
                                         showDialogBox!!.tvZoomOutIn.setText("Zoom Out")
                                         mMap!!.animateCamera(CameraUpdateFactory.zoomOut());
                                         isClicked = true

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
                                     addNewFragment(
                                         DirectiongoogleMapFragment(progressBarPB),
                                         R.id.container,
                                         true
                                     )
                                 }

                                 Glide.with(showDialogBox!!.ivSeachImage)
                                     .load(Constants.PROPERTY_IMAGE_URL + image)
                                     .fitCenter()
                                     .placeholder(R.drawable.round_logo)
                                     .error(R.drawable.round_logo)
                                     .centerCrop()
                                     .into(showDialogBox!!.ivSeachImage)

                                 showDialogBox!!.show()

                             }else{
                                 PolylineNotAvailable(title)
                             }
                             false
                         }


                     }catch (E:Exception){
                         E.printStackTrace()
                         Log.e("@@@","ExceptionMarkerclick......."+E)
                     }
 *//*

                } catch (E: Exception) {
                    E.printStackTrace()
                    Log.e("@@@", "Exception......." + E)
                }
            }

        })
*/


        viewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Api error Failed" + it.toString())
        }
        )

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

    var markerList: ArrayList<Marker> = ArrayList()
    var multiPolygonFeatures: JSONArray? = null

/*
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
                        marker.showInfoWindow() // Only show the info window after the API call completes

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
                            .geodesic(true)
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
                        .geodesic(true)
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

    private var currentMarker: Marker? = null // Store the current clicked marker

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

                val markersToAdd = mutableListOf<Triple<LatLng, String, Int>>() // Store markers for batch adding

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
        val latSum = points.sumOf { it.latitude }
        val lngSum = points.sumOf { it.longitude }
        return LatLng(latSum / points.size, lngSum / points.size)
    }

    private fun setupMarkerClickListener() {
        mMap!!.setOnMarkerClickListener { marker ->
            // If the clicked marker is the same as the current one, ignore
            if (currentMarker == marker) {
                return@setOnMarkerClickListener true
            }

            currentMarker?.hideInfoWindow() // Reset any previously clicked marker's info window
            currentMarker = marker // Set the current marker to the newly clicked one

            val title = marker.title
            val snippet = marker.snippet?.toIntOrNull()

            snippet?.let {
                if (it == 0) {
                    isLicensec = it
                    hitRluMapDetail_API(title.toString())
//                    setObserver()
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
            currentMarker = null // Clear current marker reference
        }
    }





    @SuppressLint("PotentialBehaviorOverride", "SuspiciousIndentation")
    private fun setUpClusterer() {
        clusterManager = ClusterManager(requireContext(), mMap)
        mMap?.setOnCameraIdleListener(clusterManager)
        mMap?.setOnMarkerClickListener(clusterManager)
        mMap?.setOnCameraChangeListener(this);
        mMap?.setOnInfoWindowClickListener(this);
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
                    features[i].properties.IsLicensed.toString(), i
                )
                clusterIcon = requireActivity().getResources().getDrawable(R.drawable.ellips_green)
                //    clusterManager.cluster();
                clusterManager!!.addItem(offsetItem)
                clusterManager!!.renderer =
                    ClusterRenderer(requireActivity(), mMap, clusterManager, "0")

            }
            else {

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
                        features[i].properties.IsLicensed.toString(), i
                    )
                    clusterIcon =
                        requireActivity().getResources().getDrawable(R.drawable.ellips_green)
                    //    clusterManager.cluster();
                    clusterManager!!.addItem(offsetItem)
                    clusterManager!!.renderer =
                        ClusterRenderer(requireActivity(), mMap, clusterManager, "1")

                }

            }


            clusterManager!!.setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<MyItem?> {

                for (i in 0 until features.size) {

                    if (features.get(i).geometry.coordinates.get(0) == it.latlng.longitude &&
                        features.get(i).geometry.coordinates.get(1) == it.latlng.latitude
                    ) {
                        val coordinates_: java.util.ArrayList<Double> =
                            java.util.ArrayList<Double>()
                        coordinates_.add(it.latlng.longitude)
                        coordinates_.add(it.latlng.latitude)

                        if (features.get(i).geometry.coordinates == coordinates_) {
                            mapId = features.get(i).properties.RLUNo
                            isLicensec = features.get(i).properties.IsLicensed
                            //  hitRluMapDetail_API(rluNo)
                        }
                    }
                }
                true
            })
        }

    }


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




    private fun onMapMethod() {

//        if (simplecoordinates.isNotEmpty()) {
//            for (i in 0 until simplecoordinates.size) {
//                particularLiness = mMap!!.addPolyline(
//                    PolylineOptions().addAll(simplecoordinates).width(5f)
//                        .color(requireActivity().getResources().getColor(R.color.green))
//                )
//                particularLineList.add(particularLiness!!)
//            }
//        }
//
//        val simpleMarker = mMap!!.addMarker(
//            MarkerOptions().position(
//                LatLng(
//                    simplecoordinates[1].latitude,
//                    simplecoordinates[0].longitude
//                )
//            ).title(mapId).icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
//        )!!
//        simpleMarker.showInfoWindow()
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(simplecoordinates[0], 12f), null)
        simplecoordinates.clear()



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


    private fun onMapMethod() {
        // Single polyline creation
        if (simplecoordinates.isNotEmpty()) {
            particularLiness = mMap?.addPolyline(
                PolylineOptions()
                    .addAll(simplecoordinates)
                    .width(5f)
                    .color(requireContext().getColor(R.color.green))
            )
            particularLineList.add(particularLiness!!)
        }

        // Single marker setup
        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(simplecoordinates[1].latitude, simplecoordinates[0].longitude))
                .title(mapId)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )?.showInfoWindow()

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(simplecoordinates[0], 12f))
        simplecoordinates.clear()

        // Map view toggle
        tvMapView.setOnClickListener { setMapType(GoogleMap.MAP_TYPE_NORMAL, tvMapView, tvHybridView) }
        tvHybridView.setOnClickListener { setMapType(GoogleMap.MAP_TYPE_HYBRID, tvHybridView, tvMapView) }
    }

    private fun setMapType(type: Int, selectedView: TextView, unselectedView: TextView) {
        selectedView.apply {
            setTextColor(Color.BLACK)
            setTypeface(typeface, Typeface.BOLD)
            setBackgroundResource(R.color.white)
        }
        unselectedView.apply {
            setTextColor(Color.parseColor("#898989"))
            setBackgroundResource(R.color.my_account_tab_grey)
        }
        mMap?.mapType = type
    }


*/

    /*

    private fun onMapMethod() {
        // Single polyline creation
        if (simplecoordinates.isNotEmpty()) {
            particularLiness = mMap?.addPolyline(
                PolylineOptions()
                    .addAll(simplecoordinates)
                    .width(5f)
                    .color(requireContext().getColor(R.color.green))
            )
            particularLineList.add(particularLiness!!)
        }

        // Single marker setup
        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(simplecoordinates[1].latitude, simplecoordinates[0].longitude))
                .title(mapId)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trans_pic))
        )?.showInfoWindow()

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(simplecoordinates[0], 11f))
        simplecoordinates.clear()

        // Set map move listener to clear the map
//        mMap?.setOnCameraMoveListener { clearMaps() }

        // Map view toggle
        tvMapView.setOnClickListener { setMapType(GoogleMap.MAP_TYPE_NORMAL, tvMapView, tvHybridView) }
        tvHybridView.setOnClickListener { setMapType(GoogleMap.MAP_TYPE_HYBRID, tvHybridView, tvMapView) }
    }


    */

    private fun clearMaps() {
        mMap?.clear()
        particularLineList.clear() // Clear the list to prevent stale references
    }

    private fun setMapType(type: Int, selectedView: TextView, unselectedView: TextView) {
        selectedView.apply {
            setTextColor(Color.BLACK)
            setTypeface(typeface, Typeface.BOLD)
            setBackgroundResource(R.color.white)
        }
        unselectedView.apply {
            setTextColor(Color.parseColor("#898989"))
            setBackgroundResource(R.color.my_account_tab_grey)
        }
        mMap?.mapType = type
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

    private fun AlertMapPopUp() {
        showDialogBox1!!.setContentView(R.layout.empty_list_property_alert)
        showDialogBox1!!.setCanceledOnTouchOutside(true)
        showDialogBox1!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        showDialogBox1!!.show()
        showDialogBox1!!.tvMapOK.setOnClickListener {
            replaceFragment(SearchFragment(), R.id.container, true)
            showDialogBox1!!.dismiss()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.getUiSettings().setZoomGesturesEnabled(true);

        imvPlus.setOnClickListener {
            mMap!!.animateCamera(CameraUpdateFactory.zoomIn());
            val zoomIn = mMap!!.cameraPosition.zoom.toInt()

            if (zoomIn == 9) {

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
                        clusterIcon =
                            requireActivity().getResources().getDrawable(R.drawable.ellips_green)
                        clusterManager!!.cluster();
                        clusterManager!!.addItem(offsetItem)
                        clusterManager!!.renderer =
                            ClusterRenderer(requireActivity(), mMap, clusterManager, "1")

                    }


                    clusterManager!!.setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<MyItem?> {
                        for (i in 0 until features.size - 1) {

                            if (features.get(i).geometry.coordinates.get(0) == it.latlng.longitude &&
                                features.get(i).geometry.coordinates.get(1) == it.latlng.latitude
                            ) {
                                val coordinates_: java.util.ArrayList<Double> =
                                    java.util.ArrayList<Double>()
                                coordinates_.add(it.latlng.longitude)
                                coordinates_.add(it.latlng.latitude)

                                if (features.get(i).geometry.coordinates == coordinates_) {
                                    mapId = features.get(i).properties.RLUNo
                                    isLicensec = features.get(i).properties.IsLicensed
                                    hitRluMapDetail_API(mapId)
                                }
                            }
                        }
                        true
                    })
                }
            }

        }

        imvMinus.setOnClickListener {
            mMap!!.animateCamera(CameraUpdateFactory.zoomOut());

            val zoomOut = mMap!!.cameraPosition.zoom.toInt()
            Log.e("call", "zoomOut:-  " + zoomOut)

            if (zoomOut == 6) {
                clusterManager!!.clearItems()

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

                    clusterManager!!.setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<MyItem?> {

                        for (i in 0 until features.size - 1) {

                            if (features.get(i).geometry.coordinates.get(0) == it.latlng.longitude &&
                                features.get(i).geometry.coordinates.get(1) == it.latlng.latitude
                            ) {
                                val coordinates_: java.util.ArrayList<Double> =
                                    java.util.ArrayList<Double>()
                                coordinates_.add(it.latlng.longitude)
                                coordinates_.add(it.latlng.latitude)

                                if (features.get(i).geometry.coordinates == coordinates_) {
                                    mapId = features.get(i).properties.RLUNo
                                    isLicensec = features.get(i).properties.IsLicensed

                                }
                            }
                        }
                        true
                    })
                }
            }

            if (zoomOut == 13) {

                viewModel.pointLayer(
                    "1=1",
                    "*",
                    true,
                    "geojson",
                )

            }

            if (zoomOut == 14) {

                for (polyLine in multiPolylineList) {
                    polyLine.remove()
                }
                for (polyLine in polylineList) {
                    polyLine.remove()
                }
                for (polyLine in particularLineList) {
                    polyLine.remove()
                }

            }
        }
    }


    private fun hitRluMapDetail_API(mapId: String) {
        viewModel.rluMapDetails(RluBody(mapId), pref.getString(Constants.TOKEN))
        onRLUMapDetailsSuccessObserver()
    }

    override fun onClusterItemInfoWindowClick(item: MyItem?) {
        TODO("Not yet implemented")
    }

    override fun onClusterItemClick(item: MyItem?): Boolean {
        TODO("Not yet implemented")
    }
    var rluNo = ""


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

        // Optionally, clear markers if you don't want to show them at lower zoom levels
        Log.e("sncjdncd","skncks clear marker")
        markerList.clear()
        // markers.clear()

    }

/*
    override fun onCameraChange(p0: CameraPosition) {

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
                for (feature in features) {
                    // Check if the feature is within the visible bounds
                    val latLng = LatLng(
                        feature.geometry.coordinates[1],
                        feature.geometry.coordinates[0]
                    )

                    // Only add item if it's within the visible bounds
                    if (visibleBounds.contains(latLng) && feature.properties.IsLicensed == 0) {
                        val offsetItem = MyItem(
                            latLng.latitude,
                            latLng.longitude,
                            "Title ${features.indexOf(feature)}",
                            "",
                            feature.properties.IsLicensed.toString(),
                            features.indexOf(feature)
                        )
                        clusterManager!!.addItem(offsetItem)
                    }
                }

                // Set the renderer for the cluster manager
                clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "0")

                // Set the cluster item click listener only once after adding all items
                clusterManager!!.setOnClusterItemClickListener { item ->
                    features.forEach { feature ->
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

                // Call cluster once after adding items
                clusterManager!!.cluster()
            }

        }

        print(p0.zoom)

        Log.d("Current Zoom Level", p0.zoom.toString())

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
        }


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
        }


//        if (p0.zoom>= 3f && p0.zoom<4f){
//
//            Log.e("2222 ", "333333")
//
//            if (zoomOutFlag == -1){
//                return
//            }
//            zoomOutFlag = -1
//
//            //features.clear()
//
//            clusterManager!!.clearItems()
//
//            for (i in 0..features.size-1) {
//
//
//                if (features[i].properties.IsLicensed == 0) {
//                    latLng = LatLng(features[i].geometry.coordinates.get(1), features[i].geometry.coordinates.get(0))
//                    offsetItem = MyItem(
//                        features[i].geometry.coordinates.get(1),
//                        features[i].geometry.coordinates.get(0), "Title $i", "", features[i].properties.IsLicensed.toString(), i)
//                    clusterIcon = requireActivity().getResources().getDrawable(R.drawable.ellips_green)
//                    clusterManager!!.cluster();
//                    clusterManager!!.addItem(offsetItem)
//                    clusterManager!!.renderer = ClusterRenderer(requireActivity(), mMap, clusterManager, "0")
//
//                }
//
//
//                clusterManager!!.setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<MyItem?> {
//
//                    for (i in 0 until features.size - 1) {
//
//                        if (features.get(i).geometry.coordinates.get(0) == it.latlng.longitude &&
//                            features.get(i).geometry.coordinates.get(1) == it.latlng.latitude
//                        ) {
//                            val coordinates_: java.util.ArrayList<Double> =
//                                java.util.ArrayList<Double>()
//                            coordinates_.add(it.latlng.longitude)
//                            coordinates_.add(it.latlng.latitude)
//
//                            if (features.get(i).geometry.coordinates == coordinates_) {
//                                mapId = features.get(i).properties.RLUNo
//                                isLicensec = features.get(i).properties.IsLicensed
//                                hitRluMapDetail_API(mapId)
//                            }
//                        }
//                    }
//                    true
//                })
//            }
//
//            //print(clusterManager.markerCollection.markers[cu])
//        }

    }
*/


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


                    clusterManager!!.setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<MyItem?> {

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


    // Store polygons globally in a list
    private var polygonsList = ArrayList<Polygon>()

    var fillMapFeatures: ArrayList<com.myoutdoor.agent.models.fill_map_areas.Feature> = ArrayList()

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



    fun clearMap() {
        mMap!!.clear()
    }

    override fun onInfoWindowClick(p0: Marker) {
        TODO("Not yet implemented")
    }


}
