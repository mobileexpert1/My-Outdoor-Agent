package com.myoutdoor.agent.models.fill_map_areas


import com.google.gson.annotations.SerializedName

data class FillMapAreasResponse(
    @SerializedName("features")
    var features: List<Feature>,
    @SerializedName("type")
    var type: String
)