package com.myoutdoor.agent.models.product_type_rlu.arkansas


import com.google.gson.annotations.SerializedName

data class Feature(
    @SerializedName("geometry")
    var geometry: Geometry,
    @SerializedName("properties")
    var properties: Properties,
    @SerializedName("type")
    var type: String
)