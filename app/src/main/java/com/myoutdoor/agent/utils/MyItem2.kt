package com.myoutdoor.agent.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class MyItem2(
//    lat: Double,
//    lng: Double,
    latlong: LatLng,
    title: String,
    snippet: String,

    ) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String


    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }

    init {
        position = latlong
        this.title = title
        this.snippet = snippet
    }
}
