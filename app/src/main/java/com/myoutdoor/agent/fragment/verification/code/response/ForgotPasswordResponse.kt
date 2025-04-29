package com.myoutdoor.agent.fragment.verification.code.response


import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("model")
    var model: Model,
    @SerializedName("statusCode")
    var statusCode: Int
)
