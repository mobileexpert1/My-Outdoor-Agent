package com.myoutdoor.agent.models.fill_map_areas


import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("ESRI_OID")
    var eSRIOID: Int,
    @SerializedName("ProductType")
    var productType: String,
    @SerializedName("RLUNo")
    var rLUNo: String
)