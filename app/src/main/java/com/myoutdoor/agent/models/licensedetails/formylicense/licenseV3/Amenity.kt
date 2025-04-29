package com.myoutdoor.agent.models.licensedetails.formylicense.licenseV3


import com.google.gson.annotations.SerializedName

data class Amenity(
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
    @SerializedName("lastModifiedBy")
    var lastModifiedBy: Any,
    @SerializedName("lastModifiedDate")
    var lastModifiedDate: String,
    @SerializedName("productAmenitiesID")
    var productAmenitiesID: Int,
    @SerializedName("productID")
    var productID: Int,
    @SerializedName("totalLicenseFee")
    var totalLicenseFee: Double
)