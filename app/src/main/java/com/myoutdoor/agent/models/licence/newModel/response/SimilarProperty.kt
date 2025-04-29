package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName

data class SimilarProperty(
    @SerializedName("countyName")
    var countyName: String,
    @SerializedName("displayText")
    var displayText: String,
    @SerializedName("imageFilename")
    var imageFilename: String,
    @SerializedName("licenseActivityID")
    var licenseActivityID: Int,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("productNo")
    var productNo: String,
    @SerializedName("publicKey")
    var publicKey: String,
    @SerializedName("stateName")
    var stateName: String
)