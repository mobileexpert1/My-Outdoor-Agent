package com.myoutdoor.agent.models.multi_polygons.new_models


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("coordinates")
    var coordinates: List<List<List<Any>>>,
    @SerializedName("type")
    var type: String
)