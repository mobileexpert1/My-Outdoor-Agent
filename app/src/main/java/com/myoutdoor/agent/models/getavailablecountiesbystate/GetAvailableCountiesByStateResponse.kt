package com.myoutdoor.agent.models.getavailablecountiesbystate

data class GetAvailableCountiesByStateResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)