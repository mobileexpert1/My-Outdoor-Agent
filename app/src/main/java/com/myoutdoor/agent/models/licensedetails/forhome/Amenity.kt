package com.myoutdoor.agent.models.licensedetails.forhome

data class Amenity(
    var amenities:String= "",
    var amenityIcon: String,
    var amenityName: String,
    var amenityType: String,
    var amenityTypeID: Int,
    var description: String,
    var productID: Int
)