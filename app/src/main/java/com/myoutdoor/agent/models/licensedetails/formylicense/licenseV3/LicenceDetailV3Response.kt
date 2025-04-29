package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class LicenceDetailV3Response(
    @SerializedName("message")
    var message: String,
    @SerializedName("model")
    var model: Model,
    @SerializedName("statusCode")
    var statusCode: Int
)