package com.myoutdoor.agent.models.gate_access_points


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("coordinates")
    var coordinates: List<Double>,
    @SerializedName("type")
    var type: String
)