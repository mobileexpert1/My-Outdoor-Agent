package com.myoutdoor.agent.models.licence.newModel.response


import com.google.gson.annotations.SerializedName

data class Amenity(
    @SerializedName("amenities")
    var amenities: Any,
    @SerializedName("amenityIcon")
    var amenityIcon: String,
    @SerializedName("amenityName")
    var amenityName: String,
    @SerializedName("amenityType")
    var amenityType: String,
    @SerializedName("amenityTypeID")
    var amenityTypeID: Int,
    @SerializedName("description")
    var description: String,
    @SerializedName("productID")
    var productID: Int
)