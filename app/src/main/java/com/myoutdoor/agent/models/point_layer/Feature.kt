package com.myoutdoor.agent.models.point_layer

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.myoutdoor.agent.R
import org.json.JSONArray

data class Feature(
    val geometry: Geometry,
    val id: Int,
    val properties: Properties,
    val type: String
)


