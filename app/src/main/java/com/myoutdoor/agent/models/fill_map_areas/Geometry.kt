package com.myoutdoor.agent.models.fill_map_areas


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("coordinates")
    var coordinates: List<List<List<List<Double>>>>,
    @SerializedName("type")
    var type: String
)