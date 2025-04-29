package com.myoutdoor.agent.models.freeaccess.response


import com.google.gson.annotations.SerializedName

data class FreeAccessResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("model")
    var model: Any,
    @SerializedName("statusCode")
    var statusCode: Int
)