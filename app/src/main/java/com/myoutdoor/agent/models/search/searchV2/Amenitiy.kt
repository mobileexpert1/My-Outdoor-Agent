package com.myoutdoor.agent.models.search.searchV2


import com.google.gson.annotations.SerializedName

data class Amenitiy(
    @SerializedName("amenityIcon")
    var amenityIcon: String,
    @SerializedName("amenityName")
    var amenityName: String
)