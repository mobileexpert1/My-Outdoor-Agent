package com.myoutdoor.agent.models.gate_access_points


import com.google.gson.annotations.SerializedName

data class GateAccessPointsResponse(
    @SerializedName("features")
    var features: List<Feature>,
    @SerializedName("type")
    var type: String
)