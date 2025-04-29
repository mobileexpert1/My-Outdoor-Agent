package com.myoutdoor.agent.models.getallamenities

data class Model(
    var amenities: Any,
    var amenityIcon: String,
    var amenityName: String,
    var amenityType: String,
    var amenityTypeID: Int,
    var description: String,
    var productID: Int,
    var isSelected:Boolean = false
)