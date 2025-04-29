package com.myoutdoor.agent.models.DayPassAvailibility

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DayPassResponse {
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("model")
    @Expose
    var model: DayPassBody? = null
}