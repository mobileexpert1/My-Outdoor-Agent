package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class MapFile(
    @SerializedName("countyName")
    var countyName: Any,
    @SerializedName("displayName")
    var displayName: Any,
    @SerializedName("mapFileID")
    var mapFileID: Int,
    @SerializedName("mapFileName")
    var mapFileName: String,
    @SerializedName("mapInfoJson")
    var mapInfoJson: String,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("productName")
    var productName: Any,
    @SerializedName("productNo")
    var productNo: Any,
    @SerializedName("regionName")
    var regionName: Any,
    @SerializedName("stateName")
    var stateName: Any
)