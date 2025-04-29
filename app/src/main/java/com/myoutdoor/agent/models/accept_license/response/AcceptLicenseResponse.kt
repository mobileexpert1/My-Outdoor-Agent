package com.myoutdoor.agent.models.accept_license.response


import com.google.gson.annotations.SerializedName

data class AcceptLicenseResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("model")
    var model: Any,
    @SerializedName("statusCode")
    var statusCode: Int
)