package com.myoutdoor.agent.fragment.verification.code.response

import com.google.gson.annotations.SerializedName

data class UpdatePasswordResponse(
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("value")
    var value: String
)