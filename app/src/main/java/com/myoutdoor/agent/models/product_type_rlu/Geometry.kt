package com.myoutdoor.agent.models.product_type_rlu


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("coordinates")
    var coordinates: List<List<List<List<Double>>>>,
    @SerializedName("type")
    var type: String
)