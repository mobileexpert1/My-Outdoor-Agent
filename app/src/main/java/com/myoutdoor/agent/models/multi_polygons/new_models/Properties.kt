package com.myoutdoor.agent.models.multi_polygons.new_models


import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("IsLicensed")
    var isLicensed: Int,
    @SerializedName("ObjectID")
    var objectID: Int,
    @SerializedName("RLUNo")
    var rLUNo: String
)