package com.myoutdoor.agent.models.getallamenities

data class GetAllAmenitiesResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)