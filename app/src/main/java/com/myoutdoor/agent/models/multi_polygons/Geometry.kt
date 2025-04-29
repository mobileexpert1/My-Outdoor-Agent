package com.myoutdoor.agent.models.multi_polygons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Geometry {
    @SerializedName("type")
    @Expose
    var type: String? = null



    var finalCoordinates: Array<Array<Double>>? = null

    @SerializedName("coordinates")
    @Expose
    var coordinates: Any? = null




}