package com.myoutdoor.agent.models.gate_access_points


import com.google.gson.annotations.SerializedName

data class Feature(
    @SerializedName("geometry")
    var geometry: Geometry,
    @SerializedName("id")
    var id: Int,
    @SerializedName("properties")
    var properties: Properties,
    @SerializedName("type")
    var type: String
)