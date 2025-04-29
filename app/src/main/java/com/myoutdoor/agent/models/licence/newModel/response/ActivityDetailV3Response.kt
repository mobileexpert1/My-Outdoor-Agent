package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName

data class ActivityDetailV3Response(
    @SerializedName("message")
    var message: String,
    @SerializedName("model")
    var model: Model,
    @SerializedName("statusCode")
    var statusCode: Int
)