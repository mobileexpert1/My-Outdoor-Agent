package com.myoutdoor.agent.models.licensedetails.formylicense.license

data class Amenity(
    var amenityIcon: String,
    var amenityName: String,
    var amenityType: String,
    var amenityTypeID: Int,
    var description: String,
    var lastModifiedBy: Any,
    var lastModifiedDate: String,
    var productAmenitiesID: Int,
    var productID: Int,
    var totalLicenseFee: Double
)