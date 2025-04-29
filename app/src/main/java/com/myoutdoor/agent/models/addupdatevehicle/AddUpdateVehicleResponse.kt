package com.myoutdoor.agent.models.addupdatevehicle

data class AddUpdateVehicleResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)