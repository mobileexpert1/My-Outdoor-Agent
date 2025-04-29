package com.myoutdoor.agent.models.multi_polygons.new_models


import com.google.gson.annotations.SerializedName

data class MultiplePolygon(
    @SerializedName("features")
    var features: List<Feature>,
    @SerializedName("type")
    var type: String
)