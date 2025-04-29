package com.myoutdoor.agent.models.fill_map_areas


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