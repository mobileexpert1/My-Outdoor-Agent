package com.myoutdoor.agent.models.search.searchV2


import com.google.gson.annotations.SerializedName

data class SearchV2Response(
    @SerializedName("message")
    var message: String,
    @SerializedName("model")
    var model: List<Model>,
    @SerializedName("statusCode")
    var statusCode: Int
)

