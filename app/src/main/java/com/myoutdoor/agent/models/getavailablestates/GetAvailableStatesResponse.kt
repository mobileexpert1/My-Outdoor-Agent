package com.myoutdoor.agent.models.getavailablestates

data class GetAvailableStatesResponse(
    var message: String,
    var model: List<Model>,
    var statusCode: Int
)